package com.auro.application.util.permission;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AlertDialog;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class PermissionUtil {
    private static final String TAG = "PermissionUtils";
    private static AlertDialog alert;

    // List of callbacks in case instance is used for multiple permission checks. Uses Dictionary to permit removing reference once complete.
    @SuppressLint("UseSparseArrays")
    private final int mNextCallback = 0;
    private static final String[] allPermissions = new String[]{CAMERA,
            READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE,
            READ_CONTACTS,
            SEND_SMS, READ_SMS,
            CALL_PHONE,
            RECORD_AUDIO,
            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
    public static final String[] mCameraPermissions = new String[]{CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    private static final String[] mCallPermissions = new String[]{CALL_PHONE};
    private static final String[] mVideoPermission = new String[]{CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, RECORD_AUDIO};
    public static final String[] mContactPermission = new String[]{READ_CONTACTS};
    public static final String[] mLocationPermission = new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
    private static final String[] mSMSPermission = new String[]{SEND_SMS, READ_SMS};
    public static final String[] mStorage = new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    private static final String[] mStorageAndLocation = new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
    private static final String[] mReceiveSmsPermissions = new String[]{RECEIVE_SMS};

}