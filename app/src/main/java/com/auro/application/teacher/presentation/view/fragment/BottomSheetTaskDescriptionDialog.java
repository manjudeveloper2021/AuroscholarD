package com.auro.application.teacher.presentation.view.fragment;


import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.BottomSheetAddUserStepLayoutBinding;
import com.auro.application.databinding.BottomSheetTaskDescriptionLayoutBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.CourseModule.GenerateLinkList;
import com.auro.application.home.data.model.CourseModule.ModuleChapterList;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.data.model.signupmodel.AddStudentStepDataModel;
import com.auro.application.home.presentation.view.activity.CompleteStudentProfileWithPinActivity;
import com.auro.application.home.presentation.view.activity.SetPinActivity;
import com.auro.application.home.presentation.view.adapter.StepsAddChildAdapter;
import com.auro.application.home.presentation.view.fragment.StudentProfileFragment;
import com.auro.application.util.AppLogger;
import com.auro.application.util.CourseRemoteApi;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetTaskDescriptionDialog extends BottomSheetDialogFragment{
    BottomSheetTaskDescriptionLayoutBinding binding;
    String completeLink;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_task_description_layout, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        String lms_taskdescription = prefs.getString("lms_taskdescription", "");
        binding.tvdescValue.setText(lms_taskdescription);
        binding.buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGenerateLink();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    private void performClick(String link) throws IllegalStateException {
        AppLogger.e("performClick-", "performClick calling 1");
        completeLink = "";
        completeLink = getActivity().getResources().getString(R.string.teacher_taskinfo_msg);
        completeLink = completeLink + link;
        openWhatsApp("", completeLink);


    }

    private void getGenerateLink()
    {
        SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        String lmsuserid = prefs.getString("lmsuserid", "");
        String lms_moduleid = prefs.getString("lms_moduleid","");
        String lms_courseid = prefs.getString("lms_courseid","");
        String lms_taskid = prefs.getString("lms_taskid","");
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("taskID",lms_taskid);
        map_data.put("userID",lmsuserid);
        map_data.put("lmsPartnerID","11");
        map_data.put("courseID",lms_courseid);
        map_data.put("moduleID",lms_moduleid);
        CourseRemoteApi.Companion.invoke().getGenerateLink(map_data)
                .enqueue(new Callback<GenerateLinkList>()
                {
                    @Override
                    public void onResponse(Call<GenerateLinkList> call, Response<GenerateLinkList> response)
                    {
                        try {
                            if (response.code()==400){
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
                                }

                                catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            else if (response.isSuccessful()) {

                                if (!(response.body().getResult() == null || response.body().getResult().isEmpty() || response.body().getResult().equals("") || response.body().getResult().equals("null"))) {
                                  String generatelink = response.body().getResult().get(0).getLink();
                                    performClick(generatelink);
                                }

                            }
                            else {
                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<GenerateLinkList> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openWhatsApp(String numero, String mensaje) {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + numero + "&text=" + mensaje)));
        } catch (Exception e) {
            ViewUtil.showSnackBar(binding.getRoot(), "Please install the whats app first");
        }

    }

}

