
package com.auro.application.home.presentation.view.activity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.auro.application.core.network.URLConstant;
import com.auro.application.core.util.uiwidget.RPEditText;
import com.auro.application.databinding.ActivityResetPasswordBinding;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.SetPasswordReqModel;
import com.auro.application.home.presentation.viewmodel.LoginScreenViewModel;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.teacher.presentation.view.activity.TeacherProfileActivity;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener, CommonCallBackListner {
    private static String TAG = ResetPasswordActivity.class.getSimpleName();
    @Inject
    @Named("ResetPasswordActivity")
    ViewModelFactory viewModelFactory;
    ActivityResetPasswordBinding binding;
    LoginScreenViewModel viewModel;
    String comingFrom = "";
    PrefModel prefModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setLogin(true);
        SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
        editor.putString("statusparentprofile", "false");
        editor.putString("isLogin","true");
        editor.putString("statusfillstudentprofile", "false");
        editor.putString("statussetpasswordscreen", "true");
        editor.putString("statuschoosegradescreen", "false");
        editor.putString("statussubjectpref","false");
        editor.putString("statusopenprofileteacher", "false");
        editor.putString("statusopendashboardteacher", "false");
        editor.putString("statuschoosedashboardscreen", "false");
        editor.putString("statusopenprofilewithoutpin", "false");
        editor.putString("statuslogin", "true");
        editor.apply();

    }

    @Override
    protected void init() {

        binding = DataBindingUtil.setContentView(this, getLayout());
        ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginScreenViewModel.class);
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        binding.setLifecycleOwner(this);
        if (getIntent() != null) {
            comingFrom = getIntent().getStringExtra(AppConstant.COMING_FROM);

                binding.headText.setText(this.getString(R.string.set_a_new_password));
                binding.RPAccept.setText(this.getString(R.string.set_password));
                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                prefModel.setCurrentScreenFlag(AppConstant.CurrentFlagStatus.SET_PASSWORD);
                AuroAppPref.INSTANCE.setPref(prefModel);

        }
        else {
            binding.headText.setText(this.getString(R.string.reset_password));
            binding.RPAccept.setText(this.getString(R.string.reset_password));

        }

        setListener();
        ViewUtil.customTextView(binding.termsCondition, this);







        AppUtil.loadAppLogo(binding.auroScholarLogo, this);
        AppStringDynamic.setResetPasswordPagetrings(binding);
    }

    @Override
    protected void setListener() {
        binding.backButton.setOnClickListener(this);
        binding.RPAccept.setOnClickListener(this);
        binding.passwordIcon.setOnClickListener(this);
        binding.confirmpasswordIcon.setOnClickListener(this);
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);

        } else {
            observeServiceResponse();
        }
        binding.RPButtonSendOtp.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_reset_password;
    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:

                    break;

                case SUCCESS:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    Toast.makeText(this, prefModel.getLanguageMasterDynamic().getDetails().getSet_password_sucessfully(), Toast.LENGTH_SHORT).show();
                    checkForGradeScreen();
                    break;

                case NO_INTERNET:
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    break;

                case AUTH_FAIL:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    break;

                case FAIL_400:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    break;

                default:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    break;
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_button:
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.RPAccept:
                String password = binding.etPassword.getText().toString();
                String confrimpass = binding.etconfirmPassword.getText().toString();
                Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
            if (password.isEmpty()||password.equals("")){
                Toast.makeText(this, details.getPlease_enter_password(), Toast.LENGTH_SHORT).show();
            }

               else if (confrimpass.isEmpty()||confrimpass.equals("")){
                    Toast.makeText(this, details.getEnter_get_confirm_password(), Toast.LENGTH_SHORT).show();
                }


                else if (password.equals(confrimpass) || password == confrimpass){
                    setPasswordApi();
                }
                else if (password.startsWith(" ")){
                Toast.makeText(this, details.getEnter_space_password(), Toast.LENGTH_SHORT).show();
            }

            else if (confrimpass.startsWith(" ")){
                Toast.makeText(this, details.getEnter_space_confirmpassword(), Toast.LENGTH_SHORT).show();
            }

            else{
                    Toast.makeText(this, details.getPassword_confirm_password(), Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.passwordIcon:
                handleIconClickPassword(binding.etPassword, binding.passwordIcon);
                break;

            case R.id.confirmpasswordIcon:
                handleIconClickPassword(binding.etconfirmPassword, binding.confirmpasswordIcon);
                break;

        }
    }

    void handleIconClickPassword(RPEditText editText, ImageView passwordIcon) {
        String password = editText.getText().toString();
        TransformationMethod transformationMethod = editText.getTransformationMethod();
        if (transformationMethod == null) {
            editText.setTransformationMethod(new PasswordTransformationMethod());
            passwordIcon.setColorFilter(null); // Add tint color
        } else {
            editText.setTransformationMethod(null);
            passwordIcon.setColorFilter(getResources().getColor(R.color.blue_color)); // Add tint color
        }
        if (!TextUtil.isEmpty(password)) {
            editText.setSelection(password.length());
        }
    }


    private void checkForGradeScreen() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel.getUserType() == AppConstant.UserType.TEACHER) {
            if (prefModel.isForgotPassword()) {
                openLoginActivity();
            } else {
                openTeacherHomeActivity();
            }

        } else {
            SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
            comingFrom = prefs.getString(AppConstant.COMING_FROM, "");
            if (!TextUtil.isEmpty(comingFrom) && comingFrom.equalsIgnoreCase(AppConstant.FROM_FORGOT_PASSWORD)) {
                openLoginActivity();
            } else {
                int studentClass = 0;

                if (!prefModel.getStudentData().getGrade().isEmpty()) {
                    studentClass = ConversionUtil.INSTANCE.convertStringToInteger(prefModel.getStudentData().getGrade());
                }

                AppLogger.e(TAG, "--" + prefModel.getStudentData().getStudentName());
                AppLogger.e(TAG, "--" + studentClass);
                if (prefModel.getUserType() == AppConstant.userTypeLogin.STUDENT && studentClass > 0) {
                    startDashboardActivity();
                } else if (prefModel.getUserType() == AppConstant.userTypeLogin.STUDENT && studentClass == 0) {
                    openChooseGradeActivity();
                } else {
                    startDashboardActivity();
                }
            }
        }
    }

    private void openLoginActivity() {
        Intent newIntent = new Intent(this, LoginActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
        finish();
    }

    private void openChooseGradeActivity() {
        setEmptyForCurrentFlag();
        Intent tescherIntent = new Intent(this, ChooseGradeActivity.class);
        startActivity(tescherIntent);
        finish();
    }

    private void setEmptyForCurrentFlag() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setCurrentScreenFlag("");
        AuroAppPref.INSTANCE.setPref(prefModel);
    }

    private void startDashboardActivity() {
        setEmptyForCurrentFlag();
        Intent i = new Intent(this, DashBoardMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void setPasswordApi() {
        ViewUtil.hideKeyboard(this);
        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etconfirmPassword.getText().toString();
        Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        if(binding.etPassword.getText().toString().isEmpty()||binding.etPassword.getText().toString().equals("")){
            Toast.makeText(this, details.getPlease_enter_password(), Toast.LENGTH_SHORT).show();
        }
        else if(binding.etconfirmPassword.getText().toString().isEmpty()||binding.etconfirmPassword.getText().toString().equals("")){
            Toast.makeText(this, details.getEnter_get_confirm_password(), Toast.LENGTH_SHORT).show();
        }

       else if (TextUtil.isEmpty(password)) {
            showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getEnter_the_password());
            return;
        } else if (password.length() < 6) {
            showSnackbarError( details.getPassword_should_have());
            return;
        } else if (TextUtil.isEmpty(confirmPassword)) {
            showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getEnter_get_confirm_password());
            return;
        } else if (confirmPassword.length() < 6) {
            showSnackbarError(details.getConfirm_password_should_have());
            return;
        } else if (!password.equals(confirmPassword)) {
            showSnackbarError(prefModel.getLanguageMasterDynamic().getDetails().getPassword_confirm_password());
            return;
        }


        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String forgetusermobilenumber = prefs.getString("forgetusermobilenumber", "");
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        CheckUserResModel checkUserResModel = prefModel.getCheckUserResModel();
        SetPasswordReqModel reqmodel = new SetPasswordReqModel();
        reqmodel.setMobileNo(forgetusermobilenumber);
        reqmodel.setPassword(password);

        AppLogger.e("setPasswordApi-","setPasswordApi step 1");
        if (checkUserResModel != null && !checkUserResModel.getUserDetails().isEmpty()) {
                reqmodel.setUserId(prefModel.getCheckUserResModel().getUserDetails().get(0).getUserId()); //prefModel.getParentData().getUserId()
                viewModel.checkInternet(reqmodel, Status.SET_PASSWORD);
                binding.progressbar.pgbar.setVisibility(View.VISIBLE);
        }





    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getSource()) {
            case AppConstant.PRIVACY_POLICY_TEXT:
                openWebActivty(URLConstant.PRIVACY_POLICY);

                break;

            case AppConstant.TERMS_CONDITION_TEXT:
                openWebActivty(URLConstant.TERM_CONDITION);
                break;

        }
    }

    private void openWebActivty(String link) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(AppConstant.WEB_LINK, link);
        startActivity(intent);
    }

    private void openTeacherHomeActivity() {
        setEmptyForCurrentFlag();
        getProfile();

    }

    private void getProfile()
    {
        String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String userlangid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);
        map_data.put("user_prefered_language_id",userlangid);

        RemoteApi.Companion.invoke().getTeacherData(map_data)
                .enqueue(new Callback<MyProfileResModel>()
                {
                    @Override
                    public void onResponse(Call<MyProfileResModel> call, Response<MyProfileResModel> response)
                    {
                        if (response.isSuccessful()) {
                            String teachername = response.body().getTeacherName();
                            String statename = response.body().getState_name();
                            String districtname = response.body().getDistrict_name();
                            String schoolname = response.body().getSchool_name();

                            if (teachername == null || teachername.equals("null") || teachername.equals("") || teachername.isEmpty()||
                                    statename == null || statename.equals("null") || statename.equals("") || statename.isEmpty()||
                                    districtname == null || districtname.equals("null") || districtname.equals("") || districtname.isEmpty()||
                                    schoolname == null || schoolname.equals("null") || schoolname.equals("") || schoolname.isEmpty()){
                                Intent i = new Intent(ResetPasswordActivity.this, TeacherProfileActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                            else{
                                Intent i = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }

                        }
                        else
                        {
                            Log.d(TAG, "onResponser: "+response.message().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<MyProfileResModel> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }
}