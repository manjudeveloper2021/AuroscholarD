package com.auro.application.teacher.presentation.view.fragment;

import static com.auro.application.core.common.Status.BOARD;
import static com.auro.application.core.common.Status.DISTRICT;
import static com.auro.application.core.common.Status.GENDER;
import static com.auro.application.core.common.Status.GRADE;
import static com.auro.application.core.common.Status.GRADEID;
import static com.auro.application.core.common.Status.SCHHOLMEDIUM;
import static com.auro.application.core.common.Status.SCHHOLTYPE;
import static com.auro.application.core.common.Status.SCHOOL;
import static com.auro.application.core.common.Status.STATE;
import static com.auro.application.core.common.Status.TUTION;
import static com.auro.application.core.common.Status.TUTIONTYPE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.FragmentClassRoomGroupBinding;
import com.auro.application.databinding.FragmentTeacherpassportBinding;
import com.auro.application.home.data.model.BoardData;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.DistrictData;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.GradeData;
import com.auro.application.home.data.model.GradeDataModel;
import com.auro.application.home.data.model.PrivateTutionData;
import com.auro.application.home.data.model.SchoolData;
import com.auro.application.home.data.model.SchoolLangData;
import com.auro.application.home.data.model.SchoolTypeData;
import com.auro.application.home.data.model.StateData;
import com.auro.application.home.data.model.TutionData;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.presentation.view.activity.ChildAccountsActivity;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.view.activity.StudentProfileActivity;
import com.auro.application.home.presentation.view.adapter.GradeSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SelectYourParentChildAdapter;
import com.auro.application.teacher.data.model.request.TeacherUserIdReq;
import com.auro.application.teacher.data.model.response.StudentPassportDetailResModel;
import com.auro.application.teacher.data.model.response.TeacherClassRoomResModel;
import com.auro.application.teacher.data.model.response.TeacherGradeResModel;
import com.auro.application.teacher.data.model.response.TeacherGroupRes;
import com.auro.application.teacher.data.model.response.TeacherStudentPassportResModel;
import com.auro.application.teacher.data.model.response.TotalStudentResModel;
import com.auro.application.teacher.data.model.response.UserImageInGroupResModel;
import com.auro.application.teacher.presentation.view.adapter.GroupAdapter;
import com.auro.application.teacher.presentation.view.adapter.StudentListAdapter;
import com.auro.application.teacher.presentation.view.adapter.StudentPassportListAdapter;
import com.auro.application.teacher.presentation.view.adapter.TeacherGradeSpinnerAdapter;
import com.auro.application.teacher.presentation.viewmodel.MyClassroomViewModel;
import com.auro.application.teacher.presentation.viewmodel.StudentPassportViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringTeacherDynamic;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyStudentPassportFragment extends BaseFragment implements  CommonCallBackListner, View.OnFocusChangeListener,View.OnTouchListener {

    @Inject
    @Named("MyStudentPassportFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "MyStudentPassportFragment";
    FragmentTeacherpassportBinding binding;
    StudentPassportViewModel viewModel;
    StudentPassportListAdapter studentListAdapter;
    TeacherStudentPassportResModel resModel;
    boolean isStateRestore;
    Details details;
    String gradeid;
    List<StudentPassportDetailResModel> listchilds = new ArrayList<>();
    List<StudentPassportDetailResModel> list = new ArrayList<>();
    List<TeacherGradeResModel> gradelist = new ArrayList<>();
    public MyStudentPassportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
       // ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
       // viewModel = ViewModelProviders.of(this, viewModelFactory).get(StudentPassportViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        binding.btnsearch.setText(details.getSearch() != null   ? details.getSearch() : "Search");
                 binding.etsearchstudent.setHint(details.getSearch_student() != null   ? details.getSearch_student() : "search student");

        binding.etgrade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    binding.etgrade.showDropDown();
                }
            }
        });
        binding.etgrade.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.etgrade.showDropDown();
                getStudentGrade(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId(),"","");

                return false;
            }
        });



        binding.btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etsearchstudent.getText().toString().isEmpty()||binding.etsearchstudent.getText().toString().equals("")){
                    //  binding.editsearch.setHint("Search school here..");
                    Toast.makeText(getActivity(), "Please enter name", Toast.LENGTH_SHORT).show();
                }
                else if (binding.etsearchstudent.getText().toString().startsWith(" ")){
                    Toast.makeText(getActivity(), "Don't enter space", Toast.LENGTH_SHORT).show();

                }
                else{
                    ((InputMethodManager)getContext().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getView().getWindowToken(), 0);
                  //  binding.etsearchstudent.setText("");
                    String search = binding.etsearchstudent.getText().toString();

                    getStudentList(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId(),gradeid, search);

                    //   addDropDownSchool(districtList);
                }

            }
        });




        // getStudentList(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId(),"");
        return binding.getRoot();

        // return inflater.inflate(R.layout.fragment_class_room_group, container, false);
    }

    @Override
    protected void init() {
        AppStringTeacherDynamic.setMyStudentPassportFragmentStrings(binding);

    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        AppLogger.e("SummaryData", "Stem 0");
        HomeActivity.setListingActiveFragment(HomeActivity.TEACHER_MY_CLASSROOM_FRAGMENT);
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            getStudentGrade(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId(),"","");
          //  getGrade();
            getStudentList(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId(),"","");
           // observeServiceResponse();
        }
        binding.imgnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new TeacherMoreDetailFragment());
            }
        });

//        binding.etgrade.setOnFocusChangeListener(this);
//        binding.etgrade.setOnTouchListener(this);
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_teacherpassport;
    }

    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(getActivity(), fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }

    public void setAdapterAllListStudent(List<StudentPassportDetailResModel> totalStudentList) {
        binding.rvPassportStudent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        binding.rvPassportStudent.setHasFixedSize(true);

        if (!totalStudentList.isEmpty()) {
            binding.rvPassportStudent.setVisibility(View.VISIBLE);
            binding.studentListMessage.setVisibility(View.GONE);

        } else {
            binding.rvPassportStudent.setVisibility(View.GONE);
            binding.studentListMessage.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        setToolbar();
        setListener();
    }

    private void getStudentList(String userid, String gradeid, String name)
    {
        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle(details.getProcessing());
        progress.setMessage(details.getProcessing());
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);
        map_data.put("grade", gradeid);
        map_data.put("student_name",name);

        RemoteApi.Companion.invoke().getStudentPassportData(map_data)
                .enqueue(new Callback<TeacherStudentPassportResModel>()
                {
                    @Override
                    public void onResponse(Call<TeacherStudentPassportResModel> call, Response<TeacherStudentPassportResModel> response)
                    {
                        try {


                            if (response.isSuccessful()) {
                                list.clear();
                                listchilds.clear();
                                progress.dismiss();
                                if (!(response.body().getStudentData() == null) || !(response.body().getStudentData().isEmpty())) {
                                    listchilds = response.body().getStudentData();
                                    String count = response.body().getTotalCount();
                                    binding.RPDetailInformation.setText(count);
                                    for (int i = 0; i < listchilds.size(); i++) {
                                        list.add(listchilds.get(i));
//                                        int listcount  = response.body().getTotalCount();
//
                                    }


                                    }
                                setAdapterAllListStudent(list);
                                studentListAdapter = new StudentPassportListAdapter(getActivity(), list);
                                binding.rvPassportStudent.setAdapter(studentListAdapter);

                                }

                            }
                         catch (Exception e) {
                             progress.dismiss();
                            Toast.makeText(getActivity(), details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TeacherStudentPassportResModel> call, Throwable t)
                    {
                        progress.dismiss();
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void getStudentGrade(String userid, String gradeid, String name)
    {

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);
        map_data.put("grade", gradeid);
        map_data.put("student_name",name);

        RemoteApi.Companion.invoke().getStudentPassportData(map_data)
                .enqueue(new Callback<TeacherStudentPassportResModel>()
                {
                    @Override
                    public void onResponse(Call<TeacherStudentPassportResModel> call, Response<TeacherStudentPassportResModel> response)
                    {
                        try {


                            if (response.isSuccessful()) {

                                gradelist.clear();

                                    for ( int i=0 ;i < response.body().getTeacherGrade().size();i++)
                                    {
                                        String state_id = response.body().getTeacherGrade().get(i).getGradeName();
                                        String grade_id = response.body().getTeacherGrade().get(i).getGrade();
                                        TeacherGradeResModel stateData = new TeacherGradeResModel();
                                        stateData.setGradeName(state_id);
                                        stateData.setGrade(grade_id);
                                        gradelist.add(stateData);


                                    }
                                    addDropDownGrade();


                            }

                        }
                        catch (Exception e) {

                            Toast.makeText(getActivity(), details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TeacherStudentPassportResModel> call, Throwable t)
                    {

                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void getGrade()
    {

        gradelist.clear();
        RemoteApi.Companion.invoke().getGrade()
                .enqueue(new Callback<GradeDataModel>()
                {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.GradeDataModel> call, Response<com.auro.application.home.data.model.GradeDataModel> response)
                    {
                        if (response.isSuccessful())
                        {

                            for ( int i=0 ;i < response.body().getResult().size();i++)
                            {
                                String state_id = response.body().getResult().get(i).getGrade_id();

                                GradeData stateData = new GradeData(state_id);
                               // gradelist.add(stateData);


                            }
                            addDropDownGrade();

                        }
                        else
                        {
                            Log.d(TAG, "onResponser: "+response.message().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.GradeDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }


    private void addDropDownGrade()
    {
        TeacherGradeSpinnerAdapter adapter = new TeacherGradeSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, gradelist, this);
        binding.etgrade.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etgrade.setThreshold(1);
        binding.etgrade.setTextColor(Color.BLACK);//will start working from first character
    }
    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {

         if (commonDataModel.getClickType()==GRADE)
        {
            TeacherGradeResModel gData = (TeacherGradeResModel) commonDataModel.getObject();
            binding.etgrade.setText(gData.getGradeName());
            gradeid = gData.getGrade();
            String name = binding.etsearchstudent.getText().toString();

            getStudentList(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId(),gradeid,name);


        }


    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        AppLogger.v("Pradeep", "CLICK LISNER " + v.getId());
        if (hasFocus) {
            if (v.getId() == R.id.etgrade) {
                binding.etgrade.showDropDown();
            }
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // binding.etGender.showDropDown();
        AppLogger.v("Pradeep", "CLICK LISNER " + v.getId());
        if (v.getId() == R.id.etgrade) {
            binding.etgrade.showDropDown();
        }

        return false;
    }

}