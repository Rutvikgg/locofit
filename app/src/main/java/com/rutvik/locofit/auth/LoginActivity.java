package com.rutvik.locofit.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rutvik.locofit.BaseActivity;
import com.rutvik.locofit.R;
import com.rutvik.locofit.util.DBHandler;

public class LoginActivity extends Activity {
    private Button signinBtn;
    private TextView signupInsteadBtn, loginShowMessageTextView;
    private EditText usernameField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signinBtn = findViewById(R.id.signinBtn);
        signupInsteadBtn = findViewById(R.id.signupInsteadBtn);
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        loginShowMessageTextView = findViewById(R.id.loginShowMessageTextView);
        SharedPreferences sharedPreferences = getSharedPreferences("com.rutvik.locofit.SHAREDPREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                DBHandler dbHandler = new DBHandler(LoginActivity.this);
                if(username.equals("") || password.equals("")){
                   loginShowMessageTextView.setText("Both fields are required!");
                } else if (dbHandler.containsUser(username, password)) {
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                    startActivity(intent);
                }
                else {
                    loginShowMessageTextView.setText("This User does not Exists!");
                }
            }
        });

        signupInsteadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed(){
        // Do nothing
    }
}