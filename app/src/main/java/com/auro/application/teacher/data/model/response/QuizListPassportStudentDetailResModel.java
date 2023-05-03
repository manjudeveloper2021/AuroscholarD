package com.auro.application.teacher.data.model.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizListPassportStudentDetailResModel {

    @SerializedName("topic_name")
    @Expose
    private String topic_name;

    @SerializedName("quiz_detail")
    @Expose
    private List<QuizDetailPassportStudentDetailResModel> quiz_detail = null;

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public List<QuizDetailPassportStudentDetailResModel> getQuiz_detail() {
        return quiz_detail;
    }

    public void setQuiz_detail(List<QuizDetailPassportStudentDetailResModel> quiz_detail) {
        this.quiz_detail = quiz_detail;
    }
}