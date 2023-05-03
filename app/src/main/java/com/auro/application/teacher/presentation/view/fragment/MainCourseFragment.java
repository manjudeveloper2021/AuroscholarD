package com.auro.application.teacher.presentation.view.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;


import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.FragmentCourseMainBinding;;
import com.auro.application.home.data.model.CourseModule.CertificateData;
import com.auro.application.home.data.model.CourseModule.CertificateList;
import com.auro.application.home.data.model.CourseModule.CourseData;
import com.auro.application.home.data.model.CourseModule.CourseList;
import com.auro.application.home.data.model.CourseModule.RegisterData;
import com.auro.application.home.data.model.SelectLanguageModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;

import com.auro.application.home.presentation.view.adapter.CertificateListAdapter;
import com.auro.application.home.presentation.view.adapter.CourseListAdapter;


import com.auro.application.util.CourseRemoteApi;

import com.auro.application.util.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainCourseFragment extends BaseFragment implements CommonCallBackListner
{
    public static final String TAG = "MainCourseFragment";
    FragmentCourseMainBinding binding;
    List<CourseData> coursedata = new ArrayList<>();
    List<CourseData> coursedatalist = new ArrayList<>();
    List<CertificateData> certificateData = new ArrayList<>();
    List<CertificateData> certificateDataList = new ArrayList<>();
    public MainCourseFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
            binding.setLifecycleOwner(this);
        }
        setRetainInstance(true);
        ViewUtil.setLanguageonUi(getActivity());
        binding.recyclerModule.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerModule.setHasFixedSize(true);
        binding.recyclerCertificate.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerCertificate.setHasFixedSize(true);
        getLMSRegister();


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        setToolbar();
        setListener();


    }

    @Override
    protected void init() {
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.FAQ_FRAGMENT);
        setListener();
    }

    @Override
    protected void setToolbar() {
        /*Do cod ehere*/
    }


    @Override
    protected void setListener() {
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.PRIVACY_POLICY_FRAGMENT);

        binding.imgnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new TeacherMoreDetailFragment());
            }
        });





    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_course_main;
    }






    @Override
    public void onResume() {
        super.onResume();
        DashBoardMainActivity.setListingActiveFragment(DashBoardMainActivity.PRIVACY_POLICY_FRAGMENT);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

     private void openAdapter(List<CourseData> coursedatalist){
         CourseListAdapter kyCuploadAdapter = new CourseListAdapter(getActivity(), coursedatalist, this);
         binding.recyclerModule.setAdapter(kyCuploadAdapter);
     }
    private void openCertificateAdapter(List<CertificateData> coursedatalist){
        CertificateListAdapter kyCuploadAdapter = new CertificateListAdapter(getActivity(), coursedatalist, this);
        binding.recyclerCertificate.setAdapter(kyCuploadAdapter);
    }

    private void getLMSRegister()
    {
        String userid = AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId();
        SelectLanguageModel selectLanguageModel = new SelectLanguageModel();

        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("partnerSource","Auroscholar");
        map_data.put("partnerUserID",userid);
        map_data.put("languageName","english");
        CourseRemoteApi.Companion.invoke().getRegister(map_data)
                .enqueue(new Callback<RegisterData>()
                {
                    @Override
                    public void onResponse(Call<RegisterData> call, Response<RegisterData> response)
                    {
                        try {
                            if (response.code()==400){
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();

                                }

                                catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            else if (response.isSuccessful()) {

                                String lmsuserid = String.valueOf(response.body().getResponse().get(0).getLmsUserID());

                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                editor.putString("lmsuserid", lmsuserid);
                                editor.apply();
                                getCourseList(lmsuserid);
                                getCertificateList(lmsuserid);


                                }




                            else {
                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterData> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }

    private void getCourseList(String lmsuserid)
    {

        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("userID",lmsuserid);
        map_data.put("lmsPartnerID","11");
        CourseRemoteApi.Companion.invoke().getActiveCourseList(map_data)
                .enqueue(new Callback<CourseList>()
                {
                    @Override
                    public void onResponse(Call<CourseList> call, Response<CourseList> response)
                    {
                        try {
                            if (response.code()==400){
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();

                                }

                                catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }



                            }

                            else if (response.isSuccessful()) {

                                if (response.body().getStatus().equals("Success")){
                                    coursedata.clear();
                                    coursedatalist.clear();
                                    if (!(response.body().getResult() == null || response.body().getResult().isEmpty() || response.body().getResult().equals("") || response.body().getResult().equals("null"))) {

                                        coursedata = response.body().getResult();
                                        for (int i = 0; i < coursedata.size(); i++) {
                                            coursedatalist.add(coursedata.get(i));
                                        }
                                        binding.recyclerModule.setVisibility(View.VISIBLE);
                                        openAdapter(coursedatalist);

                                    }
                                    else{
                                     binding.recyclerModule.setVisibility(View.GONE);
                                    }
                                }

                            }
                            else {
                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CourseList> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }
    private void getCertificateList(String lmsuserid)
    {

        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("lmsPartnerID","11");
        map_data.put("userID",lmsuserid);
        CourseRemoteApi.Companion.invoke().getCertificateList(map_data)
                .enqueue(new Callback<CertificateList>()
                {
                    @Override
                    public void onResponse(Call<CertificateList> call, Response<CertificateList> response)
                    {
                        try {
                            if (response.code()==400){
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();

                                }

                                catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            else if (response.isSuccessful()) {

                              
                                    certificateData.clear();
                                    certificateDataList.clear();
                                    if (!(response.body().getResult() == null || response.body().getResult().isEmpty() || response.body().getResult().equals("") || response.body().getResult().equals("null"))) {

                                        certificateData = response.body().getResult();
                                        for (int i = 0; i < certificateData.size(); i++) {
                                            if (!(response.body().getResult().get(i).getCertificatePath() == null || response.body().getResult().get(i).getCertificatePath().isEmpty() || response.body().getResult().get(i).getCertificatePath().equals("") || response.body().getResult().get(i).getCertificatePath().equals("null"))){
                                                certificateDataList.add(certificateData.get(i));
                                            }

                                        }

                                        binding.recyclerCertificate.setVisibility(View.VISIBLE);
                                        binding.txtStatus.setVisibility(View.GONE);
                                        openCertificateAdapter(certificateDataList);

                                    }
                                    else{
                                        binding.recyclerCertificate.setVisibility(View.GONE);
                                        binding.txtStatus.setVisibility(View.VISIBLE);
                                    }


                            }
                            else {
                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CertificateList> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case COURSE_CLICK:
                CourseData crsmodel = (CourseData) commonDataModel.getObject();
                openFragment(new TeacherModulePagerFragment());
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                editor.putString("lms_courseid", crsmodel.getCCCourseID());
                editor.apply();

                break;
            case DOCUMENT_CLICK:
                CertificateData model = (CertificateData) commonDataModel.getObject();
                String path1 = model.getCertificatePath();
                path1 = path1.replace("~","");
                String fpath = "http://192.168.0.244:3001/"+path1;   // https://lms.projectinclusion.in/
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Certificate shareable link "+fpath);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
              startActivity(shareIntent);
              break;
            case DOWNLOAD_CLICK:
                CertificateData modelcert = (CertificateData) commonDataModel.getObject();
            DownloadManager downloadManager = (DownloadManager) AuroApp.getAppContext().getSystemService(Context.DOWNLOAD_SERVICE);
           String path = modelcert.getCertificatePath();
           path = path.replace("~","");
            Uri uri = Uri.parse("http://192.168.0.244:3001/"+path);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
            downloadManager.enqueue(request);
            break;
            case VIEW_CLICK:
                CertificateData cmodel = (CertificateData) commonDataModel.getObject();
                String cpath = cmodel.getCertificatePath();
                cpath = cpath.replace("~","");
                String fnpath = "http://192.168.0.244:3001/"+cpath;
                Intent browseintent=new Intent(Intent.ACTION_VIEW,
                        Uri.parse(fnpath));
                getActivity().startActivity(browseintent);
                break;

        }
    }
    public void openFragment(Fragment fragment) {
        FragmentUtil.addFragment(getActivity(), fragment, R.id.home_container, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }
}
