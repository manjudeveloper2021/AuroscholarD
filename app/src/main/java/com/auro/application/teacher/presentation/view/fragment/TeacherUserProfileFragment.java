package com.auro.application.teacher.presentation.view.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.auro.application.core.common.Status.DISTRICT;
import static com.auro.application.core.common.Status.GENDER;
import static com.auro.application.core.common.Status.GET_PROFILE_TEACHER_API;
import static com.auro.application.core.common.Status.SCHOOL;
import static com.auro.application.core.common.Status.STATE;
import static com.auro.application.core.common.Status.UPDATE_TEACHER_PROFILE_API;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.common.ValidationModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.FragmentTeacherNewprofileBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.DistrictData;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.SchoolData;
import com.auro.application.home.data.model.StateData;
import com.auro.application.home.data.model.StudentResponselDataModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithoutPin;
import com.auro.application.home.presentation.view.activity.SplashScreenAnimationActivity;
import com.auro.application.home.presentation.view.activity.StudentProfileActivity;
import com.auro.application.home.presentation.view.adapter.DistrictSpinnerUserAdapter;
import com.auro.application.home.presentation.view.adapter.GenderSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SchoolSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.StateSpinnerUserAdapter;
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.common.StateDataModel;
import com.auro.application.teacher.data.model.request.TeacherReqModel;
import com.auro.application.teacher.data.model.response.AddNewSchoolResModel;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.teacher.data.model.response.School;
import com.auro.application.teacher.presentation.view.activity.TeacherProfileActivity;
import com.auro.application.teacher.presentation.view.adapter.ProfileScreenAdapter;
import com.auro.application.teacher.presentation.viewmodel.TeacherProfileViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ImageUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.auro.application.util.strings.AppStringTeacherDynamic;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.googlejavaformat.Indent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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


public class TeacherUserProfileFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnTouchListener, View.OnClickListener, CommonCallBackListner{

    @Inject
    @Named("TeacherUserProfileFragment")
    ViewModelFactory viewModelFactory;
    FragmentTeacherNewprofileBinding binding;
    TeacherProfileViewModel viewModel;
    TeacherReqModel teacherProfileModel = new TeacherReqModel();
    String TAG = TeacherUserProfileFragment.class.getSimpleName();
    List<GenderData> genderList = new ArrayList<>();
    RequestBody lRequestBody;
    List<String> genderListString = new ArrayList<>();
    String stateCode = "";
    String districtCode = "";
    String schoolName = "";
    String school_name_id;
    String GenderName = "";
    String Schoolsearch = "";
    String image_path,filename;
    PrefModel prefModel;
    Details details;
    boolean isStateRestore;
    ProfileScreenAdapter mProfileClassAdapter;
    ProfileScreenAdapter mProfileSubjectAdapter;
    MyProfileResModel model;
    List<StateData> statelist = new ArrayList<>();
    List<SchoolData> districtList = new ArrayList<>();
    String f_state,f_district,SchoolName,state_Code,district_code;
    String getschool_id;
    public TeacherUserProfileFragment() {
        // Required empty public constructor
    }
    List<String> genderLines;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (binding != null) {
            isStateRestore = true;
            return binding.getRoot();
        }
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TeacherProfileViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
        details = model.getDetails();
        init();
        setListener();
        getAllStateList();
        getGender();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void init() {
        // setListener();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        binding.etPhoneNumber.setText(prefModel.getUserMobile());
//        viewModel.getStateListData();
//        viewModel.getDistrictListData();
        setRecycleView();
        // addDropDownGender();
        handleProgress(0, "");
        AppLogger.v("GetProfiler", "Step -- 1 -- ");
        callGetTeacherApi();
        AppStringTeacherDynamic.setTeacherUserProfileStrings(binding);
        binding.addnewschool.setText(details.getAdd_school() != null   ? details.getAdd_school() : "Add School +");
        binding.autoCompleteTextView1.setHint(details.getSearch_school() != null   ? details.getSearch_school() : "Search School");
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
        binding.etDistict.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    binding.etDistict.showDropDown();
                }
            }
        });
        binding.etDistict.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.etDistict.showDropDown();
                return false;
            }
        });
        binding.etSchoolName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (districtList!=null||!districtList.isEmpty()){
                    if (!binding.etDistict.getText().toString().isEmpty()||!binding.etDistict.getText().toString().equals("")){
                        binding.etSchoolName.showDropDown();
                        addDropDownSchool(districtList);
                    }


                }


                return false;
            }
        });

        binding.etSchoolName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    if (districtList!=null||!districtList.isEmpty()){
                        if (!binding.etDistict.getText().toString().isEmpty()||!binding.etDistict.getText().toString().equals("")){
                            binding.etSchoolName.showDropDown();
                            addDropDownSchool(districtList);
                        }


                    }


                }
            }
        });
        binding.btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editsearch.getText().toString().isEmpty()||binding.editsearch.getText().toString().equals("")){
                    //     binding.editsearch.setHint("Search school here..");
                }
                else{
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);                    String search = binding.editsearch.getText().toString();
                    binding.etSchoolName.setText("");

                    String search2 = binding.editsearch.getText().toString();
                    getSchoolsearch(stateCode,districtCode,search2);


                }

            }
        });
        binding.editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.editsearch.getText().toString().isEmpty()||binding.editsearch.getText().toString().equals("")){
                    // binding.editsearch.setHint("Search school here..");
                    Schoolsearch="";
                    binding.etSchoolName.dismissDropDown();
                    getSchool(stateCode,districtCode,Schoolsearch);

                }
                else{
                    if (s.toString().equals("")||s.toString().isEmpty()){
                        binding.etSchoolName.dismissDropDown();
                    }
                    else{
                        Schoolsearch = s.toString();
                        getSchool(state_Code,district_code,s.toString());
                        //  binding.etSchoolname.showDropDown();
                    }
                }
            }
        });
        binding.txtaddnewschool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = binding.editsearch.getText().toString();
                binding.etSchoolName.setText(search);
                if (binding.editsearch.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter school name", Toast.LENGTH_SHORT).show();
                }
                else{
                    addNewSchool(stateCode,districtCode,search);
                }

            }
        });
        binding.autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem=binding.autoCompleteTextView1.getAdapter().getItem(position).toString();
                String schoolname = binding.autoCompleteTextView1.getText().toString();
                getSchoolforauto(stateCode,districtCode,"",binding.autoCompleteTextView1.getText().toString());

                binding.etSchoolName.setText(schoolname);
            }
        });
        binding.autoCompleteTextView1.setAdapter(getEmailAddressAdapter(getActivity()));
        binding.autoCompleteTextView1.setThreshold(1);
        binding.autoCompleteTextView1.setTextColor(Color.BLACK);
        binding.autoCompleteTextView1.setDropDownBackgroundResource(R.color.white);

        binding.addnewschool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
                if (binding.autoCompleteTextView1.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter school name", Toast.LENGTH_SHORT).show();
                }
                else if (binding.autoCompleteTextView1.getText().toString().startsWith(" ")){
                    Toast.makeText(getActivity(), details.getEnter_space_schoolname(), Toast.LENGTH_SHORT).show();
                }
                else{
                    String search = binding.autoCompleteTextView1.getText().toString();
                    binding.etSchoolName.setText(search);
                    addNewSchool(stateCode,districtCode,search);
                }
            }
        });
    }

    void callGetTeacherApi()
    {

        viewModel.getTeacherProfileData(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void setListener() {
        binding.skipForNow.setOnClickListener(this);
        binding.nextButton.setOnClickListener(this);
        binding.profileImage.setOnClickListener(this);
        binding.editImage.setOnClickListener(this);
        binding.etGenderDrop.setOnFocusChangeListener(this);
        binding.etGenderDrop.setOnTouchListener(this);
//        binding.etState.setOnFocusChangeListener(this);
//        binding.etState.setOnTouchListener(this);
//        binding.etDistict.setOnFocusChangeListener(this);
//        binding.etDistict.setOnTouchListener(this);
       // binding.tlDistict.setVisibility(View.GONE);
        binding.saveImagebutton.setOnClickListener(this);
        binding.logout.setOnClickListener(this);
//        binding.etSchoolName.setOnFocusChangeListener(this);
//        binding.etSchoolName.setOnTouchListener(this);


        binding.imgnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new TeacherMoreDetailFragment());
            }
        });

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_teacher_newprofile;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        AppLogger.v("Pradeep", "CLICK LISNER " + v.getId());
        if (hasFocus) {
            if (v.getId() == R.id.etGenderDrop) {
                binding.etGenderDrop.showDropDown();
            } else if (v.getId() == R.id.etState) {
                binding.etState.showDropDown();
            } else if (v.getId() == R.id.etDistict) {
                binding.etDistict.showDropDown();
            }
//            else if (v.getId() == R.id.etSchoolName) {
//                binding.etSchoolName.showDropDown();
//            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // binding.etGender.showDropDown();
        AppLogger.v("Pradeep", "CLICK LISNER " + v.getId());
        if (v.getId() == R.id.etGenderDrop) {
            binding.etGenderDrop.showDropDown();
        } else if (v.getId() == R.id.etState) {
            binding.etState.showDropDown();
        } else if (v.getId() == R.id.etDistict) {
            binding.etDistict.showDropDown();
        }
//        else if (v.getId() == R.id.etSchoolName) {
//            binding.etSchoolName.showDropDown();
//        }

        return false;
    }

    public void callingStudentUpdateProfile() {
        //652585
        String email = binding.etEmailNumber.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        if (binding.etFullName.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), details.getPlease_enetr_the_name(), Toast.LENGTH_SHORT).show();
        }

        else if (binding.etFullName.getText().toString().startsWith(" ") || binding.etFullName.getText().toString().isEmpty()){

             Toast.makeText(getActivity(), details.getEnter_space_name(), Toast.LENGTH_SHORT).show();
         }
        else if (binding.etEmailNumber.getText().toString().isEmpty()){

            Toast.makeText(getActivity(), details.getPlease_enter_your_emailid(), Toast.LENGTH_SHORT).show();
        }
        else if (!email.matches(emailPattern)){
            Toast.makeText(getActivity(), details.getPlease_enter_valid_email(), Toast.LENGTH_SHORT).show();

        }
        else if (binding.etEmailNumber.getText().toString().startsWith(" ")&& !binding.etEmailNumber.getText().toString().isEmpty()){

            Toast.makeText(getActivity(), details.getEnter_space_email(), Toast.LENGTH_SHORT).show();
        }
        else if (binding.etGenderDrop.getText().toString().isEmpty()||binding.etGenderDrop.getText().toString().equals("")||binding.etGenderDrop.getText().toString()==null||binding.etGenderDrop.getText().toString().equals("null") || binding.etGenderDrop.getText().toString().equals(genderList.get(0).getTranslatedName())){
            Toast.makeText(getActivity(), details.getPlease_select_gender(), Toast.LENGTH_SHORT).show();
        }
        else if (binding.etState.getText().toString().isEmpty()||binding.etState.getText().toString().equals("")||binding.etState.getText().toString()==null||binding.etState.getText().toString().equals("null")){
            Toast.makeText(getActivity(), details.getPlease_enter_select_state(), Toast.LENGTH_SHORT).show();
        }
        else if (binding.etDistict.getText().toString().isEmpty()||binding.etDistict.getText().toString().equals("")||binding.etDistict.getText().toString()==null||binding.etDistict.getText().toString().equals("null")){
            Toast.makeText(getActivity(), details.getPlease_enter_district(), Toast.LENGTH_SHORT).show();

        }
        else if (binding.etSchoolName.getText().toString().isEmpty()||binding.etSchoolName.getText().toString().equals("")||binding.etSchoolName.getText().toString()==null||binding.etSchoolName.getText().toString().equals("null")){
            Toast.makeText(getActivity(), details.getPlease_select_school(), Toast.LENGTH_SHORT).show();

        }
        else if (model.getTeacherProfilePic()==null||model.getTeacherProfilePic().equals("")||model.getTeacherProfilePic().equals("null")||model.getTeacherProfilePic().equals(null)){
                Toast.makeText(getActivity(), details.getUploadProfilePic(), Toast.LENGTH_SHORT).show();

        }

        else {
            teacherProfileModel.setUser_id(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
            teacherProfileModel.setMobile_no(binding.etPhoneNumber.getText().toString());
            teacherProfileModel.setTeacher_name(binding.etFullName.getText().toString());
            teacherProfileModel.setEmail_id(binding.etEmailNumber.getText().toString());
            teacherProfileModel.setGender(binding.etGenderDrop.getText().toString());


            teacherProfileModel.setState_id(stateCode);
            teacherProfileModel.setDistrict_id(districtCode);
            String schoolNameexist = String.valueOf(model.getSchoolId());

            if (!(getschool_id == null||getschool_id.equals("")||getschool_id.equals("null")||getschool_id.equals(null))){
                //    schoolName = getschool_id;
                teacherProfileModel.setSchool_id(getschool_id);
            }
            else if (binding.autoCompleteTextView1.getText().toString().equals(binding.etSchoolName.getText().toString())){
                teacherProfileModel.setSchool_id(school_name_id);
            }
            else if ((binding.autoCompleteTextView1.getText().toString().isEmpty() && !binding.etSchoolName.getText().toString().isEmpty())){
                teacherProfileModel.setSchool_id(schoolName);
            }
            else {
                teacherProfileModel.setSchool_id(schoolNameexist);
            }
            AppLogger.v("SaveProfile", schoolName + "school -- state id" + stateCode + " -- district" + districtCode);
          //ValidationModel validationModel = viewModel.teacherUseCase.checkTeacherProfileValidation(teacherProfileModel);
          //  if (validationModel.isStatus()) {
              //  AppLogger.v("InfoScreen", "Step 1 Calling api update teacherprofile ");
             //   handleProgress(4, "");
                viewModel.updateTeacherProfileData(teacherProfileModel);
         //  } else {
          //   showSnackbarError(validationModel.getMessage());
          //  }
        }
    }

    private void observeServiceResponse() {
        try {
            AppLogger.v("InfoScreen", "Step 6 observeServiceResponse");
            viewModel.serviceLiveData().observeForever(responseApi -> {
                AppLogger.v("InfoScreen", "Step 7 observeServiceResponse");
                switch (responseApi.status) {
                    case SUCCESS:
                        AppLogger.v("InfoScreen", "Step 8 observeServiceResponse");
                        if (responseApi.apiTypeStatus == UPDATE_TEACHER_PROFILE_API) {
                            AppLogger.v("InfoScreen", "Step 9 observeServiceResponse");
                            handleProgress(5, "");
                            //callGetTeacherApi();

                            openFragment(new InformationDashboardFragment());

                            // model = (MyProfileResModel) responseApi.data;

                            setDataOnUi();
                        } else if (responseApi.apiTypeStatus == GET_PROFILE_TEACHER_API) {
                            AppLogger.v("InfoScreen", "Step 10 observeServiceResponse");
                            handleProgress(1, "");
                            model = (MyProfileResModel) responseApi.data;
                            setDataOnUi();
                            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                            prefModel.setTeacherProfileResModel(model);
                            AuroAppPref.INSTANCE.setPref(prefModel);
                        }
                        break;

                    case NO_INTERNET:
                    case AUTH_FAIL:
                    case FAIL_400:
                        AppLogger.v("InfoScreen", "Step 11 observeServiceResponse");
                        handleProgress(6, (String) responseApi.data);
                        break;
                    /*For state list*/
//                    case STATE_LIST_ARRAY:
//
//                        stateDataModelList = (List<StateDataModel>) responseApi.data;
//                        AppLogger.v("InfoScreen", "Step 12 observeServiceResponse");
//                        addDropDownState();
//
//                        break;

//                    case DISTRICT_LIST_DATA:
//
//                        districtDataModels = (List<DistrictDataModel>) responseApi.data;
//                        AppLogger.v("InfoScreen", "Step 13 observeServiceResponse");
//                        districtCode = String.valueOf(districtDataModels.get(0).getDistrict_code());
//                        binding.etDistict.setText(districtDataModels.get(0).getDistrict_name());
//                        addDropDownDistrict();
//
//                        break;

                    default:
                        AppLogger.v("InfoScreen", "Step 14 observeServiceResponse");
                        handleProgress(6, (String) responseApi.data);
                        break;
                }

            });
        } catch (Exception e) {
            AppLogger.v("InfoScreen", "BUG  --- " + e.getMessage());
        }

    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }
    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(getActivity(), fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }

    private void showSnackbarError(String message, int color) {
        ViewUtil.showSnackBar(binding.getRoot(), message, color);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editImage:
            case R.id.profile_image:
                askPermission();
                break;

            case R.id.skip_for_now:
                //startDashboardActivity();
                break;

            case R.id.nextButton:

                break;

            case R.id.saveImagebutton:
                callingStudentUpdateProfile();
                break;

            case R.id.logout:
                AppLogger.v("TeacherProfile","Step 1");
                openTeacherLogoutDialog();
                break;
        }

    }




    private void openTeacherLogoutDialog() {
        LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
        Details details = model.getDetails();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String yes=getActivity().getString(R.string.yes);
        String no=getActivity().getString(R.string.no);
        builder.setMessage(details.getQuizExitTxt());
        try {

            if (model != null) {
                yes=details.getYes();
                no=details.getNo();
                builder.setMessage(details.getSureToLogout());
            }
        } catch (Exception e) {
            AppLogger.e("Excdeption", e.getMessage());
        }

        // Set the alert dialog yes button click listener
        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>"+yes+"</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when user clicked the Yes button
                // Set the TextView visibility GONE
                // tv.setVisibility(View.GONE);
                dialog.dismiss();
                logout();
                AppUtil.myClassRoomResModel = null;
            }
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>"+no+"</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when No button clicked
                dialog.dismiss();
                     /*   Toast.makeText(getApplicationContext(),
                                "No Button Clicked",Toast.LENGTH_SHORT).show();*/
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();

    }

    private void logout() {
        SharedPreferences.Editor editor1 = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
        editor1.putString("statusparentprofile", "false");
        editor1.putString("isLogin","false");
        editor1.putString("statusfillstudentprofile", "false");
        editor1.putString("statussetpasswordscreen", "false");
        editor1.putString("statuschoosegradescreen", "false");
        editor1.putString("statusopenprofileteacher", "false");
        editor1.putString("statusopendashboardteacher", "false");
        editor1.putString("statuschoosedashboardscreen", "false");
        editor1.putString("statusopenprofilewithoutpin", "false");
        editor1.putString("statussubjectpref","false");
        editor1.apply();

        SharedPreferences preferences =getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.v("TeacherProfile","Step 5");
        AuroAppPref.INSTANCE.clearPref();
        prefModel.setLogin(false);
        Intent intent = new Intent(getActivity(), SplashScreenAnimationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finishAffinity();

    }

    private void updateProfilePicTeacher() {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        String langid = prefModel.getUserLanguageId();
        String teacherid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        RequestBody userid_c = RequestBody.create(MediaType.parse("text/plain"), teacherid);
        RequestBody languageid = RequestBody.create(MediaType.parse("text/plain"), langid);

        if (image_path == null || image_path.equals("null") || image_path.equals("") || image_path.isEmpty()) {
            lRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            filename = "";
        }
        else {
            lRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), teacherProfileModel.getTeacher_profile_pic());
            filename = image_path.substring(image_path.lastIndexOf("/") + 1);
        }
        MultipartBody.Part lFile = MultipartBody.Part.createFormData("teacher_profile_pic", filename, lRequestBody);
        RemoteApi.Companion.invoke()
                .update_teacher_photo(userid_c,languageid,lFile)
                .enqueue(new Callback<MyProfileResModel>() {
                    @Override
                    public void onResponse(Call<MyProfileResModel> call, Response<MyProfileResModel> response) {
                        try {

                            if (response.code()==400){
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }



                            }
                            else if (response.isSuccessful()) {
                                Log.d(TAG, "onImageResponse: ");
                                String status = response.body().getStatus().toString();
                                String msg = response.body().getMessage();
                                if (status.equalsIgnoreCase("success")) {
                                    Toast.makeText(getActivity(), msg.toString(), Toast.LENGTH_SHORT).show();

                                }
                            }

                            else {
                                Toast.makeText(getActivity(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onImageerrorResponse: " + response.errorBody().toString());
                                //  showSnackbarError(response.message());
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyProfileResModel> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();

                        Log.d(TAG, "onImgFailure: " + t.getMessage());
                    }
                });
    }

    private void askPermission() {
//        String rationale = "For Upload Profile Picture. Camera and Storage Permission is Must.";
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(getContext(), PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
                ImagePicker.with(TeacherUserProfileFragment.this)
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLogger.e("StudentProfile", "fragment requestCode=" + requestCode);

        if (requestCode == 2404) {
            // CropImages.ActivityResult result = CropImages.getActivityResult(data);
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
                        teacherProfileModel.setTeacher_profile_pic(AppUtil.encodeToBase64(picBitmap, 50));
                    } else {
                        teacherProfileModel.setTeacher_profile_pic(bytes);
                    }
                    int new_file_size = Integer.parseInt(String.valueOf(teacherProfileModel.getTeacher_profile_pic().length / 1024));
                    AppLogger.d(TAG, "Image Path  new Size kb- " + mb + "-bytes-" + new_file_size);


                    loadimage(picBitmap);
                } catch (Exception e) {
                    AppLogger.e("StudentProfile", "fragment exception=" + e.getMessage());
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showSnackbarError(ImagePicker.getError(data));
            } else {
                Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadimage(Bitmap picBitmap) {
        RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(binding.profileimage.getContext().getResources(), picBitmap);
        circularBitmapDrawable.setCircular(true);
        binding.profileimage.setImageDrawable(circularBitmapDrawable);
        binding.editImage.setVisibility(View.VISIBLE);
        updateProfilePicTeacher();
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
       // switch (commonDataModel.getClickType()) {
//            case STATE_WISE:
//
//                StateData stateDataModel = (StateData) commonDataModel.getObject();
//                binding.etState.setText(stateDataModel.getState_name());
//                getAllDistrict(stateDataModel.getState_id());
//                Log.d("state_id", stateDataModel.getState_id());
//                binding.etDistict.setText("");
//                binding.etSchoolName.setText("");
//                stateCode = stateDataModel.getState_id();
//                f_state = stateDataModel.getState_id();
//
//                break;
//            case DISTRICT:
//                DistrictData districtData = (DistrictData) commonDataModel.getObject();
//                binding.etDistict.setText(districtData.getDistrict_name());
//                districtCode = districtData.getDistrict_id();
//                f_district = districtData.getDistrict_id();;
//                binding.etSchoolName.setText("");
//                getSchool(stateCode,districtCode,"");
//                break;
//            case SCHOOL_ID:
//                SchoolData gData = (SchoolData) commonDataModel.getObject();
//                binding.etSchoolName.setText(gData.getSCHOOL_NAME());
//                SchoolName = gData.getSCHOOL_NAME();
//
//                break;
              if (commonDataModel.getClickType()==STATE)
            {
                StateData stateDataModel = (StateData) commonDataModel.getObject();
                binding.etState.setText(stateDataModel.getState_name());
                getAllDistrict(stateDataModel.getState_id());
                Log.d("state_id", stateDataModel.getState_id());
                binding.etDistict.setText("");
                binding.etSchoolName.setText("");
                state_Code = stateDataModel.getState_id();
                stateCode = stateDataModel.getState_id();
                f_state = stateDataModel.getState_id();

            }
            else if (commonDataModel.getClickType()==DISTRICT)
            {
                DistrictData districtData = (DistrictData) commonDataModel.getObject();
                binding.etDistict.setText(districtData.getDistrict_name());
                district_code = districtData.getDistrict_id();
                f_district = districtData.getDistrict_id();;
                binding.etSchoolName.setText("");
                districtCode = districtData.getDistrict_id();
                getSchool(state_Code,district_code,"");

            }
            else if (commonDataModel.getClickType()==SCHOOL)
            {
                SchoolData gData = (SchoolData) commonDataModel.getObject();
                binding.etSchoolName.setText(gData.getSCHOOL_NAME());
                SchoolName = gData.getSCHOOL_NAME();
                schoolName= String.valueOf(gData.getID());


            }
              else if (commonDataModel.getClickType()==GENDER)
              {
                  GenderData gData = (GenderData) commonDataModel.getObject();
                  binding.etGenderDrop.setText(gData.getTranslatedName());
                  GenderName = gData.getName();

              }

    }

    private void handleProgress(int status, String message) {
        switch (status) {
            case 0:

                binding.mainParentLayout.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.progressbarLayout.pgbar.setVisibility(View.VISIBLE);
                // binding.shimmerMyClassroom.startShimmer();
                break;

            case 1:
                binding.mainParentLayout.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.progressbarLayout.pgbar.setVisibility(View.GONE);
                // binding.shimmerMyClassroom.stopShimmer();
                break;
            case 2:
                binding.progressbarLayout.pgbar.setVisibility(View.VISIBLE);
                break;
            case 3:
                binding.progressbarLayout.pgbar.setVisibility(View.GONE);
                break;
            case 4:
                binding.button.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);
                break;
            case 5:
                binding.button.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                showSnackbarError(getString(R.string.saved), Color.parseColor("#4bd964"));

                break;
            case 6:
                showSnackbarError(message);
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.mainParentLayout.setVisibility(View.GONE);
                binding.progressbarLayout.pgbar.setVisibility(View.GONE);
                //  binding.shimmerMyClassroom.stopShimmer();
                binding.errorLayout.textError.setText(message);
                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.getTeacherProfileData(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
                    }
                });
                break;
        }

    }

    public void setRecycleView() {
        //for class recycleview
        GridLayoutManager gridlayout = new GridLayoutManager(getActivity(), 2);
        gridlayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recycleViewclass.setLayoutManager(gridlayout);
        binding.recycleViewclass.setHasFixedSize(true);
        binding.recycleViewclass.setNestedScrollingEnabled(false);
        mProfileClassAdapter = new com.auro.application.teacher.presentation.view.adapter.ProfileScreenAdapter(viewModel.teacherUseCase.selectClass(""), getContext(), this);
        binding.recycleViewclass.setAdapter(mProfileClassAdapter);

        //for subject recycle view
        GridLayoutManager gridlayout2 = new GridLayoutManager(getActivity(), 2);
        gridlayout2.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recycleViewsubject.setLayoutManager(gridlayout2);
        binding.recycleViewsubject.setHasFixedSize(true);
        binding.recycleViewsubject.setNestedScrollingEnabled(false);
        mProfileSubjectAdapter = new ProfileScreenAdapter(viewModel.teacherUseCase.selectSubject(""), getContext(), this);
        binding.recycleViewsubject.setAdapter(mProfileSubjectAdapter);
    }

    public void setDataOnUi() {
        binding.etFullName.setText(model.getTeacherName());
        binding.etPhoneNumber.setText(model.getMobileNo());
        binding.etEmailNumber.setText(model.getEmailId());
        binding.etGenderDrop.setText(model.getGender());
        binding.etSchoolName.setText(model.getSchool_name());
        binding.etState.setText(model.getState_name());
        binding.etDistict.setText(model.getDistrict_name());
        try {
       schoolName = String.valueOf(model.getSchoolId());
            state_Code = String.valueOf(model.getStateId());
            stateCode = String.valueOf(model.getStateId());
           // if (state_Code!=null || !state_Code.isEmpty() || !state_Code.equals("")){
              //  getAllDistrict(state_Code);
          //  }
            district_code = String.valueOf(model.getDistrictId());
            districtCode = String.valueOf(model.getDistrictId());
            getAllDistrict(state_Code);

            district_code = String.valueOf(model.getDistrictId());
           // if (district_code!=null || !district_code.isEmpty() || !district_code.equals("")){
                getSchool(state_Code,district_code,"");
           // }



            addDropDownGender();
        } catch (Exception e) {
            AppLogger.v("SchoolIdCheck", e.getMessage());
        }
        if (!TextUtil.isEmpty(model.getTeacherProfilePic())) {
            ImageUtil.loadCircleImage(binding.profileimage, model.getTeacherProfilePic());
        }

        // binding.etSchoolName.setText(model.);
    }

    public void addDropDownGender() {
        GenderSpinnerAdapter adapter = new GenderSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, genderList, this);
        binding.etGenderDrop.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etGenderDrop.setThreshold(1);
        binding.etGenderDrop.setTextColor(Color.BLACK);
    }

    private void getGender()
    {
        genderList.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("key","Gender");
        map_data.put("language_id",prefModel.getUserLanguageId());
        RemoteApi.Companion.invoke().getAllData(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.GenderDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.GenderDataModel> call, Response<com.auro.application.home.data.model.GenderDataModel> response)
                    {
                        if (response.isSuccessful())
                        {

                            for ( int i=0 ;i < response.body().getResult().size();i++)
                            {
                                int gender_id = response.body().getResult().get(i).getID();
                                String gender_name = response.body().getResult().get(i).getName();
                                String translated_name = response.body().getResult().get(i).getTranslatedName();

                                GenderData districtData = new  GenderData(gender_id,translated_name,gender_name);
                                genderList.add(districtData);
                               // binding.etGenderDrop.setText(genderList.get(0).getTranslatedName());

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

    private void getAllStateList()
    {
        statelist.clear();
        RemoteApi.Companion.invoke().getStateData()
                .enqueue(new Callback<com.auro.application.home.data.model.StateDataModel>()
                {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.StateDataModel> call, Response<com.auro.application.home.data.model.StateDataModel> response)
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
                                statelist.add(stateData);
                            }
                            addDropDownState(statelist);
                        }
                        else
                        {
                            Log.d(TAG, "onResponser: "+response.message().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.StateDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }
    private void getAllDistrict(String state_id)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
//        ProgressDialog progress = new ProgressDialog(getActivity());
//        progress.setTitle(details.getProcessing());
//        progress.setMessage(details.getProcessing());
//        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
//        progress.show();
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
                           // progress.dismiss();
                        }
                        else
                        {
                            Log.d(TAG, "onResponseError: "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.DistrictDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }
    private void getSchool(String state_id, String district_id, String search)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
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
                        if (response.isSuccessful()) {
                            districtList.clear();
                            genderListString.clear();
                            if (response.body().getSchools() != null || !response.body().getSchools().isEmpty()) {
                                Log.d(TAG, "onDistrictResponse: " + response.message());
                                for (int i = 0; i < response.body().getSchools().size(); i++) {
                                    int school_id = response.body().getSchools().get(i).getID();
                                    String school_name = response.body().getSchools().get(i).getSCHOOL_NAME();

                                    Log.d(TAG, "onDistrictResponse: " + school_name);
                                    SchoolData districtData = new SchoolData(school_name, school_id);
                                    districtList.add(districtData);
                                    genderListString.add(school_name);

                                }

                            }



                        }

                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.SchoolDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }

    private void getSchoolforauto(String state_id, String district_id, String search, String schoolname)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
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
                        if (response.isSuccessful()) {
                            districtList.clear();
                            genderListString.clear();
                            if (response.body().getSchools() != null || !response.body().getSchools().isEmpty()) {
                                Log.d(TAG, "onDistrictResponse: " + response.message());
                                for (int i = 0; i < response.body().getSchools().size(); i++) {
                                    if (schoolname.equals(response.body().getSchools().get(i).getSCHOOL_NAME())){
                                        school_name_id = String.valueOf(response.body().getSchools().get(i).getID());
                                        teacherProfileModel.setSchool_id(school_name_id);
                                    }

                                }

                            }



                        }

                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.SchoolDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }

    private void getSchoolsearch(String state_id, String district_id, String search)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle(details.getProcessing());
        progress.setMessage(details.getProcessing());
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();

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
                            districtList.clear();
                            progress.dismiss();
                            Log.d(TAG, "onDistrictResponse: "+response.message());
                            if (response.body().getSchools()!=null||!response.body().getSchools().isEmpty()) {
                                for (int i = 0; i < response.body().getSchools().size(); i++) {
                                    int school_id = response.body().getSchools().get(i).getID();
                                    String school_name = response.body().getSchools().get(i).getSCHOOL_NAME();

                                    Log.d(TAG, "onDistrictResponse: " + school_name);
                                    SchoolData districtData = new SchoolData(school_name, school_id);
                                    districtList.add(districtData);


                                }
                                if (districtList != null || !districtList.isEmpty()) {
                                    progress.dismiss();
                                    if (districtList != null && districtList.size()!=0) {
                                        binding.etSchoolName.showDropDown();
                                        addDropDownSchool(districtList);
                                    } else {
                                        progress.dismiss();
                                        String schoolsearch = binding.editsearch.getText().toString();
                                      //  binding.etSchoolName.setText(schoolsearch);
                                        Toast.makeText(getActivity(), details.getNo_data_found(), Toast.LENGTH_SHORT).show();                                    }

                                }
                                else {
                                    progress.dismiss();
                                    Toast.makeText(getActivity(), details.getNo_data_found(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                progress.dismiss();
                                String searchname = binding.editsearch.getText().toString();
                              //  binding.etSchoolName.setText(searchname);
                                Toast.makeText(getActivity(), details.getNo_data_found(), Toast.LENGTH_SHORT).show();
                            }


                        }
                        else {
                            if (districtList == null || districtList.isEmpty()) {
                                progress.dismiss();
                                String schoolsearch = binding.editsearch.getText().toString();
                              //  binding.etSchoolName.setText(schoolsearch);
                                Toast.makeText(getActivity(), details.getNo_data_found(), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.SchoolDataModel> call, Throwable t)
                    {
                        progress.dismiss();
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }
    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        for (int i = 0; i < genderListString.size(); i++) {
            genderListString.set(i, genderListString.get(i));
        }
        String newschool = binding.autoCompleteTextView1.getText().toString();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.select_dialog_item,genderListString);
        return adapter;
    }
    private void addNewSchool(String state_id, String district_id, String search)
    {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        ProgressDialog progress = new ProgressDialog(getActivity());
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
                               getschool_id = "";
                                school_name_id = String.valueOf(model.getSchoolId());
                                jsonObject = new JSONObject(response.errorBody().string());
                                String message = jsonObject.getString("message");
                                Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();

                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                        }

                        else if (response.isSuccessful()) {

                            progress.dismiss();
                            getschool_id = String.valueOf(response.body().getSchoolID());

                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();



                        }
                        else{
                            progress.dismiss();
                            Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
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
    public void addDropDownState(List<StateData> stateList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + stateList.size());
        com.auro.application.home.presentation.view.adapter.StateSpinnerAdapter stateSpinnerAdapter = new com.auro.application.home.presentation.view.adapter.StateSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, stateList, this);
        binding.etState.setAdapter(stateSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etState.setThreshold(1);//will start working from first character
        binding.etState.setTextColor(Color.BLACK);
    }
    public void addDropDownDistrict(List<DistrictData> districtList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + districtList.size());
        com.auro.application.home.presentation.view.adapter.DistrictSpinnerAdapter districtSpinnerAdapter = new com.auro.application.home.presentation.view.adapter.DistrictSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, districtList, this);
        binding.etDistict.setAdapter(districtSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etDistict.setThreshold(1);//will start working from first character
        binding.etDistict.setTextColor(Color.BLACK);
    }
    public void addDropDownSchool(List<SchoolData> districtList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + districtList.size());
        com.auro.application.home.presentation.view.adapter.SchoolSpinnerAdapter districtSpinnerAdapter = new SchoolSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, districtList, this);
        binding.etSchoolName.setAdapter(districtSpinnerAdapter);//setting the adapter data into the AutoCompleteTextView
        binding.etSchoolName.setThreshold(1);//will start working from first character
        binding.etSchoolName.setTextColor(Color.BLACK);
    }

}