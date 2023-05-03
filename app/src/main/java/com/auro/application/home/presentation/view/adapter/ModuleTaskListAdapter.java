package com.auro.application.home.presentation.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.LayoutCoursemoduleItemBinding;
import com.auro.application.databinding.LayoutModulecourselitItemBinding;
import com.auro.application.home.data.model.CourseModule.ModuleData;
import com.auro.application.home.data.model.CourseModule.ModuleTaskData;
import com.auro.application.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class ModuleTaskListAdapter extends RecyclerView.Adapter<ModuleTaskListAdapter.ViewHolder> {

    List<ModuleTaskData> mValues;
    Context mContext;
    LayoutModulecourselitItemBinding binding;
    CommonCallBackListner listner;

    public ModuleTaskListAdapter(Context context, List<ModuleTaskData> values, CommonCallBackListner listner) {
        mValues = values;
        mContext = context;
        this.listner=listner;

    }

    public void updateList(ArrayList<ModuleTaskData> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutModulecourselitItemBinding binding;

        public ViewHolder(LayoutModulecourselitItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    @Override
    public ModuleTaskListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.layout_modulecourselit_item, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, @SuppressLint("RecyclerView") int position) {

        binding.txtchapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.commonEventListner(AppUtil.getCommonClickModel(1, Status.TASK_CLICK, mValues.get(position)));
                }
            }
        });
        int nposition = position+1;
        binding.headTxt.setText("Task "+nposition);
        if (mValues.get(position).getStatus() == 2){
            binding.imgTask.setVisibility(View.VISIBLE);
            binding.imgLock.setVisibility(View.GONE);
            binding.imgStart.setVisibility(View.GONE);
            binding.headStatus.setText("Completed");
            binding.headStatus.setTextColor(Color.parseColor("#22C70F"));
        }
       else if (mValues.get(position).getStatus() == 1){
            binding.imgTask.setVisibility(View.GONE);
            binding.imgLock.setVisibility(View.GONE);
            binding.imgStart.setVisibility(View.VISIBLE);
            binding.headStatus.setText("Inprocess");
            binding.headStatus.setTextColor(Color.parseColor("#EAB60F"));
        }
        else if (mValues.get(position).getStatus() == 0){
            binding.imgTask.setVisibility(View.GONE);
            binding.imgLock.setVisibility(View.GONE);
            binding.imgStart.setVisibility(View.VISIBLE);
            binding.headStatus.setText("Start now");
            binding.headStatus.setTextColor(Color.parseColor("#0F92EC"));
        }

        binding.txtchapter.setText(String.valueOf(mValues.get(position).getName()));



    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



}
