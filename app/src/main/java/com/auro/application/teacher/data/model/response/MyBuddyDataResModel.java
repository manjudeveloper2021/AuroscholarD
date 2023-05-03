package com.auro.application.teacher.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class
MyBuddyDataResModel {
    @SerializedName("teacher_name")
    @Expose
    private String teacher_name;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("teacher_profile_pic")
    @Expose
    private String teacher_profile_pic;
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

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTeacher_profile_pic() {
        return teacher_profile_pic;
    }

    public void setTeacher_profile_pic(String teacher_profile_pic) {
        this.teacher_profile_pic = teacher_profile_pic;
    }
}
