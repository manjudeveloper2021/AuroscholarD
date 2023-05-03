package com.auro.application.teacher.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReceiveTeacherBuddyDataResModel {


    @SerializedName("teacher_name")
    @Expose
    private String teacher_name;
//    @SerializedName("user_id")
//    @Expose
//    private String user_id;
    @SerializedName("teacher_profile_pic")
    @Expose
    private String teacher_profile_pic;
    @SerializedName("accepted_status")
    @Expose
    private String accepted_status;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("teacher_id")
    @Expose
    private String teacher_id;
    @SerializedName("teacher_reffered_id")
    @Expose
    private String teacher_reffered_id;

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_reffered_id() {
        return teacher_reffered_id;
    }

    public void setTeacher_reffered_id(String teacher_reffered_id) {
        this.teacher_reffered_id = teacher_reffered_id;
    }

    public String getAccepted_status() {
        return accepted_status;
    }

    public void setAccepted_status(String accepted_status) {
        this.accepted_status = accepted_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

//    public String getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(String user_id) {
//        this.user_id = user_id;
//    }

    public String getTeacher_profile_pic() {
        return teacher_profile_pic;
    }

    public void setTeacher_profile_pic(String teacher_profile_pic) {
        this.teacher_profile_pic = teacher_profile_pic;
    }
}
