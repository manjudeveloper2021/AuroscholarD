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
import com.auro.application.core.common.ErrorStatus;
import com.auro.application.core.common.ResponseStatus;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.ActivityForgotPinBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.data.model.signupmodel.request.SetUsernamePinReqModel;
import com.auro.application.home.data.model.signupmodel.response.SetUsernamePinResModel;
import com.auro.application.home.presentation.viewmodel.SetPinViewModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentForgotPinActivity extends BaseActivity implements View.OnClickListener {

    @Inject
    @Named("ForgotPinActivity")
    ViewModelFactory viewModelFactory;
    ActivityForgotPinBinding binding;

    SetPinViewModel viewModel;
    UserDetailResModel resModel;
    PrefModel prefModel;
    Details details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        init();
        setListener();

    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(this, getLayout());

        binding.setLifecycleOwner(this);
        if (getIntent() != null) {
            resModel = (UserDetailResModel) getIntent().getParcelableExtra(AppConstant.USER_PROFILE_DATA_MODEL);
        }
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppStringDynamic.setForgetPinActivityStrings(binding);
    }

    @Override
    protected void setListener() {
        binding.backButton.setOnClickListener(this);
        binding.btDoneNew.setOnClickListener(this);
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {

        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_forgot_pin;
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
                   String pinnew = binding.pinView.getText().toString();
                    setpin();
                }
                else{
                    Toast.makeText(this, details1.getPin_and_confirm_not_match(), Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }









    private void openChooseGradeActivity() {
        Intent tescherIntent = new Intent(this, ChooseGradeActivity.class);
        tescherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(tescherIntent);
        finish();
    }



    private void setpin()
    {

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String userid = prefModel.getCheckUserResModel().getUserDetails().get(0).getUserId();
       String langid = prefModel.getUserLanguageId();
        String pin = binding.pinView.getText().toString();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);
        map_data.put("pin", pin);
        map_data.put("user_prefered_language_id",langid);
        RemoteApi.Companion.invoke().setparentpin(map_data)
                .enqueue(new Callback<CheckUserResModel>()
                {
                    @Override
                    public void onResponse(Call<CheckUserResModel> call, Response<CheckUserResModel> response) {
                        try {
                            if (response.code()==400){
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(ParentForgotPinActivity.this,message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }



                            }
                            else if (response.code() == 200) {
                                Toast.makeText(ParentForgotPinActivity.this, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ParentForgotPinActivity.this, LoginActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(ParentForgotPinActivity.this, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                Log.d("Responseerror", "onResponse: " + response.body().toString());
                            }
                        } catch (Exception e) {
                            Toast.makeText(ParentForgotPinActivity.this, details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckUserResModel> call, Throwable t)
                    {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}