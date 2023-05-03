package com.auro.application.teacher.presentation.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.DetailPassportTeacherLayoutBinding;
import com.auro.application.databinding.NewstudentPassportTeacherLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.teacher.data.model.response.DataListPassportStudentDetailResModel;
import com.auro.application.teacher.data.model.response.QuizDetailPassportStudentDetailResModel;
import com.auro.application.teacher.data.model.response.StudentPassportDetailResModel;
import com.auro.application.teacher.presentation.view.fragment.TeacherStudentPassportDetailFragment;
import com.auro.application.util.TextUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StudentDetailPassportListAdapter extends RecyclerView.Adapter<StudentDetailPassportListAdapter.ClassHolder>{
    List<QuizDetailPassportStudentDetailResModel> mValues;
    Context mContext;
    DetailPassportTeacherLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;
    boolean progressStatus;

    public StudentDetailPassportListAdapter(Context mContext, List<QuizDetailPassportStudentDetailResModel> mValues) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.commonCallBackListner = commonCallBackListner;
    }

    public void setData(List<QuizDetailPassportStudentDetailResModel> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public StudentDetailPassportListAdapter.ClassHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
       // return null;
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.detail_passport_teacher_layout, viewGroup, false);
        return new StudentDetailPassportListAdapter.ClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StudentDetailPassportListAdapter.ClassHolder Vholder, int position) {
        Vholder.setData(mValues, position);
        LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
        Details details = model.getDetails();
        binding.tvScholarship.setText(details.getQuiz_score() != null   ? details.getQuiz_score() : "Quiz Score");
        binding.tvquiz.setText(details.getQuiz_status() != null   ? details.getQuiz_status() : "Quiz Status");
        binding.tvamount.setText(details.getScholarship_ammount() != null   ? details.getScholarship_ammount() : "Scholarship Amount");
        binding.tvstatus.setText(details.getScholarship_status() != null   ? details.getScholarship_status() : "Scholarship Status");






    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ClassHolder extends RecyclerView.ViewHolder {


        DetailPassportTeacherLayoutBinding binding;

        public ClassHolder(@NonNull @NotNull DetailPassportTeacherLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(List<QuizDetailPassportStudentDetailResModel> mValues, int position) {
            QuizDetailPassportStudentDetailResModel totalStudentResModel=mValues.get(position);

            if(!TextUtil.isEmpty(totalStudentResModel.getSubject())) {
                binding.tvStudentName.setText(mValues.get(position).getSubject());
            }
            if(!TextUtil.isEmpty(""+totalStudentResModel.getQuiz_name())) {
                binding.tvStudentScore.setText(mValues.get(position).getQuiz_name());
            }

            if(!TextUtil.isEmpty(totalStudentResModel.getAmount_status())) {
                binding.tvscholarshipvalue.setText(mValues.get(position).getScore());
            }
            if(!TextUtil.isEmpty(totalStudentResModel.getLevel2_remark())) {
                binding.tvquizvalue.setText(mValues.get(position).getLevel2_remark());
            }
            if(!TextUtil.isEmpty(totalStudentResModel.getAmount_status())) {
                binding.tvstatusvalue.setText(mValues.get(position).getAmount_status());
            }
            if(!TextUtil.isEmpty(totalStudentResModel.getScholarship_amount())) {
                binding.tvamountvalue.setText("₹"+mValues.get(position).getScholarship_amount());
            }



        }
    }

}
