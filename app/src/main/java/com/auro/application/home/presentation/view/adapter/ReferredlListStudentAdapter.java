package com.auro.application.home.presentation.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.StudentUserLayoutBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.GetAllChildModel;
import com.auro.application.home.data.model.response.GetAllReferChildDetailResModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReferredlListStudentAdapter extends RecyclerView.Adapter<ReferredlListStudentAdapter.ClassHolder> {
    List<GetAllReferChildDetailResModel> mValues;
    Context mContext;
    GetAllChildModel checkUserResModel2;
    CheckUserResModel checkUserResModel;
    StudentUserLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;
    boolean progressStatus;
    String comingFromText = "";

    public ReferredlListStudentAdapter(Context mContext, List<GetAllReferChildDetailResModel> mValues) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.commonCallBackListner = commonCallBackListner;
    }

    @NonNull
    @Override
    public ClassHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.student_user_layout, viewGroup, false);
        return new ReferredlListStudentAdapter.ClassHolder(binding);
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

        StudentUserLayoutBinding binding;
        PrefModel prefModel;
        Details details;

        public ClassHolder(@NonNull @NotNull StudentUserLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            prefModel = AuroAppPref.INSTANCE.getModelInstance();
            details = prefModel.getLanguageMasterDynamic().getDetails();
        }

        public void setData(List<GetAllReferChildDetailResModel> mValues, int position) {



            binding.tvStudentUserName.setVisibility(View.VISIBLE);
            binding.txtchangepin.setVisibility(View.GONE);
            binding.imgnext.setVisibility(View.GONE);
            binding.cardView.setVisibility(View.VISIBLE);
            binding.cardViewButton.setVisibility(View.GONE);
            if (mValues.get(position).getProfile_pic().equals("")||mValues.get(position).getProfile_pic().equals("null")||mValues.get(position).getProfile_pic().isEmpty()){
                Glide.with(mContext)
                        .load(mContext.getResources().getIdentifier("my_drawable_image_name", "drawable",mContext.getPackageName()))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.drawable.account_circle)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .into(binding.studentImage);
            }
            else {

                Glide.with(mContext).load(mValues.get(position).getProfile_pic()).circleCrop().into(binding.studentImage);
            }


            binding.tvStudentUserName.setText("Status"+": " + mValues.get(position).getRequest_status());

            binding.tvStudentName.setText("Name"+": " + mValues.get(position).getStudent_name());
            binding.tvStudentGrade.setText("Mobile No."+": " + mValues.get(position).getMobile_no());



        }
    }










}
