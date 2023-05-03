package com.auro.application.home.presentation.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.FragmentCertificateItemBinding;
import com.auro.application.databinding.LayoutCourselistItemBinding;

import com.auro.application.home.data.model.CourseModule.CourseData;
import com.auro.application.util.AppUtil;

import java.util.ArrayList;
import java.util.List;


public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {
    List<CourseData> mValues;
    Context mContext;
    LayoutCourselistItemBinding binding;
    CommonCallBackListner listner;

    public CourseListAdapter(Context context, List<CourseData> values,CommonCallBackListner listner) {
        mValues = values;
        mContext = context;
        this.listner=listner;

    }

    public void updateList(ArrayList<CourseData> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCourselistItemBinding binding;

        public ViewHolder(LayoutCourselistItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    @Override
    public CourseListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.layout_courselist_item, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, @SuppressLint("RecyclerView") int position) {

        binding.txtstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.commonEventListner(AppUtil.getCommonClickModel(1, Status.COURSE_CLICK, mValues.get(position)));
                }
            }
        });

      binding.headTxt.setText(mValues.get(position).getCourseTitle());
      if (mValues.get(position).getStatus() == 2){
          binding.txtstatus.setText("Completed");
          binding.imgnext.setVisibility(View.GONE);
          binding.relProgress.setVisibility(View.GONE);
          binding.txtpercent.setVisibility(View.GONE);
          binding.txtstatus.setTextColor(Color.parseColor("#00B00D"));
      }
      else if (mValues.get(position).getStatus() == 0){
          binding.txtstatus.setText("Start now");
          binding.imgnext.setVisibility(View.VISIBLE);
          binding.relProgress.setVisibility(View.GONE);
          binding.txtpercent.setVisibility(View.GONE);
          binding.txtstatus.setTextColor(Color.parseColor("#0F92EC"));
      }
      else if (mValues.get(position).getStatus() == 1){
          binding.txtstatus.setText("In progress");
          binding.imgnext.setVisibility(View.GONE);
          binding.relProgress.setVisibility(View.VISIBLE);
          binding.txtpercent.setVisibility(View.VISIBLE);
          binding.txtpercent.setText(mValues.get(position).getProgress()+"%");
          binding.progressbarPredict.setProgress(mValues.get(position).getProgress());
          binding.txtstatus.setTextColor(Color.parseColor("#EA8E12"));
      }
        binding.txttime.setText(String.valueOf(mValues.get(position).getMinutes() + " Min"));
        binding.txtmodule.setText(String.valueOf((mValues.get(position).getModuleCount() + " Module")));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



}
