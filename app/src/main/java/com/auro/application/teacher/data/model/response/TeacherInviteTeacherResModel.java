package com.auro.application.teacher.data.model.response;

import com.auro.application.home.data.model.passportmodels.PassportSubjectQuizMonthModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeacherInviteTeacherResModel {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<TeacherInviteTeacherDataResModel> teacherinvitedata = null;

    public List<TeacherInviteTeacherDataResModel> getTeacherinvitedata() {
        return teacherinvitedata;
    }

    public void setTeacherinvitedata(List<TeacherInviteTeacherDataResModel> teacherinvitedata) {
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
