package com.auro.application.home.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivityAppLanguageBinding;
import com.auro.application.databinding.ActivityPartnerListingBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.LanguageMasterReqModel;
import com.auro.application.home.data.model.PartnerDetailModel;
import com.auro.application.home.data.model.PartnerModel;
import com.auro.application.home.data.model.SelectLanguageModel;
import com.auro.application.home.data.model.response.LanguageListResModel;
import com.auro.application.home.data.model.response.LanguageResModel;
import com.auro.application.home.presentation.view.adapter.LanguageAdapter;
import com.auro.application.home.presentation.view.adapter.PartnerListAdapter;
import com.auro.application.home.presentation.viewmodel.AppLanguageViewModel;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherResModel;
import com.auro.application.teacher.presentation.view.fragment.TeacherListForInviteFragment;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.firebaseAnalytics.AnalyticsRegistry;
import com.auro.application.util.strings.AppStringDynamic;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerListingActivity extends BaseActivity {

    @Inject
    @Named("AppLanguageActivity")
    ViewModelFactory viewModelFactory;
    ActivityPartnerListingBinding binding;

    private AppLanguageViewModel viewModel;
    private Context mContext;
    PartnerListAdapter adapter;
    List<PartnerDetailModel> laugList;
    boolean click = true;
    LanguageMasterDynamic language;
    PrefModel prefModel;
    Details details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayout());
        binding.setLifecycleOwner(this);
        mContext = PartnerListingActivity.this;

        init();
        setListener();


    }

    @Override
    protected void init() {

        binding.recyclerViewpartner.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerViewpartner.setHasFixedSize(true);
        binding.recyclerViewpartner.setNestedScrollingEnabled(false);
        getPartnerList();
        AppStringDynamic.setPartnerListStrings(binding);

        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        details = prefModel.getLanguageMasterDynamic().getDetails();

    }

    @Override
    protected void setListener() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();


        ViewUtil.setProfilePic(binding.imageView6);
        binding.RPTextView9.setText("Meet Our Partners");
        binding.txtusername.setText(prefModel.getStudentData().getStudentName());
        binding.submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PartnerListingActivity.this, DashBoardMainActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_partner_listing;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void getPartnerList()
    {
        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String dashboardsubjectcode = prefs.getString("dashboardsubjectcode", "");
        HashMap<String,String> map_data = new HashMap<>();
        String gradeid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getGrade();
        map_data.put("grade",gradeid);
        map_data.put("subject",dashboardsubjectcode);
        RemoteApi.Companion.invoke().partnerList(map_data)
                .enqueue(new Callback<PartnerModel>()
                {
                    @Override
                    public void onResponse(Call<PartnerModel> call, Response<PartnerModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {

                                if (response.body().getStatus().equals("success")){
                                    if (!(response.body().getPartnerlist() == null || response.body().getPartnerlist().isEmpty() || response.body().getPartnerlist().equals("") || response.body().getPartnerlist().equals("null"))) {
                                        laugList = response.body().getPartnerlist();
                                        adapter = new PartnerListAdapter(PartnerListingActivity.this,laugList);
                                        binding.recyclerViewpartner.setAdapter(adapter);
                                    }
                                    else{
                                        finishAndRemoveTask();
                                    }
                                }

                            }
                            else {

                                Toast.makeText(PartnerListingActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(PartnerListingActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PartnerModel> call, Throwable t)
                    {
                        Toast.makeText(PartnerListingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}