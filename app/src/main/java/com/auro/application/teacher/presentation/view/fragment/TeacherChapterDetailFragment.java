package com.auro.application.teacher.presentation.view.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.auro.application.R;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.databinding.FragmentModuleTaskBinding;
import com.auro.application.databinding.LayoutChapterdetailBinding;
import com.auro.application.home.data.model.CourseModule.ChapterData;
import com.auro.application.home.data.model.CourseModule.ChapterDetailList;
import com.auro.application.home.data.model.CourseModule.ModuleTaskData;
import com.auro.application.home.data.model.CourseModule.ModuleTaskList;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.adapter.ChapterDetailAdapter;
import com.auro.application.home.presentation.view.adapter.ModuleTaskListAdapter;
import com.auro.application.util.CourseRemoteApi;
import com.auro.application.util.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherChapterDetailFragment extends BaseFragment implements CommonCallBackListner
{
    public static final String TAG = "MainCourseFragment";
    FragmentModuleTaskBinding binding;
    List<ChapterData> coursedata = new ArrayList<>();
    List<ChapterData> coursedatalist = new ArrayList<>();
    public TeacherChapterDetailFragment() {
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
        binding.txttitle.setVisibility(View.GONE);
        binding.relNext.setVisibility(View.VISIBLE);
        binding.recyclerModule.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerModule.setHasFixedSize(true);
        getModuleList();

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
  binding.btnnext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          getSubmit();
      }
  });
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_module_task;
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
    private void openAdapter(List<ChapterData> coursedatalist){
        ChapterDetailAdapter kyCuploadAdapter = new ChapterDetailAdapter(getActivity(), coursedatalist, this);
     binding.recyclerModule.setAdapter(kyCuploadAdapter);
    }

    private void getModuleList()
    {
        SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        String lms_chapterid = prefs.getString("lms_chapterid", "");
        String lms_courseid = prefs.getString("lms_courseid", "");
        String lms_moduleid = prefs.getString("lms_moduleid", "");
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("chapterID",lms_chapterid);
        map_data.put("courseID",lms_courseid);
        map_data.put("moduleID",lms_moduleid);
        CourseRemoteApi.Companion.invoke().getChapterList(map_data)
                .enqueue(new Callback<ChapterDetailList>()
                {
                    @Override
                    public void onResponse(Call<ChapterDetailList> call, Response<ChapterDetailList> response)
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
                                    coursedata.clear();
                                    coursedatalist.clear();
                                    if (!(response.body().getResult() == null || response.body().getResult().isEmpty() || response.body().getResult().equals("") || response.body().getResult().equals("null"))) {
                                        coursedata = response.body().getResult();
                                        for (int i = 0; i < coursedata.size(); i++) {
                                            coursedatalist.add(coursedata.get(i));
                                        }
                                        openAdapter(coursedatalist);
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
                    public void onFailure(Call<ChapterDetailList> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getSubmit()
    {
        SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        String lmsuserid = prefs.getString("lmsuserid", "");
        String lms_chapterid = prefs.getString("lms_chapterid", "");
        String lms_courseid = prefs.getString("lms_courseid", "");
        String lms_moduleid = prefs.getString("lms_moduleid", "");
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("lmsPartnerID","11");
        map_data.put("courseID",lms_courseid);
        map_data.put("moduleID",lms_moduleid);
        map_data.put("chapterID",lms_chapterid);
        map_data.put("userID",lmsuserid);



        CourseRemoteApi.Companion.invoke().getSubmit(map_data)
                .enqueue(new Callback<ChapterDetailList>()
                {
                    @Override
                    public void onResponse(Call<ChapterDetailList> call, Response<ChapterDetailList> response)
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

                                if (!(response.body().getResult() == null || response.body().getResult().isEmpty() || response.body().getResult().equals("") || response.body().getResult().equals("null"))) {

                                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    getCertificate();

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
                    public void onFailure(Call<ChapterDetailList> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getCertificate()
    {
        SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        String lmsuserid = prefs.getString("lmsuserid", "");
        String lms_chapterid = prefs.getString("lms_chapterid", "");
        String lms_courseid = prefs.getString("lms_courseid", "");
        String lms_moduleid = prefs.getString("lms_moduleid", "");
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("lmsPartnerID","11");
        map_data.put("courseID",lms_courseid);
        map_data.put("moduleID",lms_moduleid);
        map_data.put("chapterID",lms_chapterid);
        map_data.put("userID",lmsuserid);



        CourseRemoteApi.Companion.invoke().getCertificate(map_data)
                .enqueue(new Callback<ChapterDetailList>()
                {
                    @Override
                    public void onResponse(Call<ChapterDetailList> call, Response<ChapterDetailList> response)
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

                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();



                            }
                            else {
                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ChapterDetailList> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case COURSE_CLICK:
                openFragment(new TeacherModulePagerFragment());
                break;

        }
    }
    public void openFragment(Fragment fragment) {
        FragmentUtil.addFragment(getActivity(), fragment, R.id.home_container, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }
}
