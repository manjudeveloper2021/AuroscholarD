package com.auro.application.home.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PartnerDetailModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("partner_name")
    @Expose
    private String partner_name;
    @SerializedName("partner_logo")
    @Expose
    private String partner_logo;
    @SerializedName("partner_url")
    @Expose
    private String partner_url;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("Grade")
    @Expose
    private String Grade;
    @SerializedName("Subject")
    @Expose
    private String Subject;
    @SerializedName("LogoIcon")
    @Expose
    private String LogoIcon;
    @SerializedName("PartnerTypeId")
    @Expose
    private String PartnerTypeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }

    public String getPartner_logo() {
        return partner_logo;
    }

    public void setPartner_logo(String partner_logo) {
        this.partner_logo = partner_logo;
    }

    public String getPartner_url() {
        return partner_url;
    }

    public void setPartner_url(String partner_url) {
        this.partner_url = partner_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getLogoIcon() {
        return LogoIcon;
    }

    public void setLogoIcon(String logoIcon) {
        LogoIcon = logoIcon;
    }

    public String getPartnerTypeId() {
        return PartnerTypeId;
    }

    public void setPartnerTypeId(String partnerTypeId) {
        PartnerTypeId = partnerTypeId;
    }
}