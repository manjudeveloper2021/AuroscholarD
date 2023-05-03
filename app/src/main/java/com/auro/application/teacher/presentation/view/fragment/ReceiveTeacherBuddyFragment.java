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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.AcceptBuddyLayoutBinding;
import com.auro.application.databinding.FragmentInviteteacherbuddyBinding;
import com.auro.application.databinding.ReceiveBuddyLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.teacher.data.model.response.AcceptTeacherBuddyDataResModel;
import com.auro.application.teacher.data.model.response.AcceptTeacherBuddyResModel;
import com.auro.application.teacher.data.model.response.ReceiveTeacherBuddyDataResModel;
import com.auro.application.teacher.data.model.response.ReceiveTeacherBuddyResModel;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherDataResModel;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherResModel;
import com.auro.application.teacher.data.model.response.TeacherStudentPassportResModel;
import com.auro.application.teacher.presentation.view.adapter.AcceptBuddyAdapter;
import com.auro.application.teacher.presentation.view.adapter.ReceiveBuddyAdapter;
import com.auro.application.teacher.presentation.view.adapter.StudentPassportListAdapter;
import com.auro.application.teacher.presentation.view.adapter.TeacherInviteTeacherAdapter;
import com.auro.application.teacher.presentation.viewmodel.StudentPassportViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.RemoteApi;
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

public class ReceiveTeacherBuddyFragment extends BaseFragment {
    @Inject
    @Named("MyStudentPassportFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "MyStudentPassportFragment";
    ReceiveBuddyLayoutBinding binding;
    StudentPassportViewModel viewModel;
    StudentPassportListAdapter studentListAdapter;
    TeacherStudentPassportResModel resModel;
    boolean isStateRestore;
    Details details;
    String gradeid;
    List<ReceiveTeacherBuddyDataResModel> listchilds = new ArrayList<>();
    List<ReceiveTeacherBuddyDataResModel> list = new ArrayList<>();

    public ReceiveTeacherBuddyFragment() {
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
        binding.rvreceivebuddy.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.rvreceivebuddy.setHasFixedSize(true);
        binding.txtrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReceiveTeacherBuddyList();
            }
        });
        return binding.getRoot();

    }

    @Override
    protected void init() {
        AppStringTeacherDynamic.setReceiveeacherBuddyFragmentStrings(binding);

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
            getReceiveTeacherBuddyList();
           // observeServiceResponse();
        }


    }


    @Override
    protected int getLayout() {
        return R.layout.receive_buddy_layout;
    }



    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        setToolbar();
        setListener();
    }



    private void getReceiveTeacherBuddyList()
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
        RemoteApi.Companion.invoke().getTeacherInvitationList(map_data)
                .enqueue(new Callback<ReceiveTeacherBuddyResModel>()
                {
                    @Override
                    public void onResponse(Call<ReceiveTeacherBuddyResModel> call, Response<ReceiveTeacherBuddyResModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {
                                progress.dismiss();
                                list.clear();
                                listchilds.clear();
                                if (response.body().getStatus().equals("success")){
                                    if (!(response.body().getTeacherinvitedata() == null || response.body().getTeacherinvitedata().isEmpty() || response.body().getTeacherinvitedata().equals("") || response.body().getTeacherinvitedata().equals("null"))) {



                                        listchilds = response.body().getTeacherinvitedata();

                                        for (int i = 0; i < listchilds.size(); i++) {
                                            if (listchilds.get(i).getAccepted_status().equals("0") || listchilds.get(i).getAccepted_status().equals(0)) {

                                                list.add(listchilds.get(i));
                                            }
                                        }


                                        ReceiveBuddyAdapter studentListAdapter = new ReceiveBuddyAdapter(getActivity(), list);
                                        binding.rvreceivebuddy.setAdapter(studentListAdapter);
                                        studentListAdapter.notifyDataSetChanged();
                                        binding.rvreceivebuddy.invalidate();
                                        binding.rvreceivebuddy.refreshDrawableState();
                                    }

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
                    public void onFailure(Call<ReceiveTeacherBuddyResModel> call, Throwable t)
                    {
                        progress.dismiss();
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }







}