package com.auro.application.home.presentation.view.adapter.passportadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.PassportItemLayoutBinding;
import com.auro.application.databinding.PassportQuizLayoutBinding;
import com.auro.application.home.data.model.passportmodels.PassportQuizModel;
import com.auro.application.home.data.model.passportmodels.PassportSubjectModel;
import com.auro.application.home.data.model.response.APIcertificate;
import com.auro.application.util.AppUtil;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;
import java.util.List;


public class PassportQuizAdapter extends RecyclerView.Adapter<PassportQuizAdapter.ViewHolder> {

    List<PassportQuizModel> mValues;
    Context mContext;
    PassportQuizLayoutBinding binding;
    CommonCallBackListner listner;

    public PassportQuizAdapter(Context context, List<PassportQuizModel> values, CommonCallBackListner listner) {
        mValues = values;
        mContext = context;
        this.listner = listner;
    }

    public void updateList(ArrayList<PassportQuizModel> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        PassportQuizLayoutBinding binding;

        public ViewHolder(PassportQuizLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(APIcertificate resModel, int position) {

        }

    }

    @Override
    public PassportQuizAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.passport_quiz_layout, viewGroup, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(ViewHolder Vholder, @SuppressLint("RecyclerView") int position) {

        binding.topicName.setText(mValues.get(position).getTopicName());

        int pos = position + 1;
        String num = "" + pos;
        if (pos < 10) {
            num = "0" + pos;
        }
        binding.topicNumber.setText(num);
        setQuizAdapter(position);
        Vholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.commonEventListner(AppUtil.getCommonClickModel(1, Status.ITEM_CLICK, mValues.get(position)));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void updateData() {

    }


    public void setQuizAdapter(int position) {

        binding.subjectList.setLayoutManager(new LinearLayoutManager(mContext));
        binding.subjectList.setHasFixedSize(true);
        binding.subjectList.setNestedScrollingEnabled(false);
        PassportQuizSubDetailAdapter passportSpinnerAdapter = new PassportQuizSubDetailAdapter(mContext, mValues.get(position).getQuizDetail(), null);
        binding.subjectList.setAdapter(passportSpinnerAdapter);
    }
}
