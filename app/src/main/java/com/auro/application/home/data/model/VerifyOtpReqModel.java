package com.auro.application.home.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtpReqModel implements Parcelable {

    @SerializedName("mobile_no")
    @Expose
    String mobileNumber;

    @SerializedName("otp_val")
    @Expose
    String otpVerify;

    @SerializedName("regitration_source")
    @Expose
    String resgistrationSource;

    @SerializedName("user_type")
    @Expose
    int userType;

    @SerializedName("device_token")
    @Expose
    String deviceToken;



    @SerializedName("sr_id")
    @Expose
    String srId;
    @SerializedName("user_prefered_language_id")
    @Expose
    String user_prefered_language_id;

    public String getUser_prefered_language_id() {
        return user_prefered_language_id;
    }

    public void setUser_prefered_language_id(String user_prefered_language_id) {
        this.user_prefered_language_id = user_prefered_language_id;
    }

    public VerifyOtpReqModel() {

    }


    protected VerifyOtpReqModel(Parcel in) {
        mobileNumber = in.readString();
        otpVerify = in.readString();
        resgistrationSource = in.readString();
        userType = in.readInt();
        deviceToken = in.readString();
        srId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobileNumber);
        dest.writeString(otpVerify);
        dest.writeString(resgistrationSource);
        dest.writeInt(userType);
        dest.writeString(deviceToken);
        dest.writeString(srId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VerifyOtpReqModel> CREATOR = new Creator<VerifyOtpReqModel>() {
        @Override
        public VerifyOtpReqModel createFromParcel(Parcel in) {
            return new VerifyOtpReqModel(in);
        }

        @Override
        public VerifyOtpReqModel[] newArray(int size) {
            return new VerifyOtpReqModel[size];
        }
    };

    public String getSrId() {
        return srId;
    }

    public void setSrId(String srId) {
        this.srId = srId;
    }


    public String getResgistrationSource() {
        return resgistrationSource;
    }

    public void setResgistrationSource(String resgistrationSource) {
        this.resgistrationSource = resgistrationSource;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOtpVerify() {
        return otpVerify;
    }

    public void setOtpVerify(String otpVerify) {
        this.otpVerify = otpVerify;
    }
}
