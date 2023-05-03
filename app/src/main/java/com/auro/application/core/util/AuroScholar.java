package com.auro.application.core.util;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.AppConstant;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.AuroScholarInputModel;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.home.presentation.view.fragment.FriendsLeaderBoardListFragment;

import com.auro.application.home.presentation.view.fragment.MainQuizHomeFragment;
import com.auro.application.util.AppLogger;
import com.auro.application.util.TextUtil;


public class AuroScholar {

    public static Fragment openAuroDashboardFragment(AuroScholarDataModel auroScholarDataModel) {
        if (auroScholarDataModel == null || auroScholarDataModel.getActivity() == null) {
            AppLogger.e("Auro scholar sdk not initialise", "error");
            return null;
        }
        if (auroScholarDataModel != null) {
            String input = auroScholarDataModel.getMobileNumber() + "\n" + auroScholarDataModel.getScholrId() + "\n" + auroScholarDataModel.isEmailVerified() + "\n" +
                    auroScholarDataModel.getRegitrationSource() + "\n" + auroScholarDataModel.getReferralLink();
            AppLogger.e("Auro scholar input data", input);
        }
        if (TextUtil.isEmpty(auroScholarDataModel.getPartnerSource())) {
            auroScholarDataModel.setPartnerSource("");
        }
        if (TextUtil.isEmpty(auroScholarDataModel.getShareIdentity())) {
            auroScholarDataModel.setShareIdentity("");
        }
        if (TextUtil.isEmpty(auroScholarDataModel.getShareType())) {
            auroScholarDataModel.setShareType("");
        }

        if (TextUtil.isEmpty(auroScholarDataModel.getRegitrationSource())) {
            auroScholarDataModel.setRegitrationSource("");
        }
        AuroApp.setAuroModel(auroScholarDataModel);

        if (AuroApp.getAuroScholarModel() != null) {
            switch (AuroApp.getAuroScholarModel().getSdkFragmentType()) {
                case AppConstant.FragmentType.FRIENDS_LEADER_BOARD:
                    if (auroScholarDataModel == null || auroScholarDataModel.getActivity() == null) {
                        AppLogger.e("Auro scholar sdk not initialise 2", "error");
                        return null;
                    }
                    return new FriendsLeaderBoardListFragment();
                default:
                    return new MainQuizHomeFragment();
            }
        } else {
            return new MainQuizHomeFragment();
        }
    }

    /*For generic with PhoneNumber and class*/
    public static Fragment startAuroSDK(AuroScholarInputModel inputModel) {
        AuroScholarDataModel auroScholarDataModel = new AuroScholarDataModel();
        auroScholarDataModel.setMobileNumber(inputModel.getMobileNumber());
        auroScholarDataModel.setStudentClass(inputModel.getStudentClass());
        auroScholarDataModel.setRegitrationSource(inputModel.getRegitrationSource());
        auroScholarDataModel.setActivity(inputModel.getActivity());
        auroScholarDataModel.setFragmentContainerUiId(inputModel.getFragmentContainerUiId());
        auroScholarDataModel.setReferralLink(inputModel.getReferralLink());
        auroScholarDataModel.setDevicetoken(inputModel.getDeviceToken());
        if (TextUtil.isEmpty(inputModel.getPartnerSource())) {
            auroScholarDataModel.setPartnerSource("");
        } else {
            auroScholarDataModel.setPartnerSource(inputModel.getPartnerSource());
        }
        AuroApp.setAuroModel(auroScholarDataModel);
        MainQuizHomeFragment quizHomeFragment = new MainQuizHomeFragment();
        return quizHomeFragment;
    }


    public static void startTeacherSDK(AuroScholarDataModel auroScholarDataModel) {
        if (auroScholarDataModel != null) {
            String input = auroScholarDataModel.getMobileNumber() + "\n" + auroScholarDataModel.getScholrId() + "\n" + auroScholarDataModel.isEmailVerified() + "\n" +
                    auroScholarDataModel.getRegitrationSource() + "\n" + auroScholarDataModel.getReferralLink();
            AppLogger.e("Auro scholar input data", input);
        }

        if (TextUtil.isEmpty(auroScholarDataModel.getPartnerSource())) {
            auroScholarDataModel.setPartnerSource("");
        }
        if (TextUtil.isEmpty(auroScholarDataModel.getShareIdentity())) {
            auroScholarDataModel.setShareIdentity("");
        }
        if (TextUtil.isEmpty(auroScholarDataModel.getShareType())) {
            auroScholarDataModel.setShareType("");
        }

        if (TextUtil.isEmpty(auroScholarDataModel.getRegitrationSource())) {
            auroScholarDataModel.setRegitrationSource("");
        }
        AuroApp.setAuroModel(auroScholarDataModel);
        auroScholarDataModel.getActivity().startActivity(new Intent(auroScholarDataModel.getActivity(), HomeActivity.class));
    }

    public static void setReferralLink(String referralLink) {
        if (AuroApp.getAuroScholarModel() != null) {
            AuroApp.getAuroScholarModel().setReferralLink(referralLink);
        }
    }


}
