package com.auro.application.teacher.presentation.view.adapter;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.StateSpinnerItemBinding;
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.response.School;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;

import java.util.List;

public class SchoolSpinnerAdapter extends ArrayAdapter {

    StateSpinnerItemBinding binding;
    List<School> list;
    CommonCallBackListner onItemClickState;
    LayoutInflater inflter;

    public SchoolSpinnerAdapter(@NonNull Context context, int resource, List<School> list, CommonCallBackListner onItemClickState) {
        super(context, resource);
        inflter = (LayoutInflater.from(getApplicationContext()));
        this.list = list;
        this.onItemClickState = onItemClickState;
    }

/*
    public DistrictSpinnerUserAdapter(List<DistrictDataModel> list) {
        inflter = (LayoutInflater.from(getApplicationContext()));
        this.list = list;
    }
*/

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
        binding.stateTitle.setText(list.get(position).getSchoolName());

        binding.StateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLogger.v("District","Click Item Step 1"+ list.get(position));
                if (onItemClickState != null) {
                    AppLogger.v("District","Click Item Step 2"+ list.get(position));
                    onItemClickState.commonEventListner(AppUtil.getCommonClickModel(position, Status.SCHOOL_ID, list.get(position)));
                }
            }
        });
        return binding.getRoot();
    }
}
