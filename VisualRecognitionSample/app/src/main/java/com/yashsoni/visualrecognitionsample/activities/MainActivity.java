package com.yashsoni.visualrecognitionsample.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;
import com.yashsoni.visualrecognitionsample.R;
import com.yashsoni.visualrecognitionsample.models.VisualRecognitionResponseModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    // IBM WATSON VISUAL RECOGNITION RELATED
    private final String API_KEY = "<YOUR-API-KEY-HERE>";

    Button btnFetchResults;
    EditText etUrl;
    ProgressBar progressBar;
    View content;
    Single<ClassifiedImages> observable;
    private float threshold = (float) 0.6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        observable = Single.create((SingleOnSubscribe<ClassifiedImages>) emitter -> {
            IamOptions options = new IamOptions.Builder()
                    .apiKey(API_KEY)
                    .build();

            VisualRecognition visualRecognition = new VisualRecognition("2018-03-19", options);
            ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                    .url(etUrl.getText().toString())
                    .classifierIds(Collections.singletonList("default"))
                    .threshold(threshold)
                    .owners(Collections.singletonList("me"))
                    .build();
            ClassifiedImages classifiedImages = visualRecognition.classify(classifyOptions).execute();
            emitter.onSuccess(classifiedImages);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void goToNext(String url, List<ClassResult> resultList) {
        progressBar.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        ArrayList<VisualRecognitionResponseModel> classes = new ArrayList<>();
        for (ClassResult result : resultList) {
            VisualRecognitionResponseModel model = new VisualRecognitionResponseModel();
            model.setClassName(result.getClassName());
            model.setScore(result.getScore());
            classes.add(model);
        }
        Intent i = new Intent(MainActivity.this, SecondActivity.class);
        i.putExtra("url", url);
        i.putParcelableArrayListExtra("classes", classes);
        startActivity(i);
    }

    private void initializeViews() {
        etUrl = findViewById(R.id.et_url);
        btnFetchResults = findViewById(R.id.btn_fetch_results);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        content = findViewById(R.id.ll_content);

        btnFetchResults.setOnClickListener(v -> {
            if (etUrl.getText().toString().endsWith(".png") || etUrl.getText().toString().endsWith(".jpg") || etUrl.getText().toString().endsWith(".jpeg")) {
                content.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                observable.subscribe(new SingleObserver<ClassifiedImages>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ClassifiedImages classifiedImages) {
                        System.out.println(classifiedImages.toString());
                        List<ClassResult> resultList = classifiedImages.getImages().get(0).getClassifiers().get(0).getClasses();
                        String url = classifiedImages.getImages().get(0).getSourceUrl();
                        goToNext(url, resultList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.getMessage());
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, "Please make sure image URL is proper!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}