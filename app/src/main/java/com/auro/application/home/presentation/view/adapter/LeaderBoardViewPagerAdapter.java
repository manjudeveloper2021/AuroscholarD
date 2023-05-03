package com.auro.application.home.presentation.view.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.auro.application.home.presentation.view.fragment.FriendsLeaderBoardAddFragment;
import com.auro.application.home.presentation.view.fragment.FriendsLeaderBoardListFragment;

public class LeaderBoardViewPagerAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;


    public LeaderBoardViewPagerAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        context = c;
        this.totalTabs = totalTabs;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:


                Bundle bundle = new Bundle();
                FriendsLeaderBoardListFragment friendsLeaderBoardListFragment = new FriendsLeaderBoardListFragment();

                return friendsLeaderBoardListFragment;
            case 1:

                FriendsLeaderBoardAddFragment friendsLeaderBoardAddFragment = new FriendsLeaderBoardAddFragment();

                return friendsLeaderBoardAddFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
