package com.auro.application.teacher.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.StateSpinnerItemBinding;
import com.auro.application.home.data.model.passportmodels.PassportSubjectQuizMonthModel;
import com.auro.application.teacher.data.model.response.PassportMonthResModel;
import com.auro.application.util.AppUtil;

import java.util.List;

public class PassportSubjectSpinnerAdapter extends ArrayAdapter {
    StateSpinnerItemBinding binding;
    List<PassportSubjectQuizMonthModel> list;

    LayoutInflater inflter;
    int resource;
    CommonCallBackListner onItemClickState;


    public PassportSubjectSpinnerAdapter(@NonNull Context context, int resource, List<PassportSubjectQuizMonthModel> list, CommonCallBackListner onItemClickState) {
        super(context, resource);
        inflter = (LayoutInflater.from(AuroApp.getAppContext().getApplicationContext()));
        this.list = list;
        this.onItemClickState = onItemClickState;

    }

/*    public StateSpinnerUserAdapter(List<StateDataModel> list) {
        super(c);

    }*/

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
    public View getView(int position, View convertView, ViewGroup parent) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.state_spinner_item, parent, false);
        binding.stateTitle.setText(list.get(position).getSubject());


        binding.StateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickState != null) {
                    onItemClickState.commonEventListner(AppUtil.getCommonClickModel(position, Status.SUBJECT, list.get(position)));
                }
               // onItemClickState.onItemClick(list.get(position ).getState_name());
                // onItemClickState.onItemClick(list.get(position ).getState_name());
            }
        });
        return binding.getRoot();

    }

}
