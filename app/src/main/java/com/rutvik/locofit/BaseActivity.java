package com.rutvik.locofit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.rutvik.locofit.auth.LoginActivity;
import com.rutvik.locofit.models.User;
import com.rutvik.locofit.util.DBHandler;
import com.rutvik.locofit.util.ImageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseActivity extends Activity {
    private static final int REQUEST_IMAGE_PICK = 1;
    private TextView showUserFirstName;
    private Button logoutBtn;
    private CircleImageView profilePicView;
    private User user;
    DBHandler dbHandler = new DBHandler(BaseActivity.this);
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        showUserFirstName = findViewById(R.id.showUserFirstName);
        logoutBtn = findViewById(R.id.logoutBtn);
        profilePicView = findViewById(R.id.profilePicView);
        sharedPreferences = getSharedPreferences("com.rutvik.locofit.SHAREDPREFERENCES", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);

        if(username == null || password == null){
            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            user = dbHandler.getUser(username, password);
            showUserFirstName.setText("Hi " + user.getFirstName() + "!");
            loadProfilePicture();
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                if (dbHandler.deleteUser(user)){
                    Toast.makeText(BaseActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                    dbHandler.deleteAllExcercise(user);
                }else {
                    Toast.makeText(BaseActivity.this, "User delection failed", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        profilePicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectProfilePhoto(view);
            }
        });
    }

    public void selectProfilePhoto(View view) {
        // Use an Intent to open the image picker
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri selectedImageUri = data.getData();
                Bitmap selectedImageBitmap = ImageUtil.getBitmapFromUri(BaseActivity.this, selectedImageUri);
                profilePicView.setImageBitmap(selectedImageBitmap);

            }
        }
    }
    private void loadProfilePicture() {
         sharedPreferences = getSharedPreferences("com.rutvik.locofit.SHAREDPREFERENCES", Context.MODE_PRIVATE);
        String imagePath = sharedPreferences.getString("profileSrc", null);

        if (imagePath != null) {
            // Display the saved profile picture
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            profilePicView.setImageBitmap(bitmap);
        } else {
            profilePicView.setImageResource(R.drawable.logo_landscape_round_corner);
        }
    }

    private void saveProfilePicture(Bitmap bitmap) {
        // Save the profile picture as a file
        try {
            File file = new File(getFilesDir(), "profileSrc");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();

            // Save the file path in SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences("com.rutvik.locofit.SHAREDPREFERENCES", Context.MODE_PRIVATE).edit();
            editor.putString("profileSrc", file.getAbsolutePath());
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Call this method to save the profile picture
    private void onProfilePictureChosen(Bitmap profilePictureBitmap) {
        saveProfilePicture(profilePictureBitmap);
    }
    @Override
    public void onBackPressed(){
        finishAffinity();
    }

}
