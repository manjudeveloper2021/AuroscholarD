package com.auro.application.home.presentation.view.adapter.passportadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.PassportQuizDetailLayoutBinding;
import com.auro.application.databinding.PassportQuizLayoutBinding;
import com.auro.application.home.data.model.passportmodels.PassportQuizDetailModel;
import com.auro.application.home.data.model.passportmodels.PassportQuizModel;
import com.auro.application.home.data.model.passportmodels.PassportQuizMonthModel;
import com.auro.application.home.data.model.response.APIcertificate;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.RemoteApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PassportQuizSubDetailAdapter extends RecyclerView.Adapter<PassportQuizSubDetailAdapter.ViewHolder> {

    List<PassportQuizDetailModel> mValues;
    Context mContext;
    PassportQuizDetailLayoutBinding binding;
    CommonCallBackListner listner;

    public PassportQuizSubDetailAdapter(Context context, List<PassportQuizDetailModel> values, CommonCallBackListner listner) {
        mValues = values;
        mContext = context;
        this.listner = listner;
    }

    public void updateList(ArrayList<PassportQuizDetailModel> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        PassportQuizDetailLayoutBinding binding;

        public ViewHolder(PassportQuizDetailLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(APIcertificate resModel, int position) {


        }

    }


    @Override
    public PassportQuizSubDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.passport_quiz_detail_layout, viewGroup, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(ViewHolder Vholder, @SuppressLint("RecyclerView") int position) {

        setQuizAdapter(position);
        if (mValues.get(position).getQuiz_id()==null||mValues.get(position).getQuiz_id()==""||mValues.get(position).getQuiz_id()=="null"||mValues.get(position).getQuiz_id().isEmpty()||mValues.get(position).getQuiz_id().equals(null)){
            binding.downloadcert.setVisibility(View.GONE);
        }
        else{
            binding.downloadcert.setVisibility(View.VISIBLE);
        }
        binding.downloadcert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                downloadPassport(mValues.get(position).getQuiz_id(), mValues.get(position).getSubject_id(),mValues.get(position).getExamMonth(),mValues.get(position).getExamCompelete());
//                if (listner != null) {
//                    listner.commonEventListner(AppUtil.getCommonClickModel(1, Status.DOWNLOAD_CLICK, mValues.get(position)));
//                }
            }
        });
//        Vholder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listner != null) {
//                    listner.commonEventListner(AppUtil.getCommonClickModel(1, Status.ITEM_CLICK, mValues.get(position)));
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void downloadPassport(String topic_id, String subject_id, String EXAM_MONTH, String DynamicDate)
    {
        String suserid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("UserId",suserid);
        map_data.put("CertificateTemplateId","31");
        map_data.put("TopicId",topic_id);
        map_data.put("Month",EXAM_MONTH);
        map_data.put("SubjectId",subject_id);
        map_data.put("DynamicDate",DynamicDate);


        RemoteApi.Companion.invoke().getGeneratedCertificace(map_data)
                .enqueue(new Callback<PassportQuizMonthModel>()
                {
                    @Override
                    public void onResponse(Call<PassportQuizMonthModel> call, Response<PassportQuizMonthModel> response)
                    {
                        try {

                            if (response.code()==400){
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(mContext,message, Toast.LENGTH_SHORT).show();

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if (response.isSuccessful()) {
                                Toast.makeText(mContext, "Download Success. Please go to Certificate", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(mContext, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PassportQuizMonthModel> call, Throwable t)
                    {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();

                        //  Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }


    public void setQuizAdapter(int position) {
        binding.gridRecycler.setLayoutManager(new GridLayoutManager(mContext,2));
        binding.gridRecycler.setHasFixedSize(true);
        binding.gridRecycler.setNestedScrollingEnabled(false);
        PassportQuizDetailAdapter passportSpinnerAdapter = new PassportQuizDetailAdapter(mContext,mValues.get(position).getPassportQuizGridModelList(),null);
        binding.gridRecycler.setAdapter(passportSpinnerAdapter);
    }
}
