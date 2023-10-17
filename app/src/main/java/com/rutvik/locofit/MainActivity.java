package com.rutvik.locofit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.rutvik.locofit.auth.LoginActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                }
                catch (Exception e){
                    Log.d("General", "Splash screen error");
                }
                finally {
                    Intent intent = new Intent(MainActivity.this, BaseActivity.class);
                    startActivity(intent);
                }
            }
        };
        thread.start();

    }
}