package com.auro.application.home.data.model.passportmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassportReqModel {

//    @SerializedName("mobile_no")
//    @Expose
//    private String mobileNumber;

    @SerializedName("month")
    @Expose
    private String month;

    @SerializedName("subject")
    @Expose
    private String subject;

    @SerializedName("is_all")
    @Expose
    private String isAll;


    @SerializedName("user_id")
    @Expose
    private String userid;
    @SerializedName("user_prefered_language_id")
    @Expose
    private Integer userPreferedLanguageId;
    @SerializedName("topic_name")
    @Expose
    private String topic_name;

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public Integer getUserPreferedLanguageId() {
        return userPreferedLanguageId;
    }

    public void setUserPreferedLanguageId(Integer userPreferedLanguageId) {
        this.userPreferedLanguageId = userPreferedLanguageId;
    }

    public String getIsAll() { return isAll; }

    public void setIsAll(String isAll) { this.isAll = isAll; }

//    public String getMobileNumber() {
//        return mobileNumber;
//    }

//    public void setMobileNumber(String mobileNumber) {
//        this.mobileNumber = mobileNumber;
//    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
