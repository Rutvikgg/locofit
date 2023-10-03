package com.rutvik.locofit;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);


    }
}
