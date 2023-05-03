package com.auro.application.home.presentation.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.databinding.FragmentFriendListDialogBinding;
import com.auro.application.home.data.model.AcceptInviteRequest;
import com.auro.application.home.data.model.FriendRequestList;
import com.auro.application.home.presentation.view.adapter.FriendRequestListAdapter;
import com.auro.application.home.presentation.viewmodel.FriendsLeaderShipViewModel;
import com.auro.application.util.ViewUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

import static com.auro.application.core.common.Status.ACCEPT_INVITE_REQUEST;
import static com.auro.application.core.common.Status.FRIENDS_REQUEST_LIST;

public class FriendRequestListDialogFragment extends BottomSheetDialogFragment implements CommonCallBackListner {

    @Inject
    @Named("FriendRequestListDialogFragment")
    ViewModelFactory viewModelFactory;

    FriendRequestList friendRequestList;
    FriendRequestListAdapter friendRequestListAdapter;

    FragmentFriendListDialogBinding binding;



    FriendsLeaderShipViewModel viewModel;

    FriendsLeaderBoardFragment friendsLeaderBoardFragment;

    public FriendRequestListDialogFragment(FriendsLeaderBoardFragment friendsLeaderBoardFragment, FriendRequestList friendRequestList) {
        this.friendRequestList = friendRequestList;
        this.friendsLeaderBoardFragment = friendsLeaderBoardFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setLanguageonUi(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendsLeaderShipViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        return binding.getRoot();
    }

    public int getLayout() {
        return R.layout.fragment_friend_list_dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.listRequest.setLayoutManager(new LinearLayoutManager(AuroApp.getAppContext()));
        binding.listRequest.setHasFixedSize(true);

        friendRequestListAdapter = new FriendRequestListAdapter(getActivity(), friendRequestList.getFriends(), friendRequestList.getFriends().size(), this);
        binding.listRequest.setAdapter(friendRequestListAdapter);
        setListener();
    }

    protected void setListener() {


        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);

        } else {
            observeServiceResponse();
        }


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    //For ProgressBar
                    if (responseApi.apiTypeStatus == ACCEPT_INVITE_REQUEST) {
                        handleProgress(0, "");
                    }

                    break;

                case SUCCESS:
                    if (responseApi.apiTypeStatus == ACCEPT_INVITE_REQUEST) {
                        AcceptInviteRequest acceptInviteRequest = (AcceptInviteRequest) responseApi.data;

                        if (acceptInviteRequest.isError()) {
                            handleProgress(1, acceptInviteRequest.getMessage());
                        } else if (acceptInviteRequest.getStatus().equalsIgnoreCase("success")) {
                            handleProgress(1, "Friend request accepted.");
                            if (friendsLeaderBoardFragment != null) {
                                friendsLeaderBoardFragment.loadData();
                                //firebasefunnel
                            }
                        }

                    }
                    break;

                case NO_INTERNET:
                case FAIL_400:
                    if (responseApi.apiTypeStatus == FRIENDS_REQUEST_LIST) {
                        handleProgress(3, (String) responseApi.data);
                    }
                    showSnackbarError((String) responseApi.data);
                    break;

                default:
                    if (responseApi.apiTypeStatus == FRIENDS_REQUEST_LIST) {
                        handleProgress(3, (String) responseApi.data);
                    }
                    showSnackbarError((String) responseApi.data);
                    break;
            }

        });
    }

    private void handleProgress(int i, String msg) {
        switch (i) {
            case 0:

                break;

            case 1:
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                dismiss();
                break;

            case 2:
                showSnackbarError(msg);
                break;

            case 3:

                break;

        }

    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case REQUEST_ACCEPT:
                int requestId = commonDataModel.getSource();
                int regId = (Integer) commonDataModel.getObject();

                viewModel.friendAcceptData(requestId, "Accepted");

                break;

            case REQUEST_DECLINE:
                requestId = commonDataModel.getSource();
                regId = (Integer) commonDataModel.getObject();
                viewModel.friendAcceptData(requestId, "Decline");
                break;

        }
    }

}
