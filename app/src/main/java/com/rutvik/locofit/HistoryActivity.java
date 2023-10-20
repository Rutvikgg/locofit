package com.rutvik.locofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.rutvik.locofit.exerciseActivities.StatisticsActivity;
import com.rutvik.locofit.models.Exercise;
import com.rutvik.locofit.models.User;
import com.rutvik.locofit.util.DBHandler;
import com.rutvik.locofit.util.HistoryRecyclerViewAdapter;
import com.rutvik.locofit.util.RecyclerViewInterface;

import java.util.ArrayList;

public class HistoryActivity extends Activity implements RecyclerViewInterface {
    RecyclerView historyRecyclerView;
    ArrayList<Exercise> exerciseArrayList;
    DBHandler dbHandler = new DBHandler(HistoryActivity.this);
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        user = dbHandler.getUser(username, password);
        exerciseArrayList = dbHandler.getAllExercise(user);

        HistoryRecyclerViewAdapter adapter = new HistoryRecyclerViewAdapter(this, exerciseArrayList, this);
        historyRecyclerView.setAdapter(adapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(HistoryActivity.this, StatisticsActivity.class);
        intent.putExtra("username", user.getUsername());
        intent.putExtra("password", user.getPassword());
        intent.putExtra("type", exerciseArrayList.get(position).getExerciseType());
        intent.putExtra("date", exerciseArrayList.get(position).getOnDate());
        intent.putExtra("time", exerciseArrayList.get(position).getOnTime());
        startActivity(intent);
    }
}