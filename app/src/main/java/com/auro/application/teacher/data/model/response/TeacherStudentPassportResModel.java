package com.auro.application.teacher.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeacherStudentPassportResModel implements Parcelable {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("response_code")
    @Expose
    private int responseCode;

    @SerializedName("message")
    @Expose
    private String message;



    @SerializedName("totalCount")
    @Expose
    private String totalCount;
    @SerializedName("studentData")
    @Expose
    private List<StudentPassportDetailResModel> studentData = null;
    @SerializedName("teacherGrade")
    @Expose
    private List<TeacherGradeResModel> teacherGrade = null;

    public List<TeacherGradeResModel> getTeacherGrade() {
        return teacherGrade;
    }

    public void setTeacherGrade(List<TeacherGradeResModel> teacherGrade) {
        this.teacherGrade = teacherGrade;
    }

    public List<StudentPassportDetailResModel> getStudentData() {
        return studentData;
    }

    public void setStudentData(List<StudentPassportDetailResModel> studentData) {
        this.studentData = studentData;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    protected TeacherStudentPassportResModel(Parcel in) {
        status = in.readString();
        error = in.readByte() != 0;
        responseCode = in.readInt();
        totalCount = in.readString();
        message = in.readString();

        studentData = in.createTypedArrayList(StudentPassportDetailResModel.CREATOR);

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error ? 1 : 0));
        dest.writeInt(responseCode);
        dest.writeString(totalCount);
        dest.writeString(message);
        dest.writeTypedList(studentData);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeacherStudentPassportResModel> CREATOR = new Creator<TeacherStudentPassportResModel>() {
        @Override
        public TeacherStudentPassportResModel createFromParcel(Parcel in) {
            return new TeacherStudentPassportResModel(in);
        }

        @Override
        public TeacherStudentPassportResModel[] newArray(int size) {
            return new TeacherStudentPassportResModel[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
