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
import com.auro.application.databinding.LayoutModulechapterlistItemBinding;
import com.auro.application.databinding.LayoutModulecourselitItemBinding;
import com.auro.application.home.data.model.CourseModule.ModuleChapterData;
import com.auro.application.home.data.model.CourseModule.ModuleTaskData;
import com.auro.application.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class ModuleChapterListAdapter extends RecyclerView.Adapter<ModuleChapterListAdapter.ViewHolder> {

    List<ModuleChapterData> mValues;
    Context mContext;
    LayoutModulechapterlistItemBinding binding;
    CommonCallBackListner listner;

    public ModuleChapterListAdapter(Context context, List<ModuleChapterData> values, CommonCallBackListner listner) {
        mValues = values;
        mContext = context;
        this.listner=listner;
    }

    public void updateList(ArrayList<ModuleChapterData> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutModulechapterlistItemBinding binding;

        public ViewHolder(LayoutModulechapterlistItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    @Override
    public ModuleChapterListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.layout_modulechapterlist_item, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, @SuppressLint("RecyclerView") int position) {

        binding.txtchapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.commonEventListner(AppUtil.getCommonClickModel(1, Status.CHAPTER_CLICK, mValues.get(position)));
                }
            }
        });
        int nposition = position+1;
        binding.headTxt.setText("Chapter "+nposition);
        binding.headStatus.setText(mValues.get(position).getMinutes() + " Min");
        binding.headStatus.setTextColor(Color.parseColor("#929191"));
        binding.txtchapter.setText(String.valueOf(mValues.get(position).getChapterTitle()));
        if (mValues.get(position).getType() == 1){
            binding.imgDoc.setVisibility(View.VISIBLE);
            binding.imgVideo.setVisibility(View.GONE);
        }
        else if (mValues.get(position).getType() == 0){
            binding.imgDoc.setVisibility(View.GONE);
            binding.imgVideo.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



}
