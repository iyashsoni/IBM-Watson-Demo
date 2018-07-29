package com.yashsoni.visualrecognitionsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Button btnFetchResults;
    EditText etUrl;
    ProgressBar progressBar;
    View content;
    Single observable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        observable = Single.create((SingleOnSubscribe<ClassifiedImages>) emitter -> {
            IamOptions options = new IamOptions.Builder()
                    .apiKey("pnHAchU5aVgyNYbilfv7eaWMwRI7Pnuj8UuziM0gjXgv")
                    .build();

            VisualRecognition visualRecognition = new VisualRecognition("2018-03-19", options);
            ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                    .url(etUrl.getText().toString())
                    .classifierIds(Collections.singletonList("default"))
                    .threshold((float) 0.6)
                    .owners(Collections.singletonList("me"))
                    .build();
            emitter.onSuccess(visualRecognition.classify(classifyOptions).execute());
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void goToNext() {
        progressBar.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        startActivity(new Intent(MainActivity.this, SecondActivity.class));
    }

    private void initializeViews() {
        etUrl = findViewById(R.id.et_url);
        btnFetchResults = findViewById(R.id.btn_fetch_results);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        content = findViewById(R.id.ll_content);

        btnFetchResults.setOnClickListener(v -> {
            content.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            observable.subscribe(new SingleObserver<ClassifiedImages>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(ClassifiedImages o) {
                    System.out.println(o.toString());
                    goToNext();
                }

                @Override
                public void onError(Throwable e) {

                }
            });
        });
    }
}
