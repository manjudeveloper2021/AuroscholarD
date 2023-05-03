package com.auro.application.teacher.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.SendMoneyFragmentLayoutBinding;
import com.auro.application.databinding.TeacherBuddyViewpagerFragmentLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.response.InstructionsResModel;
import com.auro.application.home.data.model.response.StudentWalletResModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.fragment.KYCViewFragment;
import com.auro.application.payment.presentation.view.adapter.ViewPagerAdapter;
import com.auro.application.payment.presentation.viewmodel.SendMoneyViewModel;
import com.auro.application.teacher.presentation.view.adapter.BuddyViewPagerAdapter;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.alert_dialog.disclaimer.DisclaimerMoneyTransferDialog;
import com.auro.application.util.strings.AppStringDynamic;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;


public class TeacherBuddyFragment extends BaseFragment implements CommonCallBackListner {


    @Inject
    @Named("SendMoneyFragment")
    ViewModelFactory viewModelFactory;
    TeacherBuddyViewpagerFragmentLayoutBinding binding;
    SendMoneyViewModel viewModel;
    BuddyViewPagerAdapter viewPagerAdapter;
    StudentWalletResModel studentWalletResModel;
    private static final String TAG = "SendMoneyFragment";
    private String comingFrom;
    Details details;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setLanguageonUi(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
       // ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
       // viewModel = ViewModelProviders.of(this, viewModelFactory).get(SendMoneyViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
         details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
        return binding.getRoot();
    }


    @Override
    protected void init() {

        for (int i = 0; i < getTabList().size(); i++) {
            binding.dineHomeTabs.addTab(binding.dineHomeTabs.newTab().setCustomView(getViewForEachTab(i)));
        }

        initialiseTabs();

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.e("chhonker  sendmoney init --", "" + prefModel.isDashboardaApiNeedToCall());
        AppStringDynamic.setTeacherBuddyViewPagerStrings(binding);


    }




    public void initialiseTabs() {

        setViewPagerAdapter();
        binding.dineHomeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setSelectedTabText(tab.getPosition());
                binding.dineHomeTabs.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.blue_color));
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do code here

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do code here
                if (binding.dineHomeTabs.getSelectedTabPosition() == 0) {
                    setSelectedTabText(0);
                    binding.dineHomeTabs.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.blue_color));

                }

            }
        });
        setSelectedTabText(0);
        binding.dineHomeTabs.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.blue_color));
    }


    public void setViewPagerAdapter() {
        viewPagerAdapter = new BuddyViewPagerAdapter(getActivity(), getChildFragmentManager(), binding.dineHomeTabs.getTabCount(), studentWalletResModel);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.dineHomeTabs));
    }

    private View getViewForEachTab(int tabNo) {
        View view = getLayoutInflater().inflate(R.layout.tab_text_layout, null);
        TextView tabTitle = view.findViewById(R.id.text_one);
        //tabTitle.setTextSize(8);
        tabTitle.setText(getTabList().get(tabNo));
        return view;
    }

    public void setSelectedTabText(int pos) {
        for (int i = 0; i < binding.dineHomeTabs.getTabCount(); i++) {
            View view = binding.dineHomeTabs.getTabAt(i).getCustomView();
            TextView text = view.findViewById(R.id.text_one);
            if (i == pos) {
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Poppins-Regular.ttf");
                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.blue_color));
                text.setTypeface(face);
            } else {
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Poppins-Regular.ttf");
                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                text.setTypeface(face);
            }
        }
    }


    public List<String> getTabList() {
        List<String> tabList = new ArrayList<>();
//        tabList.add(details.getMy_buddy());
//
//        tabList.add(details.getReceive_buddy());
//        tabList.add(details.getSent_buddy());

        tabList.add(details.getMy_buddy() != null   ? details.getMy_buddy() : "My Buddies");

        tabList.add(details.getReceived() != null   ? details.getReceived() : "Received");
        tabList.add(details.getSent() != null   ? details.getSent() : "Sent");
        return tabList;
    }


    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
       // DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.SEND_MONEY_FRAGMENT);


    }

    @Override
    protected int getLayout() {
        return R.layout.teacher_buddy_viewpager_fragment_layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
        }
        init();
        setToolbar();
        setListener();
    }


    @Override
    public void onResume() {
        super.onResume();
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();


    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        AppLogger.e("GET_INSTRUCTIONS_API--", "LISTNER_SUCCESS step 1");
        switch (commonDataModel.getClickType()) {
            case LISTNER_SUCCESS:

               // studentWalletResModel = (StudentWalletResModel) commonDataModel.getObject();
                //((DashBoardMainActivity) getActivity()).dashboardModel(studentWalletResModel);
                initialiseTabs();
                break;
            case LISTNER_FAIL:

                break;

            case GET_INSTRUCTIONS_API:
                if (isVisible()) {

                   // InstructionsResModel instructionsResModel = (InstructionsResModel) commonDataModel.getObject();
                    //checkDisclaimerMoneyDialog(instructionsResModel);
                }
                break;
        }
    }







    public void onBackPressed() {
        // getActivity().getSupportFragmentManager().popBackStack();
        getActivity().onBackPressed();
    }

    public void backButton() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setDashboardaApiNeedToCall(true);
        AuroAppPref.INSTANCE.setPref(prefModel);
        PrefModel prefModel2 = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.e("chhonker backButton --", "" + prefModel2.isDashboardaApiNeedToCall());
        ((DashBoardMainActivity) getActivity()).selectNavigationMenu(0);
        ((DashBoardMainActivity) getActivity()).auroStudentscholarSdk(0);
    }
}
