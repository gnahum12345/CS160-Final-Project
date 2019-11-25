package com.example.gabriel.sociala;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//
        final EditText etUsername = findViewById(R.id.username);
        final EditText etPassword = findViewById(R.id.password);
        final EditText etEmail = findViewById(R.id.email);
        final EditText confEmail = findViewById(R.id.conf_email);
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
                ParseUser user = new ParseUser();
                user.setEmail(email);
                user.setPassword(password);
                user.setUsername(username);
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

}
