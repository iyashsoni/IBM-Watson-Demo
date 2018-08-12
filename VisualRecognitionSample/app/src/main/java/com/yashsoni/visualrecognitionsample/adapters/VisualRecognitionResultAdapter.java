package com.yashsoni.visualrecognitionsample.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yashsoni.visualrecognitionsample.R;
import com.yashsoni.visualrecognitionsample.models.VisualRecognitionResponseModel;

import java.util.ArrayList;

public class VisualRecognitionResultAdapter extends RecyclerView.Adapter<VisualRecognitionResultAdapter.ViewHolder> {
    private ArrayList<VisualRecognitionResponseModel> classes;

    public VisualRecognitionResultAdapter(ArrayList<VisualRecognitionResponseModel> classes) {
        this.classes = classes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.score.setText(String.valueOf(classes.get(position).getScore()));
        holder.className.setText(classes.get(position).getClassName());
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView className;
        TextView score;

        ViewHolder(LinearLayout v) {
            super(v);
            className = v.findViewById(R.id.className);
            score = v.findViewById(R.id.score);
        }
    }
}
