package com.auro.application.teacher.presentation.view.adapter;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.auro.application.core.common.AppConstant;
import com.auro.application.home.data.model.response.StudentWalletResModel;
import com.auro.application.payment.presentation.view.fragment.BankFragment;
import com.auro.application.payment.presentation.view.fragment.PaytmFragment;
import com.auro.application.teacher.presentation.view.fragment.AcceptTeacherBuddyFragment;
import com.auro.application.teacher.presentation.view.fragment.ReceiveTeacherBuddyFragment;
import com.auro.application.teacher.presentation.view.fragment.SentTeacherBuddyFragment;

public class BuddyViewPagerAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    StudentWalletResModel studentWalletResModel;

    public BuddyViewPagerAdapter(Context c, FragmentManager fm, int totalTabs, StudentWalletResModel studentWalletResModel) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
        this.studentWalletResModel = studentWalletResModel;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle bundleviewbuddy = new Bundle();
                AcceptTeacherBuddyFragment paytmFragment = new AcceptTeacherBuddyFragment();
                bundleviewbuddy.putParcelable(AppConstant.DASHBOARD_RES_MODEL, studentWalletResModel);
                paytmFragment.setArguments(bundleviewbuddy);
                return paytmFragment;

            case 1:
                Bundle bundlereceivebuddy = new Bundle();
                ReceiveTeacherBuddyFragment bankFragment = new ReceiveTeacherBuddyFragment();
                bundlereceivebuddy.putParcelable(AppConstant.DASHBOARD_RES_MODEL, studentWalletResModel);
                bankFragment.setArguments(bundlereceivebuddy);
                return bankFragment;

            case 2:
                Bundle bundlesentbuddy = new Bundle();
                SentTeacherBuddyFragment bankFragment2 = new SentTeacherBuddyFragment();
                bundlesentbuddy.putParcelable(AppConstant.DASHBOARD_RES_MODEL, studentWalletResModel);
                bankFragment2.setArguments(bundlesentbuddy);
                return bankFragment2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}