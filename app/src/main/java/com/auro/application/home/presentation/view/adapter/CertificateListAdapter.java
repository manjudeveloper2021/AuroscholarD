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
import com.auro.application.databinding.LayoutCertificateitemDesignBinding;
import com.auro.application.databinding.LayoutCourselistItemBinding;
import com.auro.application.home.data.model.CourseModule.CertificateData;
import com.auro.application.home.data.model.CourseModule.CourseData;
import com.auro.application.util.AppUtil;

import java.util.ArrayList;
import java.util.List;


public class CertificateListAdapter extends RecyclerView.Adapter<CertificateListAdapter.ViewHolder> {

    List<CertificateData> mValues;
    Context mContext;
    LayoutCertificateitemDesignBinding binding;
    CommonCallBackListner listner;

    public CertificateListAdapter(Context context, List<CertificateData> values, CommonCallBackListner listner) {
        mValues = values;
        mContext = context;
        this.listner=listner;

    }

    public void updateList(ArrayList<CertificateData> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCertificateitemDesignBinding binding;

        public ViewHolder(LayoutCertificateitemDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    @Override
    public CertificateListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.layout_certificateitem_design, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, @SuppressLint("RecyclerView") int position) {

        binding.txtstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.commonEventListner(AppUtil.getCommonClickModel(1, Status.VIEW_CLICK, mValues.get(position)));
                }
            }
        });
        binding.txtshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.commonEventListner(AppUtil.getCommonClickModel(1, Status.DOCUMENT_CLICK, mValues.get(position)));
                }
            }
        });
        binding.txtdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.commonEventListner(AppUtil.getCommonClickModel(1, Status.DOWNLOAD_CLICK, mValues.get(position)));
                }
            }
        });

      binding.headTxt.setText(mValues.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



}
