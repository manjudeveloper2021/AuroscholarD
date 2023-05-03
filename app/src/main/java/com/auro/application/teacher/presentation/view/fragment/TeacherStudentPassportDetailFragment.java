package com.auro.application.teacher.presentation.view.fragment;

import static com.auro.application.core.common.Status.GRADE;
import static com.auro.application.core.common.Status.MONTH_CLICK;
import static com.auro.application.core.common.Status.SUBJECT;
import static com.auro.application.core.common.Status.SUBJECT_CLICK;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.auro.application.R;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.FragmentTeacherpassportBinding;
import com.auro.application.databinding.NewstudentPassportTeacherLayoutBinding;
import com.auro.application.databinding.NewstudentdetailPassportTeacherLayoutBinding;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.StateData;
import com.auro.application.home.data.model.passportmodels.PassportMonthModel;
import com.auro.application.home.data.model.passportmodels.PassportQuizMonthModel;
import com.auro.application.home.data.model.passportmodels.PassportSubjectQuizMonthModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.view.adapter.PassportSpinnerAdapter;
import com.auro.application.teacher.data.model.common.MonthDataModel;
import com.auro.application.teacher.data.model.response.DataListPassportStudentDetailResModel;
import com.auro.application.teacher.data.model.response.PassportMonthResModel;
import com.auro.application.teacher.data.model.response.PassportStudentDetailResModel;
import com.auro.application.teacher.data.model.response.QuizDetailPassportStudentDetailResModel;
import com.auro.application.teacher.data.model.response.StudentPassportDetailResModel;
import com.auro.application.teacher.data.model.response.TeacherGradeResModel;
import com.auro.application.teacher.data.model.response.TeacherStudentPassportDetailResModel;
import com.auro.application.teacher.data.model.response.TeacherStudentPassportResModel;
import com.auro.application.teacher.presentation.view.adapter.PassportMonthSpinnerAdapter;
import com.auro.application.teacher.presentation.view.adapter.PassportSubjectSpinnerAdapter;
import com.auro.application.teacher.presentation.view.adapter.StudentDetailPassportListAdapter;
import com.auro.application.teacher.presentation.view.adapter.StudentPassportListAdapter;
import com.auro.application.teacher.presentation.view.adapter.TeacherGradeSpinnerAdapter;
import com.auro.application.teacher.presentation.viewmodel.StudentPassportViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.DateUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.strings.AppStringTeacherDynamic;
import com.bumptech.glide.Glide;

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

public class TeacherStudentPassportDetailFragment extends BaseFragment implements  CommonCallBackListner{
    @Inject
    @Named("MyStudentPassportFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "TeacherStudentPassportDetailFragment";
    NewstudentdetailPassportTeacherLayoutBinding binding;
    StudentPassportViewModel viewModel;
    boolean isMonthSelected = false;
    MonthDataModel spinnerMonth;
    PassportSpinnerAdapter subjectSpinner;
    PassportSubjectQuizMonthModel spinnerSubject2;
    List<MonthDataModel> subjectResModelList;
    boolean userClick = false;
    List<PassportSubjectQuizMonthModel> list1= new ArrayList<>();
    List<String> demoList = new ArrayList<>();
    List<MonthDataModel> monthDataModelList;
    StudentDetailPassportListAdapter studentListAdapter;
    TeacherStudentPassportResModel resModel;
    boolean isStateRestore;
    Details details;
    String stregdate;
    String monthid,subjectname;
    List<QuizDetailPassportStudentDetailResModel> listchilds = new ArrayList<>();
    List<QuizDetailPassportStudentDetailResModel> list = new ArrayList<>();
    List<PassportMonthResModel> monthlist = new ArrayList<>();
    List<PassportSubjectQuizMonthModel> subjectlist = new ArrayList<>();


    public TeacherStudentPassportDetailFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
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

        //    getStudentList("576232","7,12");



        binding.etmonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("stateid", parent.getItemAtPosition(position).toString());
            }
        });
        binding.etmonth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    binding.etmonth.showDropDown();

                }
            }
        });
        binding.etmonth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.etmonth.showDropDown();
              // getPassport(monthid);
          getStudentMonth();
                return false;
            }
        });

        binding.etsubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("stateid", parent.getItemAtPosition(position).toString());
            }
        });
        binding.etsubject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    binding.etsubject.showDropDown();

                }
            }
        });
        binding.etsubject.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.etsubject.showDropDown();
                // getPassport(monthid);
              getStudentSubject(monthid);
                return false;
            }
        });
        binding.imgnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new MyStudentPassportFragment());
            }
        });
        return binding.getRoot();

        // return inflater.inflate(R.layout.fragment_class_room_group, container, false);
    }

    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(getActivity(), fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
        FragmentUtil.replaceFragment(getActivity(), fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }
    @Override
    protected void init() {
        if (getArguments() != null) {
            String userid = getArguments().getString("studentuserid");
            String studentname = getArguments().getString("studentname");
            binding.tvStudentName.setText(studentname);
            String studentquizattempt = getArguments().getString("studentquizattempt");
            binding.tvquizvalue.setText(studentquizattempt);
            String studentdisbursalamount = getArguments().getString("studentdisbursalamount");
            binding.tvscholarshipvalue.setText("₹"+studentdisbursalamount);
            String studentkycstatus = getArguments().getString("studentkycstatus");

            binding.tvStudentScore.setText(" "+studentkycstatus);
            String studentprofilepic = getArguments().getString("studentprofilepic");
            Glide.with(getActivity()).load(studentprofilepic).circleCrop().into(binding.studentImage);
        }

        AppStringTeacherDynamic.setTeacherStudentPassportDetailFragmentStrings(binding);
      getStudentMonth();



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

            getProfile();
            //getStudentList();

            // observeServiceResponse();
        }


    }


    @Override
    protected int getLayout() {
        return R.layout.newstudentdetail_passport_teacher_layout;
    }

    public void setAdapterAllListStudent(List<QuizDetailPassportStudentDetailResModel> totalStudentList) {
        binding.rvscholarshipdetail.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        binding.rvscholarshipdetail.setHasFixedSize(true);

        if (!totalStudentList.isEmpty()) {
            binding.rvscholarshipdetail.setVisibility(View.VISIBLE);
          //  binding.studentListMessage.setVisibility(View.GONE);

        } else {
            binding.rvscholarshipdetail.setVisibility(View.GONE);
          //  binding.studentListMessage.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        setToolbar();
        setListener();

    }

    private void getStudentList()
    {
//        ProgressDialog progress = new ProgressDialog(getActivity());
//        progress.setTitle(details.getProcessing());
//        progress.setMessage(details.getProcessing());
//        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
//        progress.show();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        String userid = getArguments().getString("studentuserid");

        HashMap<String,String> map_data = new HashMap<>();
        if (subjectname == null || subjectname.equals("null") || subjectname.equals("") || subjectname.isEmpty()||subjectname.equals("All")){
            map_data.put("user_id",userid);
            map_data.put("month", monthid);
            map_data.put("subject", "");
            map_data.put("is_all", "1");
        }
        else{
            map_data.put("user_id",userid);
            map_data.put("month", monthid);
            map_data.put("subject", subjectname);
            map_data.put("is_all", "0");
        }


        RemoteApi.Companion.invoke().getPassportData(map_data)
                .enqueue(new Callback<PassportStudentDetailResModel>()
                {
                    @Override
                    public void onResponse(Call<PassportStudentDetailResModel> call, Response<PassportStudentDetailResModel> response)
                    {
                        try {


                            if (response.isSuccessful()) {
                                list.clear();
                                listchilds.clear();
                                //    progress.dismiss();
                                //  binding.RPDetailInformation.setText(response.body().getTotalCount());
                                if (!(response.body().getData_list() == null) || !(response.body().getData_list().isEmpty())) {
                                    for (int i = 0; i < response.body().getData_list().size(); i++) {
                                        for (int j = 0; j<response.body().getData_list().get(i).getQuiz_data().size(); j++){
                                            for(int k = 0; k<response.body().getData_list().get(i).getQuiz_data().get(j).getQuiz_detail().size(); k++){

                                                listchilds = response.body().getData_list().get(i).getQuiz_data().get(j).getQuiz_detail();
                                                list.add(listchilds.get(k));
                                            }
                                        }





                                    }
                                    setAdapterAllListStudent(list);
                                    studentListAdapter = new StudentDetailPassportListAdapter(getActivity(), list);
                                    binding.rvscholarshipdetail.setAdapter(studentListAdapter);

                                }

                            }
                        }
                        catch (Exception e) {
                          //  progress.dismiss();
                            Toast.makeText(getActivity(), details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PassportStudentDetailResModel> call, Throwable t)
                    {
                      //  progress.dismiss();
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void getProfile()
    {
        String userid = getArguments().getString("studentuserid");
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String userlangid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);
        map_data.put("user_prefered_language_id",userlangid);

        RemoteApi.Companion.invoke().getStudentData(map_data)
                .enqueue(new Callback<GetStudentUpdateProfile>()
                {
                    @Override
                    public void onResponse(Call<GetStudentUpdateProfile> call, Response<GetStudentUpdateProfile> response)
                    {
                        if (response.isSuccessful()) {
                            stregdate = response.body().getRegistrationdate();
                        }
                        else
                        {
                            Log.d(TAG, "onResponser: "+response.message().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<GetStudentUpdateProfile> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {

        if (commonDataModel.getClickType()==MONTH_CLICK)
        {
            PassportMonthResModel gData = (PassportMonthResModel) commonDataModel.getObject();
            binding.etmonth.setText(gData.getDateName());
            monthid = gData.getDate();
            getStudentSubject(monthid);
           getStudentList();
        }
                else if (commonDataModel.getClickType()==SUBJECT)
        {
            PassportSubjectQuizMonthModel gData = (PassportSubjectQuizMonthModel) commonDataModel.getObject();
            binding.etsubject.setText(gData.getSubject());
            subjectname = gData.getSubject();
            getStudentList();
        }


    }


 //   @Override
 //   public void onFocusChange(View v, boolean hasFocus) {
    //    AppLogger.v("Pradeep", "CLICK LISNER " + v.getId());
     //   if (hasFocus) {
//            if (v.getId() == R.id.etmonth) {
//                binding.etmonth.showDropDown();
//            }
//            else if (v.getId() == R.id.etsubject) {
//                binding.etsubject.showDropDown();
//            }
       // }
   // }
  //  @Override
  //  public boolean onTouch(View v, MotionEvent event) {
//        if (v.getId() == R.id.etmonth) {
//            binding.etmonth.showDropDown();
//        }
//        else if (v.getId() == R.id.etsubject) {
//            binding.etsubject.showDropDown();
//        }

     //   return false;
   // }

// working
    private void getStudentMonth()
    {
        String userid = getArguments().getString("studentuserid");
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);

        RemoteApi.Companion.invoke().getStudentPassportMonth(map_data)
                .enqueue(new Callback<TeacherStudentPassportDetailResModel>()
                {
                    @Override
                    public void onResponse(Call<TeacherStudentPassportDetailResModel> call, Response<TeacherStudentPassportDetailResModel> response)
                    {
                        try {


                            if (response.isSuccessful()) {

                                monthlist.clear();

                                for ( int i=0 ;i < response.body().getData().size();i++)
                                {
                                    String month_id = response.body().getData().get(i).getDate();
                                    String month_name = response.body().getData().get(i).getDateName();
                                    PassportMonthResModel stateData = new PassportMonthResModel();
                                    stateData.setDateName(month_name);
                                    stateData.setDate(month_id);
                                    monthlist.add(stateData);


                                }
                                addDropDownMonth(monthlist);


                            }

                        }
                        catch (Exception e) {

                            Toast.makeText(getActivity(), details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TeacherStudentPassportDetailResModel> call, Throwable t)
                    {

                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getStudentSubject(String monthid)
    {
//        ProgressDialog progress = new ProgressDialog(getActivity());
//        progress.setTitle(details.getProcessing());
//        progress.setMessage(details.getProcessing());
//        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
//        progress.show();
        String userid = getArguments().getString("studentuserid");
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);
        map_data.put("month",monthid);

        RemoteApi.Companion.invoke().getQuizMonthSubject(map_data)
                .enqueue(new Callback<PassportQuizMonthModel>()
                {
                    @Override
                    public void onResponse(Call<PassportQuizMonthModel> call, Response<PassportQuizMonthModel> response)
                    {
                        try {


                            if (response.isSuccessful()) {

                                subjectlist.clear();

                                for ( int i=0 ;i < response.body().getPassportSubjectModelList().size();i++)
                                {
                                    String sub_name = response.body().getPassportSubjectModelList().get(i).getSubject();
                                    PassportSubjectQuizMonthModel stateData = new PassportSubjectQuizMonthModel();
                                    stateData.setSubject(sub_name);
                                    subjectlist.add(stateData);


                                }
                                addDropDownSubject(subjectlist);


                            }

                        }
                        catch (Exception e) {

                         //   Toast.makeText(getActivity(), details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PassportQuizMonthModel> call, Throwable t)
                    {

                       // Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addDropDownMonth(List<PassportMonthResModel> stateList) {
        AppLogger.v("StatePradeep", "addDropDownState    " + stateList.size());
        PassportMonthSpinnerAdapter adapter = new PassportMonthSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, stateList, this);
        binding.etmonth.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etmonth.setThreshold(1);
        binding.etmonth.showDropDown();//will start working from first character
        binding.etmonth.setTextColor(Color.BLACK);
    }

    public void addDropDownSubject(List<PassportSubjectQuizMonthModel> subjectlist) {
        AppLogger.v("StatePradeep", "addDropDownState    " + subjectlist.size());
        PassportSubjectSpinnerAdapter adapter = new PassportSubjectSpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, subjectlist, this);
        binding.etsubject.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.etsubject.setThreshold(1);
        binding.etsubject.setTextColor(Color.BLACK);
    }



}







