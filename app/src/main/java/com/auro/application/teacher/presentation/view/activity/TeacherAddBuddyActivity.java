package com.auro.application.teacher.presentation.view.activity;

import static com.auro.application.core.common.Status.DISTRICT;
import static com.auro.application.core.common.Status.GENDER;
import static com.auro.application.core.common.Status.GET_PROFILE_TEACHER_API;
import static com.auro.application.core.common.Status.SCHOOL;
import static com.auro.application.core.common.Status.STATE;
import static com.auro.application.core.common.Status.UPDATE_TEACHER_PROFILE_API;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.ValidationModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivityAddteacherbuddyBinding;
import com.auro.application.databinding.FragmentTeacherNewprofileBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.DistrictData;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.SchoolData;
import com.auro.application.home.data.model.StateData;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.view.activity.SplashScreenAnimationActivity;
import com.auro.application.home.presentation.view.adapter.GenderSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SchoolSpinnerAdapter;
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.common.StateDataModel;
import com.auro.application.teacher.data.model.request.TeacherReqModel;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.teacher.data.model.response.School;
import com.auro.application.teacher.presentation.view.adapter.ProfileScreenAdapter;
import com.auro.application.teacher.presentation.view.fragment.TeacherUserProfileFragment;
import com.auro.application.teacher.presentation.viewmodel.TeacherProfileViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ImageUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringTeacherDynamic;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeacherAddBuddyActivity extends BaseActivity  {

    @Inject
    @Named("TeacherUserProfileFragment")
    ViewModelFactory viewModelFactory;
    ActivityAddteacherbuddyBinding binding;
    TeacherProfileViewModel viewModel;
    TeacherReqModel teacherProfileModel = new TeacherReqModel();
    String TAG = TeacherUserProfileFragment.class.getSimpleName();
    List<StateDataModel> stateDataModelList;
    List<DistrictDataModel> districtDataModels;
    List<School> schoolDataList;
    String stateCode = "";
    String districtCode = "";
    String schoolName = "";
    String GenderName = "";
    Details details;
    boolean isStateRestore;
    ProfileScreenAdapter mProfileClassAdapter;
    ProfileScreenAdapter mProfileSubjectAdapter;
    MyProfileResModel model;
    HashMap<String, String> subjectHashmap = new HashMap<>();
    HashMap<String, String> classHashmap = new HashMap<>();
    List<StateData> statelist = new ArrayList<>();
    List<SchoolData> districtList = new ArrayList<>();
    String f_state,f_district,SchoolName,state_Code,district_code;
    List<GenderData> genderList = new ArrayList<>();
    PrefModel prefModel;
    public TeacherAddBuddyActivity() {
        // Required empty public constructor
    }

    List<String> genderLines;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayout());
//        ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
//        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TeacherProfileViewModel.class);
        binding.setLifecycleOwner(this);


        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
         details = model.getDetails();
        init();
        setListener();


    }


    @Override
    protected void init() {
        // setListener();

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();

        AppStringTeacherDynamic.setTeacherAddBuddyStrings(binding);




    }





    @Override
    protected void setListener() {



        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
           // observeServiceResponse();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_addteacherbuddy;
    }


    @Override
    public void onBackPressed() {
        Details details1 = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(details1.getQuizExitTxt())
                .setCancelable(false)
                .setPositiveButton(details1.getYes(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton(details1.getNo(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }




    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private void showSnackbarError(String message, int color) {
        ViewUtil.showSnackBar(binding.getRoot(), message, color);
    }

}