package com.auro.application.teacher.presentation.view.fragment;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.FragmentClassRoomGroupBinding;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.view.activity.StudentProfileActivity;
import com.auro.application.teacher.data.model.StudentData;
import com.auro.application.teacher.data.model.StudentListDataModel;
import com.auro.application.teacher.data.model.request.TeacherUserIdReq;
import com.auro.application.teacher.data.model.response.MyBuddyDataResModel;
import com.auro.application.teacher.data.model.response.TeacherClassRoomResModel;
import com.auro.application.teacher.data.model.response.TeacherGroupRes;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherResModel;
import com.auro.application.teacher.data.model.response.TotalStudentResModel;
import com.auro.application.teacher.data.model.response.UserImageInGroupResModel;
import com.auro.application.teacher.presentation.view.adapter.CreateGroupStudentListAdapter;
import com.auro.application.teacher.presentation.view.adapter.MyBuddyListAdapter;
import com.auro.application.teacher.presentation.view.adapter.StudentListAdapter;
import com.auro.application.teacher.presentation.view.adapter.GroupAdapter;
import com.auro.application.teacher.presentation.viewmodel.MyClassroomViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringTeacherDynamic;
//import com.facebook.share.model.ShareLinkContent;
//import com.facebook.share.widget.ShareDialog;

import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyClassRoomGroupFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener {


    @Inject
    @Named("MyClassRoomGroupFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "MyClassRoomGroupFragment";
    FragmentClassRoomGroupBinding binding;
    MyClassroomViewModel viewModel;
    boolean isStateRestore;
    String completeLink;
    UserImageInGroupResModel userImageInGroupResModel;
    StudentListAdapter studentListAdapter;
    CreateGroupStudentListAdapter studentListAdapter2;
    List<StudentData> listchilds = new ArrayList<>();
    List<StudentData> list = new ArrayList<>();


    GroupAdapter newClassAdapter;


    TeacherClassRoomResModel resModel;

    public MyClassRoomGroupFragment() {

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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyClassroomViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        setListener();
        return binding.getRoot();


    }

    @Override
    protected void init() {

        AppStringTeacherDynamic.setMyClassRoomGroupFragmentStrings(binding);
        ViewUtil.setTeacherProfilePic(binding.imageView6);

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
            observeServiceResponse();
        }
        binding.btAddGroup.setOnClickListener(this);
        binding.txtShareWithOther.setOnClickListener(this);
        binding.whatsAppLayout.setOnClickListener(this);
        callTeacherClassroomApi();

    }


    private void callTeacherClassroomApi() {

        TeacherUserIdReq teacherUserIdReq = new TeacherUserIdReq();
        teacherUserIdReq.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        AppLogger.v("InfoScreen", " step 1 ");
        handleProgress(0, "");
        viewModel.checkInternet(teacherUserIdReq, Status.TEACHER_CLASSROOM);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_class_room_group;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_add_group:
                openCreateGroupFragment();
                break;

            case R.id.txtShareWithOther:
            case R.id.whats_app_layout:
                getTeacherList();
                break;

        }
    }
    public void callRefferApi() {
        AppLogger.e("callRefferApi","step 1");
        try {
            DynamiclinkResModel dynamiclinkResModel = new DynamiclinkResModel();
            dynamiclinkResModel.setReffeUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
            dynamiclinkResModel.setSource(AppConstant.AURO_ID);
            dynamiclinkResModel.setNavigationTo(AppConstant.NavigateToScreen.STUDENT_DASHBOARD);
            dynamiclinkResModel.setReffer_type("" + AppConstant.UserType.TEACHER);
            viewModel.checkInternet(dynamiclinkResModel, Status.DYNAMIC_LINK_API);
        }catch (Exception e)
        {

        }
    }

    private void getTeacherList()
    {
        handleRefferProgress(0);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        HashMap<String,String> map_data = new HashMap<>();
        String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        map_data.put("reffer_user_id",userid);
        map_data.put("source",AppConstant.AURO_ID);
        map_data.put("navigation_to",AppConstant.NavigateToScreen.STUDENT_DASHBOARD);
        map_data.put("reffer_type", String.valueOf(AppConstant.UserType.TEACHER));
        map_data.put("user_type_id", String.valueOf(AppConstant.UserType.STUDENT)); //"576232"
        RemoteApi.Companion.invoke().getreffertostudent(map_data)
                .enqueue(new Callback<DynamiclinkResModel>()
                {
                    @Override
                    public void onResponse(Call<DynamiclinkResModel> call, Response<DynamiclinkResModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {
                                handleRefferProgress(1);
                                if (response.body().getStatus().equals("success")){
                                    if (!(response.body().getLink() == null || response.body().getLink().isEmpty() || response.body().getLink().equals("") || response.body().getLink().equals("null"))) {

                                       String link = response.body().getLink();
                                        performClick(link);
                                    }
                                    else{

                                        Toast.makeText(getActivity(), "No link found", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                            else {

                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DynamiclinkResModel> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }


    void openCreateGroupFragment() {

        int count = 0;
        for (TotalStudentResModel model : resModel.getTotalStudentList()) {
            if (model.getGroupId().isEmpty()) {
                CreateGroupFragment createGroupFragment = new CreateGroupFragment();

                ((HomeActivity) getActivity()).openFragment(createGroupFragment);
                break;
            } else {
                count++;
            }
        }
        if (resModel.getTotalStudentList().size() == count) {


            CreateGroupFragment createGroupFragment = new CreateGroupFragment();
            ((HomeActivity) getActivity()).openFragment(createGroupFragment);

        }

    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case GROUP_CLICK_CALLBACK:

                TeacherGroupRes teacherGroupRes = (TeacherGroupRes) commonDataModel.getObject();
                String groupid = teacherGroupRes.getGroupId().toString();

                if (teacherGroupRes.getGroupId() == "0" || teacherGroupRes.getGroupId().equals(0) || teacherGroupRes.getGroupId().equals("0")){
                    if (studentListAdapter != null) {
                        List<TotalStudentResModel> list = updateList(teacherGroupRes);
                        if (!list.isEmpty()) {
                            binding.rvChooseStudent.setVisibility(View.VISIBLE);
                            binding.rvChooseStudent2.setVisibility(View.GONE);
                            binding.studentListMessage.setVisibility(View.GONE);
                            studentListAdapter.setData(list);
                        } else {
                            binding.rvChooseStudent.setVisibility(View.GONE);
                            binding.rvChooseStudent2.setVisibility(View.GONE);
                            binding.studentListMessage.setVisibility(View.VISIBLE);
                        }

                    }
                }
             else{
                    getStudentList(groupid);
                }
                break;

            case REFFER_API_SUCCESS:
                AppLogger.e("performClick-", "REFFER_API_SUCCESS");
                handleRefferProgress(1);
                if (isVisible()) {
                    AppLogger.e("performClick-", "REFFER_API_SUCCESS");

                }
                break;

            case REFFER_API_ERROR:
                if (isVisible()) {
                    AppLogger.e("performClick-", "REFFER_API_ERROR");

                }
                break;
        }
    }

    private void openWhatsApp(String numero, String mensaje) {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + numero + "&text=" + mensaje)));
        } catch (Exception e) {
            ViewUtil.showSnackBar(binding.getRoot(), "Please install the whats app first");
        }

    }

    private void performClick(String link) throws IllegalStateException {
        AppLogger.e("performClick-", "performClick calling 1");
        completeLink = "";
         completeLink = getActivity().getResources().getString(R.string.teacher_share_msg);
//        if (AuroApp.getAuroScholarModel() != null && !TextUtil.isEmpty(AuroApp.getAuroScholarModel().getReferralLink())) {
//            completeLink = completeLink + link;
//        }
        completeLink = completeLink + link;
        openWhatsApp("", completeLink);


    }

    public void shareWithFriends(String link) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Invite to AuroScholar Scholarship");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        getActivity().startActivity(shareIntent);
    }


    public void setAdapterInMyClassRoom(List<TeacherGroupRes> groups) {
        binding.rvItemPic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        binding.rvItemPic.setHasFixedSize(true);
        newClassAdapter = new GroupAdapter(getActivity(), groups, this);
        binding.rvItemPic.setAdapter(newClassAdapter);
    }

    public void setAdapterAllListStudent(List<TotalStudentResModel> totalStudentList) {
        binding.rvChooseStudent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        binding.rvChooseStudent.setHasFixedSize(true);
        binding.rvChooseStudent2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        binding.rvChooseStudent2.setHasFixedSize(true);
        studentListAdapter = new StudentListAdapter(getActivity(), totalStudentList, this);
        binding.rvChooseStudent.setAdapter(studentListAdapter);
        if (!totalStudentList.isEmpty()) {

            binding.rvChooseStudent.setVisibility(View.VISIBLE);
            binding.rvChooseStudent2.setVisibility(View.GONE);
            binding.studentListMessage.setVisibility(View.GONE);

        } else {
            binding.rvChooseStudent.setVisibility(View.GONE);
            binding.rvChooseStudent2.setVisibility(View.GONE);
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


    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {
            AppLogger.v("observeServiceResponse", "MyClassRoomGroupFragment");


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
                    if (responseApi.apiTypeStatus == Status.TEACHER_CLASSROOM) {
                        AppLogger.v("InfoScreen", " step 3 ");
                        TeacherClassRoomResModel commonDataResModel = (TeacherClassRoomResModel) responseApi.data;
                        handleProgress(1, "");
                        resModel = commonDataResModel;
                        AppLogger.v("InfoScreen", " step 4 ");
                        setDataOnInitializeView(resModel);
                    }
                    AppLogger.v("InfoScreen", " step 5 ");

                    break;

                case FAIL:
                case NO_INTERNET:
                    if (isVisible()) {
                        handleProgress(2, (String) responseApi.data);
                    }
                    break;
                default:
                    if (isVisible()) {
                        handleProgress(2, (String) responseApi.data);
                    }
                    break;
            }

        });
    }

    private void handleProgress(int status, String message) {
        switch (status) {
            case 0:
                binding.mainParentLayout.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.shimmerMyClassroom.setVisibility(View.VISIBLE);
                binding.shimmerMyClassroom.startShimmer();
                break;

            case 1:
                binding.mainParentLayout.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.shimmerMyClassroom.setVisibility(View.GONE);
                binding.shimmerMyClassroom.stopShimmer();
                break;

            case 2:
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.mainParentLayout.setVisibility(View.GONE);
                binding.shimmerMyClassroom.setVisibility(View.GONE);
                binding.shimmerMyClassroom.stopShimmer();
                binding.errorLayout.textError.setText(message);
                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //viewModel.getTeacherProfileData(auroScholarDataModel);
                    }
                });
                break;
        }

    }

    public void setDataOnInitializeView(TeacherClassRoomResModel teacherDasboardl) {
        AppLogger.e("InfoScreen -=total student-", "" + teacherDasboardl.getTotalStudentList().size());
        if (teacherDasboardl.getTotalStudentList().size() < 2) {
            binding.RPDetailInformation.setText(teacherDasboardl.getTotalStudentList().size() + " Student");
        } else {
            binding.RPDetailInformation.setText(teacherDasboardl.getTotalStudentList().size() + " Students");
        }

        addOneItem(teacherDasboardl);
        setAdapterInMyClassRoom(teacherDasboardl.getGroups());
        setAdapterAllListStudent(teacherDasboardl.getTotalStudentList());
    }

    public void addOneItem(TeacherClassRoomResModel teacherClassRoomResModel) {
        TeacherGroupRes teacherGroupRes = new TeacherGroupRes();
        teacherGroupRes.setGroupId("0");
        teacherGroupRes.setGroupName("All");
        teacherGroupRes.setGroupMembers(Collections.emptyList());
        teacherGroupRes.setGroupCreationDate("" + System.currentTimeMillis());
        List<TeacherGroupRes> groupList = teacherClassRoomResModel.getGroups();
        groupList.add(0, teacherGroupRes);
    }

    public List<TotalStudentResModel> updateList(TeacherGroupRes groupRes) {
        List<TotalStudentResModel> newlist = new ArrayList<>();
        if (groupRes.getGroupId().equalsIgnoreCase("0")) {
            return resModel.getTotalStudentList();
        } else {
//            for (TotalStudentResModel studentResModel : resModel.getTotalStudentList()) {
//                if (studentResModel.getGroupId().equalsIgnoreCase(groupRes.getGroupId())) {
//                    newlist.add(studentResModel);
//                }
//            }
//            return newlist;
            getStudentList(groupRes.getGroupId());
        }
        return newlist;
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private void handleRefferProgress(int val) {
        switch (val) {
            case 0:
                binding.progressbar.pgbar.setVisibility(View.VISIBLE);
                break;

            case 1:
                binding.progressbar.pgbar.setVisibility(View.GONE);
                break;

        }

    }

    private boolean isAppInstalled(String packageName) {
        PackageManager packageManager = AuroApp.getAppContext().getPackageManager();
        for (PackageInfo packageInfo : packageManager.getInstalledPackages(0)) {
            AppLogger.e("isAppInstalled-", packageInfo.packageName);
            if (packageInfo.packageName.equals(AppConstant.PACKAGE_WHATSAPP)) {

                return true;
            }
        }

        return false;
    }

    private void getStudentList(String groupid)
    {
        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Processing");
        progress.setMessage("fetching data..");
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        map_data.put("Group_Id",groupid);    //"576232"
        RemoteApi.Companion.invoke().getGroupStudentDetailsByGroupIb(map_data)
                .enqueue(new Callback<StudentListDataModel>()
                {
                    @Override
                    public void onResponse(Call<StudentListDataModel> call, Response<StudentListDataModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {
                                progress.dismiss();
                                list.clear();
                                listchilds.clear();
                                if (response.body().getStatus().equals("success")){
                                    if (!(response.body().getTotal_student_list() == null || response.body().getTotal_student_list().isEmpty() || response.body().getTotal_student_list().equals("") || response.body().getTotal_student_list().equals("null"))) {
                                        listchilds = response.body().getTotal_student_list();

                                        for (int i = 0; i < listchilds.size(); i++) {
                                            list.add(listchilds.get(i));
                                        }

                                        if (!list.isEmpty()) {
                                            binding.rvChooseStudent.setVisibility(View.GONE);
                                            binding.rvChooseStudent2.setVisibility(View.VISIBLE);
                                            binding.studentListMessage.setVisibility(View.GONE);
                                            studentListAdapter2 = new CreateGroupStudentListAdapter(getActivity(), list);
                                            binding.rvChooseStudent2.setAdapter(studentListAdapter2);
                                          //  studentListAdapter2.setData(list);
                                        } else {
                                            binding.rvChooseStudent2.setVisibility(View.GONE);
                                            binding.rvChooseStudent.setVisibility(View.GONE);
                                            binding.studentListMessage.setVisibility(View.VISIBLE);
                                        }

                                    }
                                    else{
                                        binding.rvChooseStudent.setVisibility(View.GONE);
                                        binding.rvChooseStudent2.setVisibility(View.GONE);
                                        binding.studentListMessage.setVisibility(View.VISIBLE);
                                       // Toast.makeText(getActivity(), "No Student Found", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                            else{
                                progress.dismiss();
                                binding.rvChooseStudent.setVisibility(View.GONE);
                                binding.rvChooseStudent2.setVisibility(View.GONE);
                                binding.studentListMessage.setVisibility(View.VISIBLE);
                             //   Toast.makeText(getActivity(), "No Student Found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            progress.dismiss();
                            Toast.makeText(getActivity(), "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<StudentListDataModel> call, Throwable t)
                    {  progress.dismiss();
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}