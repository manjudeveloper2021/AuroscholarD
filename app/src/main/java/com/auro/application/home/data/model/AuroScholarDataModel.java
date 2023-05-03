package com.auro.application.home.data.model;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import com.auro.application.core.common.SdkCallBack;

public class AuroScholarDataModel implements Parcelable {

    String mobileNumber;
    Activity activity;
    int fragmentContainerUiId;
    String scholrId;
    String studentClass;
    String regitrationSource = "";
    String shareType = "";
    String shareIdentity = "";
    String referralLink = "";
    SdkCallBack sdkcallback;
    String partnerSource="";
    int sdkType;
    int sdkFragmentType;
    boolean isEmailVerified;
    String devicetoken="";
    String ip_address="";

    protected AuroScholarDataModel(Parcel in) {
        mobileNumber = in.readString();
        fragmentContainerUiId = in.readInt();
        scholrId = in.readString();
        studentClass = in.readString();
        regitrationSource = in.readString();
        shareType = in.readString();
        shareIdentity = in.readString();
        referralLink = in.readString();
        partnerSource = in.readString();
        sdkType = in.readInt();
        sdkFragmentType = in.readInt();
        isEmailVerified = in.readByte() != 0;
        devicetoken = in.readString();
        ip_address = in.readString();
        userId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobileNumber);
        dest.writeInt(fragmentContainerUiId);
        dest.writeString(scholrId);
        dest.writeString(studentClass);
        dest.writeString(regitrationSource);
        dest.writeString(shareType);
        dest.writeString(shareIdentity);
        dest.writeString(referralLink);
        dest.writeString(partnerSource);
        dest.writeInt(sdkType);
        dest.writeInt(sdkFragmentType);
        dest.writeByte((byte) (isEmailVerified ? 1 : 0));
        dest.writeString(devicetoken);
        dest.writeString(ip_address);
        dest.writeString(userId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AuroScholarDataModel> CREATOR = new Creator<AuroScholarDataModel>() {
        @Override
        public AuroScholarDataModel createFromParcel(Parcel in) {
            return new AuroScholarDataModel(in);
        }

        @Override
        public AuroScholarDataModel[] newArray(int size) {
            return new AuroScholarDataModel[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId;



    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public AuroScholarDataModel() {

    }





    public String getPartnerSource() {
        return partnerSource;
    }

    public void setPartnerSource(String partnerSource) {
        this.partnerSource = partnerSource;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public int getSdkType() {
        return sdkType;
    }

    public void setSdkType(int sdkType) {
        this.sdkType = sdkType;
    }

    public int getSdkFragmentType() {
        return sdkFragmentType;
    }

    public void setSdkFragmentType(int sdkFragmentType) {
        this.sdkFragmentType = sdkFragmentType;
    }

    public SdkCallBack getSdkcallback() {
        return sdkcallback;
    }

    public void setSdkcallback(SdkCallBack sdkcallback) {
        this.sdkcallback = sdkcallback;
    }

    public String getReferralLink() {
        return referralLink;
    }

    public void setReferralLink(String referralLink) {
        this.referralLink = referralLink;
    }


    public String getScholrId() {
        return scholrId;
    }

    public void setScholrId(String scholrId) {
        this.scholrId = scholrId;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getRegitrationSource() {
        return regitrationSource;
    }

    public void setRegitrationSource(String regitrationSource) {
        this.regitrationSource = regitrationSource;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getShareIdentity() {
        return shareIdentity;
    }

    public void setShareIdentity(String shareIdentity) {
        this.shareIdentity = shareIdentity;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getFragmentContainerUiId() {
        return fragmentContainerUiId;
    }

    public void setFragmentContainerUiId(int fragmentContainerUiId) {
        this.fragmentContainerUiId = fragmentContainerUiId;
    }


    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }
}
