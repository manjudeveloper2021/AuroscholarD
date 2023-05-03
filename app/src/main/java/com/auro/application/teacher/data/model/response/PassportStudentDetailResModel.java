package com.auro.application.teacher.data.model.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PassportStudentDetailResModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("response_code")
    @Expose
    private String response_code;
    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("teacher_photo")
    @Expose
    private String teacher_photo;



    @SerializedName("data_list")
    @Expose
    private List<DataListPassportStudentDetailResModel> data_list = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTeacher_photo() {
        return teacher_photo;
    }

    public void setTeacher_photo(String teacher_photo) {
        this.teacher_photo = teacher_photo;
    }

    public List<DataListPassportStudentDetailResModel> getData_list() {
        return data_list;
    }

    public void setData_list(List<DataListPassportStudentDetailResModel> data_list) {
        this.data_list = data_list;
    }
}