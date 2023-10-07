package com.rutvik.locofit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rutvik.locofit.auth.LoginActivity;
import com.rutvik.locofit.auth.RegisterFormActivity;
import com.rutvik.locofit.models.User;
import com.rutvik.locofit.util.DBHandler;

public class BaseActivity extends Activity {
    private TextView showUserField;
    private Button logoutBtn;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        showUserField = findViewById(R.id.showUserField);
        logoutBtn = findViewById(R.id.logoutBtn);
        SharedPreferences sharedPreferences = getSharedPreferences("com.rutvik.locofit.SHAREDPREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        DBHandler dbHandler = new DBHandler(BaseActivity.this);
        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);

        if(username == null || password == null){
            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            user = dbHandler.getUser(username, password);
            showUserField.setText(user.toString());
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                if (dbHandler.deleteUser(user)){
                    Toast.makeText(BaseActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(BaseActivity.this, "User delection failed", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


}
