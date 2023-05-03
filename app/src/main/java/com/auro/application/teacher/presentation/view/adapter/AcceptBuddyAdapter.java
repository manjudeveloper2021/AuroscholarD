package com.auro.application.teacher.presentation.view.adapter;


import static com.auro.application.core.application.AuroApp.context;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.AddTeacherBuddyDesignLayoutBinding;
import com.auro.application.databinding.MybuddiesTeacherBuddyDesignLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.KYCResListModel;
import com.auro.application.home.data.model.UpdateParentProfileResModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.teacher.data.model.response.AcceptTeacherBuddyDataResModel;
import com.auro.application.teacher.data.model.response.AcceptTeacherBuddyResModel;
import com.auro.application.teacher.data.model.response.MyBuddyDataResModel;
import com.auro.application.teacher.data.model.response.MyBuddyResModel;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherDataResModel;
import com.auro.application.teacher.data.model.response.TeacherInviteTeacherResModel;
import com.auro.application.util.RemoteApi;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptBuddyAdapter extends RecyclerView.Adapter<AcceptBuddyAdapter.ClassHolder> {
    List<MyBuddyDataResModel> mValues;
    Context mContext;
    MybuddiesTeacherBuddyDesignLayoutBinding binding;
    CommonCallBackListner oncommonCallBackListner;
    AlertDialog alertDialog;
    public AcceptBuddyAdapter(Context mContext, List<MyBuddyDataResModel> mValues) {
        this.mValues = mValues;
        this.mContext = mContext;
        //this.oncommonCallBackListner = oncommonCallBackListner;
    }

    @NonNull
    @Override
    public ClassHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.mybuddies_teacher_buddy_design_layout, viewGroup, false);
        return new AcceptBuddyAdapter.ClassHolder(binding);
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

                Glide.with(mContext).load(mValues.get(position).getTeacher_profile_pic()).circleCrop().into(binding.studentImage);
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
                     txtconfirmdetail.setText(details.getRemove_buddy() != null   ? details.getRemove_buddy() : "Do uou really want to remove from    remove_buddy\n" +
                             "your My Buddies list ?");
                     txtconfirm.setText(details.getSure_txt() != null   ? details.getTeacher_buddy() : "Are You Sure");
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

                          deleteInvite(mValues.get(position).getTeacher_id(),mValues.get(position).getTeacher_reffered_id());

                         }
                     });
                     alertDialog = builder.create();
                     alertDialog.show();





                 }
             });


        }
    }

    private void deleteInvite(String userid,String teacherid)
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
                                notifyDataSetChanged();
                                notify();

                            }
                            else {
                                Toast.makeText(mContext, "Successfully Deleted", Toast.LENGTH_SHORT).show();

                              //  Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
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


