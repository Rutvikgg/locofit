package com.rutvik.locofit.util;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rutvik.locofit.R;
import com.rutvik.locofit.models.Exercise;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<Exercise> exerciseArrayList;
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
    SimpleDateFormat inputTimeFormat = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat outputTimeFormat = new SimpleDateFormat("hh:mm a");
    public HistoryRecyclerViewAdapter(Context context, ArrayList<Exercise> exerciseArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.exerciseArrayList = exerciseArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public HistoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        return new HistoryRecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.hcexerciseLabelView.setText(exerciseArrayList.get(position).getExerciseType().toUpperCase());
        holder.hcexerciseImgView.setImageResource(exerciseArrayList.get(position).getImgResource());
        String date = exerciseArrayList.get(position).getOnDate();
        String time = exerciseArrayList.get(position).getOnTime();
        try {
            Date inputDate = inputDateFormat.parse(date);
            String outputDateString = outputDateFormat.format(inputDate);
            Date inputTime = inputTimeFormat.parse(time);
            String outputTime = outputTimeFormat.format(inputTime);
            holder.hcdateTimeView.setText(outputDateString + ", " + outputTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return exerciseArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView hcexerciseImgView;
        TextView hcexerciseLabelView, hcdateTimeView;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            hcexerciseImgView = itemView.findViewById(R.id.hcexerciseImgView);
            hcexerciseLabelView = itemView.findViewById(R.id.hcexerciseLabelView);
            hcdateTimeView = itemView.findViewById(R.id.hcdateTimeView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
