package com.auro.application.home.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivitySetPinBinding;
import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.DistrictData;
import com.auro.application.home.data.model.DistrictDataModel;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.data.model.signupmodel.request.RegisterApiReqModel;
import com.auro.application.home.data.model.signupmodel.request.SetUsernamePinReqModel;
import com.auro.application.home.data.model.signupmodel.response.RegisterApiResModel;
import com.auro.application.home.data.model.signupmodel.response.SetUsernamePinResModel;
import com.auro.application.home.presentation.viewmodel.SetPinViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetParentPinActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    @Named("SetPinActivity")
    ViewModelFactory viewModelFactory;
    ActivitySetPinBinding binding;

    SetPinViewModel viewModel;
    UserDetailResModel resModel;
    String isComingFrom = "";
    CheckUserResModel checkUserResModel;
    RegisterApiResModel registerApiResModel;
    UserDetailResModel userDetailForSecondStudent;
    Details details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        setListener();
        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();


    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(this, getLayout());

        binding.setUserName.setVisibility(View.GONE);
        binding.mobileLayout.setVisibility(View.GONE);

        binding.setLifecycleOwner(this);
        if (getIntent() != null) {
            resModel = (UserDetailResModel) getIntent().getParcelableExtra(AppConstant.USER_PROFILE_DATA_MODEL);
            isComingFrom = getIntent().getStringExtra(AppConstant.COMING_FROM);

        }
        binding.titleFirst.setVisibility(View.GONE);
        if (isComingFrom.equalsIgnoreCase(AppConstant.ComingFromStatus.COMING_FROM_ADD_STUDENT)) {
            binding.titleFirst.setText(this.getString(R.string.hey_enter_set));
        } else {
            binding.titleFirst.setText(this.getString(R.string.add_student));
        }
        AppStringDynamic.setPinPagetrings(binding);

    }

    @Override
    protected void setListener() {
        binding.backButton.setOnClickListener(this);
        binding.btDoneNew.setOnClickListener(this);


    }

    @Override
    protected int getLayout() {
        return R.layout.activity_set_pin;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.bt_done_new:
                AppLogger.e("onClick--", "step 1");
                String pin = binding.pinView.getText().toString();
                String confirmpin = binding.confirmPin.getText().toString();
                Details details1 = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

                if(pin.isEmpty()||pin.equals("")){
                    Toast.makeText(this, details1.getEnter_the_pin(), Toast.LENGTH_SHORT).show();
                }
                else if (pin.length()<4){
                    Toast.makeText(this, details1.getEnter_pin_digit(), Toast.LENGTH_SHORT).show();

                }
                else if (confirmpin.isEmpty()||confirmpin.equals("")){
                    Toast.makeText(this, details1.getEnter_the_confirm_pin(), Toast.LENGTH_SHORT).show();

                }
                else if (confirmpin.length()<4){
                    Toast.makeText(this, details1.getEnter_confirmpin_digit(), Toast.LENGTH_SHORT).show();

                }
                else if (pin == confirmpin || pin.equals(confirmpin)){
                    setparentpin(pin);
                }
                else{
                    Toast.makeText(this, details1.getPin_and_confirm_not_match(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void setparentpin(String pin)
    {

        String parentusername = getIntent().getExtras().getString("parentusername");
        String parentuserid = getIntent().getExtras().getString("parentuserid");
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",parentuserid);
        map_data.put("pin",pin);
        map_data.put("user_prefered_language_id",AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId());
        RemoteApi.Companion.invoke().setparentuserpin(map_data)
                .enqueue(new Callback<SetUsernamePinResModel>() {
                    @Override
                    public void onResponse(Call<SetUsernamePinResModel> call, Response<SetUsernamePinResModel> response) {
                        try {
                            if (response.code()==400){
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(SetParentPinActivity.this,message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }



                            }
                            else if (response.isSuccessful()) {


                                String message = response.body().getMessage();
                                ViewUtil.showSnackBar(binding.getRoot(), message);
                                Intent i = new Intent(getApplicationContext(), ParentProfileActivity.class);


                                startActivity(i);


                            } else {
                              //  ViewUtil.showSnackBar(binding.getRoot(), response.message());
                                Toast.makeText(SetParentPinActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(SetParentPinActivity.this, details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                        }
                    }

                        @Override
                    public void onFailure(Call<SetUsernamePinResModel> call, Throwable t)
                    {
                        ViewUtil.showSnackBar(binding.getRoot(),t.getMessage());
                        Toast.makeText(SetParentPinActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}