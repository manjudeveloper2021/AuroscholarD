package com.auro.application.teacher.presentation.view.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.MybuddiesTeacherBuddyDesignLayoutBinding;
import com.auro.application.databinding.SentTeacherBuddyDesignLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.teacher.data.model.response.AcceptTeacherBuddyDataResModel;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherResModel;
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

public class SentBuddyAdapter extends RecyclerView.Adapter<SentBuddyAdapter.ClassHolder> {
    List<AcceptTeacherBuddyDataResModel> mValues;
    Context mContext;
    SentTeacherBuddyDesignLayoutBinding binding;
    CommonCallBackListner oncommonCallBackListner;

    public SentBuddyAdapter(Context mContext, List<AcceptTeacherBuddyDataResModel> mValues) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.oncommonCallBackListner = oncommonCallBackListner;
    }

    @NonNull
    @Override
    public ClassHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.sent_teacher_buddy_design_layout, viewGroup, false);
        return new SentBuddyAdapter.ClassHolder(binding);
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

        SentTeacherBuddyDesignLayoutBinding binding;
        PrefModel prefModel;
        Details details;

        public ClassHolder(@NonNull @NotNull SentTeacherBuddyDesignLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            prefModel = AuroAppPref.INSTANCE.getModelInstance();
            details = prefModel.getLanguageMasterDynamic().getDetails();
        }

        public void setData(List<AcceptTeacherBuddyDataResModel> mValues, int position) {
              binding.txtname.setText(mValues.get(position).getTeacher_name());
             binding.txtstatus.setText(mValues.get(position).getStatus());
             if (mValues.get(position).getStatus().equals("Sent")){
                 binding.txtstatus.setText(details.getSent() != null   ? details.getRefresh() : "Sent");

             }
            else if (mValues.get(position).getStatus().equals("Accepted")){
                binding.txtstatus.setText(details.getAccept() != null   ? details.getAccept() : "Accepted");

            }
             else if (mValues.get(position).getStatus().equals("Rejected")||mValues.get(position).getStatus().equals("Reject")){
                 binding.txtstatus.setText(details.getReject_buddy() != null   ? details.getReject_buddy() : "Rejected");

             }

            Glide.with(mContext)
                    .load(mValues.get(position).getTeacher_profile_pic())
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    .placeholder(R.drawable.account_circle)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).circleCrop()
                    .into(binding.studentImage);

                //Glide.with(mContext).load(mValues.get(position).getTeacher_profile_pic()).circleCrop().into(binding.studentImage);
        }
    }

    private void sentInvite(String teacherid)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        map_data.put("user_id","576232");
        map_data.put("teacher_ids", teacherid);  //"576232"
        RemoteApi.Companion.invoke().getSendInvite(map_data)
                .enqueue(new Callback<TeacherInviteTeacherResModel>()
                {
                    @Override
                    public void onResponse(Call<TeacherInviteTeacherResModel> call, Response<TeacherInviteTeacherResModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {

                                Toast.makeText(mContext, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();



                            }
                            else {

                                Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
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
}


