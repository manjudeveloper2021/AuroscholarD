package com.auro.application.teacher.presentation.view.fragment;

import static com.auro.application.core.common.Status.SEND_REFERRAL_API;
import static com.auro.application.util.AppUtil.commonCallBackListner;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.auro.application.ChatActivity;
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
import com.auro.application.databinding.FragmentInformationDashboardBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.RefferalReqModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.teacher.data.model.request.TeacherDasboardSummaryResModel;
import com.auro.application.teacher.data.model.request.TeacherUserIdReq;
import com.auro.application.teacher.data.model.response.StudentWalletTeacherResModel;
import com.auro.application.teacher.presentation.view.adapter.AutoScrollPagerAdapter;
import com.auro.application.teacher.presentation.view.adapter.StudentWalletInfoAdapter;
import com.auro.application.teacher.presentation.viewmodel.MyClassroomViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringTeacherDynamic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;


public class InformationDashboardFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener {


    @Inject
    @Named("InformationDashboardFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "InformationDashboardFragment";
    FragmentInformationDashboardBinding binding;
    MyClassroomViewModel viewModel;
    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 1000;
    FloatingActionButton floatingActionButton;
    Details details;


    boolean isStateRestore;
    List<StudentWalletTeacherResModel> listUploadData;

    public InformationDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyClassroomViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        init();
        setListener();
        setToolbar();
        floatingActionButton = binding.getRoot().findViewById(R.id.floatingActionButton);
       floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getActivity(), "under maintenance", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });

        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        binding.txtviewprofile.setText(details.getMyProfile() != null   ? details.getMyProfile() : "View Profile");
        return binding.getRoot();
    }

    @Override
    protected void init() {
        AppStringTeacherDynamic.setMyInformationStrings(binding);
        HomeActivity.setListingActiveFragment(HomeActivity.TEACHER_DASHBOARD_FRAGMENT);
        ViewUtil.setTeacherProfilePic(binding.imageView6);
        addViewPager();
       checkRefferedData();
    }


    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        binding.rlBookMySeat.setOnClickListener(this);
        binding.languageLayout.setOnClickListener(this);
        AppLogger.e("SummaryData", "Stem 0");

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
        callGetDashbaordSummaryData();

        binding.imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new TeacherUserProfileFragment());//TeacherProfileFragment
            }
        });
        binding.txtviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new TeacherUserProfileFragment());//TeacherProfileFragment
            }
        });

    }
    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(getActivity(), fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }
    void callGetDashbaordSummaryData()
    {
        handleProgress(0, "");
        TeacherUserIdReq teacherUserIdReq = new TeacherUserIdReq();
        teacherUserIdReq.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        // teacherUserIdReq.setUserId("538709");
        viewModel.getTeacherDashboardSummaryData(teacherUserIdReq);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_information_dashboard;
    }

    public void addViewPager() {
        AutoScrollPagerAdapter autoScrollPagerAdapter = new AutoScrollPagerAdapter(getActivity().getSupportFragmentManager());
        // AutoScrollViewPager viewPager = findViewById(R.id.view_pager);
        binding.viewPager.setAdapter(autoScrollPagerAdapter);

        binding.tabs.setupWithViewPager(binding.viewPager);
        // start auto scroll
        binding.viewPager.startAutoScroll();
        // set auto scroll time in mili
        binding.viewPager.setInterval(AUTO_SCROLL_THRESHOLD_IN_MILLI);
        // enable recycling using true
        binding.viewPager.setCycle(true);
    }

    public List<StudentWalletTeacherResModel> listUpload(TeacherDasboardSummaryResModel teacherDasboardl) {
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        listUploadData = new ArrayList<>();
        StudentWalletTeacherResModel studentModel = new StudentWalletTeacherResModel();
        studentModel.setNameOfDocument(details.getKyc_uploaded());
        studentModel.setTotalValue(teacherDasboardl.getKycUpload().toString());
        studentModel.setDrawable(getActivity().getResources().getDrawable(R.drawable.kyc_teacher_uploaded));
        listUploadData.add(studentModel);

        StudentWalletTeacherResModel studentModel1 = new StudentWalletTeacherResModel();
        studentModel1.setNameOfDocument(details.getKyc_approved());
        studentModel1.setTotalValue(teacherDasboardl.getKycApproved().toString());
        studentModel1.setDrawable(getActivity().getResources().getDrawable(R.drawable.upload_card_background));
        listUploadData.add(studentModel1);


        StudentWalletTeacherResModel studentModel2 = new StudentWalletTeacherResModel();
        studentModel2.setNameOfDocument(details.getApproved_scholarship());
        studentModel2.setTotalValue(teacherDasboardl.getWinningStudent().toString());
        studentModel2.setDrawable(getActivity().getResources().getDrawable(R.drawable.approved_scholarschip));
        listUploadData.add(studentModel2);

        StudentWalletTeacherResModel studentModel3 = new StudentWalletTeacherResModel();
        studentModel3.setNameOfDocument(details.getDisbursed_scholarship());
        studentModel3.setDrawable(getActivity().getResources().getDrawable(R.drawable.disbursed_scholarship));
        studentModel3.setTotalValue(teacherDasboardl.getDisbursedScholarship().toString());
        listUploadData.add(studentModel3);

        return listUploadData;


    }

    public void setAdapter() {
        // List<CertificateResModel> list = viewModel.homeUseCase.makeCertificateList();
        binding.rvWalletTeachet.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.rvWalletTeachet.setHasFixedSize(true);
        StudentWalletInfoAdapter kyCuploadAdapter = new StudentWalletInfoAdapter(getActivity(), listUploadData, this);
        binding.rvWalletTeachet.setAdapter(kyCuploadAdapter);
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {

    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {
            AppLogger.v("observeServiceResponse", "InformationDashbaordFragment");


            switch (responseApi.status) {
                case LOADING:
                    if (isVisible()) {
                        if (!isStateRestore) {
                            handleProgress(0, "");
                        }
                    }
                    break;

                case SUCCESS:
                    AppLogger.v("InfoScreen", " step 2 ");
                    if (responseApi.apiTypeStatus == Status.TEACHER_DASBOARD_SUMMARY) {
                        AppLogger.v("InfoScreen", " step 3 ");
                        handleProgress(1, "");
                        TeacherDasboardSummaryResModel resModel = (TeacherDasboardSummaryResModel) responseApi.data;
                        AppLogger.v("InfoScreen", " step 3.1  " + resModel.getMessage());
                        setDataOnInitializeView(resModel);
                    }
                    else if (responseApi.apiTypeStatus == SEND_REFERRAL_API) {
                        DynamiclinkResModel dynamiclinkResModel = (DynamiclinkResModel) responseApi.data;
                        AppLogger.v("SEND_REFERRAL_API", " response Step 1");
                        if (!dynamiclinkResModel.getError()) {
                            AppLogger.v("SEND_REFERRAL_API", "response Step 2");
                            AppUtil.setEmptyToDynamicResponseModel();
                        }
                    }
                    else if (responseApi.apiTypeStatus == Status.DYNAMIC_LINK_API) {
                        AppLogger.v("observeServiceResponse", " response Step 2");
                        DynamiclinkResModel dynamiclinkResModel = (DynamiclinkResModel) responseApi.data;
                        AppLogger.v("observeServiceResponse", " response Step 3");
                        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                        prefModel.setDynamiclinkResModel(dynamiclinkResModel);
                        AuroAppPref.INSTANCE.setPref(prefModel);
                        AppLogger.v("observeServiceResponse", " response Step 4");
                        if (dynamiclinkResModel.getError()) {
                            AppLogger.v("observeServiceResponse", " response Step 5");
                            sendRefferCallback(dynamiclinkResModel, 1);
                        } else {
                            AppLogger.v("observeServiceResponse", " response Step 6");
                            sendRefferCallback(dynamiclinkResModel, 0);
                        }
                    }
                    break;

                case FAIL:
                    callGetDashbaordSummaryData();
                    break;
                case NO_INTERNET:
                    callGetDashbaordSummaryData();
                    break;
                default:
                    if (isVisible()) {
                        callGetDashbaordSummaryData();
                       // handleProgress(2, (String) responseApi.data);
                    }
                    break;
            }

        });
    }
    public void sendRefferCallback(DynamiclinkResModel dynamiclinkResModel, int status) {
        AppLogger.v("observeServiceResponse", " response Step 8");
        if (commonCallBackListner != null) {
            if (status == 1) {
                AppLogger.v("observeServiceResponse", " response 9 ");
                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(status, Status.REFFER_API_SUCCESS, dynamiclinkResModel));
            } else {
                AppLogger.v("observeServiceResponse", " response Step 10");
                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(status, Status.REFFER_API_ERROR, dynamiclinkResModel));
            }
        }
    }
    private void checkRefferedData() {
        AppLogger.e("SEND_REFERRAL_API", "dynamiclink step 1");
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        DynamiclinkResModel dynamiclinkResModel = prefModel.getDynamiclinkResModel();
        if (dynamiclinkResModel != null && dynamiclinkResModel.getReffeUserId() != null && !dynamiclinkResModel.getReffeUserId().isEmpty()) {
            AppLogger.e("SEND_REFERRAL_API", "dynamiclink" + dynamiclinkResModel.getReffeUserId());
            RefferalReqModel reqModel = new RefferalReqModel();
            reqModel.setReferredById(dynamiclinkResModel.getReffeUserId());
            reqModel.setReferredUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
            reqModel.setReferredByType(dynamiclinkResModel.getReffer_type());
            reqModel.setReferredUserMobile(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserMobile());
            reqModel.setUser_type_id("1");
            viewModel.checkInternet(reqModel,Status.SEND_REFERRAL_API);

        } else {
            AppLogger.e(TAG, "No Link Available");
        }
    }
    private void handleProgress(int status, String message) {
        switch (status) {
            case 0:
                binding.mainParentLayout.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.progressbar.pgbar.setVisibility(View.VISIBLE);
                // binding.shimmerMyClassroom.startShimmer();
                break;

            case 1:
                binding.mainParentLayout.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.progressbar.pgbar.setVisibility(View.GONE);
                //  binding.shimmerMyClassroom.stopShimmer();
                break;

            case 2:
//                binding.errorConstraint.setVisibility(View.VISIBLE);
//                binding.mainParentLayout.setVisibility(View.GONE);
//                binding.progressbar.pgbar.setVisibility(View.GONE);

                binding.mainParentLayout.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.progressbar.pgbar.setVisibility(View.VISIBLE);


                // binding.shimmerMyClassroom.stopShimmer();
                binding.errorLayout.textError.setText(message);
                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callGetDashbaordSummaryData();
                    }
                });
                break;
        }

    }

    public void setDataOnInitializeView(TeacherDasboardSummaryResModel teacherDasboardl) {
        AppLogger.e("InfoScreen -=total student-", "" + teacherDasboardl.getAllWinQuiz());
        //  AppLogger.e("InfoScreen -=total student-", "" + totalStudent);
        String totalStudent = teacherDasboardl.getTotalStudent();
        String totalWiningQuiz = teacherDasboardl.getAllWinQuiz();
        String totalTestTaken = teacherDasboardl.getTotalQuizTaken();
        String uniqueStudentTakenQuiz = teacherDasboardl.getUniqueStudentTakenQuiz();

        binding.tvTotalStudent.setText(totalStudent + "");
        binding.tvTotalWinning.setText(totalWiningQuiz + "");
        binding.Rpmarks.setText(totalTestTaken + "");
        binding.RpoptainMarks.setText(uniqueStudentTakenQuiz + "");
        listUpload(teacherDasboardl);
        setAdapter();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rlBook_my_seat:
                ((HomeActivity) getActivity()).openFragment(new BookSlotFragment());
                break;

            case R.id.language_layout:
                ((HomeActivity)getActivity()).openChangeLanguageDialog();
                break;

        }

    }



}