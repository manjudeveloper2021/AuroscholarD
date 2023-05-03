package com.auro.application.home.presentation.view.fragment;


import static android.content.Context.MODE_PRIVATE;
import static com.auro.application.core.common.Status.CLICK_CHILDPROFILE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.BottomStudentListBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithPinActivity;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.EnterParentPinActivity;
import com.auro.application.home.presentation.view.activity.EnterPinActivity;
import com.auro.application.home.presentation.view.activity.ForgotPinActivity;
import com.auro.application.home.presentation.view.activity.ParentProfileActivity;
import com.auro.application.home.presentation.view.activity.SetParentPinActivity;
import com.auro.application.home.presentation.view.activity.SetPinActivity;
import com.auro.application.home.presentation.view.adapter.SelectParentAdapter;
import com.auro.application.home.presentation.view.adapter.SelectYourChildAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetUsersDialog extends BottomSheetDialogFragment implements CommonCallBackListner {
    BottomStudentListBinding binding;
    CheckUserResModel checkUserResModel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_student_list, container, false);
        checkUserResModel = AuroAppPref.INSTANCE.getModelInstance().getCheckUserResModel();
        setAdapterAllListStudent(checkUserResModel.getUserDetails());


        LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
        Details details = model.getDetails();
        if (model!=null){
            binding.txtchildaccount.setText(details.getChild_accounts()!=null ? details.getChild_accounts() : "Child Accounts");
            binding.btnparent.setText(details.getContinue_parent_profile()!=null ? details.getContinue_parent_profile() : "Continue With Parent Profile");
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);
    }

    public void setAdapterAllListStudent(List<UserDetailResModel> totalStudentList) {
        List<UserDetailResModel> list = new ArrayList<>();
        List<UserDetailResModel> plist = new ArrayList<>();
        for (UserDetailResModel resmodel : totalStudentList) {
            if (resmodel.getIsMaster().equalsIgnoreCase("0")) {
                list.add(resmodel);
            }

        }
        for (UserDetailResModel resmodel : totalStudentList) {
            if (resmodel.getIsMaster().equalsIgnoreCase("1")) {
                plist.add(resmodel);
            }

        }

        binding.btnparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (plist.get(0).getPin().isEmpty() || plist.get(0).getPin().equals("")){
                    Intent i = new Intent(getActivity(), SetParentPinActivity.class);
                    i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
                    i.putExtra("parentusername", plist.get(0).getUserName());
                    i.putExtra("parentuserid", plist.get(0).getUserId());
                    getActivity().startActivity(i);
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                    editor.putString("usertype", "ParentLogin");
                    editor.putString("parentuserid", plist.get(0).getUserId());
                    editor.putString("parentusername", plist.get(0).getUserName());
                    editor.commit();
                }
                else{
                    String userid = plist.get(0).getUserId();
                    String username = plist.get(0).getUserName();
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                    editor.putString("usertype", "ParentLogin");
                    editor.putString("parentuserid", userid);
                    editor.putString("parentusername", username);
                    editor.commit();

                    Intent i = new Intent(getActivity(), EnterParentPinActivity.class);
                    i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
                    i.putExtra("parentusername", plist.get(0).getUserName());
                    i.putExtra("parentuserid", plist.get(0).getUserId());
                    getActivity().startActivity(i);
                }
            }
        });
        binding.btnaddstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetAddUserDialog bottomSheet = new BottomSheetAddUserDialog();
                bottomSheet.show(getActivity().getSupportFragmentManager(),
                        "ModalBottomSheet");

            }
        });
        binding.studentList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
       binding.studentList.setHasFixedSize(true);
       SelectYourChildAdapter studentListAdapter = new SelectYourChildAdapter(getActivity(), list, this);
       binding.studentList.setAdapter(studentListAdapter);


       binding.parentList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
       binding.parentList.setHasFixedSize(true);
       SelectParentAdapter studentListAdapter2 = new SelectParentAdapter(getActivity(), plist, this);
       binding.parentList.setAdapter(studentListAdapter2);

       if (list.size() == 5 || list.size() > 5){
           binding.btnaddstudent.setVisibility(View.GONE);
       }
       else{
           binding.btnaddstudent.setVisibility(View.VISIBLE);
       }

    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case CLICK_PARENTPROFILE:
                Intent i = new Intent(getActivity(), ParentProfileActivity.class);
                i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
              startActivity(i);

                break;

        case CLICK_CHILDPROFILE:
        Intent i1 = new Intent(getActivity(), DashBoardMainActivity.class);
        i1.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
        startActivity(i1);

        break;
    }

    }





}

