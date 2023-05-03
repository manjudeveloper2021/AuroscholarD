package com.auro.application.teacher.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyBuddyResModel {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<MyBuddyDataResModel> teacherinvitedata = null;

    public List<MyBuddyDataResModel> getTeacherinvitedata() {
        return teacherinvitedata;
    }

    public void setTeacherinvitedata(List<MyBuddyDataResModel> teacherinvitedata) {
        this.teacherinvitedata = teacherinvitedata;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
