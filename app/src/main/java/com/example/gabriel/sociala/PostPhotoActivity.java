package com.example.gabriel.sociala;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PostPhotoActivity extends AppCompatActivity {

    ImageView selectedImageView;
    ImageButton backButton;
    Button postButton;
    TextView userName;
    ImageView profilePic;
    EditText rv_caption;
    EditText rv_purpose;

    ViewPager viewpager;
    LinearLayout sliderDotsPanel;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        selectedImageView = findViewById(R.id.image_view);
        userName = findViewById(R.id.textView_username);
        profilePic = findViewById(R.id.profile_image);
        rv_caption = findViewById(R.id.editText_caption);
        rv_purpose = findViewById(R.id.editText_purpose);

        Intent intent = getIntent();
        Bundle args = getIntent().getBundleExtra("BUNDLE");
        ArrayList<String> selectedImageUri = (ArrayList<String>) args.getSerializable("ARRAYLIST");


        final ArrayList<String> selectedImagePath = new ArrayList<String>();

        for (int i=0; i<selectedImageUri.size(); i++) {
            String imagePath = selectedImageUri.get(i);
            Uri fileUri = Uri.parse(imagePath);

            final String[] columns = { MediaStore.MediaColumns.DATA };
            final Cursor cursor = this.getContentResolver().query(fileUri, columns, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            String filePath = cursor.getString(columnIndex);
            selectedImagePath.add(filePath);
        }


        viewpager = findViewById(R.id.view_pager);
        sliderDotsPanel = (LinearLayout) findViewById(R.id.slider_dots);

        if (selectedImageUri.size() == 1){
            final String imagePath = selectedImageUri.get(0);
            final Uri fileUri = Uri.parse(imagePath);
            selectedImageView.setImageURI(fileUri);

        } else{
            viewpager = (ViewPager) findViewById(R.id.view_pager);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, selectedImageUri);
            viewpager.setAdapter(viewPagerAdapter);

            dotscount = viewPagerAdapter.getCount();
            dots = new ImageView[dotscount];

            for (int i = 0; i < dotscount; i++) {
                dots[i] = new ImageView(this);
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8,0,8,0);

                sliderDotsPanel.addView(dots[i], params);
            }

            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    for (int j = 0; j < dotscount; j++) {
                        dots[j].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                    }
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });

        }


        backButton = findViewById(R.id.imageButtonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                // TODO: Go back to photo selecting page
            }
        });

        postButton = findViewById(R.id.button_post);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<File> files = new ArrayList<>();
                for (int i = 0; i < selectedImagePath.size(); i++) {
                    File f = new File(selectedImagePath.get(i));

                    if (!f.exists()) {
                        Snackbar.make(view, "oh oh this isn't right" + f.getAbsolutePath() + " " + f.exists(),Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    files.add(f);
                }

                String caption = rv_caption.getText().toString();
                String purpose = rv_purpose.getText().toString();

                if (caption.isEmpty()) {
                    Snackbar.make(view, "Please fill out the caption of posting", Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
                }

                if (purpose.isEmpty()) {
                    Snackbar.make(view, "Please fill out the purpose of posting", Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
                }



                finish();
            }
        });
    }
}
