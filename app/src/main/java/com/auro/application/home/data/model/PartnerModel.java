package com.auro.application.home.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.auro.application.home.data.model.partnersmodel.PartnerRequiredParamModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PartnerModel  {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response_code")
    @Expose
    private String response_code;

    @SerializedName("partnerlist")
    @Expose
    private List<PartnerDetailModel> partnerlist;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public List<PartnerDetailModel> getPartnerlist() {
        return partnerlist;
    }

    public void setPartnerlist(List<PartnerDetailModel> partnerlist) {
        this.partnerlist = partnerlist;
    }
}