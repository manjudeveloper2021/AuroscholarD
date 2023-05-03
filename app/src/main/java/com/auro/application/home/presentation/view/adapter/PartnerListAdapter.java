package com.auro.application.home.presentation.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.FragmentCertificateItemBinding;
import com.auro.application.databinding.LanguageItemLayoutBinding;
import com.auro.application.databinding.PartnerlistDesignLayoutBinding;
import com.auro.application.home.data.model.PartnerDetailModel;
import com.auro.application.home.data.model.SelectLanguageModel;
import com.auro.application.home.data.model.response.APIcertificate;
import com.auro.application.home.presentation.view.activity.PartnerListingActivity;
import com.auro.application.home.presentation.view.activity.PartnerWebviewActivity;
import com.auro.application.util.AppUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class PartnerListAdapter extends RecyclerView.Adapter<PartnerListAdapter.ViewHolder> {

    List<PartnerDetailModel> list;
    PartnerlistDesignLayoutBinding binding;
    Context mContext;
    CommonCallBackListner commonCallBackListner;


    public PartnerListAdapter(Context context, List<PartnerDetailModel> values) {
        list = values;
        mContext = context;
    }
    public void updateList(ArrayList<PartnerDetailModel> values) {
        list = values;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PartnerlistDesignLayoutBinding binding;

        public ViewHolder(PartnerlistDesignLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }



    }
    @Override
    public PartnerListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.partnerlist_design_layout, viewGroup, false);
        return new PartnerListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
binding.partnerTitle.setText(list.get(position).getPartner_name());
        Glide.with(mContext)
                .load(list.get(position).getPartner_logo())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.teacher_select)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).circleCrop()
                .into(binding.partnerlogo);


        binding.partnerlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent(mContext, PartnerWebviewActivity.class);
                sendIntent.putExtra("partnerweburl", list.get(position).getPartner_url());
                sendIntent.putExtra("partnerid", list.get(position).getId());
                sendIntent.putExtra("partnername", list.get(position).getPartner_name());
                sendIntent.putExtra("partnerlogo", list.get(position).getPartner_logo());
                mContext.startActivity(sendIntent);

                notifyDataSetChanged();
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

}
