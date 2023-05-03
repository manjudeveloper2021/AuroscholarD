package com.auro.application.teacher.presentation.view.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.TeacherBuddyViewpagerFragmentLayoutBinding;
import com.auro.application.databinding.TeacherModuleViewpagerFragmentLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.response.StudentWalletResModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.payment.presentation.viewmodel.SendMoneyViewModel;
import com.auro.application.teacher.presentation.view.adapter.BuddyViewPagerAdapter;
import com.auro.application.teacher.presentation.view.adapter.TeacherModuleViewPagerAdapter;
import com.auro.application.util.AppLogger;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;


public class TeacherModulePagerFragment extends BaseFragment implements CommonCallBackListner {

    @Inject
    @Named("TeacherModulePageFragment")
    TeacherModuleViewpagerFragmentLayoutBinding binding;

    TeacherModuleViewPagerAdapter viewPagerAdapter;
    StudentWalletResModel studentWalletResModel;

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


       // AppStringDynamic.setTeacherBuddyViewPagerStrings(binding);


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
        viewPagerAdapter = new TeacherModuleViewPagerAdapter(getActivity(), getChildFragmentManager(), binding.dineHomeTabs.getTabCount());
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.dineHomeTabs));
    }

    private View getViewForEachTab(int tabNo) {
        View view = getLayoutInflater().inflate(R.layout.tab_text_layout, null);
        TextView tabTitle = view.findViewById(R.id.text_one);
        tabTitle.setText(getTabList().get(tabNo));
        return view;
    }

    public void setSelectedTabText(int pos) {
        for (int i = 0; i < binding.dineHomeTabs.getTabCount(); i++) {
            View view = binding.dineHomeTabs.getTabAt(i).getCustomView();
            TextView text = view.findViewById(R.id.text_one);
            if (i == pos) {
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Poppins-Regular.ttf");
                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                text.setTypeface(face);
            } else {
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Poppins-Regular.ttf");
                text.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_black_trns));
                text.setTypeface(face);
            }
        }
    }


    public List<String> getTabList() {
        List<String> tabList = new ArrayList<>();

        tabList.add("Modules");
        tabList.add("Overview");
      //  tabList.add("Benefits");
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
        return R.layout.teacher_module_viewpager_fragment_layout;
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



    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        AppLogger.e("GET_INSTRUCTIONS_API--", "LISTNER_SUCCESS step 1");
        switch (commonDataModel.getClickType()) {
            case LISTNER_SUCCESS:


                initialiseTabs();
                break;
            case LISTNER_FAIL:

                break;

            case GET_INSTRUCTIONS_API:
                if (isVisible()) {


                }
                break;
        }
    }







    public void onBackPressed() {

        getActivity().onBackPressed();
    }


}
