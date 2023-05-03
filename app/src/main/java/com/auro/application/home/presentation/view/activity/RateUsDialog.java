package com.auro.application.home.presentation.view.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.auro.application.R;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.home.data.model.AddRatingModel;
import com.auro.application.home.presentation.view.fragment.MainQuizHomeFragment;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.ViewUtil;
import com.github.dhaval2404.imagepicker.util.IntentUtils;

import java.util.HashMap;
import java.util.concurrent.CountedCompleter;

public class RateUsDialog extends Dialog {
    private static final String TAG = "RateUsDialog";
    Context mcontext;
    Button rateNowBtn;
    TextView lateBtn;
    public RateUsDialog(MainQuizHomeFragment mainQuizHomeFragment) {
        super(mainQuizHomeFragment.getContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rate_us_dialog_layout);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rateNowBtn = findViewById(R.id.rateNowBtn);
        lateBtn=findViewById(R.id.lateBtn);
        lateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rateNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddRating();
            }

        });



    }
        private void getAddRating() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String suserid =  AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("id",suserid);
        map_data.put("status","1");

        RemoteApi.Companion.invoke().getAddRating(map_data)
                .enqueue(new Callback<AddRatingModel>()
                {

                    @Override
                    public void onResponse(Call<AddRatingModel> call, Response<AddRatingModel> response)
                    {
                        if (response.isSuccessful()) {
                              String getpopup = String.valueOf(response.body().getStatus());
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.auro.application")));

                               Toast.makeText(getContext(), getpopup, Toast.LENGTH_SHORT).show();
                           // openPlayStore();
                        }

                        else
                        {

                            Log.d(TAG, "onResponser: "+response.message().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<AddRatingModel> call, Throwable t)
                    {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }
    private void openPlayStore() {

       startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.auro.application")));

//        try {
//            Intent viewIntent =
//                    new Intent("android.intent.action.VIEW",
//                            Uri.parse("https://play.google.com/store/apps/details?id=com.auro.application"));
//            startActivity(viewIntent);
//        }catch(Exception e) {
//            Toast.makeText(getContext(),"Unable to Connect Try Again...",
//                    Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//        }

    }

    private void startActivity(Intent intent) {
    }

}