package com.auro.application.teacher.presentation.view.adapter;


import static com.auro.application.core.application.AuroApp.context;
import static com.auro.application.core.application.AuroApp.getAppContext;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.auro.application.databinding.MybuddiesTeacherBuddyDesignLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.teacher.data.model.response.AcceptTeacherBuddyDataResModel;
import com.auro.application.teacher.data.model.response.MyBuddyDataResModel;
import com.auro.application.teacher.data.model.response.MyBuddyResModel;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherResModel;
import com.auro.application.teacher.presentation.view.fragment.AcceptTeacherBuddyFragment;
import com.auro.application.teacher.presentation.view.fragment.BookSlotFragment;
import com.auro.application.teacher.presentation.view.fragment.TeacherBuddyFragment;
import com.auro.application.teacher.presentation.view.fragment.TeacherListForInviteFragment;
import com.auro.application.util.RemoteApi;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBuddyListAdapter extends RecyclerView.Adapter<MyBuddyListAdapter.ClassHolder> {
    List<MyBuddyDataResModel> mValues;
    Context mContext;
    MybuddiesTeacherBuddyDesignLayoutBinding binding;
    CommonCallBackListner oncommonCallBackListner;
    AlertDialog alertDialog;
    List<MyBuddyDataResModel> listchilds = new ArrayList<>();
    List<MyBuddyDataResModel> list = new ArrayList<>();
    public MyBuddyListAdapter(Context mContext, List<MyBuddyDataResModel> mValues) {
        this.mValues = mValues;
        this.mContext = mContext;
        //this.oncommonCallBackListner = oncommonCallBackListner;
    }

    @NonNull
    @Override
    public ClassHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.mybuddies_teacher_buddy_design_layout, viewGroup, false);
        return new MyBuddyListAdapter.ClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassHolder Vholder, @SuppressLint("RecyclerView") int position) {
        Vholder.setData(mValues, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public void updateData(List data) {

        this.mValues = data;
        notifyDataSetChanged();
    }
    public class ClassHolder extends RecyclerView.ViewHolder {


        MybuddiesTeacherBuddyDesignLayoutBinding binding;
        PrefModel prefModel;
        Details details;

        public ClassHolder(@NonNull @NotNull MybuddiesTeacherBuddyDesignLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            prefModel = AuroAppPref.INSTANCE.getModelInstance();
            details = prefModel.getLanguageMasterDynamic().getDetails();
        }

        public void setData(List<MyBuddyDataResModel> mValues, int position) {

              binding.txtbuddyname.setText(mValues.get(position).getTeacher_name());

            Glide.with(mContext)
                    .load(mValues.get(position).getTeacher_profile_pic())
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    .placeholder(R.drawable.account_circle)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).circleCrop()
                    .into(binding.studentImage);

               // Glide.with(mContext).load(mValues.get(position).getTeacher_profile_pic()).circleCrop().into(binding.studentImage);
             binding.imgdelete.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                     LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                     final View customLayout
                             = inflater
                             .inflate(
                                     R.layout.teacherbuddy_delete_pop_up,
                                     null);
                     builder.setView(customLayout);
                     builder.setCancelable(false);
                     Button txtcancel = customLayout.findViewById(R.id.buttonCancel);
                     Button txtdelete = customLayout.findViewById(R.id.buttondelete);
                     TextView txtconfirm = customLayout.findViewById(R.id.txtconfirm);
                     TextView txtconfirmdetail = customLayout.findViewById(R.id.txtconfirmdetail);
                     txtconfirmdetail.setText(details.getRemove_buddy() != null   ? details.getRemove_buddy() : "Do you really want to remove buddy\n" +
                             "from My Buddies list ?");
                     txtconfirm.setText(details.getSure_txt() != null   ? details.getSure_txt() : "Are You Sure");
                      txtcancel.setText(details.getBudddy_cancle() != null   ? details.getBudddy_cancle() : "Cancel");
                     txtdelete.setText(details.getYes_remove_buddy() != null   ? details.getYes_remove_buddy() : "Yes,remove");

                     txtcancel.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             alertDialog.dismiss();
                         }
                     });
                     txtdelete.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {

                          deleteInvite(mValues.get(position).getTeacher_id(),mValues.get(position).getTeacher_reffered_id(),position);

                         }
                     });
                     alertDialog = builder.create();
                     alertDialog.show();





                 }
             });


        }
    }

    private void deleteInvite(String userid,String teacherid, int position)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        //String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        map_data.put("user_id",userid);
        map_data.put("teacher_ids", teacherid);  //"576232"
        RemoteApi.Companion.invoke().getDeleteTeacher(map_data)
                .enqueue(new Callback<TeacherInviteTeacherResModel>()
                {
                    @Override
                    public void onResponse(Call<TeacherInviteTeacherResModel> call, Response<TeacherInviteTeacherResModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {

                                Toast.makeText(mContext, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();

                                alertDialog.dismiss();
                               // ((AcceptTeacherBuddyFragment)mContext).getTeacherBuddyList();
                                openFragment(new TeacherBuddyFragment());
                                notifyItemInserted(position);
                                notifyDataSetChanged();
                                MyBuddyListAdapter.this.notify();
                                MyBuddyListAdapter.this.notifyAll();
                                notifyItemChanged(position);
                                updateData(mValues);


                            }
                            else {
                                Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
                                notifyItemInserted(position);
                                notifyDataSetChanged();
                              //  MyBuddyListAdapter.this.notify();
                              //  MyBuddyListAdapter.this.notifyAll();
                                notifyItemChanged(position);
                                updateData(mValues);
                              //  Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            notifyItemInserted(position);
                            notifyDataSetChanged();
                           // MyBuddyListAdapter.this.notify();
                           // MyBuddyListAdapter.this.notifyAll();
                            notifyItemChanged(position);
                            updateData(mValues);
                           // Toast.makeText(mContext, "Internet connection", Toast.LENGTH_SHORT).show();
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


