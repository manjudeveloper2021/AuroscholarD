package com.auro.application.teacher.presentation.view.adapter;


import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.AddTeacherBuddyDesignLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherDataResModel;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherResModel;
import com.auro.application.util.AppUtil;
import com.auro.application.util.RemoteApi;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherInviteTeacherAdapter extends RecyclerView.Adapter<TeacherInviteTeacherAdapter.ClassHolder> {
    List<TeacherInviteTeacherDataResModel> mValues;
    Context mContext;
    AddTeacherBuddyDesignLayoutBinding binding;
    CommonCallBackListner oncommonCallBackListner;
    List<String> list2 = new ArrayList<String>();
    public TeacherInviteTeacherAdapter(Context mContext, List<TeacherInviteTeacherDataResModel> mValues) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.oncommonCallBackListner = oncommonCallBackListner;
    }



    @NonNull
    @Override
    public ClassHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.add_teacher_buddy_design_layout, viewGroup, false);
        return new TeacherInviteTeacherAdapter.ClassHolder(binding);
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

        AddTeacherBuddyDesignLayoutBinding binding;
        PrefModel prefModel;
        Details details;

        public ClassHolder(@NonNull @NotNull AddTeacherBuddyDesignLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            prefModel = AuroAppPref.INSTANCE.getModelInstance();
            details = prefModel.getLanguageMasterDynamic().getDetails();
        }

        public void setData(List<TeacherInviteTeacherDataResModel> mValues, int position) {
              binding.teachername.setText(mValues.get(position).getTeacher_name());

            Glide.with(mContext)
                    .load(mValues.get(position).getTeacher_profile_pic())
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    .placeholder(R.drawable.account_circle)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).circleCrop()
                    .into(binding.studentImage);

               // Glide.with(mContext).load(mValues.get(position).getTeacher_profile_pic()).circleCrop().into(binding.studentImage);
             binding.imgcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @RequiresApi(api = Build.VERSION_CODES.N)
                 @Override
                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                     StringBuilder csvList = new StringBuilder();
                     if(isChecked){
                         String teacheruserid = mValues.get(position).getUser_id();
                         list2.add(teacheruserid);
                         Set<String> listWithoutDuplicates = new LinkedHashSet<String>(list2);
                         list2.clear();
                         list2.addAll(listWithoutDuplicates);
                         for(String s1 : list2){
                             csvList.append(s1);
                             csvList.append(",");
                         }
                         SharedPreferences.Editor editor = mContext.getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                         editor.putString("multiple_teacherid", csvList.toString());
                         editor.apply();
                         //sentInvite(teacheruserid);
//                         if (oncommonCallBackListner != null) {
//                             oncommonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(position, Status.SEND_INVITE_CLICK, mValues.get(position)));
//                         }

                     }
                     else{
                         String teacheruserid = mValues.get(position).getUser_id();
                         list2.remove(teacheruserid);

                         Set<String> listWithoutDuplicates = new LinkedHashSet<String>(list2);
                         list2.clear();
                         list2.addAll(listWithoutDuplicates);
                         for(String s1 : list2){
                             csvList.append(s1);
                             csvList.append(",");
                         }

                         SharedPreferences.Editor editor = mContext.getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                         editor.putString("multiple_teacherid", csvList.toString());
                         editor.apply();
                     }
                 }
             });

     //  notifyDataSetChanged();
        }
    }

    private void sentInvite(String teacherid)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        map_data.put("user_id",userid);
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


