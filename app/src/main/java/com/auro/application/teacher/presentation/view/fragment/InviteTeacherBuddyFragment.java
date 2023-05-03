package com.auro.application.teacher.presentation.view.fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.auro.application.core.common.Status.DISTRICT;
import static com.auro.application.core.common.Status.GENDER;
import static com.auro.application.core.common.Status.GRADE;
import static com.auro.application.core.common.Status.REFFER_API_ERROR;
import static com.auro.application.core.common.Status.REFFER_API_SUCCESS;
import static com.auro.application.core.common.Status.SCHOOL;
import static com.auro.application.core.common.Status.SEND_INVITE_CLICK;
import static com.auro.application.core.common.Status.STATE;
import static com.auro.application.util.AppUtil.commonCallBackListner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import com.auro.application.databinding.FragmentInviteteacherbuddyBinding;
import com.auro.application.databinding.FragmentTeacherpassportBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.DistrictData;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.GradeData;
import com.auro.application.home.data.model.GradeDataModel;
import com.auro.application.home.data.model.SchoolData;
import com.auro.application.home.data.model.StateData;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.home.presentation.view.activity.ChildAccountsActivity;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.view.adapter.SelectYourParentChildAdapter;
import com.auro.application.teacher.data.model.response.AcceptTeacherBuddyDataResModel;
import com.auro.application.teacher.data.model.response.AcceptTeacherBuddyResModel;
import com.auro.application.teacher.data.model.response.MyBuddyDataResModel;
import com.auro.application.teacher.data.model.response.MyBuddyResModel;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.teacher.data.model.response.ReceiveTeacherBuddyDataResModel;
import com.auro.application.teacher.data.model.response.ReceiveTeacherBuddyResModel;
import com.auro.application.teacher.data.model.response.StudentPassportDetailResModel;
import com.auro.application.teacher.data.model.response.TeacherGradeResModel;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherDataResModel;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherResModel;
import com.auro.application.teacher.data.model.response.TeacherStudentPassportResModel;
import com.auro.application.teacher.presentation.view.adapter.AcceptBuddyAdapter;
import com.auro.application.teacher.presentation.view.adapter.ReceiveBuddyAdapter;
import com.auro.application.teacher.presentation.view.adapter.StudentPassportListAdapter;
import com.auro.application.teacher.presentation.view.adapter.TeacherGradeSpinnerAdapter;
import com.auro.application.teacher.presentation.view.adapter.TeacherInviteTeacherAdapter;
import com.auro.application.teacher.presentation.viewmodel.InviteTeacherViewModel;
import com.auro.application.teacher.presentation.viewmodel.MyClassroomViewModel;
import com.auro.application.teacher.presentation.viewmodel.StudentPassportViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringTeacherDynamic;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteTeacherBuddyFragment extends BaseFragment implements CommonCallBackListner,View.OnClickListener {
    @Inject
    @Named("MyStudentPassportFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "MyStudentPassportFragment";
    FragmentInviteteacherbuddyBinding binding;
   InviteTeacherViewModel viewModel;
   // private MyClassroomViewModel viewModel;
    StudentPassportListAdapter studentListAdapter;
    TeacherStudentPassportResModel resModel;
    boolean isStateRestore;
    Details details;
    String gradeid;

    List<MyBuddyDataResModel> listchilds = new ArrayList<>();
    List<MyBuddyDataResModel> list = new ArrayList<>();
    List<TeacherInviteTeacherDataResModel> listchilds1 = new ArrayList<>();
    List<TeacherInviteTeacherDataResModel> list1 = new ArrayList<>();
    List<ReceiveTeacherBuddyDataResModel> listchildsrec = new ArrayList<>();
    List<ReceiveTeacherBuddyDataResModel> listrec = new ArrayList<>();

    public InviteTeacherBuddyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);


        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        getTeacherBuddyList();
        getReceiveTeacherBuddyList();
        return binding.getRoot();

    }

    @Override
    protected void init() {


        AppStringTeacherDynamic.setInviteTeacherBuddyFragmentStrings(binding);

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
            binding.recyclerviewbuddylist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            binding.recyclerviewbuddylist.setHasFixedSize(true);
            binding.btnaddbuddy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
                    String multiple_teacherid = prefs.getString("multiple_teacherid", "");
                    if (multiple_teacherid.equals("null")||multiple_teacherid.isEmpty()||multiple_teacherid.equals("")){
                        Toast.makeText(getActivity(), "Please select teacher", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        sentInvite(multiple_teacherid);
                    }

                }
            });
            binding.txtrefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                   getTeacherList();
                  //  getTeacherBuddyList();
                   // getReceiveTeacherBuddyList();
                }
            });
            binding.btnaddbuddyinvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    binding.mainParentLayout.setVisibility(View.VISIBLE);
//                    binding.mainParentLayoutInvite.setVisibility(View.GONE);
                    getTeacherList();
                }
            });

            binding.imgbuddy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFragment(new TeacherBuddyFragment());
                }
            });
            binding.btnshare.setOnClickListener(this);
            binding.btnsharebuddy.setOnClickListener(this);
           observeServiceResponse();
        }
    }

    private void sentInvite(String teacherid)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        map_data.put("user_id",userid);
        map_data.put("teacher_ids", teacherid);  //"576232"
        RemoteApi.Companion.invoke().getSendInvite(map_data)
                .enqueue(new Callback<TeacherInviteTeacherResModel>()
                {
                    @Override
                    public void onResponse(Call<TeacherInviteTeacherResModel> call, Response<TeacherInviteTeacherResModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {

                                Toast.makeText(getActivity(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                editor.putString("multiple_teacherid", "");
                                editor.apply();
                                openFragment(new TeacherBuddyFragment());
                            }
                            else {

                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TeacherInviteTeacherResModel> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }



    private void observeServiceResponse() {
     ((HomeActivity) getActivity()).observeServiceResponse();

    }
    public void sendRefferCallback(DynamiclinkResModel dynamiclinkResModel, int status) {
        if (commonCallBackListner != null) {
            if (status == 1) {
                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(status, Status.REFFER_API_SUCCESS, dynamiclinkResModel));
            } else {
                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(status, Status.REFFER_API_ERROR, dynamiclinkResModel));
            }
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

    @Override
    protected int getLayout() {
        return R.layout.fragment_inviteteacherbuddy;
    }



    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        setToolbar();
        setListener();
    }



    private void getTeacherList()
    {

        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle(details.getProcessing());
        progress.setMessage(details.getProcessing());
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        map_data.put("user_id",userid);    //"576232"
        RemoteApi.Companion.invoke().getInviteTeacherList(map_data)
                .enqueue(new Callback<TeacherInviteTeacherResModel>()
                {
                    @Override
                    public void onResponse(Call<TeacherInviteTeacherResModel> call, Response<TeacherInviteTeacherResModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {
                                progress.dismiss();
                                list1.clear();
                                listchilds1.clear();
                                if (response.body().getStatus().equals("success")){
                                    if (!(response.body().getTeacherinvitedata() == null || response.body().getTeacherinvitedata().isEmpty() || response.body().getTeacherinvitedata().equals("") || response.body().getTeacherinvitedata().equals("null"))) {

                                        binding.mainParentLayoutInvite.setVisibility(View.GONE);
                                        binding.mainParentLayout.setVisibility(View.VISIBLE);
                                        listchilds1 = response.body().getTeacherinvitedata();
                                        for (int i = 0; i < listchilds1.size(); i++) {
                                            list1.add(listchilds1.get(i));
                                        }

                                        TeacherInviteTeacherAdapter studentListAdapter = new TeacherInviteTeacherAdapter(getActivity(), list1);
                                        binding.recyclerviewbuddylist.setAdapter(studentListAdapter);
                                       //studentListAdapter.notifyDataSetChanged();
                                        binding.recyclerviewbuddylist.post(new Runnable()
                                        {
                                            @Override
                                            public void run() {
                                                studentListAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                      else{
                                        binding.mainParentLayoutInvite.setVisibility(View.VISIBLE);
                                        binding.mainParentLayout.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "No Teacher Found", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                else {

                                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                                }

                            }
                            else {
                                progress.dismiss();
                               Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            progress.dismiss();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TeacherInviteTeacherResModel> call, Throwable t)
                    {
                        progress.dismiss();
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }


    private void getReceiveTeacherBuddyList()
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        map_data.put("user_id",userid);    //"576232"
        RemoteApi.Companion.invoke().getTeacherInvitationList(map_data)
                .enqueue(new Callback<ReceiveTeacherBuddyResModel>()
                {
                    @Override
                    public void onResponse(Call<ReceiveTeacherBuddyResModel> call, Response<ReceiveTeacherBuddyResModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {
                                listrec.clear();
                                listchildsrec.clear();
                                if (response.body().getStatus().equals("success")){
                                    if (!(response.body().getTeacherinvitedata() == null || response.body().getTeacherinvitedata().isEmpty() || response.body().getTeacherinvitedata().equals("") || response.body().getTeacherinvitedata().equals("null"))) {

                                        listchildsrec = response.body().getTeacherinvitedata();
                                        for (int i = 0; i < listchildsrec.size(); i++) {
                                            if (listchildsrec.get(i).getAccepted_status().equals("0") || listchildsrec.get(i).getAccepted_status().equals(0)) {

                                                listrec.add(listchildsrec.get(i));
                                                int countnot = listrec.size();
                                                if (countnot == 0){
                                                    binding.txtcountpending.setVisibility(View.GONE);
                                                }
                                                else{
                                                    binding.txtcountpending.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }

                                    }

                                }
                            }
                            else {
                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            binding.txtcountpending.setVisibility(View.VISIBLE);
                          //  Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReceiveTeacherBuddyResModel> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }

    private void getTeacherBuddyList()
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        map_data.put("user_id",userid);    //"576232"
        RemoteApi.Companion.invoke().getMyBuddyListInvite(map_data)
                .enqueue(new Callback<MyBuddyResModel>()
                {
                    @Override
                    public void onResponse(Call<MyBuddyResModel> call, Response<MyBuddyResModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {
                                list.clear();
                                listchilds.clear();
                                if (response.body().getStatus().equals("success")){
                                    if (!(response.body().getTeacherinvitedata() == null || response.body().getTeacherinvitedata().isEmpty() || response.body().getTeacherinvitedata().equals("") || response.body().getTeacherinvitedata().equals("null"))) {
                                        listchilds = response.body().getTeacherinvitedata();




                                        openFragment(new TeacherBuddyFragment());



//                                        AcceptBuddyAdapter studentListAdapter = new AcceptBuddyAdapter(getActivity(), list);
//                                        binding.recyclerviewbuddylist.setAdapter(studentListAdapter);
                                    }
                                    else{

                                        binding.mainParentLayoutInvite.setVisibility(View.VISIBLE);
                                        binding.mainParentLayout.setVisibility(View.GONE);
                                    }

                                }
                                else{
                                    binding.mainParentLayoutInvite.setVisibility(View.VISIBLE);
                                    binding.mainParentLayout.setVisibility(View.GONE);
                                }




                            }
                            else {

                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyBuddyResModel> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel)
    {

        if (commonDataModel.getClickType()==SEND_INVITE_CLICK)
        {
            TeacherInviteTeacherDataResModel stateDataModel = (TeacherInviteTeacherDataResModel) commonDataModel.getObject();
            String userid = stateDataModel.getUser_id();
        }
        else if (commonDataModel.getClickType()==REFFER_API_SUCCESS){
            AppLogger.e("performClick-", "REFFER_API_SUCCESS");
            handleRefferProgress(1);
            if (isVisible()) {
                AppLogger.e("performClick-", "REFFER_API_SUCCESS");

            }
        }
        else if (commonDataModel.getClickType()==REFFER_API_ERROR){
            if (isVisible()) {
                AppLogger.e("performClick-", "REFFER_API_ERROR");
                ((HomeActivity) getActivity()).setCommonCallBackListner(null);
                handleRefferProgress(1);
            }
        }
    }
    private void performClick(String link) throws IllegalStateException {
        AppLogger.e("performClick-", "performClick calling 1");
        String completeLink = getActivity().getResources().getString(R.string.teacher_share_msg);
        completeLink = completeLink + link;
        openWhatsApp("", completeLink);


    }
    private void openWhatsApp(String numero, String mensaje) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + numero + "&text=" + mensaje)));
        } catch (Exception e) {
            ViewUtil.showSnackBar(binding.getRoot(), "Please install the whatsapp first");
        }

    }

    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(getActivity(), fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnshare:
                getTeacherShare();
                break;
            case R.id.btnsharebuddy:
                getTeacherShare();
                break;

        }
    }
    private void getTeacherShare()
    {
        handleRefferProgress(0);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        HashMap<String,String> map_data = new HashMap<>();
        String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        map_data.put("reffer_user_id",userid);
        map_data.put("source",AppConstant.AURO_ID);
        map_data.put("navigation_to",AppConstant.NavigateToScreen.TEACHER_DASHBOARD);
        map_data.put("reffer_type", String.valueOf(AppConstant.UserType.TEACHER));
        map_data.put("user_type_id", String.valueOf(AppConstant.UserType.TEACHER)); //"576232"//"576232"
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
}