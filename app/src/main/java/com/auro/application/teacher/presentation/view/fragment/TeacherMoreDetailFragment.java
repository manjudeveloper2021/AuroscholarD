package com.auro.application.teacher.presentation.view.fragment;

import static com.auro.application.core.common.Status.MONTH_CLICK;
import static com.auro.application.core.common.Status.SUBJECT;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.auro.application.databinding.MoreTeacherLayoutBinding;
import com.auro.application.databinding.NewstudentdetailPassportTeacherLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.GenderDataModel;
import com.auro.application.home.data.model.passportmodels.PassportQuizMonthModel;
import com.auro.application.home.data.model.passportmodels.PassportSubjectQuizMonthModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.view.adapter.PassportSpinnerAdapter;
import com.auro.application.teacher.data.model.common.MonthDataModel;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.teacher.data.model.response.PassportMonthResModel;
import com.auro.application.teacher.data.model.response.PassportStudentDetailResModel;
import com.auro.application.teacher.data.model.response.QuizDetailPassportStudentDetailResModel;
import com.auro.application.teacher.data.model.response.TeacherStudentPassportDetailResModel;
import com.auro.application.teacher.data.model.response.TeacherStudentPassportResModel;
import com.auro.application.teacher.presentation.view.adapter.PassportMonthSpinnerAdapter;
import com.auro.application.teacher.presentation.view.adapter.PassportSubjectSpinnerAdapter;
import com.auro.application.teacher.presentation.view.adapter.StudentDetailPassportListAdapter;
import com.auro.application.teacher.presentation.viewmodel.StudentPassportViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringTeacherDynamic;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherMoreDetailFragment extends BaseFragment {
    @Inject
    @Named("MyStudentPassportFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "TeacherStudentPassportDetailFragment";
    MoreTeacherLayoutBinding binding;
    List<String> menuList = new ArrayList<>();
    public TeacherMoreDetailFragment() {
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
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
     //   details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        ViewUtil.setTeacherProfilePic(binding.imageView6);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        binding.tvMobile.setText(prefModel.getUserMobile());
        MyProfileResModel model = AuroAppPref.INSTANCE.getModelInstance().getTeacherProfileResModel();

        binding.tvStudentName.setText(model.getTeacherName());
        return binding.getRoot();
    }


    @Override
    protected void init() {
        if (getArguments() != null) {

        }
       // AppStringTeacherDynamic.setTeacherMoreFragmentStrings(binding);
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        AppLogger.e("SummaryData", "Stem 0");
        HomeActivity.setListingActiveFragment(HomeActivity.TEACHER_MY_CLASSROOM_FRAGMENT);
      binding.rel2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              openFragment(new MainCourseFragment());
          }
      });
        binding.rel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new TeacherUserProfileFragment());
            }
        });
        binding.rel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new MyStudentPassportFragment());
            }
        });

    }

    @Override
    protected int getLayout() {
        return R.layout.more_teacher_layout;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        setToolbar();
        setListener();

    }

    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(getActivity(), fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }


}







