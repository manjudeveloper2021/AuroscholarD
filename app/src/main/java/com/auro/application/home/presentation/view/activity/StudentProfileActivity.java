package com.auro.application.home.presentation.view.activity;
import static com.auro.application.core.common.Status.DISTRICT;
import static com.auro.application.core.common.Status.GENDER;
import static com.auro.application.core.common.Status.SCHOOL;
import static com.auro.application.core.common.Status.STATE;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.OnItemClickListener;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivityParentProfileDemoBinding;
import com.auro.application.databinding.FragmentUserProfileBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.DistrictData;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.SchoolData;
import com.auro.application.home.data.model.StateData;
import com.auro.application.home.data.model.StateDataModel;
import com.auro.application.home.data.model.StateDataModelNew;
import com.auro.application.home.data.model.StudentResponselDataModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.presentation.view.adapter.DistrictSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.GenderSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SchoolSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.StateSpinnerAdapter;
import com.auro.application.home.presentation.view.fragment.BottomSheetAddUserDialog;
import com.auro.application.home.presentation.viewmodel.ParentProfileViewModel;
import com.auro.application.home.presentation.viewmodel.StudentProfileViewModel;
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.response.AddNewSchoolResModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.DeviceUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.cropper.CropImages;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.auro.application.util.strings.AppStringDynamic;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StudentProfileActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnTouchListener, View.OnClickListener, CommonCallBackListner {

    @Inject
    @Named("ParentProfileActivity")  // changed by Ankesh
    ViewModelFactory viewModelFactory;
    FragmentUserProfileBinding binding;
    String filename;
    private static final int CAMERA_REQUEST = 1888;
    StudentProfileViewModel viewModel;
    GetStudentUpdateProfile studentProfileModel = new GetStudentUpdateProfile();
    String TAG = StudentProfileActivity.class.getSimpleName();
    List<StateDataModelNew> stateDataModelNewList;
    List<DistrictDataModel> districtDataModels;
    RequestBody lRequestBody;
    List<StateDataModelNew> stateDataModelList;
    String stateCode = "";
    String districtCode = "";
    String SchoolName = "";
    String GenderName = "";
    String Schoolsearch = "";
    PrefModel prefModel;
    String fbnewToken="";
    String schoolnamechild;
    List<StateData> state_list = new ArrayList<>();
    List<GenderData> genderList = new ArrayList<>();
    List<String> genderListString = new ArrayList<>();
    String image_path,getschool_id;
    int new_file_size;
    List<SchoolData> districtList = new ArrayList<>();
    private static final int PERMISSION_REQUEST_CODE = 200;

    public StudentProfileActivity() {

    }

    List<String> genderLines;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, getLayout());
       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        binding.setLifecycleOwner(this);
        AppUtil.loadAppLogo(binding.auroScholarLogo, this);
        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            fbnewToken = instanceIdResult.getToken();
            Log.e("newTokens", fbnewToken);
        });

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setLogin(true);
        SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
        editor.putString("statusparentprofile", "false");
        editor.putString("isLogin","true");
        editor.putString("statusfillstudentprofile", "true");
        editor.putString("statussetpasswordscreen", "false");
        editor.putString("statuschoosegradescreen", "false");
        editor.putString("statuschoosedashboardscreen", "false");
        editor.putString("statusopenprofilewithoutpin", "false");
        editor.putString("statusopenprofileteacher", "false");
        editor.putString("statusopendashboardteacher", "false");
        editor.putString("statuslogin", "true");
        editor.apply();

        getAllStateList();
        getGender();
        init();
        setListener();






        binding.etState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("stateid", parent.getItemAtPosition(position).toString());
            }
        });
        binding.etState.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    binding.etState.showDropDown();
                }
            }
        });
        binding.etState.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.etState.showDropDown();
                return false;
            }
        });
        binding.etSchoolname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (districtList!=null||!districtList.isEmpty()){
                    binding.etSchoolname.showDropDown();

                    addDropDownSchool(districtList);
                }


                return false;
            }
        });
        binding.etSchoolname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    if (districtList!=null||!districtList.isEmpty()){
                        binding.etSchoolname.showDropDown();

                        addDropDownSchool(districtList);
                    }


                }
            }
        });
        binding.autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String schoolname = binding.autoCompleteTextView1.getText().toString();
                binding.etSchoolname.setText(schoolname);
            }
        });





        binding.addnewschool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
                if (binding.autoCompleteTextView1.getText().toString().isEmpty()){
                    Toast.makeText(StudentProfileActivity.this, "Please enter school name", Toast.LENGTH_SHORT).show();
                }
                else if (binding.autoCompleteTextView1.getText().toString().startsWith(" ")){
                    Toast.makeText(StudentProfileActivity.this, details.getEnter_space_schoolname(), Toast.LENGTH_SHORT).show();
                }
                else{
                    String search = binding.autoCompleteTextView1.getText().toString();
                    binding.etSchoolname.setText(search);
                    addNewSchool(stateCode,districtCode,search);
                }
            }
        });

        binding.autoCompleteTextView1.setAdapter(getEmailAddressAdapter(StudentProfileActivity.this));
        binding.autoCompleteTextView1.setThreshold(1);
        binding.autoCompleteTextView1.setTextColor(Color.BLACK);
        binding.autoCompleteTextView1.setDropDownBackgroundResource(R.color.white);

    }

    private void addNewSchool(String state_id, String district_id, String search)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        ProgressDialog progress = new ProgressDialog(StudentProfileActivity.this);
        progress.setTitle(details.getProcessing());
        progress.setMessage(details.getProcessing());
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();

        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("school_name", search);
        map_data.put("state_id",state_id);
        map_data.put("district_id",district_id);

        RemoteApi.Companion.invoke().addNewSchool(map_data)
                .enqueue(new Callback<AddNewSchoolResModel>() {
                    @Override
                    public void onResponse(Call<AddNewSchoolResModel> call, Response<AddNewSchoolResModel> response) {
                        if (response.code()==400){
                            progress.dismiss();
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response.errorBody().string());
                                String message = jsonObject.getString("message");
                                Toast.makeText(StudentProfileActivity.this,message, Toast.LENGTH_SHORT).show();

                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                        }

                        else if (response.isSuccessful()) {

                            progress.dismiss();
                            getschool_id = String.valueOf(response.body().getSchoolID());

                            Toast.makeText(StudentProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();



                        }
                        else{
                            progress.dismiss();
                            Toast.makeText(StudentProfileActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AddNewSchoolResModel> call, Throwable t)
                    {
                        progress.dismiss();
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }
    @Override
    protected void init() {
        setCurrentFlag(AppConstant.CurrentFlagStatus.SET_PROFILE_SCREEN);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        binding.etPhoneNumber.setText(prefModel.getStudentData().getUserMobile());


        AppStringDynamic.setUserProfilePageStrings(binding);

    }


    @Override
    protected void setListener() {


        binding.nextButton.setOnClickListener(this);
        binding.profileImage.setOnClickListener(this);
        binding.editImage.setOnClickListener(this);
        binding.etGender.setOnFocusChangeListener(this);
        binding.etGender.setOnTouchListener(this);




        binding.etDistict.setOnFocusChangeListener(this);
        binding.etDistict.setOnTouchListener(this);

        binding.tlDistict.setVisibility(View.VISIBLE);


        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {

        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_user_profile;
    }



    public void addDropDownDistrict(List<DistrictData> districtList) {

        DistrictSpinnerAdapter districtSpinnerAdapter = new DistrictSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, districtList, this);
        binding.etDistict.setAdapter(districtSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etDistict.setThreshold(1);//will start working from first character
        binding.etDistict.setTextColor(Color.BLACK);

    }
    public void addDropDownSchool(List<SchoolData> districtList) {

        SchoolSpinnerAdapter districtSpinnerAdapter = new SchoolSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, districtList, this);
        binding.etSchoolname.setAdapter(districtSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etSchoolname.setThreshold(1);//will start working from first character
        binding.etSchoolname.setTextColor(Color.BLACK);


    }
    private void addDropDownGender()
    {
        GenderSpinnerAdapter adapter = new GenderSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderList, this);



        binding.etGender.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etGender.setThreshold(1);
        binding.etGender.setTextColor(Color.BLACK);//will start working from first character
    }
    public void addDropDownState(List<StateData> stateList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + stateList.size());
        StateSpinnerAdapter stateSpinnerAdapter = new StateSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, stateList, this);
        binding.etState.setAdapter(stateSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etState.setThreshold(1);//will start working from first character
        binding.etState.setTextColor(Color.BLACK);

    }
    private void getAllStateList()
    {

        state_list.clear();
        RemoteApi.Companion.invoke().getStateData()
                .enqueue(new Callback<StateDataModel>()
                {
                    @Override
                    public void onResponse(Call<StateDataModel> call, Response<StateDataModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            String msg = response.body().getMessage();
                            for ( int i=0 ;i < response.body().getStates().size();i++)
                            {
                                String state_id = response.body().getStates().get(i).getState_id();
                                String state_name = response.body().getStates().get(i).getState_name();
                                Log.d(TAG, "onStateResponse: "+state_name);
                                StateData stateData = new StateData(state_name,state_id);
                                state_list.add(stateData);


                            }

                            addDropDownState(state_list);




                        }
                        else
                        {
                            Log.d(TAG, "onResponser: "+response.message().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<StateDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }
    private void getAllDistrict(String state_id)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        ProgressDialog progress = new ProgressDialog(StudentProfileActivity.this);
        progress.setTitle(details.getProcessing());
        progress.setMessage(details.getProcessing());
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();
        List<DistrictData> districtList = new ArrayList<>();
        districtList.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("state_id",state_id);
        RemoteApi.Companion.invoke().getDistrict(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.DistrictDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.DistrictDataModel> call, Response<com.auro.application.home.data.model.DistrictDataModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            progress.dismiss();
                            Log.d(TAG, "onDistrictResponse: "+response.message());
                            for ( int i=0 ;i < response.body().getDistricts().size();i++)
                            {
                                String city_id = response.body().getDistricts().get(i).getDistrict_id();
                                String city_name = response.body().getDistricts().get(i).getDistrict_name();
                                Log.d(TAG, "onDistrictResponse: "+city_name);
                                DistrictData districtData = new  DistrictData(city_name,city_id);
                                districtList.add(districtData);

                            }

                            addDropDownDistrict(districtList);
                            progress.dismiss();


                        }
                        else
                        {
                            progress.dismiss();
                            Log.d(TAG, "onResponseError: "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.DistrictDataModel> call, Throwable t)
                    {
                        progress.dismiss();
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }
    private void getSchool(String state_id, String district_id, String search)
    {


        districtList.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("state_id",state_id);
        map_data.put("district_id",district_id);
        map_data.put("search",search);
        RemoteApi.Companion.invoke().getSchool(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.SchoolDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.SchoolDataModel> call, Response<com.auro.application.home.data.model.SchoolDataModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            genderListString.clear();

                            Log.d(TAG, "onDistrictResponse: "+response.message());
                            for ( int i=0 ;i < response.body().getSchools().size();i++)
                            {
                                int school_id = response.body().getSchools().get(i).getID();
                                String school_name = response.body().getSchools().get(i).getSCHOOL_NAME();
                                genderListString.add(school_name);
                                Log.d(TAG, "onDistrictResponse: "+school_name);
                                SchoolData districtData = new  SchoolData(school_name,school_id);
                                districtList.add(districtData);
                                addDropDownSchool(districtList);
                            }


                        }
                        else
                        {

                            Log.d(TAG, "onResponseError: "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.SchoolDataModel> call, Throwable t)
                    {

                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }

    private void getGender()
    {
        genderList.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("key","Gender");
        map_data.put("language_id",prefModel.getUserLanguageId());
        Log.d("langdata",map_data.toString());
        RemoteApi.Companion.invoke().getGender(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.GenderDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.GenderDataModel> call, Response<com.auro.application.home.data.model.GenderDataModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            Log.d(TAG, "onDistrictResponse: "+response.message());
                            for ( int i=0 ;i < response.body().getResult().size();i++)
                            {
                                int gender_id = response.body().getResult().get(i).getID();
                                String gender_name = response.body().getResult().get(i).getName();
                                String translated_name = response.body().getResult().get(i).getTranslatedName();

                                GenderData districtData = new  GenderData(gender_id,translated_name,gender_name);
                                genderList.add(districtData);
                                binding.etGender.setText(genderList.get(0).getTranslatedName());

                            }
                            addDropDownGender();
                        }
                        else
                        {
                            Log.d(TAG, "onResponseError: "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.GenderDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }

    private void updateUser()
    {
        Details details1 = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        String name = binding.etFullName.getText().toString();
        String gender = GenderName;
        String email = binding.etEmail.getText().toString();

        String userid = prefModel.getStudentData().getUserId();
        String username = prefModel.getStudentData().getUserName();
        String preflanguageid = prefModel.getUserLanguageId();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (image_path == null || image_path.equals("null") || image_path.equals("") || image_path.isEmpty()) {

            Toast.makeText(this, details1.getUpload_your_photo(), Toast.LENGTH_SHORT).show();
    }
         else if (binding.etFullName.getText().toString().equals("")||binding.etFullName.getText().toString().isEmpty()){
            Toast.makeText(this, details1.getEnter_your_name(), Toast.LENGTH_SHORT).show();
        }
        else if (!binding.etEmail.getText().toString().isEmpty()&&!binding.etEmail.getText().toString().equals("")&&!email.matches(emailPattern)){

            Toast.makeText(this, details1.getPlease_enter_valid_email(), Toast.LENGTH_SHORT).show();

        }
        else if (binding.etGender.getText().toString().isEmpty()||binding.etGender.getText().toString().equals("")||binding.etGender.getText().toString().equals("Please Select Gender")||binding.etGender.getText().toString().equals("Student Gender")||genderList.get(0).getTranslatedName().equals(binding.etGender.getText().toString())){
            Toast.makeText(this, details1.getPlease_select_gender(), Toast.LENGTH_SHORT).show();

        }
        else if (binding.etState.getText().toString().isEmpty()||binding.etState.getText().toString().equals("")||binding.etState.getText().toString().equals("State")){
            Toast.makeText(this, details1.getPlease_select_state(), Toast.LENGTH_SHORT).show();

        }
        else if (binding.etDistict.getText().toString().isEmpty()||binding.etDistict.getText().toString().equals("")||binding.etDistict.getText().toString().equals("District")){
            Toast.makeText(this, details1.getPlease_select_district(), Toast.LENGTH_SHORT).show();

        }

        else if (binding.etFullName.getText().toString().startsWith(" ")){
            Toast.makeText(this, details1.getEnter_space_name(), Toast.LENGTH_SHORT).show();
        }
        else if (binding.etEmail.getText().toString().startsWith(" ") ||binding.etEmail.getText().toString().endsWith(" ")){
            Toast.makeText(this, details1.getEnter_space_email(), Toast.LENGTH_SHORT).show();
        }



        else if (binding.etSchoolname.getText().toString().isEmpty()||binding.etSchoolname.getText().toString().equals("")){

            Toast.makeText(this, details1.getPlease_select_school(), Toast.LENGTH_SHORT).show();
        }



else {
      if (districtList==null||districtList.isEmpty()){
                schoolnamechild = binding.autoCompleteTextView1.getText().toString();
            }
      else{
          schoolnamechild = binding.etSchoolname.getText().toString();
      }

            String buildversion = AppUtil.getAppVersionName();
            RequestBody useridp = RequestBody.create(MediaType.parse("text/plain"), userid);
            RequestBody buildversionp = RequestBody.create(MediaType.parse("text/plain"), buildversion);
            RequestBody namep = RequestBody.create(MediaType.parse("text/plain"), username);
            RequestBody studentname = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody stateid = RequestBody.create(MediaType.parse("text/plain"), stateCode);
            RequestBody devicetoken = RequestBody.create(MediaType.parse("text/plain"), fbnewToken);
            RequestBody districtid = RequestBody.create(MediaType.parse("text/plain"), districtCode);
            RequestBody prtnersource = RequestBody.create(MediaType.parse("text/plain"), "AURO3VE4j7");
            RequestBody schoolname = RequestBody.create(MediaType.parse("text/plain"), schoolnamechild);
            RequestBody gendertype = RequestBody.create(MediaType.parse("text/plain"), gender);
            RequestBody emailid = RequestBody.create(MediaType.parse("text/plain"), email);
            RequestBody languageid = RequestBody.create(MediaType.parse("text/plain"), preflanguageid);
            if (image_path == null || image_path.equals("null") || image_path.equals("") || image_path.isEmpty()) {
                lRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                filename = "";
            } else {
                lRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), studentProfileModel.getImageBytes());
                filename = image_path.substring(image_path.lastIndexOf("/") + 1);

            }
            MultipartBody.Part lFile = MultipartBody.Part.createFormData("user_profile_image", filename, lRequestBody);

            RemoteApi.Companion.invoke()
                        .updateuserdetail(buildversionp, schoolname, emailid, gendertype, prtnersource,
                                devicetoken, stateid, districtid, namep, useridp, studentname,languageid
                                , lFile)
                        .enqueue(new Callback<StudentResponselDataModel>() {
                            @Override
                            public void onResponse(Call<StudentResponselDataModel> call, Response<StudentResponselDataModel> response) {
                              try {
                                  if (response.code()==400){
                                      JSONObject jsonObject = null;
                                      try {
                                          jsonObject = new JSONObject(response.errorBody().string());
                                          String message = jsonObject.getString("message");
                                          Toast.makeText(StudentProfileActivity.this,message, Toast.LENGTH_SHORT).show();

                                      } catch (JSONException | IOException e) {
                                          e.printStackTrace();
                                      }



                                  }

                                  else if (response.isSuccessful()) {
                                      Log.d(TAG, "onImageResponse: ");
                                      String status = response.body().getStatus().toString();
                                      String msg = response.body().getMessage();
                                      if (status.equalsIgnoreCase("success")) {
                                          showSnackbarError(msg);
                                          startDashboardActivity();
                                      }
                                  } else {

                                      Log.d(TAG, "onImageerrorResponse: " + response.errorBody().toString());
                                      showSnackbarError(response.message());
                                  }
                              } catch (Exception e) {
                                  Toast.makeText(StudentProfileActivity.this, details1.getInternetCheck(), Toast.LENGTH_SHORT).show();
                              }
                            }

                            @Override
                            public void onFailure(Call<StudentResponselDataModel> call, Throwable t) {
                                Log.d(TAG, "onImgFailure: " + t.getMessage());
                            }
                        });

        }


    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            if (v.getId() == R.id.etGender) {
                binding.etGender.showDropDown();
            }
            else if (v.getId() == R.id.etDistict) {
                binding.etDistict.showDropDown();
                AppLogger.v("UpdatePradeep","onFocusChange distict ");
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if (v.getId() == R.id.etGender) {
            binding.etGender.showDropDown();
        } else if (v.getId() == R.id.etDistict) {
            binding.etDistict.showDropDown();
        }

        return false;
    }




    private void handleProgress(int val, String message) {
        switch (val) {
            case 0:
                binding.progressbar.pgbar.setVisibility(View.VISIBLE);
                break;

            case 1:
                binding.progressbar.pgbar.setVisibility(View.GONE);
                break;
            case 2:
                binding.progressbar.pgbar.setVisibility(View.GONE);
                showSnackbarError(message);
                break;
        }
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editImage:
            case R.id.profile_image:
                if (Build.VERSION.SDK_INT > 26) {
                    askPermission();
                }
                else{
                    askPermission();

                }

                break;

            case R.id.skip_for_now:
                startDashboardActivity();
                break;

            case R.id.nextButton:


                    updateUser();



                break;

        }

    }
    private void askPermission() {

                ImagePicker.with(StudentProfileActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLogger.e("StudentProfile", "fragment requestCode=" + requestCode);

        if (requestCode == 2404) {

            if (resultCode == RESULT_OK) {
                try {


                    Uri uri = data.getData();
                    AppLogger.e("StudentProfile", "image path=" + uri.getPath());
                    image_path = uri.getPath();
                    Bitmap picBitmap = BitmapFactory.decodeFile(uri.getPath());
                    byte[] bytes = AppUtil.encodeToBase64(picBitmap, 100);
                    long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
                    int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));

                    AppLogger.e("StudentProfile", "image size=" + uri.getPath());
                    if (file_size >= 500) {
                        studentProfileModel.setImageBytes(AppUtil.encodeToBase64(picBitmap, 50));
                    } else {
                        studentProfileModel.setImageBytes(bytes);
                    }


                    loadimage(picBitmap);
                } catch (Exception e) {
                    AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showSnackbarError(ImagePicker.getError(data));
            } else {

            }
        }
        else if (requestCode == CAMERA_REQUEST ) {

            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
            {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                image_path = String.valueOf(data.getExtras().get("data"));
                byte[] bytes = AppUtil.encodeToBase64(photo, 100);
                long mb = AppUtil.bytesIntoHumanReadable(bytes.length);
                int file_size = Integer.parseInt(String.valueOf(bytes.length / 1024));

                if (file_size >= 500) {
                    studentProfileModel.setImageBytes(AppUtil.encodeToBase64(photo, 50));
                } else {
                    studentProfileModel.setImageBytes(bytes);
                }
                loadimage(photo);
            }

        }
    }
    private void loadimage(Bitmap picBitmap) {

        RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(binding.profileimage.getContext().getResources(), picBitmap);
        circularBitmapDrawable.setCircular(true);
        binding.profileimage.setImageDrawable(circularBitmapDrawable);
        binding.editImage.setVisibility(View.VISIBLE);

    }

    private void startDashboardActivity() {
        finish();
        Intent i = new Intent(this, DashBoardMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {

        if (commonDataModel.getClickType()==STATE)
        {
            StateData stateDataModel = (StateData) commonDataModel.getObject();
            binding.etState.setText(stateDataModel.getState_name());
            binding.etDistict.setText("");
            binding.etSchoolname.setText("");
            getAllDistrict(stateDataModel.getState_id());
            Log.d("state_id", stateDataModel.getState_id());
            stateCode = stateDataModel.getState_id();
            binding.etSchoolname.dismissDropDown();
        }
        else if (commonDataModel.getClickType()==DISTRICT)
        {
            DistrictData districtData = (DistrictData) commonDataModel.getObject();
            binding.etDistict.setText(districtData.getDistrict_name());
            districtCode = districtData.getDistrict_id();
            binding.etSchoolname.setText("");
            getSchool(stateCode,districtCode,Schoolsearch);

        }
        else if (commonDataModel.getClickType()==GENDER)
        {
            GenderData gData = (GenderData) commonDataModel.getObject();
            binding.etGender.setText(gData.getTranslatedName());
            GenderName = gData.getName();


        }
        else if (commonDataModel.getClickType()==SCHOOL)
        {
            SchoolData gData = (SchoolData) commonDataModel.getObject();
            binding.etSchoolname.setText(gData.getSCHOOL_NAME());
            SchoolName = gData.getSCHOOL_NAME();

        }


    }


    private void setCurrentFlag(String setProfileScreen) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setCurrentScreenFlag(setProfileScreen);
        prefModel.setLogin(true);
        AuroAppPref.INSTANCE.setPref(prefModel);
        SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();

        editor.putString("statuslogin", "true");
        editor.apply();
    }
    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {

        for (int i = 0; i < genderListString.size(); i++) {
            genderListString.set(i, genderListString.get(i));
        }

        String newschool = binding.autoCompleteTextView1.getText().toString();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.select_dialog_item,genderListString);
        return adapter;
    }
}