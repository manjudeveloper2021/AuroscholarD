package com.auro.application.home.presentation.view.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.impl.utils.futures.FutureCallback;
import androidx.camera.core.impl.utils.futures.Futures;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.installreferrer.api.InstallReferrerClient;
import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.common.NotificationDataModel;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.SdkCallBack;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.core.network.ErrorResponseModel;
import com.auro.application.core.util.AuroScholar;
import com.auro.application.databinding.ActivityDashBoardMainBinding;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.data.model.DashboardResponselDataModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.FbGoogleUserModel;
import com.auro.application.home.data.model.FetchStudentPrefReqModel;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.GetAllChildModel;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.OtpOverCallReqModel;
import com.auro.application.home.data.model.ParentProfileDataModel;
import com.auro.application.home.data.model.PendingKycDocsModel;
import com.auro.application.home.data.model.ReferralPopUpDataModel;
import com.auro.application.home.data.model.RefferalReqModel;
import com.auro.application.home.data.model.SendOtpReqModel;
import com.auro.application.home.data.model.StudentResponselDataModel;
import com.auro.application.home.data.model.VerifyOtpReqModel;
import com.auro.application.home.data.model.response.CheckVerResModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.home.data.model.response.FetchStudentPrefResModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.InstructionsResModel;
import com.auro.application.home.data.model.response.NoticeInstruction;
import com.auro.application.home.data.model.response.OtpOverCallResModel;
import com.auro.application.home.data.model.response.SendOtpResModel;
import com.auro.application.home.data.model.response.ShowDialogModel;
import com.auro.application.home.data.model.response.SlabsResModel;
import com.auro.application.home.data.model.response.VerifyOtpResModel;
import com.auro.application.home.data.model.signupmodel.InstructionModel;
import com.auro.application.home.data.model.signupmodel.UserSlabsRequest;
import com.auro.application.home.presentation.view.adapter.SelectYourParentChildAdapter;
import com.auro.application.home.presentation.view.fragment.CertificateFragment;
import com.auro.application.home.presentation.view.fragment.DemographicFragment;
import com.auro.application.home.presentation.view.fragment.FAQFragment;
import com.auro.application.home.presentation.view.fragment.FriendsLeaderBoardListFragment;
import com.auro.application.home.presentation.view.fragment.GradeChangeFragment;
import com.auro.application.home.presentation.view.fragment.KYCFragment;
import com.auro.application.home.presentation.view.fragment.KYCViewFragment;
import com.auro.application.home.presentation.view.fragment.ParentProfileFragment;
import com.auro.application.home.presentation.view.fragment.PrivacyPolicyFragment;
import com.auro.application.home.presentation.view.fragment.QuizTestFragment;
import com.auro.application.home.presentation.view.fragment.StudentKycInfoFragment;
import com.auro.application.home.presentation.view.fragment.StudentProfileFragment;
import com.auro.application.home.presentation.view.fragment.TransactionsFragment;
import com.auro.application.home.presentation.view.fragment.PartnersFragment;
import com.auro.application.home.presentation.view.fragment.SubjectPreferencesActivity;
import com.auro.application.home.presentation.viewmodel.AuroScholarDashBoardViewModel;
import com.auro.application.payment.presentation.view.fragment.SendMoneyFragment;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ConversionUtil;
import com.auro.application.util.DateUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.CustomDialogModel;
import com.auro.application.util.alert_dialog.LanguageChangeDialog;
import com.auro.application.util.alert_dialog.UpdateCustomDialog;
import com.auro.application.util.alert_dialog.disclaimer.CustomOtpDialog;
import com.auro.application.util.alert_dialog.disclaimer.GradeChnageCongDialogBox;
import com.auro.application.util.alert_dialog.disclaimer.LoginDisclaimerDialog;
import com.auro.application.util.alert_dialog.disclaimer.NoticeDialogBox;
import com.auro.application.util.authenticate.GoogleSignInHelper;
import com.auro.application.util.firebaseAnalytics.AnalyticsRegistry;
import com.auro.application.util.strings.AppStringDynamic;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.googlejavaformat.Indent;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.instabug.apm.APM;
import com.instabug.bug.BugReporting;
import com.instabug.crash.CrashReporting;
import com.instabug.library.Feature;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.instabug.library.ui.onboarding.WelcomeMessage;
import com.instabug.library.visualusersteps.State;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import static com.auro.application.core.application.AuroApp.context;
import static com.auro.application.core.common.Status.DASHBOARD_API;
import static com.auro.application.core.common.Status.GET_SLABS_API;
import static com.auro.application.core.common.Status.LISTNER_FAIL;
import static com.auro.application.core.common.Status.LISTNER_SUCCESS;
import static com.auro.application.core.common.Status.SEND_OTP;
import static com.auro.application.core.common.Status.SEND_REFERRAL_API;

import static org.openjdk.tools.javac.jvm.ByteCodes.error;

import org.json.JSONException;
import org.json.JSONObject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.validators.IntegrationValidator;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardMainActivity extends BaseActivity implements GradeChangeFragment.OnClickButton, BottomNavigationView.OnNavigationItemSelectedListener,CommonCallBackListner {
    @Inject
    @Named("DashBoardMainActivity")
    ViewModelFactory viewModelFactory;
    public ActivityDashBoardMainBinding binding;
    private AuroScholarDashBoardViewModel viewModel;
    private Context mContext;
    PrefModel prefModel;
    String reffer_user_id, refferal_type;
    int backPress = 0;
    CustomOtpDialog customOtpDialog;
    LoginDisclaimerDialog loginDisclaimerDialog;
    NoticeDialogBox noticeDialogBox;
    GradeChnageCongDialogBox gradeChnageCongDialogBox;
    AlertDialog alertDialog;

    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    String TAG = "AppUpdate";
    CheckVerResModel checkVerResModel;
    UpdateCustomDialog updateCustomDialog;
    public AlertDialog dialogQuit;
    String typeGradeChange;
    InstallReferrerClient referrerClient;

    private static int LISTING_ACTIVE_FRAGMENT = 0;
    public static final int QUIZ_DASHBOARD_FRAGMENT = 1;
    public static final int QUIZ_KYC_FRAGMENT = 2;
    public static final int QUIZ_KYC_VIEW_FRAGMENT = 3;
    public static final int WALLET_INFO_FRAGMENT = 4;
    public static final int QUIZ_TEST_FRAGMENT = 5;
    public static final int PRIVACY_POLICY_FRAGMENT = 6;
    public static final int CERTIFICATE_FRAGMENT = 7;
    public static final int KYC_DIRECT_FRAGMENT = 8;
    public static final int CERTIFICATE_DIRECT_FRAGMENT = 9;
    public static final int PAYMENT_DIRECT_FRAGMENT = 10;
    public static final int TRANSACTION_FRAGMENT = 11;
    public static final int LEADERBOARD_FRAGMENT = 12;
    public static final int PAYMENT_FRAGMENT = 13;
    public static final int STUDENT_PROFILE_FRAGMENT = 14;
    public static final int FAQ_FRAGMENT = 22;
    public static final int PARTNERS_FRAGMENT = 15;
    public static final int GRADE_CHANGE_FRAGMENT = 16;
    public static final int SEND_MONEY_FRAGMENT = 17;
    public static final int PARTNERS_FRAGMENT_FRAGMENT = 18;
    public static final int NATIVE_QUIZ_FRAGMENT = 19;
    public static final int DEMOGRAPHIC_FRAGMENT = 20;
    public static final int STUDENT_UPLOAD_DOCUMENT_FRAGMENT = 21;

    DashboardResModel dashboardResModel;
    AuroScholarInputModel inputModel;
    CommonCallBackListner commonCallBackListner;
    CommonCallBackListner clickLisner;
    String deviceToken = "";
    public boolean isBackNormal = true;
    List<GenderData> genderList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_auro_scholar_dash_board);
        ViewUtil.setLanguageonUi(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            deviceToken = instanceIdResult.getToken();
            Log.e("newToken", deviceToken);
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            prefModel.setDeviceToken(deviceToken);
            getPreferences(Context.MODE_PRIVATE).edit().putString("fb_device_token", deviceToken).apply();
        });
        try{
            if (AuroAppPref.INSTANCE.getModelInstance()==null){
                alertDialogForQuit();
            }
            else {
                askPermission();
                FirebaseDynamicLinks.getInstance()
                        .getDynamicLink(getIntent())
                        .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                            @Override
                            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                Uri deepLink = null;
                                if (pendingDynamicLinkData != null) {
                                    deepLink = Uri.parse("https://auroscholar.page.link/1LT23H7pzNvQe8CP6"); //pendingDynamicLinkData.getLink();
                                    Toast.makeText(DashBoardMainActivity.this, deepLink.toString(), Toast.LENGTH_SHORT).show();
                                }
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user == null
                                        && deepLink != null
                                        && deepLink.getBooleanQueryParameter("invitedby", false)) {
                                    String referrerUid = deepLink.getQueryParameter("invitedby");


                                }
                            }
                        });

                getInstabug();
                String mobilenumber = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserMobile();
                SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                editor.putString("statusparentprofile", "false");
                editor.putString("isLogin","true");
                editor.putString("statusfillstudentprofile", "false");
                editor.putString("statussetpasswordscreen", "false");
                editor.putString("statusopenprofileteacher", "false");
                editor.putString("statusopendashboardteacher", "false");
                editor.putString("statuschoosegradescreen", "false");
                editor.putString("statuschoosedashboardscreen", "true");
                editor.putString("statussubjectpref","false");
                editor.putString("statusentermobilenumber",mobilenumber);
                editor.putString("statusopenprofilewithoutpin", "false");
                editor.apply();


                init();
                checkRefferedData();
                getRefferalPopUp();
                setListener();
                AppLogger.e("DashbaordMain", "oncreate step 1");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            alertDialogForQuit();
        }

    }


    public void callFetchUserPreference() {
        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String gradeforsubjectpreference = prefs.getString("gradeforsubjectpreference", "");
        AppLogger.e("DashbaordMain", "oncreate step 2");
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.e("DashbaordMain", "" + prefModel.getStudentClass());
        if (prefModel.getStudentClass() > 10 || gradeforsubjectpreference.equals("11")||gradeforsubjectpreference.equals("12")||gradeforsubjectpreference.equals(11)||gradeforsubjectpreference.equals(12)) {
            FetchStudentPrefReqModel fetchStudentPrefReqModel = new FetchStudentPrefReqModel();

            fetchStudentPrefReqModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
            String childid =  AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();



            viewModel.checkInternet(Status.FETCH_STUDENT_PREFERENCES_API, fetchStudentPrefReqModel);



        }
    }

    @Override
    protected void init() {


        binding = DataBindingUtil.setContentView(this, getLayout());
        ((AuroApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AuroScholarDashBoardViewModel.class);
        binding.setLifecycleOwner(this);
        mContext = DashBoardMainActivity.this;

        setProgressVal();
        if (getIntent().hasExtra(FbGoogleUserModel.class.getSimpleName())) {

        }
        funnelStudentDashBoard();
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        setListener();



        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        }
        else {
            SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
            String gradeforsubjectpreference = prefs.getString("gradeforsubjectpreference", "");

            if (gradeforsubjectpreference.equals("11")||gradeforsubjectpreference.equals("12")||gradeforsubjectpreference.equals(11)||gradeforsubjectpreference.equals(12)){
                SharedPreferences.Editor editor1 = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                editor1.putString("gradeforsubjectpreferencewithoutpin", "false");
                editor1.apply();
                openSubjectPreferenceScreen();


            }
            else{
                observeServiceResponse();
            }


        }
        binding.naviagtionContent.bottomNavigation.setVisibility(View.GONE);

        getDashboardMenu();

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setLogin(true);
        SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
        editor.putString("statuslogin", "true");
        editor.apply();
        AuroAppPref.INSTANCE.setPref(prefModel);
        callGetInstructionsApi(AppConstant.InstructionsType.AFTER_LOGIN);


        checkForGradeScreen();

    }

    private void checkForGradeScreen() {


        setHomeFragmentTab();



    }

    public void openGradeChangeFragment(String source) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.COMING_FROM, source);
        GradeChangeFragment gradeChangeFragment = new GradeChangeFragment(this);
        gradeChangeFragment.setArguments(bundle);
        openFragment(gradeChangeFragment);
    }

    @Override
    protected void setListener() {
        clickLisner = this;
        binding.naviagtionContent.bottomNavigation.setOnNavigationItemSelectedListener(this);
        binding.naviagtionContent.bottomSecondnavigation.setOnNavigationItemSelectedListener(this);
        selectMoreNavigationMenu(4);


    }
    @Override
    public void onStart() {
        super.onStart();

//        IntegrationValidator.validate(this);
//        Branch.sessionBuilder(this).withCallback(new Branch.BranchUniversalReferralInitListener() {
//            @Override
//            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError error) {
//                if (error != null) {
//                    Log.e("BranchSDK_Tester", "branch init failed. Caused by -" + error.getMessage());
//                } else {
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
//        }).withData(this.getIntent().getData()).init();


        // Branch init
//            Branch.getInstance().initSession(new Branch.BranchUniversalReferralInitListener() {
//                @Override
//                public void onInitFinished(@Nullable BranchUniversalObject branchUniversalObject, @Nullable LinkProperties linkProperties, @Nullable BranchError error) {
//                    if (branchUniversalObject != null) {
//                        // Retrieve deeplink keys from 'referringParams' and evaluate the values to determine where to route the user
//                        // Check '+clicked_branch_link' before deciding whether to use your Branch routing logic
//                    }
//                    else if (error != null) {
//                        // Retrieve deeplink keys from 'referringParams' and evaluate the values to determine where to route the user
//                        // Check '+clicked_branch_link' before deciding whether to use your Branch routing logic
//                    }
//                    else if (linkProperties != null) {
//                        // Retrieve deeplink keys from 'referringParams' and evaluate the values to determine where to route the user
//                        // Check '+clicked_branch_link' before deciding whether to use your Branch routing logic
//                    }
//                    else {
//                        Log.i("BRANCH SDK", error.getMessage());
//                    }
//                }
//
//            }, this.getIntent().getData(), this);
//            }
//
//        @Override
//        public void onNewIntent(Intent intent) {
//            super.onNewIntent(intent);
//            this.setIntent(intent);
//        }

    }
//    private Branch.BranchReferralInitListener branchReferralInitListener = new Branch.BranchReferralInitListener() {
//        @Override
//        public void onInitFinished(JSONObject linkProperties, BranchError error) {
//            if (error == null) {
//                Log.i("BranchDeepLink", "" + linkProperties.toString());
//                try {
//                    Gson gson = new Gson();
//                    JsonParser parser = new JsonParser();
//                    JsonElement json = parser.parse(linkProperties.toString());
//                } catch (JsonSyntaxException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };

    @Override
    protected int getLayout() {
        return R.layout.activity_dash_board_main;
    }

    public void auroStudentscholarSdk(int status) {
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        inputModel = new AuroScholarInputModel();

        if (!prefModel.getCheckUserResModel().getUserDetails().get(0).getUserMobile().isEmpty()||!prefModel.getCheckUserResModel().getUserDetails().get(0).getUserMobile().equals("")){
            inputModel.setMobileNumber(prefModel.getCheckUserResModel().getUserDetails().get(0).getUserMobile());
        }
        else{

            inputModel.setMobileNumber(prefModel.getStudentData().getUserMobile());
        }

        String newdeviceToken = deviceToken;
        if (!TextUtil.isEmpty(newdeviceToken)) {
            inputModel.setDeviceToken(newdeviceToken);
            AppLogger.v("sdkDeviceToken", newdeviceToken);
        }

        //Mandatory
        inputModel.setStudentClass(prefModel.getStudentData().getGrade());

        if (prefModel.getDynamiclinkResModel() != null && !TextUtil.isEmpty(prefModel.getDynamiclinkResModel().getSource())) {
            inputModel.setRegitrationSource(prefModel.getDynamiclinkResModel().getSource());
        } else {
            inputModel.setRegitrationSource("AuroScholr");
        }

        inputModel.setActivity(this); //Mandatory
        inputModel.setFragmentContainerUiId(R.id.home_container);
        //Mandatory
        inputModel.setReferralLink("");
        inputModel.setPartnerSource(AppConstant.AURO_ID); //this id is provided by auroscholar for valid partner
        //  inputModel.setPartnerSource("IDREMDvF4g");  //Demo
        inputModel.setSdkcallback(new SdkCallBack() {
            @Override
            public void callBack(String message) {

            }

            @Override
            public void logOut() {
                AppLogger.e("Chhonker", "Logout");
                int userType = prefModel.getUserType();
                //   mgoogleSignInHelper.signOut();

                SharedPreferences preferences =getSharedPreferences("My_Pref",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                prefModel.setLogin(false);

                AuroAppPref.INSTANCE.clearPref();

                funnelStudentLogOut();

                finishAffinity();
            }

            @Override
            public void commonCallback(Status status, Object o) {
                switch (status) {
                    case NAV_CHANGE_GRADE_CLICK:

                        break;
                }
            }
        });
        setRequiredData();
        /*Update Dynamic  to empty*/
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        if (status == 0) {
            AppLogger.e("notification ", "step 9 ");

            DynamiclinkResModel model = prefModel.getDynamiclinkResModel();
            dashboardResModel = prefModel.getDashboardResModel();
            if (model != null && !TextUtil.isEmpty(model.getNavigationTo()) && model.getNavigationTo().equalsIgnoreCase(AppConstant.NavigateToScreen.STUDENT_KYC)) {

                if (prefModel.getDashboardResModel() != null && viewModel.homeUseCase.checkKycStatus(dashboardResModel)) {
                    openKYCViewFragment(dashboardResModel, 1);
                } else {
                    openKYCFragment(dashboardResModel, 1);
                }

            } else if (model != null && !TextUtil.isEmpty(model.getNavigationTo()) && model.getNavigationTo().equalsIgnoreCase(AppConstant.NavigateToScreen.STUDENT_CERTIFICATE)) {

                openCertificate();

            } else if (model != null && !TextUtil.isEmpty(model.getNavigationTo()) && model.getNavigationTo().equalsIgnoreCase(AppConstant.NavigateToScreen.PAYMENT_TRANSFER)) {

                if (prefModel.getDashboardResModel() != null) {
                    int approvedMoney = ConversionUtil.INSTANCE.convertStringToInteger(dashboardResModel.getApproved_scholarship_money());
                    if (approvedMoney > 0) {
                        openSendMoneyFragment();
                    } else {
                        openFragment(AuroScholar.startAuroSDK(inputModel));
                    }
                } else {
                    openFragment(AuroScholar.startAuroSDK(inputModel));
                }


            } else {
                openFragment(AuroScholar.startAuroSDK(inputModel));
            }
        } else {
            AppLogger.e("notification ", "step 10 ");

            NotificationDataModel notificationDataModel = prefModel.getNotificationDataModel();
            if (notificationDataModel.getNavigateto().equalsIgnoreCase(AppConstant.NavigateToScreen.STUDENT_KYC)) {
                AppLogger.e("notification ", "step 11 ");
                if (prefModel.getDashboardResModel() != null && viewModel.homeUseCase.checkKycStatus(dashboardResModel)) {
                    openKYCViewFragment(dashboardResModel, 1);
                    AppLogger.e("notification ", "step 12 ");
                } else {
                    openKYCFragment(dashboardResModel, 1);
                    AppLogger.e("notification ", "step 13 ");

                }

            } else if (notificationDataModel.getNavigateto().equalsIgnoreCase(AppConstant.NavigateToScreen.STUDENT_CERTIFICATE)) {
                openCertificate();
            } else if (notificationDataModel.getNavigateto().equalsIgnoreCase(AppConstant.NavigateToScreen.PAYMENT_TRANSFER)) {
                if (prefModel.getDashboardResModel() != null) {

                    int approvedMoney = ConversionUtil.INSTANCE.convertStringToInteger(dashboardResModel.getApproved_scholarship_money());
                    if (approvedMoney > 0) {
                        openSendMoneyFragment();
                    } else {
                        openFragment(AuroScholar.startAuroSDK(inputModel));
                    }
                } else {
                    openFragment(AuroScholar.startAuroSDK(inputModel));
                }


            } else if (notificationDataModel.getNavigateto().equalsIgnoreCase(AppConstant.NavigateToScreen.FRIENDS_LEADERBOARD)) {
                openLeaderBoardFragment(new FriendsLeaderBoardListFragment());

            } else {
                openFragment(AuroScholar.startAuroSDK(inputModel));
            }
            prefModel.setNotificationDataModel(null);

            AuroAppPref.INSTANCE.setPref(prefModel);
        }

    }

    public void calllogout(){
        SharedPreferences preferences =getSharedPreferences("My_Pref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setLogin(false);
        AuroAppPref.INSTANCE.clearPref();
    }

    public void callRefferApi() {
        DynamiclinkResModel dynamiclinkResModel = new DynamiclinkResModel();
        dynamiclinkResModel.setReffeUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        dynamiclinkResModel.setSource(AppConstant.AURO_ID);
        dynamiclinkResModel.setNavigationTo(AppConstant.NavigateToScreen.STUDENT_DASHBOARD);
        dynamiclinkResModel.setReffer_type("" + AppConstant.UserType.STUDENT);
        viewModel.checkInternet(Status.DYNAMIC_LINK_API, dynamiclinkResModel);
    }


    public void callDashboardApi() {
        viewModel.checkInternet(DASHBOARD_API, inputModel);
        AppLogger.v("QuizNew", "Dashboard step 2");
    }

    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(mContext, fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }

    public void openLeaderBoardFragment(Fragment fragment) {
        ((AppCompatActivity) (this)).getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.home_container, fragment, DashBoardMainActivity.class
                        .getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_VERSION_UPDATE:
                if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                    AppLogger.e(TAG, "REQ_CODE_VERSION_UPDATE method calling 1 ");                    // If the update is cancelled or fails,
                    // you can request to start the update again.

                }

                break;
        }

        //must param to get the acitivity
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void dismissApplication() {
        if (backPress == 0) {
            backPress++;
            ViewUtil.showSnackBar(binding.getRoot(),prefModel.getLanguageMasterDynamic().getDetails().getPressAgainForExit());
        } else {
            finishAffinity();
        }
    }

    @Override
    public void onClickListener() {
        openProfileFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.item_home:

                selectNavigationMenu(0);

                auroStudentscholarSdk(0);
                break;

            case R.id.item_passport:
                funnelPassportScreen();
                selectNavigationMenu(3);
                openTransactionsFragment();
                break;

            case R.id.item_profile:
                funnelStudentProfileScreen();
                selectNavigationMenu(2);
                openProfileFragment();
                break;
            case R.id.item_more:

                openitemMore();
                break;

            case R.id.item_back:
                closeItemMore();
                if (!isBackNormal) {
                    selectNavigationMenu(0);
                    auroStudentscholarSdk(0);
                    isBackNormal = true;
                }

                break;

            case R.id.item_partner:
                selectNavigationMenu(1);
                handlePartnertabClick();
                funnelPartnerApp();
                break;

            case R.id.item_logout:
                //openLogoutDialog();
                SharedPreferences mySPrefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = mySPrefs.edit();
                editor.remove("viewall_finalcatlistid");
                editor.apply();
                selectMoreNavigationMenu(0);
                openFragment(new FAQFragment());
                break;

            case R.id.item_aurofriend:
                isBackNormal = false;
                funnelStudentleaderBoardScreen();
                openFriendLeaderBoardFragment();
                selectMoreNavigationMenu(1);
                break;

            case R.id.item_kyc:
                openStudentKycInfoFragment();
                selectMoreNavigationMenu(2);
                isBackNormal = false;

                break;

            case R.id.item_privacy_policy:
                isBackNormal = false;
                selectMoreNavigationMenu(3);
                handlePrivacyPolicytabClick();
                break;

        }

        return false;
    }

    private void openitemMore() {
        int mainMoreBottomNavigation = binding.naviagtionContent.bottomSecondnavigation.getVisibility();
        if (mainMoreBottomNavigation != 0) {
            AppLogger.v("Animation", "mainMoreBottomNavigation 0  item_more--->" + mainMoreBottomNavigation);
            openSwipeRightSelectionLayout(binding.naviagtionContent.bottomSecondnavigation, binding.naviagtionContent.bottomNavigation);
        }
    }


    public void closeItemMore() {
        selectMoreNavigationMenu(4);
        int mainMoresSecondNavigation = binding.naviagtionContent.bottomSecondnavigation.getVisibility();
        if (mainMoresSecondNavigation == 0) {
            AppLogger.v("Animation", "mainMoreBottomNavigation 0 item_back --->" + mainMoresSecondNavigation);
            openSwipeLeftSelectionLayout(binding.naviagtionContent.bottomSecondnavigation, binding.naviagtionContent.bottomNavigation);
        }
    }

    public void setHomeFragmentTab() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        NotificationDataModel notificationDataModel = prefModel.getNotificationDataModel();
        AppLogger.e("notification ", "step 1");
        if (notificationDataModel != null && !TextUtil.isEmpty(notificationDataModel.getNavigateto())) {
            openFragmentOnNotificationstatus(notificationDataModel);
            AppLogger.e("notification ", "step 2");

        } else {
            auroStudentscholarSdk(0);
            AppLogger.e("notification ", "step 3");
        }
        selectNavigationMenu(0);

    }

    public void openFragmentOnNotificationstatus(NotificationDataModel notificationDataModel) {
        switch (notificationDataModel.getNavigateto()) {
            case AppConstant.NavigateToScreen.STUDENT_DASHBOARD:
                auroStudentscholarSdk(0);
                AppLogger.e("notification ", "step 4 STUDENT_DASHBOARD");
                break;


            case AppConstant.NavigateToScreen.STUDENT_KYC:
                AppLogger.e("notification ", "step 4 STUDENT_KYC");
                auroStudentscholarSdk(1);
                break;
            case AppConstant.NavigateToScreen.STUDENT_CERTIFICATE:
                AppLogger.e("notification ", "step 6 STUDENT_CERTIFICATE");
                auroStudentscholarSdk(1);
                break;

            case AppConstant.NavigateToScreen.FRIENDS_LEADERBOARD:
                AppLogger.e("notification ", "step 7 FRIENDS_LEADERBOARD");
                auroStudentscholarSdk(1);

                break;

            case AppConstant.NavigateToScreen.PAYMENT_TRANSFER:
                AppLogger.e("notification ", "step 7 FRIENDS_LEADERBOARD");
                auroStudentscholarSdk(1);

                break;

            default:
                AppLogger.e("notification ", "step default");
                auroStudentscholarSdk(0);
                break;


        }

        AppLogger.e("notification ", "step 8");
    }

    public void selectNavigationMenu(int pos) {
        binding.naviagtionContent.bottomNavigation.getMenu().getItem(pos).setChecked(true);
    }

    public void selectMoreNavigationMenu(int pos) {
        binding.naviagtionContent.bottomSecondnavigation.getMenu().getItem(pos).setChecked(true);

    }

    private synchronized void backStack() {

        switch (LISTING_ACTIVE_FRAGMENT) {
            case QUIZ_DASHBOARD_FRAGMENT:
                if (commonCallBackListner != null) {
                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.BACK_CLICK, ""));
                }
                AppLogger.e(TAG, "commonEventListner BACK_CLICK");

                break;

            case QUIZ_TEST_FRAGMENT:
                alertDialogForQuitQuiz();
                break;


            case CERTIFICATE_DIRECT_FRAGMENT:
                openFragment(AuroScholar.startAuroSDK(inputModel));
                break;
            case PAYMENT_DIRECT_FRAGMENT:
                openFragment(AuroScholar.startAuroSDK(inputModel));
                break;

            case PAYMENT_FRAGMENT:
                openFragment(AuroScholar.startAuroSDK(inputModel));
                break;
            case DEMOGRAPHIC_FRAGMENT:
            case STUDENT_PROFILE_FRAGMENT:
            case FAQ_FRAGMENT:
            case TRANSACTION_FRAGMENT:
            case PARTNERS_FRAGMENT:
            case KYC_DIRECT_FRAGMENT:
            case QUIZ_KYC_FRAGMENT:
            case QUIZ_KYC_VIEW_FRAGMENT:
            case PRIVACY_POLICY_FRAGMENT:
            case LEADERBOARD_FRAGMENT:
                selectNavigationMenu(0);
                auroStudentscholarSdk(0);
                closeItemMore();
                break;


            case SEND_MONEY_FRAGMENT:
                popBackStack();
                break;

            case NATIVE_QUIZ_FRAGMENT:
                openDialogForQuit();
                break;

            default:
                popBackStack();
                break;
        }


    }

    public void popBackStack() {
        backPress = 0;
        getSupportFragmentManager().popBackStack();
    }


    public static void setListingActiveFragment(int listingActiveFragment) {
        LISTING_ACTIVE_FRAGMENT = listingActiveFragment;
    }


    public void alertDialogForQuitQuiz() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.quiz_exit_txt);


        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + this.getString(R.string.yes) + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                popBackStack();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>" + this.getString(R.string.no) + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void alertDialogForQuit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your session is expired..! Please login again");
        builder.setCancelable(false);


        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + this.getString(R.string.ok) + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               logout();
                dialog.dismiss();
            }
        });


        AlertDialog dialog = builder.create();

        dialog.show();
    }


    private void checkRefferedData() {
        AppLogger.e("SEND_REFERRAL_API", "dynamiclink step 1");
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        DynamiclinkResModel dynamiclinkResModel = prefModel.getDynamiclinkResModel();
        if (dynamiclinkResModel != null && dynamiclinkResModel.getReffeUserId() != null && !dynamiclinkResModel.getReffeUserId().isEmpty()) {
            AppLogger.e("SEND_REFERRAL_API", "dynamiclink" + dynamiclinkResModel.getReffeUserId());
            RefferalReqModel reqModel = new RefferalReqModel();
            reqModel.setReferredById(dynamiclinkResModel.getReffeUserId());
            reqModel.setReferredUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
            reqModel.setReferredByType(dynamiclinkResModel.getReffer_type());
            reqModel.setReferredUserMobile(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserMobile());
            viewModel.checkInternet(Status.SEND_REFERRAL_API, reqModel);
            AppLogger.e(TAG, dynamiclinkResModel.getRefferMobileno());
        } else {
            AppLogger.e(TAG, "No Link Available");
        }
    }

    private void observeServiceResponse() {
        AppLogger.v("observeServiceResponse", " response Step 1");

        SharedPreferences prefs = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String gradeforsubjectpreference = prefs.getString("gradeforsubjectpreference", "");
        String gradeforsubjectpreferencewithoutpin = prefs.getString("gradeforsubjectpreferencewithoutpin","");

        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    binding.naviagtionContent.progressbar.pgbar.setVisibility(View.GONE);
                    if (responseApi.apiTypeStatus == DASHBOARD_API) {
                        onApiSuccess(responseApi);
                        DashboardResModel dashboardResModel = (DashboardResModel) responseApi.data;
                        if (commonCallBackListner != null) {
                            commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, LISTNER_SUCCESS, dashboardResModel));
                        }
                    }
                    else if (responseApi.apiTypeStatus == SEND_OTP) {
                        SendOtpResModel sendOtp = (SendOtpResModel) responseApi.data;
                        if (!sendOtp.getError()) {
                            checkOtpDialog();
                        }
                    }
                    else if (responseApi.apiTypeStatus == Status.VERIFY_OTP) {
                        VerifyOtpResModel verifyOtp = (VerifyOtpResModel) responseApi.data;
                        AppLogger.v("OTP_MAIN", "Step 7");
                        if (!verifyOtp.getError()) {
                            AppLogger.v("OTP_MAIN", "Step 8");
                            // checkUserType();
                            if (customOtpDialog != null && customOtpDialog.isShowing()) {
                                customOtpDialog.dismiss();
                                customOtpDialog.hideProgress();
                                if (commonCallBackListner != null) {
                                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.OTP_VERIFY, ""));
                                }
                            }
                            AppLogger.v("OTP_MAIN", "Step 9");
                        } else {
                            AppLogger.v("OTP_MAIN", "Step 10");
                            if (customOtpDialog != null && customOtpDialog.isShowing()) {
                                customOtpDialog.hideProgress();
                                customOtpDialog.showSnackBar(verifyOtp.getMessage());
                            }
                        }
                    }


                    else if (responseApi.apiTypeStatus == Status.FETCH_STUDENT_PREFERENCES_API) {

                        AppLogger.v("OTP_MAIN", "Step 11");
                        FetchStudentPrefResModel fetchStudentPrefResModel = (FetchStudentPrefResModel) responseApi.data;
                        if (TextUtil.checkListIsEmpty(fetchStudentPrefResModel.getPreference())) {
                            openSubjectPreferenceScreen();
                            AppLogger.v("OTP_MAIN", "Step 12");
                        } else {
                            AppLogger.v("OTP_MAIN", "Step 13");
                            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                            prefModel.setFetchStudentPrefResModel(fetchStudentPrefResModel);
                            AuroAppPref.INSTANCE.setPref(prefModel);

                            if (commonCallBackListner != null) {
                                AppLogger.v("OTP_MAIN", "Step 14");
                                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.FETCH_STUDENT_PREFERENCES_API, fetchStudentPrefResModel));
                            }
                        }


                    }
                    else if (responseApi.apiTypeStatus == SEND_REFERRAL_API) {
                        DynamiclinkResModel dynamiclinkResModel = (DynamiclinkResModel) responseApi.data;
                        AppLogger.v("SEND_REFERRAL_API", " response Step 1");
                        if (!dynamiclinkResModel.getError()) {
                            AppLogger.v("SEND_REFERRAL_API", "response Step 2");
                            AppUtil.setEmptyToDynamicResponseModel();
                        }
                    }
                    else if (responseApi.apiTypeStatus == Status.DYNAMIC_LINK_API) {
                        AppLogger.v("observeServiceResponse", " response Step 2");
                        DynamiclinkResModel dynamiclinkResModel = (DynamiclinkResModel) responseApi.data;
                        AppLogger.v("observeServiceResponse", " response Step 3");
                        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                        prefModel.setDynamiclinkResModel(dynamiclinkResModel);
                        AuroAppPref.INSTANCE.setPref(prefModel);
                        AppLogger.v("observeServiceResponse", " response Step 4");
                        if (dynamiclinkResModel.getError()) {
                            AppLogger.v("observeServiceResponse", " response Step 5");
                            sendRefferCallback(dynamiclinkResModel, 1);
                        } else {
                            AppLogger.v("observeServiceResponse", " response Step 6");
                            sendRefferCallback(dynamiclinkResModel, 0);

                        }
                    }
                    else if (responseApi.apiTypeStatus == Status.GET_INSTRUCTIONS_API) {
                        InstructionsResModel instructionsResModel = (InstructionsResModel) responseApi.data;
                        AppLogger.v("Notice", "else part  GET_INSTRUCTIONS_API API RESPONSE First"+instructionsResModel.getError());
                        if (!instructionsResModel.getError()) {
                            AppLogger.v("Notice", "else part  GET_INSTRUCTIONS_API API RESPONSE Second"+instructionsResModel.getError());
                            checkDisclaimer(instructionsResModel);
                            AppLogger.v("Notice", "else part  GET_INSTRUCTIONS_API API RESPONSE Third " +instructionsResModel.getError());
                            if (commonCallBackListner != null) {
                                AppLogger.v("Notice", "else part  GET_INSTRUCTIONS_API API RESPONSE Four ");
                                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.GET_INSTRUCTIONS_API, instructionsResModel));
                            }
                        } else {
                            AppLogger.v("Notice"," GET_INSTRUCTIONS_API  ");
                            AppLogger.v("Notice", "else part  GET_INSTRUCTIONS_API");
                        }
                    }
                    else if (responseApi.apiTypeStatus == Status.NOTICE_INSTRUCTION){
                        NoticeInstruction noticeInstruction = (NoticeInstruction) responseApi.data;
                        //noticeInstruction.getData().getId();
                        AppLogger.v("Notice"," NOTICE_INSTRUCTION  last  "+noticeInstruction.getData().getMsgText());
                        openNoticeDialog(noticeInstruction);
                        AppLogger.v("Notice"," NOTICE_INSTRUCTION  last"+noticeInstruction.getData().getId());
                    }
                    else if (responseApi.apiTypeStatus == Status.GET_MESSAGE_POP_UP){
                        ShowDialogModel showDialogModel = (ShowDialogModel) responseApi.data;

                        AppLogger.v("Dialog_pradeep"," NOTICE_INSTRUCTION  last  "+showDialogModel.getShowDailogue());
                        openGradeDialog(showDialogModel);
                        AppLogger.v("Dialog_pradeep"," NOTICE_INSTRUCTION  last"+showDialogModel.getShowDailogue());
                    }
                    else if (responseApi.apiTypeStatus == Status.PENDING_KYC_DOCS){
                        ErrorResponseModel noticeInstruction = (ErrorResponseModel) responseApi.data;

                        if(!noticeInstruction.getMessage().equals("")) {
                            ViewUtil.showSnackBar(binding.getRoot(), noticeInstruction.getMessage());
                        }
                        AppLogger.v("Pending_Pradeep","PENDING_KYC_DOCS");


                    }
                    else if (responseApi.apiTypeStatus == Status.OTP_OVER_CALL) {

                        OtpOverCallResModel otpOverCallResModel = (OtpOverCallResModel) responseApi.data;
                        if (!otpOverCallResModel.getError()) {



                            AppLogger.v("Otp_pradeep", "Step 6");
                            checkOtpDialog();



                        } else {
                            ViewUtil.showSnackBar(binding.getRoot(), otpOverCallResModel.getMessage());
                            AppLogger.e(TAG, "Step 7");
                        }
                    }



                    break;

                case NO_INTERNET:
                    binding.naviagtionContent.progressbar.pgbar.setVisibility(View.GONE);
                    AppLogger.v("OTP_MAIN", "Step 15");
                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, LISTNER_FAIL, (String) responseApi.data));
                    }
                    break;

                case AUTH_FAIL:
                case FAIL_400:
                    AppLogger.v("OTP_MAIN", "Step 16  " + (String) responseApi.data);
                    binding.naviagtionContent.progressbar.pgbar.setVisibility(View.GONE);
                    if (customOtpDialog != null && customOtpDialog.isShowing()) {
                        customOtpDialog.hideProgress();
                        customOtpDialog.showSnackBar((String) responseApi.data);
                    }
                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, LISTNER_FAIL, (String) responseApi.data));
                    }


                    break;
            }

        });
    }

    private void onApiSuccess(ResponseApi responseApi) {
        dashboardResModel = (DashboardResModel) responseApi.data;
        AppUtil.setDashboardResModelToPref(dashboardResModel);
        AppUtil.setEmptyToDynamicResponseModel();
    }

    public void dashboardModel(DashboardResModel model) {
        dashboardResModel = model;
        AppUtil.setDashboardResModelToPref(model);
    }

    public void openNoticeDialog( NoticeInstruction noticeInstruction ){
        AppLogger.v("Notice","Open Notice Dialog - "+noticeInstruction.getData().getShowDailogue().equals("1"));
        if(noticeInstruction.getData()!= null && noticeInstruction.getData().getShowDailogue().equals("1")){
            AppLogger.v("Notice","Open Notice Dialog - "+noticeInstruction.getData().getShowDailogue().equals("1"));
            noticeDialogBox = new NoticeDialogBox(this,clickLisner);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(noticeDialogBox.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            noticeDialogBox.getWindow().setAttributes(lp);
            Objects.requireNonNull(noticeDialogBox.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            noticeDialogBox.setCancelable(false);
            noticeDialogBox.show();
            noticeDialogBox.setAfterLoginInstruction(noticeInstruction);
        }else{
            callingCongratsDialog();
        }
    }

    public void callingCongratsDialog(){
        PendingKycDocsModel pendingKycDocsModel = new PendingKycDocsModel();
        AppLogger.v("UserIdPradeep"," User Id "+AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        pendingKycDocsModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
        pendingKycDocsModel.setUserPreferedLanguageId(Integer.parseInt(prefModel.getUserLanguageId()));
        viewModel.checkInternet(Status.GET_MESSAGE_POP_UP, pendingKycDocsModel);

    }

    public void openGradeDialog(ShowDialogModel showDialogModel){
        AppLogger.v("Dialog_pradeep","Open Notice Dialog - "+showDialogModel.getShowDailogue().equals("No"));
        if(showDialogModel.getShowDailogue().equals("Yes") && showDialogModel.getImgUrl()!= null && !showDialogModel.getImgUrl().equals("")){
            AppLogger.v("Dialog_pradeep","Enter   - "+showDialogModel.getShowDailogue().equals("NO"));
            gradeChnageCongDialogBox = new GradeChnageCongDialogBox(this,clickLisner);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(gradeChnageCongDialogBox.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            gradeChnageCongDialogBox.getWindow().setAttributes(lp);
            Objects.requireNonNull(gradeChnageCongDialogBox.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            gradeChnageCongDialogBox.setCancelable(false);
            gradeChnageCongDialogBox.show();
            gradeChnageCongDialogBox.setAfterLoginInstruction(showDialogModel);
        }
    }

    public void openKYCFragment(DashboardResModel dashboardResModel, int status) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.home_container);
        if (currentFragment instanceof KYCFragment) {
            AppLogger.e("chhonker", "find the current fragment yes");
            return;
        } else {
            AppLogger.e("chhonker", "find the current fragment not");
        }

        Bundle bundle = new Bundle();
        KYCFragment kycFragment = new KYCFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        if (status != 0) {
            bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.DYNAMIC_LINK);
        }
        kycFragment.setArguments(bundle);
        openFragment(kycFragment);
    }

    public void openKYCViewFragment(DashboardResModel dashboardResModel, int status) {
        Bundle bundle = new Bundle();
        KYCViewFragment kycViewFragment = new KYCViewFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        if (status != 0) {
            bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.DYNAMIC_LINK);
        }
        kycViewFragment.setArguments(bundle);
        openFragment(kycViewFragment);
    }

    public void openCertificate() {
        Bundle bundle = new Bundle();
        CertificateFragment certificateFragment = new CertificateFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.DYNAMIC_LINK);
        certificateFragment.setArguments(bundle);
        openFragment(certificateFragment);
    }


    public void openSendMoneyFragment() {
        Bundle bundle = new Bundle();
        SendMoneyFragment sendMoneyFragment = new SendMoneyFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.DYNAMIC_LINK);
        sendMoneyFragment.setArguments(bundle);
        openFragment(sendMoneyFragment);
    }


    public void setListner(CommonCallBackListner listner) {
        this.commonCallBackListner = listner;
    }


    private void setRequiredData() {
        /*Set data before call any fragment in auroscholarActivity*/
        AuroScholarDataModel auroScholarDataModel = new AuroScholarDataModel();
        auroScholarDataModel.setMobileNumber(inputModel.getMobileNumber());
        auroScholarDataModel.setStudentClass(inputModel.getStudentClass());
        auroScholarDataModel.setRegitrationSource(inputModel.getRegitrationSource());
        auroScholarDataModel.setActivity(inputModel.getActivity());
        auroScholarDataModel.setFragmentContainerUiId(inputModel.getFragmentContainerUiId());
        auroScholarDataModel.setReferralLink(inputModel.getReferralLink());
        if (TextUtil.isEmpty(inputModel.getPartnerSource())) {
            auroScholarDataModel.setPartnerSource("");
        } else {
            auroScholarDataModel.setPartnerSource(inputModel.getPartnerSource());
        }
        AuroApp.setAuroModel(auroScholarDataModel);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {


            AppLogger.e("chhonker-", "Touch Event");
            if (AppUtil.commonCallBackListner != null) {
                AppUtil.commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.SCREEN_TOUCH, ""));
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void hideBottomNavigationView() {
        binding.naviagtionContent.bottomNavigation.setVisibility(View.GONE);
        AppLogger.e("hideBottomNavigationView", "i am calling");
        BottomNavigationView view = binding.naviagtionContent.bottomNavigation;
        view.clearAnimation();
        view.animate().translationY(view.getHeight()).setDuration(300);
    }

    public void visibilityOfNavigation(int status) {
        if (status == 0) {
            binding.naviagtionContent.bottomNavigationDesgin.setVisibility(View.GONE);
        } else {
            binding.naviagtionContent.bottomNavigationDesgin.setVisibility(View.VISIBLE);
        }
    }

    public void showBottomNavigationView() {
        binding.naviagtionContent.bottomNavigation.setVisibility(View.VISIBLE);
        AppLogger.e("showBottomNavigationView", "i am calling");
        BottomNavigationView view = binding.naviagtionContent.bottomNavigation;
        view.clearAnimation();
        view.animate().translationY(0).setDuration(300);
    }

    public void openTransactionsFragment() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        Bundle bundle = new Bundle();
        TransactionsFragment transactionsFragment = new TransactionsFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, prefModel.getDashboardResModel());
        transactionsFragment.setArguments(bundle);
        openFragment(transactionsFragment);
    }


    public void openStudentKycInfoFragment() {
        StudentKycInfoFragment transactionsFragment = new StudentKycInfoFragment();
        openFragment(transactionsFragment);
    }

    public void openProfileFragment() {
        closeItemMore();
        selectNavigationMenu(2);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        Bundle bundle = new Bundle();
        StudentProfileFragment studentProfile = new StudentProfileFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, prefModel.getDashboardResModel());
        bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.STUDENT_PROFILE);
        studentProfile.setArguments(bundle);
        openFragment(studentProfile);
    }

    public void openPartnersFragment() {
        selectNavigationMenu(1);
        AppLogger.v("Mindler","Step 1 Partner");
        PartnersFragment partnersFragment = new PartnersFragment();
        openFragment(partnersFragment);
    }


    public void openChangeLanguageDialog() {
        LanguageChangeDialog languageChangeDialog = new LanguageChangeDialog(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(languageChangeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        languageChangeDialog.getWindow().setAttributes(lp);
        Objects.requireNonNull(languageChangeDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        languageChangeDialog.setCancelable(true);
        languageChangeDialog.show();

    }

    private void openSwipeRightSelectionLayout(View viewAnimation, View secondAnimation) {
        //Animation on button
        viewAnimation.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.frag_enter_right);
        viewAnimation.startAnimation(anim);

        //Animation on button

        Animation secondanim = AnimationUtils.loadAnimation(this, R.anim.frag_exit_left);
        secondAnimation.startAnimation(secondanim);
        secondAnimation.setVisibility(View.GONE);

    }

    private void openSwipeLeftSelectionLayout(View viewAnimation, View secondAnimation) {
        //Animation on button

        secondAnimation.setVisibility(View.VISIBLE);
        Animation secondanim = AnimationUtils.loadAnimation(this, R.anim.frag_enter_left);
        secondAnimation.startAnimation(secondanim);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.frag_exit_right);
        viewAnimation.startAnimation(anim);
        viewAnimation.setVisibility(View.GONE);

    }

    private void openLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
        Details details = model.getDetails();
        String yes = this.getString(R.string.yes);
        String no = this.getString(R.string.no);
        builder.setMessage(details.getQuizExitTxt());
        try {

            if (model != null) {
                yes = details.getYes();
                no = details.getNo();
                builder.setMessage(details.getSureToLogout());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
        // Set the alert dialog yes button click listener
        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + yes + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                logout();
            }
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>" + no + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when No button clicked
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }


    private void logout() {
        AuroAppPref.INSTANCE.clearPref();
        SharedPreferences preferences =getSharedPreferences("My_Pref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setLogin(false);

        Intent intent = new Intent(this, SplashScreenAnimationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void clearApplicationData() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(String.valueOf(new File(applicationDirectory, fileName)));
                }
            }
        }
    }
    private void openFriendLeaderBoardFragment() {
        FriendsLeaderBoardListFragment fragment = new FriendsLeaderBoardListFragment();
        openFragment(fragment);
    }


    public void showSnackbar(String msg) {
        ViewUtil.showSnackBar(binding.getRoot(), msg);
    }

    private void funnelStudentDashBoard() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentDashBoard();
    }

    private void funnelStudentLogOut() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentLogOut();

    }

    private void funnelStudentKYCscreen() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentKycScreen();
    }

    private void funnelStudentleaderBoardScreen() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentLeaderBoardScreen();
    }

    private void funnelStudentProfileScreen() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentProfileScreen();

    }

    private void funnelPassportScreen() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentPassportScreen();

    }

    private void funnelPartnerApp() {
        AnalyticsRegistry.INSTANCE.getModelInstance().trackStudentPartnerScreen();

    }

    public void setProgressVal() {
        //  AppLogger.e("Chhonker setProgressVal", "i am calling");
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setDashboardaApiNeedToCall(true);
        //AppLogger.e("setStringPref-", "thrid time 3---");
        AuroAppPref.INSTANCE.setPref(prefModel);

    }

    @Override
    protected void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    private void checkDisclaimer(InstructionsResModel instructionsResModel) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.v("Notice", "checkDisclaimer outer ");
        if (!prefModel.isPreLoginDisclaimer()) {
            AppLogger.v("Notice", "checkDisclaimer if ");
            loginDisclaimerDialog = new LoginDisclaimerDialog(this,clickLisner);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(loginDisclaimerDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            loginDisclaimerDialog.getWindow().setAttributes(lp);
            Objects.requireNonNull(loginDisclaimerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loginDisclaimerDialog.setCancelable(false);
            loginDisclaimerDialog.show();
            loginDisclaimerDialog.setAfterLoginInstruction(instructionsResModel);
        }else{
            AppLogger.v("Notice", "checkDisclaimer else ");

            if(AppConstant.InstructionsType.SCHOLARSHIP_TRANSFER != typeGradeChange) {
                viewModel.checkInternet(Status.NOTICE_INSTRUCTION, null);
            }

        }
    }


    private void checkOtpDialog() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();

        customOtpDialog = new CustomOtpDialog(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(customOtpDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        customOtpDialog.getWindow().setAttributes(lp);
        Objects.requireNonNull(customOtpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customOtpDialog.setCancelable(false);
        customOtpDialog.show();


    }


    public void callOverOTPApi() {
        OtpOverCallReqModel reqModel=new OtpOverCallReqModel();
        String phonenumber = prefModel.getStudentData().getUserMobile();
        reqModel.setIsType(AuroAppPref.INSTANCE.getModelInstance().getUserType());
        reqModel.setMobileNo(phonenumber);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.naviagtionContent.progressbar.pgbar.setVisibility(View.GONE);
            }
        }, 10000);
        binding.naviagtionContent.progressbar.pgbar.setVisibility(View.VISIBLE);
        // binding.progressbar.pgbar.setVisibility(View.VISIBLE);
        viewModel.checkInternet(Status.OTP_OVER_CALL,reqModel);
    }


    public void sendOtpApiReqPass() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.naviagtionContent.progressbar.pgbar.setVisibility(View.GONE);
            }
        }, 10000);
        binding.naviagtionContent.progressbar.pgbar.setVisibility(View.VISIBLE);
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String phonenumber = prefModel.getStudentData().getUserMobile();
        SendOtpReqModel mreqmodel = new SendOtpReqModel();
        mreqmodel.setMobileNo(phonenumber);
        mreqmodel.setUser_prefered_language_id(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId());
        viewModel.checkInternet(SEND_OTP, mreqmodel);


    }

    public void verifyOtpRxApi(String otptext) {
        ViewUtil.hideKeyboard(this);
        if (customOtpDialog != null && customOtpDialog.isShowing()) {
            customOtpDialog.showProgress();
            VerifyOtpReqModel mverifyOtpRequestModel = new VerifyOtpReqModel();
            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
            int type = prefModel.getUserType();
            if (type == AppConstant.UserType.TEACHER) {
                mverifyOtpRequestModel.setUserType(1);
            } else {
                mverifyOtpRequestModel.setUserType(0);
                mverifyOtpRequestModel.setResgistrationSource("AuroScholr");
            }
            String phonenumber = prefModel.getStudentData().getUserMobile();
            mverifyOtpRequestModel.setDeviceToken(deviceToken);
            mverifyOtpRequestModel.setMobileNumber(phonenumber);
            mverifyOtpRequestModel.setOtpVerify(otptext);
            mverifyOtpRequestModel.setSrId("");
            mverifyOtpRequestModel.setUser_prefered_language_id(AuroAppPref.INSTANCE.getModelInstance().getUserLanguageId());
            AppLogger.v("OTP_MAIN", "Step 1" + otptext);
            viewModel.checkInternet(Status.VERIFY_OTP, mverifyOtpRequestModel);
        }
    }

    public void setupNavigation() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        int studentClass = prefModel.getStudentClass();
        if (studentClass < 10) {
            Menu menuDashboard = binding.naviagtionContent.bottomNavigation.getMenu();
            menuDashboard.findItem(R.id.item_partner).setTitle(R.string.certificatesmenuauro);
            Menu backMenuDashboard = binding.naviagtionContent.bottomSecondnavigation.getMenu();
            backMenuDashboard.findItem(R.id.item_privacy_policy).setTitle(R.string.privacy_policy_auro);
        } else {
            Menu menuDashboard = binding.naviagtionContent.bottomNavigation.getMenu();
            menuDashboard.findItem(R.id.item_partner).setTitle(R.string.partner_menuauro);

            Menu backMenuDashboard = binding.naviagtionContent.bottomSecondnavigation.getMenu();
            backMenuDashboard.findItem(R.id.item_privacy_policy).setTitle(R.string.certificatesmenuauro);
        }
    }

    private void handlePrivacyPolicytabClick() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        int studentClass = prefModel.getStudentClass();
        if (studentClass < 10) {
            openFragment(new PrivacyPolicyFragment());
        } else {
            openCertificate();
        }
    }


    private void handlePartnertabClick() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        int studentClass = prefModel.getStudentClass();
        if (studentClass < 10) {
            openCertificate();
        } else {
            openPartnersFragment();
        }

        // openCertificate();
    }


    private void openSubjectPreferenceScreen() {
        if (loginDisclaimerDialog != null && loginDisclaimerDialog.isShowing()) {
            loginDisclaimerDialog.dismiss();
        }
        finish();
        Intent newIntent = new Intent(this, SubjectPreferencesActivity.class);
        startActivity(newIntent);
        finish();
    }

    public void openDialogForQuit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(prefModel.getLanguageMasterDynamic().getDetails().getPlease_confirm_if_you_want());
        // Set the alert dialog yes button click listener
        String yes = prefModel.getLanguageMasterDynamic().getDetails().getYes();//this.getResources().getString(R.string.yes);
        String no = prefModel.getLanguageMasterDynamic().getDetails().getNo();//this.getResources().getString(R.string.no);

        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + yes + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogQuit, int which) {
                if (AppUtil.commonCallBackListner != null) {
                    AppUtil.commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.FINISH_DIALOG_CLICK, ""));
                }
            }
        });
        // Set the alert dialog no button click listener
        builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>" + no + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogQuit, int which) {

                dialogQuit.dismiss();

            }
        });

        dialogQuit = builder.create();
        dialogQuit.show();
        // Display the alert dialog on interface
    }




    public void sendRefferCallback(DynamiclinkResModel dynamiclinkResModel, int status) {
        AppLogger.v("observeServiceResponse", " response Step 8");
        if (commonCallBackListner != null) {
            if (status == 1) {
                AppLogger.v("observeServiceResponse", " response 9 ");
                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(status, Status.REFFER_API_SUCCESS, dynamiclinkResModel));
            } else {
                AppLogger.v("observeServiceResponse", " response Step 10");
                commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(status, Status.REFFER_API_ERROR, dynamiclinkResModel));
            }
        }
    }

    public void callGetInstructionsApi(String type) {
        typeGradeChange = type;
        String userlangid = prefModel.getUserLanguageId();
        int langid = Integer.parseInt(userlangid);
        InstructionModel instructionModel = new InstructionModel();
        instructionModel.setLanguageId(langid);
        instructionModel.setInstructionCode(type);
        viewModel.checkInternet(Status.GET_INSTRUCTIONS_API, instructionModel);
        AppLogger.v("Notice","callGetInstructionsApi");
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        if(commonDataModel.getClickType()==Status.LOGIN_DISCLAMER_DIALOG){
            AppLogger.v("Notice","clickMethodNotification");
            viewModel.checkInternet(Status.NOTICE_INSTRUCTION, null);
        } else if(commonDataModel.getClickType()==Status.NOTICE_DIALOG){
            callingCongratsDialog();
        } else if(commonDataModel.getClickType() == Status.GRADE_CHANGE_DIALOG){
            String value = (String)commonDataModel.getObject();
            PendingKycDocsModel pendingKycDocsModel = new PendingKycDocsModel();
            pendingKycDocsModel.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
            pendingKycDocsModel.setIsAgree(value);
            pendingKycDocsModel.setUserPreferedLanguageId(Integer.parseInt(prefModel.getUserLanguageId()));
            AppLogger.v("Pending_Pradeep","  calling GRADE_CHANGE_DIALOG   " );
            viewModel.checkInternet(Status.PENDING_KYC_DOCS, pendingKycDocsModel);

        }
    }

    public void callSlabsApi() {
        UserSlabsRequest userSlabsRequest = new UserSlabsRequest();
        userSlabsRequest.setUserId(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());  //13899  //934444  //
        userSlabsRequest.setUserPreferedLanguageId(Integer.parseInt(prefModel.getUserLanguageId()));
        userSlabsRequest.setExamMonth(DateUtil.getcurrentMonthYear()); //202205
        viewModel.checkInternet(Status.GET_SLABS_API, userSlabsRequest);
    }

    private void getRefferalPopUp()

    {
        LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
        Details details = model.getDetails();
        String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();

        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_id",userid);

        RemoteApi.Companion.invoke().getPendingRefferal(map_data)
                .enqueue(new Callback<ReferralPopUpDataModel>()
                {
                    @Override
                    public void onResponse(Call<ReferralPopUpDataModel> call, Response<ReferralPopUpDataModel> response)
                    {
                        if (response.isSuccessful())
                        {

                            if (response.body().getShow_refferal_popup().equals(true)||response.body().getShow_refferal_popup().equals("true")||response.body().getShow_refferal_popup()==true){
                                for ( int i=0 ;i < response.body().getData().size();i++)
                                {
                                   // Toast.makeText(DashBoardMainActivity.this, "hii", Toast.LENGTH_SHORT).show();

                                    String mobileno = response.body().getData().get(i).getMobile_no();
                                    String requserid = String.valueOf(response.body().getData().get(i).getUser_id());
                                    String studentname = response.body().getData().get(i).getStudent_name();
                                    String profilepic = response.body().getData().get(i).getProfile_pic();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardMainActivity.this);
                                    final View customLayout
                                            = getLayoutInflater()
                                            .inflate(
                                                    R.layout.referal_pop_up_dashboard,
                                                    null);
                                    builder.setView(customLayout);
                                    builder.setCancelable(false);
                                    ImageView profileimage = customLayout.findViewById(R.id.profileimage);
                                    TextView txtname = customLayout.findViewById(R.id.txtname);
                                    TextView txtuserid = customLayout.findViewById(R.id.txtuserid);
                                    TextView txtrefermsg = customLayout.findViewById(R.id.txtrefermsg);
                                    txtrefermsg.setText(details.getDashboard_refer_msg());
                                    Button buttonOk = customLayout.findViewById(R.id.buttonOk);
                                    Button buttonrej = customLayout.findViewById(R.id.buttonrej);
                                    buttonrej.setText(details.getDashboard_refer_reject());
                                    if (profilepic.equals("")||profilepic.isEmpty()||profilepic.equals(null)||profilepic.equals("null")){
                                        Glide.with(getApplicationContext())
                                                .load(getApplicationContext().getResources().getIdentifier("my_drawable_image_name", "drawable",mContext.getPackageName()))
                                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                                .placeholder(R.drawable.account_circle)
                                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                                .into(profileimage);
                                    }
                                    else{
                                        Glide.with(getApplicationContext()).load(profilepic).circleCrop().into(profileimage);
                                    }

                                    txtname.setText("Name : "+studentname);
                                    txtuserid.setText("User ID : " + requserid);
                                    buttonOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String accept = "Accepted";
                                            String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
                                            postReferalData(requserid,userid,accept);
                                            alertDialog.dismiss();

                                        }
                                    });
                                    buttonrej.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String accept = "Rejected";
                                            String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
                                            postReferalData(requserid,userid,accept);
                                        }
                                    });

                                    alertDialog = builder.create();
                                    alertDialog.show();

                                    // Display the alert dialog on interface
                                }
                            }
                        }

                        else
                        {
                            Log.d(TAG, "onResponser: "+response.message().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.ReferralPopUpDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }

    private void postReferalData(String reqid, String userid, String status)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("requested_by_id",reqid);
        map_data.put("requested_user_id",userid);
        map_data.put("request_status",status);
        map_data.put("user_prefered_language_id",languageid);
        RemoteApi.Companion.invoke().postrefer(map_data)
                .enqueue(new Callback<GetAllChildModel>()
                {
                    @Override
                    public void onResponse(Call<GetAllChildModel> call, Response<GetAllChildModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            String msg = response.body().getMessage();
                            Toast.makeText(DashBoardMainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetAllChildModel> call, Throwable t)
                    {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getInstabug()
    {
        try{
            if (AuroAppPref.INSTANCE.getModelInstance()==null){
                alertDialogForQuit();
            }
            else{
                String suserid =  AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
                HashMap<String,String> map_data = new HashMap<>();
                map_data.put("user_id",suserid);
                map_data.put("modules","details,wallet,quizes");

                RemoteApi.Companion.invoke().getStatusForInsta(map_data)
                        .enqueue(new Callback<DashboardResponselDataModel>()
                        {
                            @Override
                            public void onResponse(Call<DashboardResponselDataModel> call, Response<DashboardResponselDataModel> response)
                            {
                                if (response.isSuccessful())
                                {

                                    String instabug = response.body().getInsta_bug();
                                    String branch = response.body().getBranch();
                                    //      if (branch.equals(true)||branch.equals("true")) {
                                    //                            Branch.enableLogging();
//                            Branch.enableTestMode();
//                            Branch.getAutoInstance(DashBoardMainActivity.this);
                                    //      }

                                    if (instabug.equals(true)||instabug.equals("true")){
                                        new Instabug.Builder(getApplication(),"ed30d18815acf92a8e7a3391ddf2ac1c").
                                                setInvocationEvents(InstabugInvocationEvent.NONE).
                                                build();
                                        Instabug.setReproStepsState(State.ENABLED);
                                        BugReporting.setShakingThreshold(800);
                                        Instabug.setSessionProfilerState(Feature.State.ENABLED);
                                        CrashReporting.setState(Feature.State.ENABLED);
                                        CrashReporting.setAnrState(Feature.State.ENABLED);
                                        CrashReporting.setNDKCrashesState(Feature.State.ENABLED);
                                        APM.setColdAppLaunchEnabled(true);
                                        APM.setHotAppLaunchEnabled(true);
                                        APM.endAppLaunch();
                                        Instabug.setTrackingUserStepsState(Feature.State.ENABLED);
                                        Instabug.setWelcomeMessageState(WelcomeMessage.State.LIVE);
                                        Instabug.showWelcomeMessage(WelcomeMessage.State.LIVE);
                                    }

                                }

                            }

                            @Override
                            public void onFailure(Call<DashboardResponselDataModel> call, Throwable t)
                            {

                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
            alertDialogForQuit();
        }


    }

    private void getDashboardMenu()
    {
        genderList.clear();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("key","Menu");
        map_data.put("language_id",prefModel.getUserLanguageId());
        Log.d("langdata",map_data.toString());
        RemoteApi.Companion.invoke().getGender(map_data)
                .enqueue(new Callback<com.auro.application.home.data.model.GenderDataModel>() {
                    @Override
                    public void onResponse(Call<com.auro.application.home.data.model.GenderDataModel> call, Response<com.auro.application.home.data.model.GenderDataModel> response)
                    {
                        if (response.isSuccessful())
                        {
                            Log.d(TAG, "onDistrictResponse: "+response.message());
                            for ( int i=0 ;i < response.body().getResult().size();i++)
                            {
                                int gender_id = response.body().getResult().get(i).getID();
                                String gender_name = response.body().getResult().get(i).getName();
                                String translated_name = response.body().getResult().get(i).getTranslatedName();
                                GenderData districtData = new  GenderData(gender_id,translated_name,gender_name);
                                genderList.add(districtData);



                            }
                            PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
                            int studentClass = prefModel.getStudentClass();
                            if (studentClass < 10) {
                                Menu menuDashboard = binding.naviagtionContent.bottomNavigation.getMenu();

                                menuDashboard.findItem(R.id.item_partner).setTitle(genderList.get(1).getTranslatedName());



                                Menu backMenuDashboard = binding.naviagtionContent.bottomSecondnavigation.getMenu();
                                backMenuDashboard.findItem(R.id.item_privacy_policy).setTitle(genderList.get(8).getTranslatedName());
                            }
                            else {
                                Menu menuDashboard = binding.naviagtionContent.bottomNavigation.getMenu();
                                menuDashboard.findItem(R.id.item_partner).setTitle(genderList.get(10).getTranslatedName());

                                Menu backMenuDashboard = binding.naviagtionContent.bottomSecondnavigation.getMenu();
                                backMenuDashboard.findItem(R.id.item_privacy_policy).setTitle(genderList.get(1).getTranslatedName());
                            }
                            Menu menuDashboard = binding.naviagtionContent.bottomNavigation.getMenu();
                            menuDashboard.findItem(R.id.item_home).setTitle(genderList.get(0).getTranslatedName());
                            menuDashboard.findItem(R.id.item_profile).setTitle(genderList.get(2).getTranslatedName());
                            menuDashboard.findItem(R.id.item_passport).setTitle(genderList.get(3).getTranslatedName());
                            menuDashboard.findItem(R.id.item_more).setTitle(genderList.get(4).getTranslatedName());

                            Menu backMenuDashboard = binding.naviagtionContent.bottomSecondnavigation.getMenu();
                            backMenuDashboard.findItem(R.id.item_logout).setTitle(genderList.get(5).getTranslatedName());
                            backMenuDashboard.findItem(R.id.item_aurofriend).setTitle(genderList.get(6).getTranslatedName());
                            backMenuDashboard.findItem(R.id.item_kyc).setTitle(genderList.get(7).getTranslatedName());
                            backMenuDashboard.findItem(R.id.item_back).setTitle(genderList.get(9).getTranslatedName());


                        }
                        else
                        {
                            Log.d(TAG, "onResponseError: "+response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.auro.application.home.data.model.GenderDataModel> call, Throwable t)
                    {
                        Log.d(TAG, "onDistrictFailure: "+t.getMessage());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
        Details details = model.getDetails();
        builder.setMessage(details.getQuizExitTxt())
                .setCancelable(false)
                .setPositiveButton(details.getYes(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(details.getNo(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }




    private void askPermission() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

            }

        }
    }
}
