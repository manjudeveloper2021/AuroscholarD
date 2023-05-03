package com.auro.application.teacher.presentation.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.AcceptBuddyLayoutBinding;
import com.auro.application.databinding.FragmentInviteteacherbuddyBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithPinActivity;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.teacher.data.model.response.AcceptTeacherBuddyDataResModel;
import com.auro.application.teacher.data.model.response.AcceptTeacherBuddyResModel;
import com.auro.application.teacher.data.model.response.MyBuddyDataResModel;
import com.auro.application.teacher.data.model.response.MyBuddyResModel;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherDataResModel;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherResModel;
import com.auro.application.teacher.data.model.response.TeacherStudentPassportResModel;
import com.auro.application.teacher.presentation.view.adapter.AcceptBuddyAdapter;
import com.auro.application.teacher.presentation.view.adapter.MyBuddyListAdapter;
import com.auro.application.teacher.presentation.view.adapter.StudentPassportListAdapter;
import com.auro.application.teacher.presentation.view.adapter.TeacherInviteTeacherAdapter;
import com.auro.application.teacher.presentation.viewmodel.StudentPassportViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.strings.AppStringTeacherDynamic;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptTeacherBuddyFragment extends BaseFragment {
    @Inject
    @Named("MyStudentPassportFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "MyStudentPassportFragment";
    AcceptBuddyLayoutBinding binding;
    StudentPassportViewModel viewModel;
    StudentPassportListAdapter studentListAdapter;
    TeacherStudentPassportResModel resModel;
    boolean isStateRestore;
    Details details;
    String gradeid;
    List<MyBuddyDataResModel> listchilds = new ArrayList<>();
    List<MyBuddyDataResModel> list = new ArrayList<>();
    List<MyBuddyDataResModel> newlist = new ArrayList<>();

    public AcceptTeacherBuddyFragment() {
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
        binding.btnaddbuddyinvite.setText(details.getAdd_new_buddy() != null   ? details.getAdd_new_buddy() : "Add New Buddies");
        binding.btnaddbuddyinvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTeacherList();
            }
        });
        binding.txtrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTeacherBuddyList();
            }
        });
        return binding.getRoot();

    }

    @Override
    protected void init() {
        AppStringTeacherDynamic.setAcceptTeacherBuddyFragmentStrings(binding);

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
            binding.rvacceptbuddy.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            binding.rvacceptbuddy.setHasFixedSize(true);
            getTeacherBuddyList();
           // getTeacherBuddyList();
           // observeServiceResponse();
        }

    }

    private void getTeacherList()
    {
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

                                if (response.body().getStatus().equals("success")){
                                    if (!(response.body().getTeacherinvitedata() == null || response.body().getTeacherinvitedata().isEmpty() || response.body().getTeacherinvitedata().equals("") || response.body().getTeacherinvitedata().equals("null"))) {

                                        openFragment(new TeacherListForInviteFragment());
                                    }
                                    else{

                                        Toast.makeText(getActivity(), "No Teacher Found", Toast.LENGTH_SHORT).show();
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
                    public void onFailure(Call<TeacherInviteTeacherResModel> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }

    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(getActivity(), fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }


    @Override
    protected int getLayout() {
        return R.layout.accept_buddy__layout;
    }



    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        setToolbar();
        setListener();
    }

    public void getTeacherBuddyList()
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
        RemoteApi.Companion.invoke().getMyBuddyList(map_data)
                .enqueue(new Callback<MyBuddyResModel>()
                {
                    @Override
                    public void onResponse(Call<MyBuddyResModel> call, Response<MyBuddyResModel> response)
                    {
                        try {
                            if (response.code()==400){
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                   // Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }



                            }
                            else if (response.isSuccessful()) {
                                progress.dismiss();
                                list.clear();
                                listchilds.clear();
                                newlist.clear();
                                if (response.body().getStatus().equals("success")){
                                   if ((response.body().getTeacherinvitedata() != null || !response.body().getTeacherinvitedata().isEmpty() || !response.body().getTeacherinvitedata().equals("") || !response.body().getTeacherinvitedata().equals("null"))) {

                                       listchilds = response.body().getTeacherinvitedata();

                                            for (int i = 0; i < listchilds.size(); i++) {
                                              //  if (listchilds.get(i).getAccepted_status().equals("1") || listchilds.get(i).getAccepted_status().equals(1)) {
                                              //  Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();

                                                    list.add(listchilds.get(i));

                                               // Toast.makeText(getActivity(), listchilds.get(i).getTeacher_name(), Toast.LENGTH_SHORT).show();
                                           }



                                       MyBuddyListAdapter studentListAdapter = new MyBuddyListAdapter(getActivity(), list);
                                       binding.rvacceptbuddy.setAdapter(studentListAdapter);
                                       studentListAdapter.notifyDataSetChanged();

                                   }

else{
                                       openFragment(new InviteTeacherBuddyFragment());
                                   }

                                   // }

                                }

                            }
                            else {
                                progress.dismiss();
                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            progress.dismiss();
                            Toast.makeText(getActivity(), "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyBuddyResModel> call, Throwable t)
                    {
                        progress.dismiss();
                        Toast.makeText(getActivity(), "Internet connection", Toast.LENGTH_SHORT).show();
                    }
                });




    }

}