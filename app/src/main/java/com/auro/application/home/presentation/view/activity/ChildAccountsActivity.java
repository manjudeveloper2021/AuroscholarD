package com.auro.application.home.presentation.view.activity;

import static com.auro.application.core.common.Status.DISTRICT;
import static com.auro.application.core.common.Status.GENDER;
import static com.auro.application.core.common.Status.SCHOOL;
import static com.auro.application.core.common.Status.STATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.application.R;
import com.auro.application.core.application.base_component.BaseActivity;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;

import com.auro.application.databinding.BottomStudentListBinding;
import com.auro.application.home.data.model.CheckUserResModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.DistrictData;
import com.auro.application.home.data.model.GenderData;
import com.auro.application.home.data.model.GetAllChildModel;
import com.auro.application.home.data.model.GetAllChildReferListModel;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.data.model.ParentProfileDataModel;
import com.auro.application.home.data.model.SchoolData;
import com.auro.application.home.data.model.StateData;
import com.auro.application.home.data.model.StateDataModel;
import com.auro.application.home.data.model.StateDataModelNew;
import com.auro.application.home.data.model.StudentResponselDataModel;
import com.auro.application.home.data.model.response.ChildDetailResModel;
import com.auro.application.home.data.model.response.GetAllChildDetailResModel;
import com.auro.application.home.data.model.response.GetStudentUpdateProfile;
import com.auro.application.home.data.model.response.SendOtpResModel;
import com.auro.application.home.data.model.response.UserDetailResModel;
import com.auro.application.home.data.model.signupmodel.response.LoginResModel;
import com.auro.application.home.presentation.view.adapter.DistrictSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.GenderSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SchoolSpinnerAdapter;
import com.auro.application.home.presentation.view.adapter.SelectParentAdapter;
import com.auro.application.home.presentation.view.adapter.SelectYourChildAdapter;
import com.auro.application.home.presentation.view.adapter.SelectYourParentChildAdapter;
import com.auro.application.home.presentation.view.adapter.StateSpinnerAdapter;
import com.auro.application.home.presentation.view.fragment.BottomSheetAddUserDialog;
import com.auro.application.home.presentation.viewmodel.ParentProfileViewModel;
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.DeviceUtil;
import com.auro.application.util.RemoteApi;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.auro.application.util.strings.AppStringDynamic;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChildAccountsActivity extends BaseActivity implements CommonCallBackListner {
    BottomStudentListBinding binding;
    PrefModel prefModel;
    TextView txtchildaccount;
    RecyclerView studentList;
    ImageView back_button;
    List<UserDetailResModel> listchilds = new ArrayList<>();
    List<UserDetailResModel> list = new ArrayList<>();
    List<UserDetailResModel> listuserdetails = new ArrayList<>();
    CheckUserResModel checkUserResModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.viewchildaccount_activity);
      //  binding.setLifecycleOwner(this);
        back_button=findViewById(R.id.back_button);
        studentList = findViewById(R.id.studentList);
        txtchildaccount=findViewById(R.id.txtchildaccount);
        prefModel =  AuroAppPref.INSTANCE.getModelInstance();
        studentList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
       studentList.setHasFixedSize(true);

        String username = prefModel.getCheckUserResModel().getUserDetails().get(0).getUserName();
        getAddedChild(username);

         back_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(ChildAccountsActivity.this, ParentProfileActivity.class);
                 startActivity(intent);
                 finish();
             }
         });


        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {

                txtchildaccount.setText(details.getChild_accounts()!=null ? details.getChild_accounts() : "Child Accounts");

            }
        } catch (Exception e) {

        }

    }

    public void setAdapterAllListStudent(List<UserDetailResModel> totalStudentList) {
        List<UserDetailResModel> list = new ArrayList<>();

        for (UserDetailResModel resmodel : totalStudentList) {
            if (resmodel.getIsMaster().equalsIgnoreCase("0") ) {
                if (resmodel.getGrade().equals(0)||resmodel.getGrade().equals("0")){

                }
                else{
                    list.add(resmodel);
                }

            }

        }
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case CLICK_PARENTPROFILE:
                Intent i = new Intent(this, ParentProfileActivity.class);
                i.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
                startActivity(i);
                break;

            case CLICK_CHILDPROFILE:
                Intent i1 = new Intent(this, DashBoardMainActivity.class);
                i1.putExtra(AppConstant.COMING_FROM, AppConstant.FROM_SET_PASSWORD);
                startActivity(i1);
                break;
        }

    }


    @Override
    protected void init() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected int getLayout() {
        return R.layout.viewchildaccount_activity;
    }



    private void getAddedChild(String user_name)
    {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        String languageid = prefModel.getUserLanguageId();
        HashMap<String,String> map_data = new HashMap<>();
        map_data.put("user_name",user_name);
        map_data.put("user_type", String.valueOf(0));
        map_data.put("user_prefered_language_id",languageid);
        RemoteApi.Companion.invoke().getUserCheck(map_data)
                .enqueue(new Callback<CheckUserResModel>()
                {
                    @Override
                    public void onResponse(Call<CheckUserResModel> call, Response<CheckUserResModel> response)
                    {
                        try {
                            if (response.isSuccessful()) {
                                list.clear();
                                listchilds.clear();
                                if (!(response.body().getUserDetails() == null) || !(response.body().getUserDetails().isEmpty())) {


                                    listchilds = response.body().getUserDetails();
                                    for (int i = 0; i < listchilds.size(); i++) {
                                        if (listchilds.get(i).getIsMaster().equals("0") || listchilds.get(i).getIsMaster().equals(0)) {
                                            if (listchilds.get(i).getGrade().equals(0) || listchilds.get(i).getGrade().equals("0")) {

                                            } else {
                                                list.add(listchilds.get(i));
                                            }

                                        }
                                    }
//
                                    SelectYourParentChildAdapter studentListAdapter = new SelectYourParentChildAdapter(ChildAccountsActivity.this, list, ChildAccountsActivity.this);
                                    studentList.setAdapter(studentListAdapter);
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(ChildAccountsActivity.this, "Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckUserResModel> call, Throwable t)
                    {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
          super.onBackPressed();
      Intent intent = new Intent(ChildAccountsActivity.this, ParentProfileActivity.class);
      startActivity(intent);
      finish();


    }
}




















