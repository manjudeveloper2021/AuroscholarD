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
import com.auro.application.home.data.model.CourseModule.ModuleData;
import com.auro.application.util.AppUtil;
import java.util.ArrayList;
import java.util.List;

public class ModuleListAdapter extends RecyclerView.Adapter<ModuleListAdapter.ViewHolder> {

    List<ModuleData> mValues;
    Context mContext;
    LayoutCoursemoduleItemBinding binding;
    CommonCallBackListner listner;

    public ModuleListAdapter(Context context, List<ModuleData> values, CommonCallBackListner listner) {
        mValues = values;
        mContext = context;
        this.listner=listner;

    }

    public void updateList(ArrayList<ModuleData> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCoursemoduleItemBinding binding;

        public ViewHolder(LayoutCoursemoduleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    @Override
    public ModuleListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.layout_coursemodule_item, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, @SuppressLint("RecyclerView") int position) {

        binding.txtstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.commonEventListner(AppUtil.getCommonClickModel(1, Status.MODULE_CLICK, mValues.get(position)));
                }
            }
        });




           if (binding.txtno.getText().length() == 2){
               binding.txtno.setText(String.valueOf(position+1));
           }
           else{
                   binding.txtno.setText("0"+String.valueOf(position+1));
               }

      binding.headTxt.setText(mValues.get(position).getModuleTitle());

        binding.txtchapter.setText(String.valueOf(mValues.get(position).getChapterCount() + " Chapter"));
        binding.txttime.setText(String.valueOf((mValues.get(position).getMinutes() + " Min")));
        binding.txttask.setText(String.valueOf((mValues.get(position).getTask() + " Task")));
        if (mValues.get(position).getStatus() == 2){
            binding.txtstatus.setText("Completed");
            binding.txtstatus.setTextColor(Color.parseColor("#22C70F"));
        }
        else if (mValues.get(position).getStatus() == 0){
            binding.txtstatus.setText("Start now");
            binding.txtstatus.setTextColor(Color.parseColor("#0F92EC"));
        }
        else if (mValues.get(position).getStatus() == 1){
            binding.txtstatus.setText("In progress");

            binding.txtstatus.setTextColor(Color.parseColor("#EA8E12"));
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



}
