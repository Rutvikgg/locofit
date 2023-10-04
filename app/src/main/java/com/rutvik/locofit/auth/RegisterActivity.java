package com.rutvik.locofit.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rutvik.locofit.BaseActivity;
import com.rutvik.locofit.R;

public class RegisterActivity extends Activity {
    private Button signupBtn;
    private EditText signupUsernameField, signupPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signupBtn = findViewById(R.id.signupBtn);
        signupUsernameField = findViewById(R.id.signupUsernameField);
        signupPasswordField = findViewById(R.id.signupPasswordField);
        SharedPreferences sharedPreferences = getSharedPreferences("com.rutvik.locofit.SHAREDPREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = signupUsernameField.getText().toString().trim();
                String password = signupPasswordField.getText().toString().trim();
                if(username.equals("") || password.equals("")){
                    Toast.makeText(RegisterActivity.this, "Both the fields are required", Toast.LENGTH_SHORT).show();
                } else if (password.length() <= 5) {
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.commit();
                    Intent intent = new Intent(RegisterActivity.this, BaseActivity.class);
                    startActivity(intent);
                }

            }
        });


    }
}