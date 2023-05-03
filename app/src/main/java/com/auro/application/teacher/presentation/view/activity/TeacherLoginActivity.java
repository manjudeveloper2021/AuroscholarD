package com.auro.application.teacher.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

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
import com.auro.application.databinding.ActivityLoginBinding;
import com.auro.application.databinding.ActivityTeacherloginBinding;
import com.auro.application.home.data.model.CheckUserApiReqModel;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.response.SendOtpResModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.data.model.signupmodel.request.LoginReqModel;
import com.auro.application.home.data.model.signupmodel.response.LoginResModel;
import com.auro.application.home.presentation.view.activity.AppLanguageActivity;
import com.auro.application.home.presentation.view.activity.ChooseGradeActivity;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.activity.EnterNumberActivity;
import com.auro.application.home.presentation.view.activity.EnterPinActivity;
import com.auro.application.home.presentation.view.activity.ForgotPinActivity;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.view.activity.OtpActivity;
import com.auro.application.home.presentation.view.activity.ResetPasswordActivity;
import com.auro.application.home.presentation.view.activity.StudentProfileActivity;
import com.auro.application.home.presentation.view.activity.ValidateStudentActivity;
import com.auro.application.home.presentation.view.activity.WebActivity;
import com.auro.application.home.presentation.view.fragment.BottomSheetUsersDialog;
import com.auro.application.home.presentation.viewmodel.LoginScreenViewModel;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.ValidatePhoneDialog;
import com.auro.application.util.alert_dialog.disclaimer.UserNotRegisterDialog;
import com.auro.application.util.firebaseAnalytics.AnalyticsRegistry;
import com.auro.application.util.strings.AppStringDynamic;

import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeacherLoginActivity extends BaseActivity implements View.OnClickListener, CommonCallBackListner {

    private static String TAG = TeacherLoginActivity.class.getSimpleName();
    @Inject
    @Named("LoginActivity")
    ViewModelFactory viewModelFactory;
    ActivityTeacherloginBinding binding;
    LoginScreenViewModel viewModel;
    CheckUserResModel checkUserResModel;
    ValidatePhoneDialog validatePhoneDialog;
    String comingFromText = "";
    Details details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setLanguageonUi(this);
        init();
    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(this, getLayout());
        ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginScreenViewModel.class);
        binding.setLifecycleOwner(this);
        binding.forgotPassword.setPaintFlags(binding.forgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.loginWithOtp.setPaintFlags(binding.forgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        setListener();
        callLoginApi();
        binding.setpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherLoginActivity.this, ResetPasswordActivity.class);
                i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
                startActivity(i);
                finish();
            }
        });

        AppUtil.loadAppLogo(binding.auroScholarLogo, this);
        ViewUtil.customTextView(binding.termsCondition, this);
        details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
       // AppStringDynamic.setLoginStings(binding);
    }


    @Override
    protected void setListener() {
        binding.backButton.setOnClickListener(this);
        binding.loginWithOtp.setOnClickListener(this);
        binding.forgotPassword.setOnClickListener(this);
        binding.passwordIcon.setOnClickListener(this);
        binding.RPAccept.setOnClickListener(this);

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
        binding.RPAccept.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_teacherlogin;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.RPButtonSendOtp:
                ViewUtil.hideKeyboard(this);
                break;

            case R.id.back_button:

                SharedPreferences preferences =getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                backButton();
                break;
            case R.id.RPAccept:
                comingFromText = "";
                ViewUtil.hideKeyboard(this);

                String mobileno = binding.etMobileNumber.getText().toString();
                SharedPreferences.Editor editor2 = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                editor2.putString("usermobilenumber", mobileno);
                editor2.apply();
                closeProgressAfterFewSeconds();
                setSrID();
                checkUserType();
                break;

            case R.id.forgotPassword:
                comingFromText = "";
                setForgotFlowStatus(true);
                try {
                    binding.passwordlayout.setVisibility(View.GONE);
                    binding.etPassword.setVisibility(View.GONE);
                    binding.etPassword.setText("");
                    String mobileno2 = binding.etMobileNumber.getText().toString();
                    sendOtpApiReqPassForForget(mobileno2);
                    SharedPreferences.Editor editor1 = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                    editor1.putString("forgetusermobilenumber", mobileno2);
                    editor1.apply();
                } catch (Exception e) {

                }
                break;

            case R.id.loginWithOtp:
                binding.passwordlayout.setVisibility(View.GONE);
                binding.etPassword.setVisibility(View.GONE);
                binding.etPassword.setText("");
                comingFromText = AppConstant.ComingFromStatus.COMING_FROM_LOGIN_WITH_OTP;
                setForgotFlowStatus(false);
                String mobileno3 = binding.etMobileNumber.getText().toString();
                sendOtpApiReqPass(mobileno3);
                break;


            case R.id.passwordIcon:
                handleClickPassword();
                break;



        }
    }

    void checkUserType() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (prefModel.getUserType() == AppConstant.UserType.TEACHER) {
            if (checkUserResModel != null && checkUserResModel.getUserDetails() != null && !checkUserResModel.getUserDetails().isEmpty() && binding.passwordlayout.getVisibility() == View.VISIBLE && checkUserResModel.getUserDetails().size() > 0) {

                callLoginApi();
            } else {
                callCheckUser();
            }
        } else {
            if (checkUserResModel != null && checkUserResModel.getUserDetails() != null && !checkUserResModel.getUserDetails().isEmpty() && binding.passwordlayout.getVisibility() == View.VISIBLE && checkUserResModel.getUserDetails().size() > 0) {
                callLoginApi();
            } else {

                callCheckUser();
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        setForgotFlowStatus(false);
    }

    private void setForgotFlowStatus(boolean status) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setForgotPassword(status);
        AuroAppPref.INSTANCE.setPref(prefModel);
    }

    private void setSrID() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String mobileNumber = binding.etMobileNumber.getText().toString();
        if (!TextUtil.isEmpty(mobileNumber) && mobileNumber.length() < 10) {
            prefModel.setSrId(mobileNumber);
        } else {
            prefModel.setSrId("");
        }
        AuroAppPref.INSTANCE.setPref(prefModel);
    }

    void handleClickPassword() {

        String password = binding.etPassword.getText().toString();
        TransformationMethod transformationMethod = binding.etPassword.getTransformationMethod();
        if (transformationMethod == null) {
            binding.etPassword.setTransformationMethod(new PasswordTransformationMethod());
            binding.passwordIcon.setColorFilter(null); // Add tint color

        } else {
            binding.etPassword.setTransformationMethod(null);
            binding.passwordIcon.setColorFilter(getResources().getColor(R.color.blue_color)); // Add tint color
        }

        if (!TextUtil.isEmpty(password)) {
            binding.etPassword.setSelection(password.length());

        }

    }


    void openSetPasswordActivity() {
        Intent i = new Intent(this, ResetPasswordActivity.class);
        i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
        startActivity(i);
    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {

        AppLogger.e(TAG, "commonEventListner step 1");
        switch (commonDataModel.getClickType()) {


            case PRIVACY_POLICY_TEXT:
                openWebActivty(URLConstant.PRIVACY_POLICY);
                AppLogger.e(TAG, "commonEventListner step 2");

                break;

            case TERMS_CONDITION_TEXT:
                openWebActivty(URLConstant.TERM_CONDITION);
                AppLogger.e(TAG, "commonEventListner step 3");

                break;

            case REGISTER_CALLBACK:
                String username = binding.etMobileNumber.getText().toString();
                if (username.length() < 10) {
                    openEnterNumberActivity();
                } else {
                    sendOtpApiReqPass(username);
                }
                break;

            case SEND_OTP:
                if (validatePhoneDialog != null) {
                    validatePhoneDialog.dismiss();
                }
                String mobileno = binding.etMobileNumber.getText().toString();
                sendOtpApiReqPass(mobileno);
                break;


            case CHANGE_NUMBER:
                if (validatePhoneDialog != null) {
                    validatePhoneDialog.dismiss();
                }
                openValidateActivty();
                break;

        }

    }


    void openEnterNumberActivity() {
        Intent intent = new Intent(this, EnterNumberActivity.class);
        startActivity(intent);
    }

    private void openWebActivty(String link) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(AppConstant.WEB_LINK, link);
        startActivity(intent);

    }

    private void sendOtpApiReqPass(String phonenumber) {
        setProgressVisibility(View.VISIBLE);

        SendOtpReqModel mreqmodel = new SendOtpReqModel();
        mreqmodel.setMobileNo(phonenumber);
        callLoginApi();

        viewModel.checkInternet(mreqmodel, Status.SEND_OTP);
    }

    private void sendOtpApiReqPassForForget(String phonenumber) {
        setProgressVisibility(View.VISIBLE);

        SendOtpReqModel mreqmodel = new SendOtpReqModel();
        mreqmodel.setMobileNo(phonenumber);
        callLoginApiForForget();
        viewModel.checkInternet(mreqmodel, Status.SEND_OTP);
    }


    private void callCheckUser() {
        int visibility = binding.passwordlayout.getVisibility();
        String username = binding.etMobileNumber.getText().toString();
        String password = binding.etPassword.getText().toString();


        if (TextUtil.isEmpty(username)) {
            showSnackbarError(details.getPlease_enter_the_mobile_number());
            return;
        }



        else if (binding.passwordlayout.getVisibility() == View.VISIBLE && TextUtil.isEmpty(password)) {
            showSnackbarError(details.getPlease_enter_password());
            return;
        }

        else if (binding.passwordlayout.getVisibility() == View.VISIBLE && binding.etPassword.getText().toString().length() < 5) {
            showSnackbarError(details.getPlease_enter_valid_password());
            return;
        }
        else if (binding.etPassword.getText().toString().length() == 5) {
            showSnackbarError(details.getPlease_enter_valid_password());
            return;
        }
        else if (binding.etPassword.getText().toString().length() > 10) {
            showSnackbarError(details.getPlease_enter_valid_password());
            return;
        }


        else {
            setProgressVisibility(View.VISIBLE);
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            CheckUserApiReqModel checkUserApiReqModel = new CheckUserApiReqModel();
            checkUserApiReqModel.setEmailId("");
            checkUserApiReqModel.setMobileNo("");
            checkUserApiReqModel.setUserType("" + prefModel.getUserType());
            checkUserApiReqModel.setUserName(binding.etMobileNumber.getText().toString());
            viewModel.checkInternet(checkUserApiReqModel, Status.CHECKVALIDUSER);
        }

    }


    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }


    private void observeServiceResponse() {
        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    setProgressVisibility(View.GONE);
                    AppLogger.e(TAG, "-- step 1");
                    if (responseApi.apiTypeStatus == Status.CHECKVALIDUSER) {
                        AppLogger.e(TAG, "-- step 2");
                        checkUserResModel = (CheckUserResModel) responseApi.data;
                        handleResponseCode(checkUserResModel);
                    } else if (responseApi.apiTypeStatus == Status.SEND_OTP) {
                        SendOtpResModel sendOtpResModel = (SendOtpResModel) responseApi.data;
                        if (!sendOtpResModel.getError()) {
                            startActivityOTPScreen(sendOtpResModel);
                        } else if (sendOtpResModel.getError()){
                            Toast.makeText(this, "Your OTP limit is exceed! Please try after 30 mins", Toast.LENGTH_SHORT).show();
                            showSnackbarError("Your OTP limit is exceed! Please try after 30 mins");

                            //showSnackbarError(sendOtpResModel.getMessage());
                        }
                    } else if (responseApi.apiTypeStatus == Status.LOGIN_API) {
                        LoginResModel loginResModel = (LoginResModel) responseApi.data;
                        if (!loginResModel.getError()) {
                            checkLoginResUsecase(loginResModel);
                            binding.passwordlayout.setVisibility(View.VISIBLE);
                            binding.RPAccept.setText("Login");
                        } else {
                            showSnackbarError(loginResModel.getMessage());
                        }
                    }
                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                default:
                    AppLogger.e(TAG, "-- step 3");
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    //  showSnackbarError((String) responseApi.data);

                    if (binding.passwordlayout.getVisibility() == View.VISIBLE && !binding.etPassword.getText().toString().isEmpty() && !binding.etPassword.getText().toString().equals("")){
                        showSnackbarError((String) details.getInvalid_login());
                    }


                    break;
            }

        });
    }


    public void startActivityOTPScreen(SendOtpResModel sendOtpResModel) {
        funnelOtpAsk();
        Intent i = new Intent(this, OtpActivity.class);
        i.putExtra(AppConstant.COMING_FROM, comingFromText);
        i.putExtra(getResources().getString(R.string.intent_phone_number), sendOtpResModel.getMobileNo());
        i.putExtra(getResources().getString(R.string.userOtp), sendOtpResModel.getOtp());

        SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
        editor.putString("loginwithotpstatus","true");
        editor.apply();
        startActivity(i);
    }

    private void funnelOtpAsk() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        int userType = prefModel.getUserType();
        if (userType == AppConstant.UserType.TEACHER) {
            AnalyticsRegistry.INSTANCE.getModelInstance().trackLoginScreen(AppConstant.UserType.TEACHER);
        } else {
            AnalyticsRegistry.INSTANCE.getModelInstance().trackLoginScreen(AppConstant.UserType.STUDENT);
        }

    }

    private void setLogValue(boolean isLog) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setLog(isLog);
        AuroAppPref.INSTANCE.setPref(prefModel);
    }

    private void setProgressVisibility(int status) {
        binding.progressbar.pgbar.setVisibility(status);
    }

    private void handleResponseCode(CheckUserResModel checkUserResModel) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setCheckUserResModel(checkUserResModel);
        AuroAppPref.INSTANCE.setPref(prefModel);

        switch (checkUserResModel.getCode()) {
            case AppConstant.UsercheckApiCode.PASS_NOT_NULL:

                break;

            case AppConstant.UsercheckApiCode.SR_ID_USER:
                AppLogger.e(TAG, "--SR_ID_USER");
                openValidatePhoneNumberDialog();
                break;

            case AppConstant.UsercheckApiCode.PASS_NULL:
                AppLogger.e(TAG, "PASS_NULL");
                sendOtpApiReqPass(checkUserResModel.getUserMobile());
                break;

            case AppConstant.UsercheckApiCode.USER_NOT_FOUND:
            case AppConstant.UsercheckApiCode.SR_ID_NOT_FOUND:
                AppLogger.e(TAG, "USER_NOT_FOUND");
                if (binding.etMobileNumber.getText().toString().length() == 10) {
                    // binding.etPassword.setVisibility(View.GONE);
                    String number = binding.etMobileNumber.getText().toString().trim();
                    String regexStr = "^[0-9]$";
                    if(binding.etMobileNumber.getText().toString().length()==10 && android.util.Patterns.PHONE.matcher(number).matches()){
                        userNotRegisterDialog();
                    }
                    else{
                        showSnackbarError(details.getPlease_enter_valid_phone_number());

                    }

                } else {
                    showSnackbarError(checkUserResModel.getMessage());
                }

                break;

            case AppConstant.UsercheckApiCode.USER_CHECK_CODE:
                callMethodForSrIdUser();
                break;

            default:
                if (AuroAppPref.INSTANCE.getModelInstance().getUserType() == AppConstant.UserType.TEACHER) {
                    checkUserForOldTeacherUser();
                } else {
                    checkUserForOldStudentUser();
                }
                break;
        }



    }


    // ADD PARENT LOGIN ALSO
    void checkUserForOldStudentUser() {
        if (checkUserResModel != null && !checkUserResModel.getUserDetails().isEmpty() ) { //&& checkUserResModel.getUserDetails().size() == 1
            UserDetailResModel resModel = checkUserResModel.getUserDetails().get(0);
            if (resModel.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_STUDENT)) {
                setDatainPref(resModel);
                if (!resModel.getSetPin()) {
                    comingFromText = AppConstant.ComingFromStatus.COMING_FROM_PASSWORD_NOT_SET;
                    openSetPinActivity(resModel);

                } else {
                    openEnterPinActivity(resModel);
                }

            }
            else {
                setVisibilityofUi(0);

            }
        }
        else if (checkUserResModel != null && !checkUserResModel.getUserDetails().isEmpty() ) { //&& checkUserResModel.getUserDetails().size() == 2

            for (UserDetailResModel resModel : checkUserResModel.getUserDetails()) {
                if (resModel.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_PARENT)) {
                    if (!resModel.getSetUserPass()) {
                        comingFromText = AppConstant.ComingFromStatus.COMING_FROM_PASSWORD_NOT_SET;
                        sendOtpApiReqPass(resModel.getUserMobile());
                    } else {

                    }
                }
                setDatainPref(resModel);
            }

        }
        else {
            for (UserDetailResModel resModel : checkUserResModel.getUserDetails()) {
                if (resModel.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_PARENT)) {
                    setDatainPref(resModel);
                }
            }
            if (binding.etPassword.getText().toString().isEmpty()||binding.etPassword.getText().toString().equals("")){

            }
            else{
                openBottomSheetDialog();
            }
        }
    }

    void checkUserForOldTeacherUser() {
        if (checkUserResModel != null && !checkUserResModel.getUserDetails().isEmpty() && checkUserResModel.getUserDetails().size() == 1) {
            UserDetailResModel resModel = checkUserResModel.getUserDetails().get(0);
            if (resModel.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_STUDENT)) {
                setDatainPref(resModel);
                if (!resModel.getSetUserPass()) {
                    comingFromText = AppConstant.ComingFromStatus.COMING_FROM_PASSWORD_NOT_SET;
                    String mobileno = binding.etMobileNumber.getText().toString();
                    sendOtpApiReqPass(mobileno);
                } else {
                    setVisibilityofUi(0);
                }

            } else {
                setVisibilityofUi(0);
            }
        }

    }

    private void openEnterPinActivity(UserDetailResModel resModel) {

        Intent intent = new Intent(this, EnterPinActivity.class);
        intent.putExtra(AppConstant.USER_PROFILE_DATA_MODEL, resModel);
        startActivity(intent);

    }


    private void openSetPinActivity(UserDetailResModel resModel) {
        Intent intent = new Intent(this, ForgotPinActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(AppConstant.USER_PROFILE_DATA_MODEL, resModel);
        startActivity(intent);

    }

    private void setDatainPref(UserDetailResModel resModel) {
        if (resModel.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_STUDENT)) {
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setStudentData(resModel);
            prefModel.setStudentClasses(checkUserResModel.getClasses());
            AuroAppPref.INSTANCE.setPref(prefModel);
        } else {
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setParentData(resModel);
            prefModel.setStudentClasses(checkUserResModel.getClasses());
            AuroAppPref.INSTANCE.setPref(prefModel);
        }
    }

    void callMethodForSrIdUser() {
        /*Means its a SRId User*/
        AppLogger.e("callMethodForSrIdUser","step 1");
        PrefModel prefModel1 = AuroAppPref.INSTANCE.getModelInstance();
        prefModel1.setStudentData(checkUserResModel.getUserDetails().get(0));
        prefModel1.setCheckUserResModel(checkUserResModel);
        prefModel1.setUserMobile(checkUserResModel.getUserDetails().get(0).getUserMobile());
        AuroAppPref.INSTANCE.setPref(prefModel1);
        openValidatePhoneNumberDialog();
    }

    private void closeProgressAfterFewSeconds() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                setProgressVisibility(View.GONE);
            }
        }, 10000);

    }

    private void callLoginApi() {

        String username = binding.etMobileNumber.getText().toString();
        String password = binding.etPassword.getText().toString();

        if (binding.passwordlayout.getVisibility() == View.VISIBLE && TextUtil.isEmpty(password)) {
            showSnackbarError(details.getPlease_enter_password());
            return;
        }
        else if (binding.passwordlayout.getVisibility() == View.VISIBLE && password.length() < 5 && !TextUtil.isEmpty(password)) {
            showSnackbarError(details.getPlease_enter_valid_password());
            return;
        }
        else if (binding.passwordlayout.getVisibility() == View.VISIBLE && password.length() == 5 && !TextUtil.isEmpty(password)) {
            showSnackbarError(details.getPlease_enter_valid_password());
            return;
        }
        else if (binding.passwordlayout.getVisibility() == View.VISIBLE && password.length() > 10 && !TextUtil.isEmpty(password)) {
            showSnackbarError(details.getPlease_enter_valid_password());
            return;
        }
        else{
            setProgressVisibility(View.VISIBLE);
            LoginReqModel loginReqModel = new LoginReqModel();
            loginReqModel.setUserName(binding.etMobileNumber.getText().toString());
            loginReqModel.setPassword(password);
            loginReqModel.setUserType("" + AuroAppPref.INSTANCE.getModelInstance().getUserType());
            viewModel.checkInternet(loginReqModel, Status.LOGIN_API);
        }


    }

    private void callLoginApiForForget() {
        String username = binding.etMobileNumber.getText().toString();
        String password = binding.etPassword.getText().toString();


        setProgressVisibility(View.VISIBLE);
        LoginReqModel loginReqModel = new LoginReqModel();
        loginReqModel.setUserName(binding.etMobileNumber.getText().toString());
        loginReqModel.setPassword(password);
        loginReqModel.setUserType("" + AuroAppPref.INSTANCE.getModelInstance().getUserType());
        viewModel.checkInternet(loginReqModel, Status.LOGIN_API);
    }

    private void userNotRegisterDialog() {
        UserNotRegisterDialog userNotRegisterDialog = new UserNotRegisterDialog(this, this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(userNotRegisterDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        userNotRegisterDialog.getWindow().setAttributes(lp);
        Objects.requireNonNull(userNotRegisterDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        userNotRegisterDialog.setCancelable(false);
        userNotRegisterDialog.show();
    }


    private void setVisibilityofUi(int status) {
        switch (status) {
            case 0:
                binding.passwordlayout.setVisibility(View.VISIBLE);
                binding.etPassword.setVisibility(View.VISIBLE);
                binding.setpassword.setVisibility(View.GONE);
                binding.etPassword.setEnabled(true);
                binding.headingTwo.setVisibility(View.VISIBLE);
                binding.layoutHeadingOne.setVisibility(View.GONE);
                binding.forgotPassword.setVisibility(View.VISIBLE);
                binding.etMobileNumber.setEnabled(false);
                binding.txtinputMobileNumber.setEnabled(false);
                String mobileNumber = binding.etMobileNumber.getText().toString();
                if (!TextUtil.isEmpty(mobileNumber) && mobileNumber.length() == 10) {
                    if (checkUserResModel != null && !checkUserResModel.getUserDetails().isEmpty()){
                        if (checkUserResModel.getUserDetails().size()<=2){
                            for (UserDetailResModel resModel : checkUserResModel.getUserDetails()) {
                                if (resModel.getIsMaster().equalsIgnoreCase(AppConstant.UserType.USER_TYPE_PARENT)) {
                                    setDatainPref(resModel);
                                }
                                setDatainPref(resModel);
                            }
                            binding.loginWithOtp.setVisibility(View.VISIBLE);

                        }
                        else{
                            binding.loginWithOtp.setVisibility(View.GONE);
                        }
                    }

                }
                break;

            case 1:
                binding.passwordlayout.setVisibility(View.GONE);
                binding.headingTwo.setVisibility(View.GONE);
                binding.layoutHeadingOne.setVisibility(View.VISIBLE);
                binding.forgotPassword.setVisibility(View.GONE);
                binding.loginWithOtp.setVisibility(View.GONE);
                break;
        }
    }

    private void backButton() {
        binding.etPassword.setText("");
        checkUserResModel = new CheckUserResModel();
        int visibility = binding.passwordlayout.getVisibility();
        if (visibility == View.VISIBLE) {
            binding.passwordlayout.setVisibility(View.GONE);
            binding.forgotPassword.setVisibility(View.GONE);
            binding.loginWithOtp.setVisibility(View.GONE);
            binding.etMobileNumber.setEnabled(true);
            binding.txtinputMobileNumber.setEnabled(true);
        } else {
            finish();
            Intent intent = new Intent(TeacherLoginActivity.this, AppLanguageActivity.class);
            startActivity(intent);
        }
    }


    private void checkLoginResUsecase(LoginResModel loginResModel) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setLogin(true);
        prefModel.setUserMobile(loginResModel.getUserMobile());
        SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();

        editor.putString("statuslogin", "true");
        editor.apply();
        AuroAppPref.INSTANCE.setPref(prefModel);
        if (prefModel.getUserType() == AppConstant.UserType.TEACHER) {
            UserDetailResModel resModel = prefModel.getStudentData();
            if (resModel != null && !resModel.getSetUserPass()) {
                openSetPasswordActivity();
            } else {
                getProfile();

            }
        }

        else{

            if (checkUserResModel.getUserDetails().size() > 1){
                Log.d("listsize", String.valueOf(checkUserResModel.getUserDetails().size()));
                openBottomSheetDialog();
            }

            else if (checkUserResModel.getUserDetails().size() == 2 && Integer.parseInt(checkUserResModel.getUserDetails().get(1).getGrade()) == 0){
                Log.d("listsize", String.valueOf(checkUserResModel.getUserDetails().size()));
                openChooseGradeActivity();
            }
            else{
                AppLogger.e("whichScreenOpen step 1-", "" + prefModel.isLogin());
                int studentClass = prefModel.getStudentClass();
                AppLogger.e("whichScreenOpen step 2-", "" + studentClass);
                if (studentClass > 0) {
                    startDashboardActivity(loginResModel.getUserMobile());
                } else {
                    openChooseGradeActivity();
                }
            }

        }

    }

    private void startDashboardActivity(String phoneNumber) {
        Intent i = new Intent(this, DashBoardMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
    private void openuserprofile() {
        Intent i = new Intent(this, StudentProfileActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }


    private void openTeacherHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
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
                                Intent i = new Intent(TeacherLoginActivity.this, TeacherProfileActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                            else{
                                openTeacherHomeActivity();
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

    private void openChooseGradeActivity() {
        Intent tescherIntent = new Intent(this, ChooseGradeActivity.class);
        startActivity(tescherIntent);
        finish();
    }

    private void openValidatePhoneNumberDialog() {
        AppLogger.e(TAG, "--openValidatePhoneNumberDialog step 1");
        try {
            validatePhoneDialog = new ValidatePhoneDialog(this, this);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(validatePhoneDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            validatePhoneDialog.getWindow().setAttributes(lp);
            Objects.requireNonNull(validatePhoneDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            validatePhoneDialog.setCancelable(false);
            validatePhoneDialog.show();
        } catch (Exception e) {
            AppLogger.e(TAG, "-- step 2" + e.getMessage());
        }
        AppLogger.e(TAG, "--openValidatePhoneNumberDialog step 3");
    }

    private void openValidateActivty() {
        Intent intent = new Intent(this, ValidateStudentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        backButton();
    }

    void openBottomSheetDialog() {
        BottomSheetUsersDialog bottomSheet = new BottomSheetUsersDialog();
        bottomSheet.show(getSupportFragmentManager(),
                "ModalBottomSheet");
    }

    private void getPassword()
    {

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String user_name = binding.etMobileNumber.getText().toString();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_name",user_name);
        map_data.put("user_type", String.valueOf(0));
        map_data.put("user_prefered_language_id",languageid);
        RemoteApi.Companion.invoke().getUserCheck(map_data)
                .enqueue(new Callback<CheckUserResModel>()
                {
                    @Override
                    public void onResponse(Call<CheckUserResModel> call, Response<CheckUserResModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {
                                if (response.body().getUserDetails().get(0).getPassword()==null||response.body().getUserDetails().get(0).getPassword().equals("")||response.body().getUserDetails().get(0).getPassword().isEmpty()||response.body().getUserDetails().get(0).getPassword().equals("null")||response.body().getUserDetails().get(0).getPassword().equals(null)){
                                    binding.loginWithOtp.setVisibility(View.GONE);
                                    binding.setpassword.setText(details.getSetPassword());
                                    binding.setpassword.setVisibility(View.GONE);
                                }
                                else{
                                    binding.loginWithOtp.setVisibility(View.VISIBLE);
                                    //   binding.setpassword.setText(details.getSetPassword());
                                    binding.setpassword.setVisibility(View.GONE);
                                    callLoginApi();

                                }

                            }
                        } catch (Exception e) {


                            Toast.makeText(TeacherLoginActivity.this, details.getInternetCheck(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckUserResModel> call, Throwable t)
                    {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        IntegrationValidator.validate(LoginActivity.this);
//        Branch.sessionBuilder(this).withCallback(new Branch.BranchUniversalReferralInitListener() {
//            @Override
//            public void onInitFinished(@Nullable BranchUniversalObject branchUniversalObject, @Nullable LinkProperties linkProperties, @Nullable BranchError error) {
//                if (error != null) {
//                }
//                else {
//                    Log.e("BranchSDK_Tester", "branch init complete!");
//                    if (branchUniversalObject != null) {
//                        Log.e("BranchSDK_Tester", "title " + branchUniversalObject.getTitle());
//                        Log.e("BranchSDK_Tester", "CanonicalIdentifier " + branchUniversalObject.getCanonicalIdentifier());
//                        Log.e("BranchSDK_Tester", "metadata " + branchUniversalObject.getContentMetadata().convertToJson());
//                    }
//
//                    if (linkProperties != null) {
//                        Log.e("BranchSDK_Tester", "Channel " + linkProperties.getChannel());
//                        Log.e("BranchSDK_Tester", "control params " + linkProperties.getControlParams());
//                    }
//                }
//            }
//
//
//        }).withData(this.getIntent().getData()).init();

    }
//    @Override
//    public void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        this.setIntent(intent);
//        Branch.sessionBuilder(this).withCallback(new Branch.BranchReferralInitListener() {
//            @Override
//            public void onInitFinished(JSONObject referringParams, BranchError error) {
//                if (error != null) {
//                    Log.e("BranchSDK_Tester", error.getMessage());
//                } else if (referringParams != null) {
//                    Log.e("BranchSDK_Tester", referringParams.toString());
//                }
//            }
//        }).reInit();
//    }
}