package com.yashsoni.visualrecognitionsample.models;

import android.os.Parcel;
import android.os.Parcelable;

public class VisualRecognitionResponseModel implements Parcelable {
    public static final Creator<VisualRecognitionResponseModel> CREATOR = new Creator<VisualRecognitionResponseModel>() {
        @Override
        public VisualRecognitionResponseModel createFromParcel(Parcel in) {
            return new VisualRecognitionResponseModel(in);
        }

        @Override
        public VisualRecognitionResponseModel[] newArray(int size) {
            return new VisualRecognitionResponseModel[size];
        }
    };
    private String className;
    private float score;

    public VisualRecognitionResponseModel() {
        className = "";
        score = 0.0F;
    }

    protected VisualRecognitionResponseModel(Parcel in) {
        className = in.readString();
        if (in.readByte() == 0) {
            score = 0.0F;
        } else {
            score = in.readFloat();
        }
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(className);
        if (score == 0.0F) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(score);
        }
    }
}
