package com.auro.application.teacher.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.databinding.SendMessageImageLayoutBinding;
import com.auro.application.teacher.data.model.StudentData;
import com.auro.application.teacher.data.model.response.TotalStudentResModel;
import com.auro.application.util.ImageUtil;
import com.auro.application.util.TextUtil;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CreateGroupStudentListAdapter extends RecyclerView.Adapter<CreateGroupStudentListAdapter.ClassHolder>{

    List<StudentData> mValues;
    Context mContext;
    SendMessageImageLayoutBinding binding;
    CommonCallBackListner commonCallBackListner;
    boolean progressStatus;

    public CreateGroupStudentListAdapter(Context mContext, List<StudentData> mValues) {
        this.mValues = mValues;
        this.mContext = mContext;
        this.commonCallBackListner = commonCallBackListner;
    }

    public void setData( List<StudentData> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public CreateGroupStudentListAdapter.ClassHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
       // return null;
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.send_message_image_layout, viewGroup, false);
        return new CreateGroupStudentListAdapter.ClassHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CreateGroupStudentListAdapter.ClassHolder Vholder, int position) {
        Vholder.setData(mValues, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ClassHolder extends RecyclerView.ViewHolder {


        SendMessageImageLayoutBinding binding;

        public ClassHolder(@NonNull @NotNull SendMessageImageLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(List<StudentData> mValues, int position) {
            StudentData totalStudentResModel=mValues.get(position);

            if(!TextUtil.isEmpty(totalStudentResModel.getStudent_name())) {
                binding.tvStudentName.setText(mValues.get(position).getStudent_name());
            }
            if(!TextUtil.isEmpty(""+totalStudentResModel.getTotal_score())) {
                if (mValues.get(position).getTotal_score() == null || mValues.get(position).getTotal_score().equals("null") || mValues.get(position).getTotal_score().equals(null)){
                    binding.tvStudentScore.setText("Score: 0");
                }
                else{
                    if(!TextUtil.isEmpty(""+totalStudentResModel.getTotal_score())) {
                        binding.tvStudentScore.setText("Score: "+mValues.get(position).getTotal_score());
                    }
                }

            }
            if(!TextUtil.isEmpty(totalStudentResModel.getProfile_pic())) {
                Glide.with(mContext).load(mValues.get(position).getProfile_pic()).circleCrop().into(binding.studentImage);

             //   ImageUtil.loadCircleImage(binding.studentImage,mValues.get(position).getProfile_pic());
            }
        }
    }
}
