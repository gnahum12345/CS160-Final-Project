package com.example.gabriel.sociala;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.gabriel.sociala.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class EditFeedbackActivity extends AppCompatActivity {

    private static final int PHOTO_SUCCESS_CODE = 200;
    private static final int RECORDING_PERMISSION_CODE = 1;
    private final String TAG = "EditFeedbackActivity";
    ImageView postImageView;
    Button backButton, nextButton;
    Button filterButton, tagButton, stickerButton, musicButton;
    ImageView profilePic;
    TextView userName;
    EditText caption, reason;
    private String filePath;
    private MediaProjectionManager mProjectionManager;
    private static final String OUTPUT_PHOTO_DIRECTORY = "SocialA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_feedback);

        mProjectionManager = (MediaProjectionManager) getSystemService (Context.MEDIA_PROJECTION_SERVICE);
        boolean isRecording = isServiceRunning(RecordService.class);

        postImageView = findViewById(R.id.image_view_photo);
        backButton = findViewById(R.id.back_button);
        nextButton = findViewById(R.id.next_button);
        filterButton = findViewById(R.id.filter_button);
        tagButton = findViewById(R.id.tag_button);
        stickerButton = findViewById(R.id.sticker_button);
        musicButton = findViewById(R.id.music_button);

        userName = findViewById(R.id.textView_username);
        profilePic = findViewById(R.id.profile_image);

        caption = findViewById(R.id.editText_caption);
        reason = findViewById(R.id.editText_reason);
        final Context context = this;

        Intent i = getIntent();
        final String objectID = i.getStringExtra("postID");
        if (objectID == null) {
            finish();
        }

        Post.Query query = new Post.Query();
        query = query.specificPost(objectID);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else if (objects.size() != 0) {
                    Post p = objects.get(0);
                    try {
                        ParseUser creator = p.getCreator().fetchIfNeeded();

                        userName.setText(creator.getUsername());
                        ParseFile pf = creator.getParseFile("profilePic");
                        if (pf != null) {
                            new PostManager.DownloadImageTask(profilePic, 300, null)
                                    .execute(pf.getUrl());
                        }
                    } catch (ParseException e1) {
                        // user doesn't have a profile pic.
                        e1.printStackTrace();
                    }
                    ParseFile pImg = p.getPhoto();
                    if (pImg != null) {
                        new PostManager.DownloadImageTask(postImageView, null, 280).execute(pImg.getUrl());
                        setUpOnClickListeners(context, p.getID());
                    }
                }
            }
        });
    }

    private void setUpOnClickListeners(final Context context, final String postID) {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: back to previous page.
                new AlertDialog.Builder(EditFeedbackActivity.this)
                        .setTitle("Exiting Feedback")
                        .setMessage("Are you sure you want to exit and dismiss your feedback?")
                        .setPositiveButton("Yes, get me out.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("No, let me edit!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "You may continue to work on your feedback.", Toast.LENGTH_SHORT).show();
                            }
                        }).create().show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filePath = Environment.getExternalStorageDirectory()+ "/" + OUTPUT_PHOTO_DIRECTORY;

                // go to postFeedbackActivity with filePath.
                Intent i = new Intent(EditFeedbackActivity.this, PostFeedbackActivity.class);
                String cap = caption.getText().toString();
                String res = reason.getText().toString();
                if (res.isEmpty()) {
                    Toast.makeText(context, "Please fill out a reason for your changes.\nThis way others can learn from them.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (cap.isEmpty()) {
                    Toast.makeText(context, "Please fill out a caption for your changes.\nThis way the user has all the requirements to post!", Toast.LENGTH_LONG).show();
                    return;
                }
                i.putExtra("filePath", filePath);
                i.putExtra("caption", cap);
                i.putExtra("reason", res);
                i.putExtra("postID", postID);
                startActivity(i);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add image processing api.
                startActivityForResult(mProjectionManager.createScreenCaptureIntent(), RECORDING_PERMISSION_CODE);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_SUCCESS_CODE:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        Log.d("EditFeedbackActivity", "onActivityResult: data is null.... ");
                    }
                    Uri outputUri = data.getData();
                    postImageView.setImageURI(outputUri);
                    Toast.makeText(this, "New image displayed", Toast.LENGTH_SHORT).show();
                }
                stopRecordingService();

                break;
            case RECORDING_PERMISSION_CODE:
                if (resultCode == RESULT_OK) {
                    startRecordingService(resultCode, data);
                    Intent dsPhotoEditorIntent = new Intent(EditFeedbackActivity.this, DsPhotoEditorActivity.class);
                    BitmapDrawable bd = ((BitmapDrawable) postImageView.getDrawable());
                    Bitmap b = bd.getBitmap();
                    Uri uriData = BitmapScalar.getUriFromBitmap(this, b);
                    dsPhotoEditorIntent.setData(uriData);
                    dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, OUTPUT_PHOTO_DIRECTORY);
                    int[] toolsToHide = {DsPhotoEditorActivity.TOOL_PIXELATE, DsPhotoEditorActivity.TOOL_ORIENTATION};
                    dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
                    startActivityForResult(dsPhotoEditorIntent, PHOTO_SUCCESS_CODE);

                } else {
                    Toast.makeText(this, "Can't record screen", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void startRecordingService(int resultCode, Intent data) {
        Intent i = RecordService.newIntent(this, resultCode, data);
        startService(i);
    }

    private void stopRecordingService() {
        Intent i = new Intent(this, RecordService.class);
        stopService(i);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
