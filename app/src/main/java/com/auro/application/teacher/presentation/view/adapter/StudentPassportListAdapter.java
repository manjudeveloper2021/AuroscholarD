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
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.NewstudentPassportTeacherLayoutBinding;
import com.auro.application.databinding.SendMessageImageLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.presentation.view.fragment.GradeChangeFragment;
import com.auro.application.home.presentation.view.fragment.StudentProfileFragment;
import com.auro.application.teacher.data.model.response.StudentPassportDetailResModel;
import com.auro.application.teacher.data.model.response.TotalStudentResModel;
import com.auro.application.teacher.presentation.view.fragment.TeacherStudentPassportDetailFragment;
import com.auro.application.util.ImageUtil;
import com.auro.application.util.TextUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StudentPassportListAdapter extends RecyclerView.Adapter<StudentPassportListAdapter.ClassHolder>{
    List<StudentPassportDetailResModel> mValues;
    Context mContext;
    NewstudentPassportTeacherLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;
    boolean progressStatus;
    Details details;
    public StudentPassportListAdapter(Context mContext, List<StudentPassportDetailResModel> mValues) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.commonCallBackListner = commonCallBackListner;
    }

    public void setData(List<StudentPassportDetailResModel> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public StudentPassportListAdapter.ClassHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
       // return null;
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.newstudent_passport_teacher_layout, viewGroup, false);
        return new StudentPassportListAdapter.ClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StudentPassportListAdapter.ClassHolder Vholder, int position) {
        Vholder.setData(mValues, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ClassHolder extends RecyclerView.ViewHolder {


        NewstudentPassportTeacherLayoutBinding binding;

        public ClassHolder(@NonNull @NotNull NewstudentPassportTeacherLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(List<StudentPassportDetailResModel> mValues, int position) {
            StudentPassportDetailResModel totalStudentResModel=mValues.get(position);


            details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
            if(!TextUtil.isEmpty(totalStudentResModel.getStudentName())) {
                binding.tvStudentName.setText(mValues.get(position).getStudentName());
            }
            if(!TextUtil.isEmpty(""+totalStudentResModel.getKyc_status())) {
                if (totalStudentResModel.getKyc_status()== "null"||totalStudentResModel.getKyc_status()== null||mValues.get(position).getKyc_status()=="null"||mValues.get(position).getKyc_status()==null){
                    binding.tvStudentScore.setText("KYC Status : ");
                }
                else{
                    binding.tvStudentScore.setText("KYC Status : "+mValues.get(position).getKyc_status());
                }

            }
            else{
                binding.tvStudentScore.setText("KYC Status : ");
            }
           // if(!TextUtil.isEmpty(totalStudentResModel.getProfilePic())) {
                Glide.with(binding.studentImage.getContext()).load(mValues.get(position).getProfilePic())
                        .apply(RequestOptions.placeholderOf(R.drawable.account_circle)
                                .error(R.drawable.account_circle)
                                .circleCrop()
                                .dontAnimate()
                                .priority(Priority.IMMEDIATE)
                                .diskCacheStrategy(DiskCacheStrategy.NONE))
                        .into(binding.studentImage);
           // }
//            else{
//                Glide.with(mContext)
//                        .load(mContext.getResources().getIdentifier("my_drawable_image_name", "drawable",mContext.getPackageName()))
//                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                        .placeholder(R.drawable.account_circle)
//                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                        .into(binding.studentImage);
//            }
            if(!TextUtil.isEmpty(totalStudentResModel.getDisbursed())) {
                binding.tvScholarship.setText("₹"+mValues.get(position).getDisbursed());
            }
            if(!TextUtil.isEmpty(totalStudentResModel.getQuizCount())) {
                binding.tvQuiz.setText(mValues.get(position).getQuizCount());
            }
            if(!TextUtil.isEmpty(totalStudentResModel.getGrade())) {


                binding.tvGrade.setText(details.getGradeStudent()+ ":" +mValues.get(position).getGrade());
            }
            binding.imgnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("studentuserid", mValues.get(position).getStudentId());
                    bundle.putString("studentdisbursalamount", mValues.get(position).getDisbursed());
                    bundle.putString("studentkycstatus", mValues.get(position).getKyc_status());
                    bundle.putString("studentprofilepic", mValues.get(position).getProfilePic());
                    bundle.putString("studentname", mValues.get(position).getStudentName());
                    bundle.putString("studentquizattempt", mValues.get(position).getQuizCount());
                    TeacherStudentPassportDetailFragment gradeChangeFragment = new TeacherStudentPassportDetailFragment();
                    gradeChangeFragment.setArguments(bundle);
                   // openFragment(gradeChangeFragment);
                    openFragment(gradeChangeFragment);
                }
            });

        }
    }
//    private void openFragment(Fragment fragment) {
//        ((AppCompatActivity) (mContext)).getSupportFragmentManager()
//                .beginTransaction()
//                .setReorderingAllowed(true)
//                .replace(AuroApp.getFragmentContainerUiId(), fragment, TeacherStudentPassportDetailFragment.class
//                        .getSimpleName())
//                .addToBackStack(null)
//                .commitAllowingStateLoss();
//    }

    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(mContext, fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }
}
