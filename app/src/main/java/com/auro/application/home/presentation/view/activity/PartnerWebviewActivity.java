package com.auro.application.home.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivityPartnerListingBinding;
import com.auro.application.databinding.ActivityPartnerWebviewBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.PartnerDetailModel;
import com.auro.application.home.data.model.PartnerModel;
import com.auro.application.home.presentation.view.adapter.PartnerListAdapter;
import com.auro.application.home.presentation.viewmodel.AppLanguageViewModel;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerWebviewActivity extends BaseActivity {

    @Inject
    @Named("AppLanguageActivity")
    ViewModelFactory viewModelFactory;
    ActivityPartnerWebviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayout());
        binding.setLifecycleOwner(this);


        init();
        setListener();
        getPartnerCount();

    }

    @Override
    protected void init() {
        String partnerweburl = getIntent().getStringExtra("partnerweburl");
        binding.webpartner.getSettings().setJavaScriptEnabled(true);
        binding.webpartner.loadUrl(partnerweburl);

    }

    @Override
    protected void setListener() {
        String partnername = getIntent().getStringExtra("partnername");
        String partnerlogo = getIntent().getStringExtra("partnerlogo");
        binding.txtname.setText(partnername);
        Glide.with(PartnerWebviewActivity.this)
                .load(partnerlogo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.teacher_select)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).circleCrop()
                .into(binding.auroScholarLogo);
       binding.backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(PartnerWebviewActivity.this, DashBoardMainActivity.class));
           }
       });

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_partner_webview;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void getPartnerCount()
    {
        String partnerid = getIntent().getStringExtra("partnerid");
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String userid = prefModel.getUserId();
        HashMap<String,String> map_data = new HashMap<>();

        map_data.put("UserId",userid);
        map_data.put("PartnerId",partnerid);
        RemoteApi.Companion.invoke().partnerClick(map_data)
                .enqueue(new Callback<PartnerModel>()
                {
                    @Override
                    public void onResponse(Call<PartnerModel> call, Response<PartnerModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {

                                if (response.body().getStatus().equals("success")){
                                 //   Toast.makeText(PartnerWebviewActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                                }

                            }
                            else {

                             //   Toast.makeText(PartnerWebviewActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(PartnerWebviewActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PartnerModel> call, Throwable t)
                    {
                        Toast.makeText(PartnerWebviewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}