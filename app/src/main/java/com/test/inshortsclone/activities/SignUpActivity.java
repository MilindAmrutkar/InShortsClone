package com.test.inshortsclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.test.inshortsclone.R;
import com.test.inshortsclone.utils.NetworkUtils;
import com.test.inshortsclone.utils.Utility;

public class SignUpActivity extends AppCompatActivity {

    public static final String emailSharedPrefKey = "emailKey";
    private static final String TAG = SignUpActivity.class.getSimpleName();
    String email = "";
    private EditText etEmail, etPassword;
    private Button btnSignUp;

    //SharedPreferences to store user session
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //for displaying up navigation arrow on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.signUpButton);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);

        //for performing action click of a sign up button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                //Email address validation
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!email.matches(emailPattern)) {
                    Toast.makeText(SignUpActivity.this,
                            "Invalid email address. ",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //Password field check
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Password is too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (NetworkUtils.isOnline(SignUpActivity.this)) {

                    progressBar.setVisibility(View.VISIBLE);

                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                               /* Toast.makeText(SignUpActivity.this,
                                        "Registration status: " + task.isSuccessful(),
                                        Toast.LENGTH_SHORT).show();*/
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this,
                                                "Registration successful.",
                                                Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        Utility.setDefaults(emailSharedPrefKey, email, SignUpActivity.this);
                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                        finish();
                                    } else if (!task.isSuccessful()) {
                                    /*Toast.makeText(SignUpActivity.this,
                                            "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();*/
                                        Toast.makeText(SignUpActivity.this,
                                                "Authentication failed. Please try again. ",
                                                Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);


                                    }
                                }
                            });
                } else {
                    Toast.makeText(SignUpActivity.this, "Network issue. Switch on the internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
