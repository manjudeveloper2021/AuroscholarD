package com.auro.application.teacher.data.model.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizDetailPassportStudentDetailResModel {

    @SerializedName("quiz_name")
    @Expose
    private String quiz_name;

    @SerializedName("score")
    @Expose
    private String score;
    @SerializedName("amount_status")
    @Expose
    private String amount_status;
    @SerializedName("level2_remark")
    @Expose
    private String level2_remark;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("scholarship_amount")
    @Expose
    private String scholarship_amount;

    public String getScholarship_amount() {
        return scholarship_amount;
    }

    public void setScholarship_amount(String scholarship_amount) {
        this.scholarship_amount = scholarship_amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLevel2_remark() {
        return level2_remark;
    }

    public void setLevel2_remark(String level2_remark) {
        this.level2_remark = level2_remark;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAmount_status() {
        return amount_status;
    }

    public void setAmount_status(String amount_status) {
        this.amount_status = amount_status;
    }
}