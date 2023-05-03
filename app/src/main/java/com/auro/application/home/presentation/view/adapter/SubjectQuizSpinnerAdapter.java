package com.auro.application.home.presentation.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.StateSpinnerItemBinding;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.passportmodels.PassportSubjectQuizMonthModel;
import com.auro.application.util.AppUtil;

import java.util.List;

public class SubjectQuizSpinnerAdapter extends ArrayAdapter {
    StateSpinnerItemBinding binding;
    List<PassportSubjectQuizMonthModel> list;

    LayoutInflater inflter;
    int resource;
    CommonCallBackListner onItemClickState;


    public SubjectQuizSpinnerAdapter(@NonNull Context context, int resource, List<PassportSubjectQuizMonthModel> list) {
        super(context, resource);
        inflter = (LayoutInflater.from(AuroApp.getAppContext().getApplicationContext()));
        this.list = list;


    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        binding.stateTitle.setText(list.get(position).getSubject());
        return  binding.stateTitle;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.state_spinner_item, parent, false);
        binding.stateTitle.setText(list.get(position).getSubject());
        Log.d("AdapterData", "getView: "+list.get(position).getSubject());


        return binding.getRoot();

    }

}
