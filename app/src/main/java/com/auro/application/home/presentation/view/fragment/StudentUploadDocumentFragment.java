package com.auro.application.home.presentation.view.fragment;

import static android.app.Activity.RESULT_OK;

import static com.auro.application.core.common.Status.AZURE_API;
import static com.auro.application.core.common.Status.GENDER;
import static com.auro.application.core.common.Status.UPLOAD_PROFILE_IMAGE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseDialog;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.FragmentUploadDocumentBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.CompareKYCDetailsModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.KYCDocumentDatamodel;
import com.auro.application.home.data.model.KYCInputModel;
import com.auro.application.home.data.model.KYCOCRDetailsModel;
import com.auro.application.home.data.model.KYCResListModel;
import com.auro.application.home.data.model.StateData;
import com.auro.application.home.data.model.UpdateParentProfileResModel;
import com.auro.application.home.data.model.response.StudentKycStatusResModel;
import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithPinActivity;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.view.activity.LoginActivity;
import com.auro.application.home.presentation.view.activity.ParentForgotPinActivity;
import com.auro.application.home.presentation.view.activity.StudentProfileActivity;
import com.auro.application.home.presentation.view.adapter.GenderSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.GenderSpinnerKYCAdapter;
import com.auro.application.home.presentation.viewmodel.KYCViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.cropper.CropImages;
import com.auro.application.util.network.ProgressRequestBody;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.auro.application.util.strings.AppStringDynamic;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.openjdk.tools.javac.comp.Check;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StudentUploadDocumentFragment extends BaseDialog implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {


    @Inject
    @Named("StudentUploadDocumentFragment")
    ViewModelFactory viewModelFactory;
    List<String> genderList = new ArrayList<>();
    public Activity activity;
    String TAG = "UploadDocumentFragment";
    FragmentUploadDocumentBinding binding;
    KYCViewModel kycViewModel;
    GenderData gData;
    KYCDocumentDatamodel kycDocumentDatamodel;
    StudentKycStatusResModel studentKycStatusResModel;
    ArrayList<KYCDocumentDatamodel> kycDocumentDatamodelArrayList;
    List<KYCResListModel> OCR_list = new ArrayList<>();
    List<KYCResListModel> compare_list = new ArrayList<>();
    PrefModel prefModel;
    Details details;
    AlertDialog alertDialog;
    String GenderName;
    public StudentUploadDocumentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kycDocumentDatamodel = getArguments().getParcelable(AppConstant.TeacherKYCDocumentStatus.TEACHER_DOCUMENT_MODEL_DATA);
            studentKycStatusResModel = getArguments().getParcelable(AppConstant.TeacherKYCDocumentStatus.TEACHER_RES_MODEL_DATA);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        kycViewModel = ViewModelProviders.of(this, viewModelFactory).get(KYCViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        init();
        setListener();
        setToolbar();


        return binding.getRoot();
    }

    @Override
    protected void init() {
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        details =prefModel.getLanguageMasterDynamic().getDetails();
        binding.documentTitle.setText(kycDocumentDatamodel.getDocumentName());
        AppStringDynamic.setStudentUploadDocumentStrings(binding);
    }

    @Override
    protected void setToolbar() {
    }

    @Override
    protected void setListener() {

        AppUtil.uploadCallbacksListner = this;
        binding.uploadIcon.setOnClickListener(this);
        binding.closeButton.setOnClickListener(this);
        binding.parentLayout.setOnClickListener(this);
        if (kycViewModel != null && kycViewModel.serviceLiveData().hasObservers()) {
            kycViewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_upload_document;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_icon:
                if(kycDocumentDatamodel.getDocumentName().equals("Upload Your Photo")){
                    if (Build.VERSION.SDK_INT > 26) {
                        askPermissionCamera();
                    }
                    else{
                        askPermissionCamera();
                    }
                }else{
                    if (Build.VERSION.SDK_INT > 26) {
                        askPermission();
                    }
                    else{
                        askPermission();
                        // askPermission();
                    }
                }

                break;
            case R.id.parentLayout:
                /*Nothing*/
                break;

            case R.id.closeButton:
                dismiss();
                break;
        }
    }


    private void askPermission() {
//        String rationale = "For Upload Profile Picture. Camera and Storage Permission is Must.";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(getContext(), PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
                ImagePicker.with(StudentUploadDocumentFragment.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                // permission denied, block the feature.
//                ViewUtil.showSnackBar(binding.getRoot(), rationale);
//            }
//        });
    }

    private void askPermissionCamera() {
//        String rationale = "For Upload Profile Picture. Camera and Storage Permission is Must.";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(getContext(), PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
                ImagePicker.with(StudentUploadDocumentFragment.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .cameraOnly()
                        .start();
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                // permission denied, block the feature.
//                ViewUtil.showSnackBar(binding.getRoot(), rationale);
//            }
       // });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLogger.e("StudentProfile", "fragment requestCode=" + requestCode);
        if (Build.VERSION.SDK_INT > 26) {
            if (requestCode == 2404) {


                if (resultCode == RESULT_OK) {
                    try {


//                        Uri uri = data.getData();
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
//                        AppLogger.e("StudentProfile", "image path=" + uri.getPath());
//                        String image_path = uri.getPath();
//                        Uri selectedImage = data.getData();
//                        String[] filePath = { MediaStore.Images.Media.DATA };
//                        Cursor c = getActivity().getContentResolver().query(selectedImage,filePath, null, null, null);
//                        c.moveToFirst();
//                        int columnIndex = c.getColumnIndex(filePath[0]);
//                        String picturePath = c.getString(columnIndex);
//                        c.close();
//                        Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                        byte[] bytes = AppUtil.encodeToBase64(bitmap, 100);
//                        long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                        int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//                        File f = new File("" + uri);
//                        if (!uri.getPath().isEmpty()) {
//                            handleUi(0);
//                            binding.fileNameTxt.setText(f.getName());
//                            updateKYCList(uri.getPath());
//                        }
//
//                        if (file_size >= 500) {
//                            Toast.makeText(getActivity(), "Image size is too large", Toast.LENGTH_SHORT).show();
//
//
//                        }

                        handleData(data);



                    } catch (Exception e) {
                        AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
                    }

                }

            }
            else if (requestCode == 1 ) {
                if (Build.VERSION.SDK_INT > 26) {
                    handleData(data);
//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    Uri selectedImage = data.getData();
//                    String image_path = String.valueOf(data.getExtras().get("data"));
//                    byte[] bytes = AppUtil.encodeToBase64(photo, 100);
//                    long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                    int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//
//                    File f = new File("" + selectedImage);
//                    if (!selectedImage.getPath().isEmpty()) {
//                        handleUi(0);
//                        binding.fileNameTxt.setText(f.getName());
//                        updateKYCList(selectedImage.getPath());
//                    }
//                    if (file_size >= 500) {
//                        Toast.makeText(getActivity(), "Image size is too large", Toast.LENGTH_SHORT).show();
//
//                    }

                }
                else{
                    if (resultCode == RESULT_OK) {
                        AppLogger.v("BigDes", "Sdk step 4");
                        try {
                            handleData(data);


                        } catch (Exception e) {

                        }

                    }
                }
            }
        }
        else{
            if (requestCode == 2404) {  //2404

                if (resultCode == RESULT_OK) {
                    try {

                        handleData(data);
//                        Uri uri = data.getData();
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
//                        Uri selectedImage = data.getData();
//                        AppLogger.e("StudentProfile", "image path=" + uri.getPath());
//                       String image_path = uri.getPath();
//                        Bitmap picBitmap = BitmapFactory.decodeFile(uri.getPath());
//                        byte[] bytes = AppUtil.encodeToBase64(bitmap, 100);
//                        long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                        int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//                        File f = new File("" + selectedImage);
//                        if (!selectedImage.getPath().isEmpty()) {
//                            handleUi(0);
//                            binding.fileNameTxt.setText(f.getName());
//                            updateKYCList(selectedImage.getPath());
//                        }
//                        AppLogger.e("StudentProfile", "image size=" + uri.getPath());
//                        if (file_size >= 500) {
//                            Toast.makeText(getActivity(), "Image size is too large", Toast.LENGTH_SHORT).show();
//                        }

                    } catch (Exception e) {
                        AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
                    }

                }




            }
            else if (requestCode == 1 ) {

                if (requestCode == 1 && resultCode == Activity.RESULT_OK)
                {
                    handleData(data);
//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    Uri selectedImage = data.getData();
//                  String  image_path = String.valueOf(data.getExtras().get("data"));
//                    byte[] bytes = AppUtil.encodeToBase64(photo, 100);
//                    long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                    int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//                    File f = new File("" + selectedImage);
//                    if (!selectedImage.getPath().isEmpty()) {
//                        handleUi(0);
//                        binding.fileNameTxt.setText(f.getName());
//                        updateKYCList(selectedImage.getPath());
//                    }
//                    if (file_size >= 500) {
//                        Toast.makeText(getActivity(), "Image size is too large", Toast.LENGTH_SHORT).show();
//                    }
                }

            }
        }

    }




    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private void setDataOnUI() {

    }


    void handleData(Intent data) {
        try {
            Uri uri = data.getData();
            AppLogger.v("StudentProfile", "image path=" + uri.getPath());

            Bitmap picBitmap = BitmapFactory.decodeFile(uri.getPath());
            byte[] bytes = AppUtil.encodeToBase64(picBitmap, 100);
            long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
            int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));

            AppLogger.v("StudentProfile", "image size=" + uri.getPath());
            File f = new File("" + uri);

            if (!uri.getPath().isEmpty()) {
                handleUi(0);
                binding.fileNameTxt.setText(f.getName());
                updateKYCList(uri.getPath());
            }
            if (file_size >= 500) {
                //   studentProfileModel.setImageBytes(AppUtil.encodeToBase64(picBitmap, 50));
            } else {
                //   studentProfileModel.setImageBytes(bytes);
            }
            // int new_file_size = Integer.parseInt(String.valueOf(studentProfileModel.getImageBytes().length / 1024));
            //AppLogger.d(TAG, "Image Path  new Size kb- " + mb + "-bytes-" + new_file_size);


            // loadimage(picBitmap);
        } catch (Exception e) {
            AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        AppLogger.e("StudentProfile", "onProgressUpdate=" + percentage);
        binding.pbProcessing.setProgress(percentage);
        binding.txtProgress.setText(percentage +" %");
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }


    private void observeServiceResponse() {

        kycViewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {

                case SUCCESS:
                    if (isVisible()) {
                        if (responseApi.apiTypeStatus == UPLOAD_PROFILE_IMAGE) {
                            details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
                            KYCResListModel kycResListModel = (KYCResListModel) responseApi.data;
                          //  KYCResListModel kycResListModelocr = (KYCResListModel) ((KYCResListModel) responseApi.data).getDetails();
                           // KYCResListModel kycResListModelcompare = (KYCResListModel) ((KYCResListModel) responseApi.data).getCompare();

                            //KYCOCRDetailsModel kycResListModel2 = kycResListModel.getList().listIterator();
                           // CompareKYCDetailsModel kycResListModel3 = (CompareKYCDetailsModel) ((KYCResListModel) responseApi.data).getCompare();

                            if (!kycResListModel.isError()) {
                                if (kycResListModel.getShow_popup().equals(true) || kycResListModel.getShow_popup().equals("true")) {


                                    AppUtil.commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.UPLOAD_DOC_CALLBACK, ""));
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    final View customLayout
                                            = getLayoutInflater()
                                            .inflate(
                                                    R.layout.compare_kyc_pop_up,
                                                    null);
                                    builder.setView(customLayout);
                                    builder.setCancelable(false);
                                    TextView txtconfirm = customLayout.findViewById(R.id.txtconfirm);
                                    txtconfirm.setText(details.getConfirm_kyc_detail() != null ? details.getConfirm_kyc_detail() : "Confirm KYC Details");

                                    TextInputLayout tidob = customLayout.findViewById(R.id.tiDOB);
                                    tidob.setHint(details.getDob_kyc() != null ? details.getDob_kyc() : "DOB (YYYY-MM-DD)");
                                    TextInputLayout tiAdhar = customLayout.findViewById(R.id.tiAdhar);
                                    tiAdhar.setHint(details.getAdhar_kyc() != null ? details.getAdhar_kyc() : "Adhaar no");
                                    TextInputLayout tiAddress = customLayout.findViewById(R.id.tiAddress);
                                    tiAddress.setHint(details.getAddress_kyc() != null ? details.getAddress_kyc() : "Address");
                                    TextInputLayout tiGender = customLayout.findViewById(R.id.tiGender);
                                    tiGender.setHint(details.getGender() != null ? details.getGender() : "Gender");
                                    TextInputLayout tiAdharName = customLayout.findViewById(R.id.tiAdharName);
                                    tiAdharName.setHint(details.getName() != null ? details.getName() : "Name");
                                    TextInputLayout tiPincode = customLayout.findViewById(R.id.tiPincode);
                                    tiPincode.setHint(details.getPincode_kyc() != null ? details.getPincode_kyc() : "Pincode");
                                    ImageView cancelimg = customLayout.findViewById(R.id.imgcancel);
                                    TextInputEditText txtdob = customLayout.findViewById(R.id.etDOB);
                                    TextInputEditText txtadhar = customLayout.findViewById(R.id.etAdhar);
                                    TextInputEditText txtaddress = customLayout.findViewById(R.id.etAddress);
                                    AutoCompleteTextView etGender = customLayout.findViewById(R.id.etGender);
                                    TextInputEditText txtname = customLayout.findViewById(R.id.etAdharName);
                                    TextInputEditText txtpin = customLayout.findViewById(R.id.etPincode);
                                    CheckBox checkdob = customLayout.findViewById(R.id.checkdob);
                                    CheckBox checkadhar = customLayout.findViewById(R.id.checkadhar);
                                    CheckBox checkgender = customLayout.findViewById(R.id.checkgender);
                                    CheckBox checkname = customLayout.findViewById(R.id.checkname);
                                    CheckBox checkpincode = customLayout.findViewById(R.id.checkpincode);
                                    txtdob.setText(((KYCResListModel) responseApi.data).getDetails().get(0).getDob());
                                    txtadhar.setText(((KYCResListModel) responseApi.data).getDetails().get(0).getAadhaar_no());
                                    txtaddress.setText(((KYCResListModel) responseApi.data).getDetails().get(0).getAddress());

                                    txtname.setText(((KYCResListModel)
                                            responseApi.data).getDetails().get(0).getStudent_name());
                                    txtpin.setText(((KYCResListModel) responseApi.data).getDetails().get(0).getPincode());

                                    Button buttonOk = customLayout.findViewById(R.id.buttonOk);
                                    Button buttonrej = customLayout.findViewById(R.id.buttonsave);
                                    buttonrej.setText(details.getSave());

                                    genderList.clear();
                                    HashMap<String, String> map_data = new HashMap<>();
                                    map_data.put("key", "Gender");
                                    map_data.put("language_id", prefModel.getUserLanguageId());
                                    RemoteApi.Companion.invoke().getGender(map_data)
                                            .enqueue(new Callback<com.auro.application.home.data.model.GenderDataModel>() {
                                                @Override
                                                public void onResponse(Call<com.auro.application.home.data.model.GenderDataModel> call, Response<com.auro.application.home.data.model.GenderDataModel> response) {
                                                    if (response.isSuccessful()) {
                                                        Log.d(TAG, "onDistrictResponse: " + response.message());
                                                        for (int i = 0; i < response.body().getResult().size(); i++) {
                                                            int gender_id = response.body().getResult().get(i).getID();
                                                            String gender_name = response.body().getResult().get(i).getName();
                                                            String translated_name = response.body().getResult().get(i).getTranslatedName();
                                                            genderList.add(translated_name);
                                                        }
                                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(builder.getContext(),
                                                                android.R.layout.simple_dropdown_item_1line, genderList);
                                                        etGender.setThreshold(1);
                                                        etGender.setAdapter(adapter);

                                                    } else {
                                                        Log.d(TAG, "onResponseError: " + response.message());
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<com.auro.application.home.data.model.GenderDataModel> call, Throwable t) {
                                                    Log.d(TAG, "onDistrictFailure: " + t.getMessage());
                                                }
                                            });
                                    checkdob.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                           // checkdob.setChecked(true);
                                            if (checkdob.isChecked()){
                                                txtdob.setEnabled(true);
                                                txtdob.setTextInputLayoutFocusedRectEnabled(true);

                                            }
                                            else{
                                                txtdob.setEnabled(false);

                                            }
                                        }
                                    });
                                    checkadhar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                            if (checkadhar.isChecked()){
                                                txtadhar.setEnabled(true);

                                            }
                                            else{
                                                txtadhar.setEnabled(false);

                                            }
                                        }
                                    });
                                    checkgender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                            if (checkgender.isChecked()){
                                                etGender.setEnabled(true);

                                            }
                                            else{
                                                etGender.setEnabled(false);

                                            }
                                        }
                                    });
                                    checkname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                            if (checkname.isChecked()){
                                                txtname.setEnabled(true);


                                            }
                                            else{
                                                txtname.setEnabled(false);

                                            }
                                        }
                                    });
                                    checkpincode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                            if (checkpincode.isChecked()){
                                                txtpin.setEnabled(true);


                                            }
                                            else{
                                                txtpin.setEnabled(false);

                                            }
                                        }
                                    });

                                    etGender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            String item_position = parent.getItemAtPosition(position).toString();
                                            etGender.setText(item_position);
                                        }
                                    });
                                    if (((KYCResListModel) responseApi.data).getDetails().get(0).getGender() != null || ((KYCResListModel) responseApi.data).getDetails().get(0).getGender().equals("") || ((KYCResListModel) responseApi.data).getDetails().get(0).getGender().equals("null") || ((KYCResListModel) responseApi.data).getDetails().get(0).getGender().equals(null)) {
                                        etGender.setText(((KYCResListModel) responseApi.data).getDetails().get(0).getGender());
                                    } else {
                                        etGender.setText(genderList.get(0));
                                    }
                                    etGender.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                        @Override
                                        public void onFocusChange(View v, boolean hasFocus) {
                                            if (!hasFocus) {
                                                etGender.showDropDown();
                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(builder.getContext(),
                                                        android.R.layout.simple_dropdown_item_1line, genderList);
                                                etGender.setThreshold(1);
                                                etGender.setAdapter(adapter);
                                            }
                                        }
                                    });
                                    etGender.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            etGender.showDropDown();
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(builder.getContext(),
                                                    android.R.layout.simple_dropdown_item_1line, genderList);
                                            etGender.setThreshold(1);
                                            etGender.setAdapter(adapter);
                                            return false;
                                        }
                                    });

                                    cancelimg.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();
                                        }
                                    });
                                    buttonOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            buttonrej.setVisibility(View.VISIBLE);
                                            buttonOk.setVisibility(View.GONE);
                                            txtdob.setEnabled(true);
                                            txtadhar.setEnabled(true);
                                            txtaddress.setEnabled(true);
                                            etGender.setEnabled(true);
                                            txtname.setEnabled(true);
                                            txtpin.setEnabled(true);

                                        }
                                    });
                                    txtdob.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Activity activity = new DashBoardMainActivity();
                                            // if (getActivity() != null) {
                                            Calendar newCalendar = Calendar.getInstance();
                                            DatePickerDialog mDatePickerDialog = new DatePickerDialog(builder.getContext(), new DatePickerDialog.OnDateSetListener() {

                                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                    Calendar newDate = Calendar.getInstance();
                                                    newCalendar.set(year, monthOfYear, dayOfMonth);
                                                    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                                                    final Date startDate = newCalendar.getTime();
                                                    String fdate = sd.format(startDate);

                                                    txtdob.setText(fdate);

                                                }
                                            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                                            mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                                            mDatePickerDialog.show();
                                            //  }
                                        }
                                    });
                                    buttonrej.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (txtdob.getText().toString().isEmpty()) {
                                                Toast.makeText(builder.getContext(), "Please enter your dob", Toast.LENGTH_SHORT).show();
                                            } else if (txtadhar.getText().toString().isEmpty()) {
                                                Toast.makeText(builder.getContext(), "Please enter your adhaar no.", Toast.LENGTH_SHORT).show();

                                            } else if (txtadhar.getText().toString().length() < 12 || txtadhar.getText().toString().length() > 12) {
                                                Toast.makeText(builder.getContext(), "Please enter valid adhaar no.", Toast.LENGTH_SHORT).show();
                                            } else if (etGender.getText().toString().isEmpty() || genderList.get(0).equals(etGender.getText().toString())) {
                                                Toast.makeText(builder.getContext(), details.getPlease_select_gender(), Toast.LENGTH_SHORT).show();

                                            } else if (txtname.getText().toString().isEmpty()) {
                                                Toast.makeText(builder.getContext(), details.getPlease_enetr_the_name(), Toast.LENGTH_SHORT).show();

                                            } else if (txtname.getText().toString().startsWith(" ")) {
                                                Toast.makeText(builder.getContext(), details.getEnter_space_name(), Toast.LENGTH_SHORT).show();

                                            } else if (txtpin.getText().toString().isEmpty()) {
                                                Toast.makeText(builder.getContext(), details.getEnterPin(), Toast.LENGTH_SHORT).show();
                                            } else if (txtpin.getText().toString().length() < 6) {
                                                Toast.makeText(builder.getContext(), details.getInvalid_pin(), Toast.LENGTH_SHORT).show();
                                            }
//                                            else if (isValidPinCode(txtpin.getText().toString()) == false){
//                                                Toast.makeText(builder.getContext(), "Please enter valid Pincode", Toast.LENGTH_SHORT).show();
//                                            }
                                            else {
                                                String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
                                                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                                                String langid = prefModel.getUserLanguageId();
                                                HashMap<String, String> map_data = new HashMap<>();
                                                map_data.put("user_id", userid);
                                                map_data.put("dob", txtdob.getText().toString());
                                                map_data.put("dob_fetched", ((KYCResListModel) responseApi.data).getDetails().get(0).getDob());
                                                map_data.put("aadhaar_no", txtadhar.getText().toString());
                                                map_data.put("aadhaar_no_fetched", ((KYCResListModel) responseApi.data).getDetails().get(0).getAadhaar_no());
                                                map_data.put("address", txtaddress.getText().toString());
                                                map_data.put("address_fetched", ((KYCResListModel) responseApi.data).getDetails().get(0).getAddress());
                                                map_data.put("gender", etGender.getText().toString());
                                                map_data.put("gender_fetched", ((KYCResListModel) responseApi.data).getDetails().get(0).getGender());
                                                map_data.put("aadhaar_name", txtname.getText().toString());
                                                map_data.put("aadhaar_name_fetched", ((KYCResListModel) responseApi.data).getDetails().get(0).getStudent_name());
                                                map_data.put("pincode", txtpin.getText().toString());
                                                map_data.put("pincode_fetched", ((KYCResListModel) responseApi.data).getDetails().get(0).getPincode());
                                                map_data.put("confidence", ((KYCResListModel) responseApi.data).getCompare().get(0).getConfidence());
                                                map_data.put("user_prefered_language_id", prefModel.getUserLanguageId());

                                                RemoteApi.Companion.invoke().setComparedoc(map_data)
                                                        .enqueue(new Callback<UpdateParentProfileResModel>() {
                                                            @Override
                                                            public void onResponse(Call<UpdateParentProfileResModel> call, Response<UpdateParentProfileResModel> response) {
                                                                try {

                                                                    if (response.code() == 400) {
                                                                        JSONObject jsonObject = null;
                                                                        try {
                                                                            jsonObject = new JSONObject(response.errorBody().string());
                                                                            String message = jsonObject.getString("message");
                                                                            Toast.makeText(builder.getContext(), details.getMismatch_dob(), Toast.LENGTH_SHORT).show();

                                                                        } catch (JSONException | IOException e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                    } else if (response.isSuccessful()) {
                                                                        alertDialog.dismiss();
                                                                        String msg = response.body().getMessage();
                                                                        Toast.makeText(builder.getContext(), msg, Toast.LENGTH_SHORT).show();

                                                                    } else {
                                                                        alertDialog.dismiss();
                                                                        Toast.makeText(builder.getContext(), response.message(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } catch (Exception e) {
                                                                    alertDialog.dismiss();
                                                                    Toast.makeText(builder.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    // Toast.makeText(requireActivity(), details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<UpdateParentProfileResModel> call, Throwable t) {
                                                                alertDialog.dismiss();
                                                                Toast.makeText(builder.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                                                                //  Toast.makeText(requireActivity(), details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }

                                        }
                                    });

                                    alertDialog = builder.create();
                                    alertDialog.show();

                                } else if (AppUtil.commonCallBackListner != null) {
                                    AppUtil.commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.UPLOAD_DOC_CALLBACK, ""));
                                }
                                ViewUtil.showSnackBar(binding.getRoot(), kycResListModel.getMessage(), Color.parseColor("#4bd964"));

                                dismiss();
                                handleUi(1);
                            }

                            else {
                                dismiss();
                                showSnackbarError(kycResListModel.getMessage());
                            }
                        }
                    }
                    break;

                case FAIL:
                case NO_INTERNET:
                default:
                    if (isVisible()) {
                        dismiss();
                        showSnackbarError(details.getDefaultError() != null ? details.getDefaultError() : getString(R.string.default_error));
                    }

                    break;
            }

        });
    }

    void handleUi(int status) {
        switch (status) {
            case 0:
                binding.selectDocumentLayout.setVisibility(View.GONE);
                binding.uploadingLayout.setVisibility(View.VISIBLE);
                break;

            case 1:
                binding.selectDocumentLayout.setVisibility(View.VISIBLE);
                binding.uploadingLayout.setVisibility(View.GONE);
                break;

        }
    }

    public static boolean isValidPinCode(String pinCode)
    {

        String regex
                = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
        Pattern p = Pattern.compile(regex);

        if (pinCode == null) {
            return false;
        }

        Matcher m = p.matcher(pinCode);

        return m.matches();
    }

    public static boolean isValidAadhaarNumber(String str)
    {
        String regex
                = "^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }

    private void updateKYCList(String path) {
        try {
            AppLogger.e("calluploadApi-", "Step 1");
            kycDocumentDatamodelArrayList = kycViewModel.homeUseCase.makeAdapterDocumentList(studentKycStatusResModel);
            int pos = kycDocumentDatamodel.getPosition();
            if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.ID_PROOF_FRONT_SIDE) {
                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_front.jpg");
            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.ID_PROOF_BACK_SIDE) {
                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_back.jpg");
            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.SCHOOL_ID_CARD) {
                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_school.jpg");
            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.UPLOAD_YOUR_PHOTO) {
                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("profile.jpg");
            }
            kycDocumentDatamodelArrayList.get(pos).setDocumentURi(Uri.parse(path));
            File file = new File(kycDocumentDatamodelArrayList.get(pos).getDocumentURi().getPath());
            InputStream is = AuroApp.getAppContext().getApplicationContext().getContentResolver().openInputStream(Uri.fromFile(file));
            kycDocumentDatamodelArrayList.get(pos).setImageBytes(kycViewModel.getBytes(is));
            AppLogger.e("calluploadApi-", "Step 2");

            uploadAllDocApi();
        } catch (Exception e) {
            AppLogger.e("calluploadApi-", "Step 3-" + e.getMessage());

            /*Do code here when error occur*/
        }
    }

    private void uploadAllDocApi() {
        KYCInputModel kycInputModel = new KYCInputModel();
        kycInputModel.setUser_phone("");
        kycViewModel.uploadProfileImage(kycDocumentDatamodelArrayList, kycInputModel);
    }



}



//    @Inject
//    @Named("StudentUploadDocumentFragment")
//    ViewModelFactory viewModelFactory;
//    String TAG = "UploadDocumentFragment";
//    FragmentUploadDocumentBinding binding;
//    KYCViewModel kycViewModel;
//    KYCDocumentDatamodel kycDocumentDatamodel;
//    StudentKycStatusResModel studentKycStatusResModel;
//    ArrayList<KYCDocumentDatamodel> kycDocumentDatamodelArrayList;
//
//    PrefModel prefModel;
//    Details details;
//
//    public StudentUploadDocumentFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            kycDocumentDatamodel = getArguments().getParcelable(AppConstant.TeacherKYCDocumentStatus.TEACHER_DOCUMENT_MODEL_DATA);
//            studentKycStatusResModel = getArguments().getParcelable(AppConstant.TeacherKYCDocumentStatus.TEACHER_RES_MODEL_DATA);
//
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        // Inflate the layout for this fragment
//        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
//        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
//        kycViewModel = ViewModelProviders.of(this, viewModelFactory).get(KYCViewModel.class);
//        binding.setLifecycleOwner(this);
//        setRetainInstance(true);
//        init();
//        setListener();
//        setToolbar();
//
//        return binding.getRoot();
//    }
//
//    @Override
//    protected void init() {
//
//        prefModel = AuroAppPref.INSTANCE.getModelInstance();
//        details =prefModel.getLanguageMasterDynamic().getDetails();
//        binding.documentTitle.setText(kycDocumentDatamodel.getDocumentName());
//        AppStringDynamic.setStudentUploadDocumentStrings(binding);
//    }
//
//    @Override
//    protected void setToolbar() {
//
//    }
//
//    @Override
//    protected void setListener() {
//
//        AppUtil.uploadCallbacksListner = this;
//        binding.uploadIcon.setOnClickListener(this);
//        binding.closeButton.setOnClickListener(this);
//        binding.parentLayout.setOnClickListener(this);
//        if (kycViewModel != null && kycViewModel.serviceLiveData().hasObservers()) {
//            kycViewModel.serviceLiveData().removeObservers(this);
//        } else {
//            observeServiceResponse();
//        }
//
//    }
//
//    @Override
//    protected int getLayout() {
//        return R.layout.fragment_upload_document;
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.upload_icon:
//                if(kycDocumentDatamodel.getDocumentName().equals("Upload Your Photo")){
//                    askPermissionCamera();
//                }else{
//                    //askPermission();
//                    selectImage();
//                }
//
//                break;
//            case R.id.parentLayout:
//                /*Nothing*/
//                break;
//
//            case R.id.closeButton:
//                dismiss();
//                break;
//        }
//    }
//
//
//    private void askPermission() {
//        String rationale = "For Upload Profile Picture. Camera and Storage Permission is Must.";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(getContext(), PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
//                ImagePicker.with(StudentUploadDocumentFragment.this)
//                        .crop()                    //Crop image(Optional), Check Customization for more option
//                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
//                        .start();
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                // permission denied, block the feature.
//                ViewUtil.showSnackBar(binding.getRoot(), rationale);
//            }
//        });
//    }
//
//    private void askPermissionCamera() {
//        String rationale = "For Upload Profile Picture. Camera and Storage Permission is Must.";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(getContext(), PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
//                ImagePicker.with(StudentUploadDocumentFragment.this)
//                        .crop()                    //Crop image(Optional), Check Customization for more option
//                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
//                        .cameraOnly()
//                        .start();
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                // permission denied, block the feature.
//                ViewUtil.showSnackBar(binding.getRoot(), rationale);
//            }
//        });
//    }
//    private void selectImage() {
//        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Add Photo!");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take Photo"))
//                {
//
//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
//                    if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                        startActivityForResult(cameraIntent, 1);
//                    }
//
//
//                }
//                else if (options[item].equals("Choose from Gallery"))
//                {
//                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, 2404);
//                }
//                else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
//    //
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        AppLogger.v("StudentPradeep", "fragment  Student requestCode=" + requestCode);
//
//        if (requestCode == 2404) {
//            // CropImages.ActivityResult result = CropImages.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                AppLogger.v("StudentPradeep", "handleData" );
//                handleData(data);
//            } else if (resultCode == ImagePicker.RESULT_ERROR) {
//                showSnackbarError(ImagePicker.getError(data));
//            } else {
//                // Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//    private void showSnackbarError(String message) {
//        ViewUtil.showSnackBar(binding.getRoot(), message);
//    }
//
//    private void setDataOnUI() {
//
//    }
//
//
//    void handleData(Intent data) {
//
//        if (Build.VERSION.SDK_INT > 26) {
//
//
//
//
//                    try {
//
//
//                        Uri uri = data.getData();
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
//                        AppLogger.e("StudentProfile", "image path=" + uri.getPath());
//                        String image_path = uri.getPath();
//
//                        Uri selectedImage = data.getData();
//                        String[] filePath = { MediaStore.Images.Media.DATA };
//                        Cursor c = getActivity().getContentResolver().query(selectedImage,filePath, null, null, null);
//                        c.moveToFirst();
//                        int columnIndex = c.getColumnIndex(filePath[0]);
//                        String picturePath = c.getString(columnIndex);
//                        c.close();
//                        Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                     //   loadimage(bitmap);
//                        byte[] bytes = AppUtil.encodeToBase64(bitmap, 100);
//                        long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                        int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//                        String filename = image_path.substring(image_path.lastIndexOf("/") + 1);
//                        File f = new File("" + uri);
//                        if (!uri.getPath().isEmpty()) {
//                            handleUi(0);
//                            binding.fileNameTxt.setText(f.getName());
//                            updateKYCList(uri.getPath());
//                        }
//                        if (file_size >= 500) {
//                          //  Toast.makeText(this, "Image size is too large", Toast.LENGTH_SHORT).show();
//
//                          //  studentProfileModel.setImageBytes(AppUtil.encodeToBase64(bitmap, 50));
//
//                        } else {
//
//                          //  studentProfileModel.setImageBytes(bytes);
//                        }
//
//
//
//
//
//                    }
//                    catch (Exception e) {
//                        AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
//                    }
//
//
//
//
//
//        }
//        else{
//
//
//
//                    try {
//
//
//                        Uri uri = data.getData();
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
//                        AppLogger.e("StudentProfile", "image path=" + uri.getPath());
//                        String image_path = uri.getPath();
//                        Bitmap picBitmap = BitmapFactory.decodeFile(uri.getPath());
//                        byte[] bytes = AppUtil.encodeToBase64(bitmap, 100);
//                        long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
//                        int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
//                        String filename = image_path.substring(image_path.lastIndexOf("/") + 1);
//                        File f = new File("" + uri);
//            if (!uri.getPath().isEmpty()) {
//                Toast.makeText(getActivity(), "uploading", Toast.LENGTH_SHORT).show();
//                handleUi(0);
//                binding.fileNameTxt.setText(f.getName());
//                updateKYCList(uri.getPath());
//            }
//                        AppLogger.e("StudentProfile", "image size=" + uri.getPath());
//                        if (file_size >= 500) {
//                            Toast.makeText(getActivity(), "Image size is too large", Toast.LENGTH_SHORT).show();
//
//                           // studentProfileModel.setImageBytes(AppUtil.encodeToBase64(bitmap, 50));
//                        } else {
//
//                           // studentProfileModel.setImageBytes(bytes);
//                        }
//                      //  loadimage(bitmap);
//                    } catch (Exception e) {
//                        AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
//                    }
//
//
//
//
//
//
//
//
//        }
//
//
////        try {
////            Uri uri = data.getData();
////            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
////            AppLogger.v("StudentProfile", "image path=" + uri.getPath());
////           String path = uri.getPath();
////        //    Bitmap picBitmap = BitmapFactory.decodeFile(uri.getPath());
////            byte[] bytes = AppUtil.encodeToBase64(bitmap, 100);
////            long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
////            int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));
////
////            AppLogger.v("StudentProfile", "image size=" + uri.getPath());
////           String filename = path.substring(path.lastIndexOf("/") + 1);
////            File f = new File("" + uri);
////
////            if (!uri.getPath().isEmpty()) {
////                handleUi(0);
////                binding.fileNameTxt.setText(filename);
////                updateKYCList(uri.getPath());
////            }
////            if (file_size >= 500) {
////                //   studentProfileModel.setImageBytes(AppUtil.encodeToBase64(picBitmap, 50));
////            } else {
////                //   studentProfileModel.setImageBytes(bytes);
////            }
////            // int new_file_size = Integer.parseInt(String.valueOf(studentProfileModel.getImageBytes().length / 1024));
////            //AppLogger.d(TAG, "Image Path  new Size kb- " + mb + "-bytes-" + new_file_size);
////
////
////            // loadimage(picBitmap);
////        } catch (Exception e) {
////            AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
////        }
//    }
//
//    @Override
//    public void onProgressUpdate(int percentage) {
//        AppLogger.e("StudentProfile", "onProgressUpdate=" + percentage);
//        binding.pbProcessing.setProgress(percentage);
//        binding.txtProgress.setText(percentage +" %");
//    }
//
//    @Override
//    public void onError() {
//
//    }
//
//    @Override
//    public void onFinish() {
//
//    }
//
//
//    private void observeServiceResponse() {
//
//        kycViewModel.serviceLiveData().observeForever(responseApi -> {
//            switch (responseApi.status) {
//
//                case SUCCESS:
//                    if (isVisible()) {
//                        if (responseApi.apiTypeStatus == UPLOAD_PROFILE_IMAGE) {
//                            KYCResListModel kycResListModel = (KYCResListModel) responseApi.data;
//                            if (!kycResListModel.isError()) {
//                                if(AppUtil.commonCallBackListner!=null)
//                                {
//                                    AppUtil.commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.UPLOAD_DOC_CALLBACK,""));
//                                }
//                                ViewUtil.showSnackBar(binding.getRoot(), kycResListModel.getMessage(), Color.parseColor("#4bd964"));
//
//                                // showSnackbarError(kycResListModel.getMessage());
//                                dismiss();
//                                handleUi(1);
//                            } else {
//                                dismiss();
//                                showSnackbarError(kycResListModel.getMessage());
//                            }
//                        }
//                    }
//                    break;
//
//                case FAIL:
//                case NO_INTERNET:
//                default:
//                    if (isVisible()) {
//                        dismiss();
//                        showSnackbarError(details.getDefaultError() != null ? details.getDefaultError() : getString(R.string.default_error));
//
//                    }
//
//                    break;
//            }
//
//        });
//    }
//
//    void handleUi(int status) {
//        switch (status) {
//            case 0:
//                binding.selectDocumentLayout.setVisibility(View.GONE);
//                binding.uploadingLayout.setVisibility(View.VISIBLE);
//                break;
//
//            case 1:
//                binding.selectDocumentLayout.setVisibility(View.VISIBLE);
//                binding.uploadingLayout.setVisibility(View.GONE);
//                break;
//
//        }
//    }
//
//    private void updateKYCList(String path) {
//        try {
//            AppLogger.e("calluploadApi-", "Step 1");
//            kycDocumentDatamodelArrayList = kycViewModel.homeUseCase.makeAdapterDocumentList(studentKycStatusResModel);
//            int pos = kycDocumentDatamodel.getPosition();
//            if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.ID_PROOF_FRONT_SIDE) {
//                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_front.jpg");
//            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.ID_PROOF_BACK_SIDE) {
//                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_back.jpg");
//            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.SCHOOL_ID_CARD) {
//                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("id_school.jpg");
//            } else if (kycDocumentDatamodelArrayList.get(pos).getDocumentId() == AppConstant.DocumentType.UPLOAD_YOUR_PHOTO) {
//                kycDocumentDatamodelArrayList.get(pos).setDocumentFileName("profile.jpg");
//            }
//            kycDocumentDatamodelArrayList.get(pos).setDocumentURi(Uri.parse(path));
//            File file = new File(kycDocumentDatamodelArrayList.get(pos).getDocumentURi().getPath());
//            Toast.makeText(getActivity(), file.toString(), Toast.LENGTH_SHORT).show();
//
//            InputStream is = AuroApp.getAppContext().getApplicationContext().getContentResolver().openInputStream(Uri.fromFile(file));
//            kycDocumentDatamodelArrayList.get(pos).setImageBytes(kycViewModel.getBytes(is));
//            AppLogger.e("calluploadApi-", "Step 2");
//
//            uploadAllDocApi();
//        } catch (Exception e) {
//            AppLogger.e("calluploadApi-", "Step 3-" + e.getMessage());
//
//            /*Do code here when error occur*/
//        }
//    }
//
//    private void uploadAllDocApi() {
//        KYCInputModel kycInputModel = new KYCInputModel();
//        kycInputModel.setUser_phone("");
//        kycViewModel.uploadProfileImage(kycDocumentDatamodelArrayList, kycInputModel);
//    }

