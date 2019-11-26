package com.example.gabriel.sociala;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;

public class SignUpActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private ImageView ivProfileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//
        final EditText etUsername = findViewById(R.id.username);
        final EditText etPassword = findViewById(R.id.password);
        final EditText etEmail = findViewById(R.id.email);
        final EditText confEmail = findViewById(R.id.conf_email);
        ivProfileImage = findViewById(R.id.profile_image);
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: pick profile img.
                pickImageFromGallery();
            }
        });
        Button btnSignUp = findViewById(R.id.btn_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String email = etEmail.getText().toString();
                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    Snackbar.make(v, "Please fill out all fields", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (!email.equals(confEmail.getText().toString())) {
                    Snackbar.make(v, "Make sure the two emails are equal", Snackbar.LENGTH_SHORT).show();
                }
                final ParseUser user = new ParseUser();
                user.setEmail(email);
                user.setPassword(password);
                user.setUsername(username);
                Bitmap bitmap = ((BitmapDrawable) ivProfileImage.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bitmapBytes = stream.toByteArray();

                final ParseFile file = new ParseFile("profilePic.png", bitmapBytes);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        user.put("profilePic", file);
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                                        @Override
                                        public void done(ParseUser user, ParseException e) {
                                            if (e == null) {
                                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                                finish();
                                                return ;
                                            } else {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });



            }
        });

    }

    private void pickImageFromGallery() {
        //intent to pick image

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            //set image to image view
            //selectedImageView = findViewById(R.id.image_view);
            //selectedImageView.setImageURI(data.getData());


            Uri selectedImage = data.getData();

            ivProfileImage.setImageURI(selectedImage);
            Bitmap b = ((BitmapDrawable) ivProfileImage.getDrawable()).getBitmap();
            b = BitmapScalar.scaleToFill(b, 300,300);
            ivProfileImage.setImageBitmap(b);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}
