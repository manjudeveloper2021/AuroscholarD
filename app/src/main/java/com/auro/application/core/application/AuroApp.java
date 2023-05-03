package com.auro.application.core.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.auro.application.BuildConfig;
import com.auro.application.core.application.di.component.AppComponent;
import com.auro.application.core.application.di.component.DaggerAppComponent;
import com.auro.application.core.application.di.module.AppModule;
import com.auro.application.core.application.di.module.UtilsModule;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.DashboardResModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import io.branch.referral.Branch;
import io.reactivex.plugins.RxJavaPlugins;


public class AuroApp extends Application {

    AppComponent appComponent;
    public static AuroApp context;
    public static AuroScholarDataModel auroScholarDataModel;
    public static int fragmentContainerUiId = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        RxJavaPlugins.setErrorHandler(throwable -> {});
        context = this;
        //Restring.init(context);
      //  Branch.enableDebugMode();
      //  Branch.enableLogging();
      //  Branch.getAutoInstance(this);
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .utilsModule(new UtilsModule())
                .build();

        appComponent.injectAppContext(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG);

//        Thread.setDefaultUncaughtExceptionHandler(
//                new Thread.UncaughtExceptionHandler() {
//                    @Override
//                    public void uncaughtException (Thread thread, Throwable e) {
//                        handleUncaughtException (thread, e);
//                    }
//                });
    }
    private void handleUncaughtException (Thread thread, Throwable e) {

        // The following shows what I'd like, though it won't work like this.
        Intent i = new Intent (getApplicationContext(), DashBoardMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

        // Add some code logic if needed based on your requirement
    }
    public AppComponent getAppComponent() {

        return appComponent;
    }

    public static AuroApp getAppContext() {

        return context;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }


    public static AuroScholarDataModel getAuroScholarModel() {
        return auroScholarDataModel;
    }

    public static void setAuroModel(AuroScholarDataModel model) {
        auroScholarDataModel = model;
        fragmentContainerUiId = model.getFragmentContainerUiId();
    }

    public static int getFragmentContainerUiId() {
        return fragmentContainerUiId;
    }

}
