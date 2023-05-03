package com.auro.application.teacher.presentation.view.adapter;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.auro.application.home.data.model.response.StudentWalletResModel;
import com.auro.application.teacher.presentation.view.fragment.AcceptTeacherBuddyFragment;
import com.auro.application.teacher.presentation.view.fragment.ReceiveTeacherBuddyFragment;
import com.auro.application.teacher.presentation.view.fragment.SentTeacherBuddyFragment;
import com.auro.application.teacher.presentation.view.fragment.TeacherModuleFragment;
import com.auro.application.teacher.presentation.view.fragment.TeacherModuleOverviewFragment;

public class TeacherModuleViewPagerAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public TeacherModuleViewPagerAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle bundleviewbuddy = new Bundle();
                TeacherModuleFragment paytmFragment = new TeacherModuleFragment();
                paytmFragment.setArguments(bundleviewbuddy);
                return paytmFragment;

            case 1:
                Bundle bundlereceivebuddy = new Bundle();
                TeacherModuleOverviewFragment bankFragment = new TeacherModuleOverviewFragment();
                bankFragment.setArguments(bundlereceivebuddy);
                return bankFragment;


          default:
            return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}