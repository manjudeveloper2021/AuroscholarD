package com.auro.application.teacher.data.model.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataListPassportStudentDetailResModel {

    @SerializedName("subject_name")
    @Expose
    private String subject_name;

    @SerializedName("quiz_data")
    @Expose
    private List<QuizListPassportStudentDetailResModel> quiz_data = null;

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public List<QuizListPassportStudentDetailResModel> getQuiz_data() {
        return quiz_data;
    }

    public void setQuiz_data(List<QuizListPassportStudentDetailResModel> quiz_data) {
        this.quiz_data = quiz_data;
    }
}