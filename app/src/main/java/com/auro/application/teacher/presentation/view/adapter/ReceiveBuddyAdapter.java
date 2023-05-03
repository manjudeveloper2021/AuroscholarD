package com.auro.application.teacher.presentation.view.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;

import com.auro.application.databinding.ReceiveBuddyListDesignBinding;
import com.auro.application.databinding.ReceivedTeacherBuddyDesignLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.teacher.data.model.response.ReceiveTeacherBuddyDataResModel;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherResModel;
import com.auro.application.teacher.presentation.view.fragment.InviteTeacherBuddyFragment;
import com.auro.application.teacher.presentation.view.fragment.ReceiveTeacherBuddyFragment;
import com.auro.application.teacher.presentation.view.fragment.TeacherBuddyFragment;
import com.auro.application.util.RemoteApi;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiveBuddyAdapter extends RecyclerView.Adapter<ReceiveBuddyAdapter.ClassHolder> {
    List<ReceiveTeacherBuddyDataResModel> mValues;
    Context mContext;

    ReceiveBuddyListDesignBinding binding;
    CommonCallBackListner oncommonCallBackListner;

    public ReceiveBuddyAdapter(Context mContext, List<ReceiveTeacherBuddyDataResModel> mValues) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.oncommonCallBackListner = oncommonCallBackListner;
    }

    @NonNull
    @Override
    public ClassHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.receive_buddy_list_design, viewGroup, false);
        return new ReceiveBuddyAdapter.ClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassHolder Vholder, @SuppressLint("RecyclerView") int position) {
        Vholder.setData(mValues, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ClassHolder extends RecyclerView.ViewHolder {

        ReceiveBuddyListDesignBinding binding;
        PrefModel prefModel;
        Details details;

        public ClassHolder(@NonNull @NotNull ReceiveBuddyListDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            prefModel = AuroAppPref.INSTANCE.getModelInstance();
            details = prefModel.getLanguageMasterDynamic().getDetails();

        }

        public void setData(List<ReceiveTeacherBuddyDataResModel> mValues, int position) {
              binding.txtusername.setText(mValues.get(position).getTeacher_name());

            Glide.with(mContext)
                    .load(mValues.get(position).getTeacher_profile_pic())
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    .placeholder(R.drawable.account_circle)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).circleCrop()
                    .into(binding.studentImage);

               // Glide.with(mContext).load(mValues.get(position).getTeacher_profile_pic()).circleCrop().into(binding.studentImage);
//          binding.btnaccept.setText(details.getAccept_buddy());
//            binding.btnaccept.setText(details.getReject_buddy());
            binding.btnaccept.setText(details.getAccept() != null   ? details.getAccept() : "Accept");

            binding.btnreject.setText(details.getReject_buddy() != null   ? details.getReject_buddy() : "Reject");

            binding.btnaccept.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String teacherid = mValues.get(position).getTeacher_id();
                   String status = "1";
                   receiveInvite(teacherid, status, position);
               }
           });
            binding.btnreject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String teacherid = mValues.get(position).getTeacher_id();
                    String status = "2";
                    receiveInvite(teacherid, status, position);
                }
            });
        }
    }

    private void receiveInvite(String teacherid, String status, int position)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        map_data.put("sender_id",teacherid);
        map_data.put("receiver_id", userid);
        map_data.put("accepted_status",status);//"576232"
        RemoteApi.Companion.invoke().getApproveInvite(map_data)
                .enqueue(new Callback<TeacherInviteTeacherResModel>()
                {
                    @Override
                    public void onResponse(Call<TeacherInviteTeacherResModel> call, Response<TeacherInviteTeacherResModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {

                                Toast.makeText(mContext, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                openFragment(new TeacherBuddyFragment());

                                notifyDataSetChanged();
//                                ReceiveBuddyAdapter.this.notify();
//                                ReceiveBuddyAdapter.this.notifyAll();
                                notifyItemChanged(position);

                            }
                            else {

                                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(mContext, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TeacherInviteTeacherResModel> call, Throwable t)
                    {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(mContext, fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }
}


