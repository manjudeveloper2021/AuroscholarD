package com.auro.application.teacher.presentation.view.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
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
import com.auro.application.databinding.FragmentModuleBinding;
import com.auro.application.home.data.model.CourseModule.ModuleData;
import com.auro.application.home.data.model.CourseModule.ModuleList;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.presentation.view.adapter.ModuleListAdapter;
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

public class TeacherModuleOverviewFragment extends BaseFragment
{
    public static final String TAG = "MainCourseFragment";
    FragmentModuleBinding binding;

    public TeacherModuleOverviewFragment() {
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
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_module;
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


    private void getModuleList()
    {
        SharedPreferences prefs = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        String lmsuserid = prefs.getString("lmsuserid", "");
        String lms_courseid = prefs.getString("lms_courseid","");
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("userID",lmsuserid);
        map_data.put("lmsPartnerID","11");
        map_data.put("courseID",lms_courseid);
        CourseRemoteApi.Companion.invoke().getModuleList(map_data)
                .enqueue(new Callback<ModuleList>()
                {
                    @Override
                    public void onResponse(Call<ModuleList> call, Response<ModuleList> response)
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
                                        String descdata  = response.body().getResult().get(0).getDescription().toString();
                                       binding.txtdesc.setVisibility(View.VISIBLE);
                                       binding.txtdesc.setText(descdata);
                                        binding.recyclerModule.setVisibility(View.GONE);

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
                    public void onFailure(Call<ModuleList> call, Throwable t)
                    {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
