package com.auro.application.teacher.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentPassportDetailResModel implements Parcelable {
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("student_name")
    @Expose
    private String studentName;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("kyc_status")
    @Expose
    private String kyc_status;
    @SerializedName("QuizCount")
    @Expose
    private String QuizCount;
    @SerializedName("Disbursed")
    @Expose
    private String Disbursed;
    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("teacher_id")
    @Expose
    private String teacher_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getKyc_status() {
        return kyc_status;
    }

    public void setKyc_status(String kyc_status) {
        this.kyc_status = kyc_status;
    }

    public String getQuizCount() {
        return QuizCount;
    }

    public void setQuizCount(String quizCount) {
        QuizCount = quizCount;
    }

    public String getDisbursed() {
        return Disbursed;
    }

    public void setDisbursed(String disbursed) {
        Disbursed = disbursed;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public StudentPassportDetailResModel()
    {

    }

    protected StudentPassportDetailResModel(Parcel in) {
        user_id = in.readString();
        studentName = in.readString();
        profilePic = in.readString();
        kyc_status = in.readString();
        QuizCount = in.readString();
        Disbursed = in.readString();
        grade = in.readString();
        teacher_id = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(studentName);
        dest.writeString(profilePic);
        dest.writeString(kyc_status);
        dest.writeString(QuizCount);
        dest.writeString(Disbursed);
        dest.writeString(grade);
        dest.writeString(teacher_id);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StudentPassportDetailResModel> CREATOR = new Creator<StudentPassportDetailResModel>() {
        @Override
        public StudentPassportDetailResModel createFromParcel(Parcel in) {
            return new StudentPassportDetailResModel(in);
        }

        @Override
        public StudentPassportDetailResModel[] newArray(int size) {
            return new StudentPassportDetailResModel[size];
        }
    };

    public String getStudentId() {
        return user_id;
    }

    public void setStudentId(String studentId) {
        this.user_id = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }


}
