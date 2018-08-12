package com.yashsoni.visualrecognitionsample.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yashsoni.visualrecognitionsample.R;
import com.yashsoni.visualrecognitionsample.adapters.VisualRecognitionResultAdapter;
import com.yashsoni.visualrecognitionsample.models.VisualRecognitionResponseModel;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    String url = "";
    ArrayList<VisualRecognitionResponseModel> classes = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString("url");
            classes = getIntent().getExtras().getParcelableArrayList("classes");
        }
        initializeViews();
    }

    private void initializeViews() {
        ImageView imageView = findViewById(R.id.image);
        Glide.with(this).load(url).into(imageView);

        mRecyclerView = findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new VisualRecognitionResultAdapter(classes);
        mRecyclerView.setAdapter(mAdapter);
    }
}
