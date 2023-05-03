package com.auro.application.teacher.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherGradeResModel implements Parcelable {
    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("gradeName")
    @Expose
    private String gradeName;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public TeacherGradeResModel()
    {

    }

    protected TeacherGradeResModel(Parcel in) {
        grade = in.readString();
        gradeName = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(grade);
        dest.writeString(gradeName);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeacherGradeResModel> CREATOR = new Creator<TeacherGradeResModel>() {
        @Override
        public TeacherGradeResModel createFromParcel(Parcel in) {
            return new TeacherGradeResModel(in);
        }

        @Override
        public TeacherGradeResModel[] newArray(int size) {
            return new TeacherGradeResModel[size];
        }
    };




}
