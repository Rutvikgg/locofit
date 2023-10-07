package com.rutvik.locofit.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rutvik.locofit.BaseActivity;
import com.rutvik.locofit.R;

public class RegisterActivity extends Activity {
    private Button proceedBtn;
    private TextView registerShowMessageTextView;
    private EditText signupUsernameField, signupPasswordField, signupConfirmPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        proceedBtn = findViewById(R.id.proceedBtn);
        signupUsernameField = findViewById(R.id.signupUsernameField);
        signupPasswordField = findViewById(R.id.signupPasswordField);
        signupConfirmPasswordField = findViewById(R.id.signupConfirmPasswordField);
        registerShowMessageTextView = findViewById(R.id.registerShowMessageTextView);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = signupUsernameField.getText().toString().trim();
                String password = signupPasswordField.getText().toString().trim();
                String confirmPassword = signupConfirmPasswordField.getText().toString().trim();
                if(username.equals("") || password.equals("") || confirmPassword.equals("")){
                    registerShowMessageTextView.setText("All the fields are required!");
                } else if (password.length() <= 5) {
                    registerShowMessageTextView.setText("Password should be at least 6 characters long");
                } else if (!password.equals(confirmPassword)) {
                    registerShowMessageTextView.setText("Password and Confirm Password are not matching!");
                } else {
                    Intent intent = new Intent(RegisterActivity.this, RegisterFormActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }

            }
        });


    }
}