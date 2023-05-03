package com.auro.application.teacher.presentation.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.presentation.view.activity.SplashScreenAnimationActivity;
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.common.StateDataModel;
import com.auro.application.teacher.data.model.request.SelectClassesSubject;
import com.auro.application.teacher.data.model.request.TeacherReqModel;
import com.auro.application.teacher.data.model.response.MyClassRoomTeacherResModel;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.teacher.presentation.view.adapter.DistrictSpinnerAdapter;
import com.auro.application.teacher.presentation.view.adapter.ProfileScreenAdapter;
import com.auro.application.teacher.presentation.view.adapter.StateSpinnerAdapter;
import com.auro.application.teacher.presentation.viewmodel.TeacherProfileViewModel;
import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.FragmentUtil;
import com.auro.application.core.common.Status;
import com.auro.application.databinding.FragmentTeacherProfileBinding;
import com.auro.application.util.AppLogger;
import com.auro.application.util.AppUtil;
import com.auro.application.util.TextUtil;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.firebaseAnalytics.AnalyticsRegistry;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;


public class TeacherProfileFragment extends BaseFragment implements TextWatcher, CommonCallBackListner {

    @Inject
    @Named("TeacherProfileFragment")
    ViewModelFactory viewModelFactory;
    TeacherProfileViewModel viewModel;
    TeacherReqModel teacherReqModel = new TeacherReqModel();

    FragmentTeacherProfileBinding binding;
    boolean isStateRestore;
    List<StateDataModel> stateDataModelList;
    List<DistrictDataModel> districtDataModels;
    List<String> classesList;
    List<String> subjectlist;
    HashMap<String, String> subjectHashmap = new HashMap<>();
    HashMap<String, String> classHashmap = new HashMap<>();
    ProfileScreenAdapter mProfileClassAdapter;
    ProfileScreenAdapter mProfileSubjectAdapter;
    String stateCode = "";
    String districtCode = "";


/*    String subject = "English, Maths, Social Science, Science,";
    String classes = "4th, 8th, 10th, 3rd, 9th, 7th, 2nd,";

    String distict = "52";
    String state =  "5";*/


    public TeacherProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (binding != null) {
            isStateRestore = true;
            return binding.getRoot();
        }
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TeacherProfileViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // HomeActivity.setListingActiveFragment(HomeActivity.TEACHER_PROFILE_FRAGMENT);
        init();
        setListener();
        if (AppUtil.myClassRoomResModel != null && AppUtil.myClassRoomResModel.getTeacherResModel() != null) {
            setModelinteacherprofile(AppUtil.myClassRoomResModel.getTeacherResModel());
            if (!TextUtil.isEmpty(AppUtil.myClassRoomResModel.getTeacherResModel().getStateId())) {
                stateCode = AppUtil.myClassRoomResModel.getTeacherResModel().getStateId();
            }

            if (!TextUtil.isEmpty(AppUtil.myClassRoomResModel.getTeacherResModel().getDistrictId())) {
                districtCode = AppUtil.myClassRoomResModel.getTeacherResModel().getDistrictId();
            }
        }
        // viewModel.getTeacherProfileData(AuroApp.getAuroScholarModel().getMobileNumber());
    }

    @Override
    protected void init() {
        binding.headerTopParent.cambridgeHeading.setVisibility(View.GONE);



        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }

        viewModel.getStateListData();
        viewModel.getDistrictListData();


        setRecycleView();
        setDataOnUI();

    }

    private void setDataOnUI() {
        if (AppUtil.myClassRoomResModel != null && AppUtil.myClassRoomResModel.getTeacherResModel() != null) {
            if (AppUtil.myClassRoomResModel.getTeacherResModel().getScoreTotal() != null && !TextUtil.isEmpty(String.valueOf(AppUtil.myClassRoomResModel.getTeacherResModel().getScoreTotal()))) {
                binding.points.setText("" + AppUtil.myClassRoomResModel.getTeacherResModel().getScoreTotal());
            } else {
                binding.points.setText("0");
            }

            if (!TextUtil.isEmpty(String.valueOf(AppUtil.myClassRoomResModel.getTeacherResModel().getWalletBalance()))) {
                binding.walletBal.setText(" " + AuroApp.getAppContext().getResources().getString(R.string.rs) + AppUtil.myClassRoomResModel.getTeacherResModel().getWalletBalance());
            } else {
                binding.walletBal.setText("0");
            }
        }


        if (!TextUtil.isEmpty(AuroApp.getAuroScholarModel().getMobileNumber())) {
            binding.editPhoneNumber.setText(AuroApp.getAuroScholarModel().getMobileNumber());
            binding.editPhoneNumber.setFocusable(false);
            binding.icmobilenumber.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        binding.editteachername.addTextChangedListener(this);
        binding.editemail.addTextChangedListener(this);
        binding.editPhoneNumber.addTextChangedListener(this);
        binding.editSchoolName.addTextChangedListener(this);
        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                String yes=getActivity().getString(R.string.yes);
                String no=getActivity().getString(R.string.no);
                builder.setMessage(getString(R.string.sure_to_logout));
                try {
                    LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
                    Details details = model.getDetails();
                    if (model != null) {
                        yes=details.getYes();
                        no=details.getNo();
                        builder.setMessage(details.getSureToLogout());
                    }
                } catch (Exception e) {
                    AppLogger.e("Excdeption", e.getMessage());
                }

                // Set the alert dialog yes button click listener
                builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>"+yes+"</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when user clicked the Yes button
                        // Set the TextView visibility GONE
                        // tv.setVisibility(View.GONE);
                        dialog.dismiss();
                        logout();
                        AppUtil.myClassRoomResModel = null;
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>"+no+"</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        dialog.dismiss();
                     /*   Toast.makeText(getApplicationContext(),
                                "No Button Clicked",Toast.LENGTH_SHORT).show();*/
                    }
                });

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();

            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSaveTeacherProfileApi();
            }
        });
    }




    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(getActivity(), fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_teacher_profile;
    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case LOADING:
                    if(isVisible()) {
                        if (responseApi.apiTypeStatus == Status.UPDATE_TEACHER_PROFILE_API) {
                            handleProgress(0);
                        } else if (responseApi.apiTypeStatus == Status.GET_PROFILE_TEACHER_API) {

                        }
                    }
                    break;

                case SUCCESS:
                    if(isVisible()) {
                        if (responseApi.apiTypeStatus == Status.UPDATE_TEACHER_PROFILE_API) {
                            handleProgress(1);
                            funnelTeacherSubmitProfile();
                            showSnackbarError(getString(R.string.saved), Color.parseColor("#4bd964"));
                        } else if (responseApi.apiTypeStatus == Status.GET_PROFILE_TEACHER_API) {
                            MyProfileResModel teacherResModel = (MyProfileResModel) responseApi.data;

                            if (teacherResModel != null) {
                                //setModelinteacherprofile(teacherResModel);
                            } else {
                           /* binding.studentList.setVisibility(View.GONE);
                            binding.errorTxt.setVisibility(View.VISIBLE);*/
                            }
                        }
                    }
                    break;

                case FAIL:
                case NO_INTERNET:
                    if(isVisible()) {
                        if (responseApi.apiTypeStatus == Status.UPDATE_TEACHER_PROFILE_API) {
                            handleProgress(1);
                            showSnackbarError((String) responseApi.data);
                        }
                    }
                    break;

                /*For state list*/
                case STATE_LIST_ARRAY:
                    if(isVisible())
                        if(isVisible()) {
                            stateDataModelList = (List<StateDataModel>) responseApi.data;
                            if (!TextUtil.isEmpty(stateCode)) {
                                stateSpinner(stateCode);
                                stateCode = "";
                            } else {
                                stateSpinner(stateCode);
                            }
                        }
                    break;

                case DISTRICT_LIST_DATA:
                    if(isVisible()) {
                        districtDataModels = (List<DistrictDataModel>) responseApi.data;
                        if (!TextUtil.isEmpty(districtCode)) {
                            districtSpinner(districtCode);
                            districtCode = "";
                        } else {
                            districtSpinner(districtCode);
                        }
                    }

                    break;

                default:
                    if(isVisible()) {
                        handleProgress(1);
                        showSnackbarError(getString(R.string.default_error));
                    }
                    break;
            }

        });
    }


    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private void showSnackbarError(String message, int color) {
        ViewUtil.showSnackBar(binding.getRoot(), message, color);
    }

    private void stateSpinner(String state) {
        if (!TextUtil.checkListIsEmpty(stateDataModelList)) {
            com.auro.application.teacher.presentation.view.adapter.StateSpinnerAdapter stateSpinnerAdapter = new StateSpinnerAdapter(stateDataModelList);
            binding.stateSpinner.setAdapter(stateSpinnerAdapter);

            binding.stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        teacherReqModel.setState_id(stateDataModelList.get(position).getState_code());
                        viewModel.getStateDistrictData(stateDataModelList.get(position).getState_code());
                        binding.cityTitle.setVisibility(View.VISIBLE);
                        binding.cityView.setVisibility(View.VISIBLE);
                        binding.citySpinner.setVisibility(View.VISIBLE);
                    } else if (stateDataModelList.get(position).getState_name().trim().equalsIgnoreCase("pleaseselectstate")) {
                        binding.cityTitle.setVisibility(View.GONE);
                        binding.cityView.setVisibility(View.GONE);
                        binding.citySpinner.setVisibility(View.GONE);
                    } else {
                        binding.cityTitle.setVisibility(View.GONE);
                        binding.cityView.setVisibility(View.GONE);
                        binding.citySpinner.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            if (state != null) {
                for (int i = 0; i < stateDataModelList.size(); i++) {
                    if (state.equalsIgnoreCase(stateDataModelList.get(i).getState_code())) {
                        binding.stateSpinner.setSelection(i);
                    }
                }
            }
        }


    }

    private void districtSpinner(String district) {
        if (!TextUtil.checkListIsEmpty(districtDataModels)) {
            binding.cityTitle.setVisibility(View.VISIBLE);
            binding.citySpinner.setVisibility(View.VISIBLE);
            com.auro.application.teacher.presentation.view.adapter.DistrictSpinnerAdapter stateSpinnerAdapter = new DistrictSpinnerAdapter(districtDataModels);
            binding.citySpinner.setAdapter(stateSpinnerAdapter);


            binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    teacherReqModel.setDistrict_id(districtDataModels.get(position).getDistrict_code());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            binding.cityTitle.setVisibility(View.GONE);
            binding.citySpinner.setVisibility(View.GONE);
        }

        if (district != null) {
            for (int i = 0; i < districtDataModels.size(); i++) {
                if (district.equalsIgnoreCase(districtDataModels.get(i).getDistrict_code())) {
                    binding.citySpinner.setSelection(i);
                }
            }
        }
    }

    public void setRecycleView() {
        //for class recycleview

        GridLayoutManager gridlayout = new GridLayoutManager(getActivity(), 2);
        gridlayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recycleViewclass.setLayoutManager(gridlayout);
        binding.recycleViewclass.setHasFixedSize(true);
        binding.recycleViewclass.setNestedScrollingEnabled(false);
        mProfileClassAdapter = new com.auro.application.teacher.presentation.view.adapter.ProfileScreenAdapter(viewModel.teacherUseCase.selectClass(""), getContext(), this);
        binding.recycleViewclass.setAdapter(mProfileClassAdapter);

        //for subject recycle view

        GridLayoutManager gridlayout2 = new GridLayoutManager(getActivity(), 2);
        gridlayout2.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recycleViewsubject.setLayoutManager(gridlayout2);
        binding.recycleViewsubject.setHasFixedSize(true);
        binding.recycleViewsubject.setNestedScrollingEnabled(false);
        mProfileSubjectAdapter = new ProfileScreenAdapter(viewModel.teacherUseCase.selectSubject(""), getContext(), this);
        binding.recycleViewsubject.setAdapter(mProfileSubjectAdapter);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


    }

    @Override
    public void afterTextChanged(Editable editable) {

        //teacher name validation
        if (editable == binding.editteachername.getEditableText()) {
            if (editable.length() >= 5) {
                binding.icteachername.setVisibility(View.VISIBLE);
            } else {
                binding.icteachername.setVisibility(View.GONE);
            }
        }
        //email validation
        if (editable == binding.editemail.getEditableText()) {
            if (emailValidation(binding.editemail)) {
                binding.icemail.setVisibility(View.VISIBLE);
            } else {
                binding.icemail.setVisibility(View.GONE);
            }
        }
        //
        if (editable == binding.editPhoneNumber.getEditableText()) {
            if (editable.length() == 10) {
                binding.icmobilenumber.setVisibility(View.VISIBLE);
            } else {
                binding.icmobilenumber.setVisibility(View.GONE);
            }
        }
        if (editable == binding.editSchoolName.getEditableText()) {
            if (editable.length() >= 5) {
                binding.icschoolname.setVisibility(View.VISIBLE);
            } else {
                binding.icschoolname.setVisibility(View.GONE);
            }
        }
    }

    public boolean emailValidation(EditText emailtext) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (emailtext.getText().toString().isEmpty()) {
            binding.inputemailedittext.setError(null);
            return false;
        } else {
            if (TextUtil.isValidEmail(emailtext.getText().toString())) {
                binding.inputemailedittext.setError(null);
                return true;
            } else {
                binding.inputemailedittext.setError("Invalid email address");
                return false;

            }
        }
    }

    private void callSaveTeacherProfileApi() {
        if (subjectHashmap.size() > 0) {
            StringBuilder subjectBuilder = new StringBuilder();
            for (String key : subjectHashmap.keySet()) {
                subjectBuilder.append(subjectHashmap.get(key) + ",");
            }
            // teacherReqModel.setTeacher_subject(TextUtil.removeLastChar(subjectBuilder.toString()));

        } else {
            //teacherReqModel.setTeacher_subject("");
        }

        if (classHashmap.size() > 0) {
            StringBuilder classBuilder = new StringBuilder();
            for (String key : classHashmap.keySet()) {
                classBuilder.append(classHashmap.get(key) + ",");
            }
            // teacherReqModel.setTeacher_class(TextUtil.removeLastChar(classBuilder.toString()));
        } else {
            // teacherReqModel.setTeacher_class("");
        }
        teacherReqModel.setMobile_no(AuroApp.getAuroScholarModel().getMobileNumber());
        teacherReqModel.setTeacher_name(binding.editteachername.getText().toString());
        // teacherReqModel.setTeacher_email(binding.editemail.getText().toString());
        // teacherReqModel.setSchool_name(binding.editSchoolName.getText().toString());
        /*ValidationModel validationModel = viewModel.teacherUseCase.checkTeacherProfileValidation(teacherReqModel);
        if (validationModel.isStatus()) {
            viewModel.updateTeacherProfileData(teacherReqModel);
        } else {
            showSnackbarError(validationModel.getMessage());
        }*/
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case SUBJECT_CLICK:
                com.auro.application.teacher.data.model.request.SelectClassesSubject subject = (com.auro.application.teacher.data.model.request.SelectClassesSubject) commonDataModel.getObject();
                if (subject.isSelected()) {
                    subjectHashmap.put(subject.getText(), subject.getText());
                } else {
                    subjectHashmap.remove(subject.getText());
                }
                break;

            case CLASS_CLICK:
                com.auro.application.teacher.data.model.request.SelectClassesSubject class_click = (SelectClassesSubject) commonDataModel.getObject();
                if (class_click.isSelected()) {
                    classHashmap.put(class_click.getText(), class_click.getText());
                } else {
                    classHashmap.remove(class_click.getText());
                }

                break;
        }
    }

    public void handleProgress(int status) {
        switch (status) {
            case 0:
                binding.button.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);
                break;
            case 1:
                binding.button.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                break;
        }
    }

    private void shimmerProgress(int status, String message) {
        switch (status) {
            case 0:
                binding.parentLayout.setVisibility(View.GONE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.shimmerMyClassroom.setVisibility(View.VISIBLE);
                binding.shimmerMyClassroom.startShimmer();
                break;

            case 1:
                binding.parentLayout.setVisibility(View.VISIBLE);
                binding.errorConstraint.setVisibility(View.GONE);
                binding.shimmerMyClassroom.setVisibility(View.GONE);
                binding.shimmerMyClassroom.stopShimmer();
                break;

            case 2:
                binding.errorConstraint.setVisibility(View.VISIBLE);
                binding.parentLayout.setVisibility(View.GONE);
                binding.shimmerMyClassroom.setVisibility(View.GONE);
                binding.shimmerMyClassroom.stopShimmer();
                binding.errorLayout.textError.setText(message);
                binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //viewModel.getTeacherProfileData(AuroApp.getAuroScholarModel().getMobileNumber());
                    }
                });
                break;
        }

    }

    public void setModelinteacherprofile(MyClassRoomTeacherResModel teacherResModel) {

        if (teacherResModel.getSchoolName() != null) {
            binding.editSchoolName.setText(teacherResModel.getSchoolName());
        }
        if (teacherResModel.getTeacherName() != null) {
            binding.editteachername.setText(teacherResModel.getTeacherName());
        }
        if (teacherResModel.getTeacherEmail() != null) {
            binding.editemail.setText(teacherResModel.getTeacherEmail());
        }
        if (teacherResModel.getTeacherClass() != null && teacherResModel.getTeacherSubject() != null) {
            List<String> classList = viewModel.teacherUseCase.getStringList(teacherResModel.getTeacherClass());
            List<String> subjectList = viewModel.teacherUseCase.getStringList(teacherResModel.getTeacherSubject());
            if (!TextUtil.checkListIsEmpty(classList)) {
                for (String s : classList) {
                    classHashmap.put(s, s);
                }
            }
            if (!TextUtil.checkListIsEmpty(subjectList)) {
                for (String s : subjectList) {
                    subjectHashmap.put(s, s);
                }
            }


            mProfileClassAdapter.updatelist(viewModel.teacherUseCase.selectClass(teacherResModel.getTeacherClass()));
            mProfileSubjectAdapter.updatelist(viewModel.teacherUseCase.selectSubject(teacherResModel.getTeacherSubject()));

            //setRecycleView(teacherResModel.getTeacherClass(), teacherResModel.getTeacherSubject());
        }
    }

    private void logout() {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        AppLogger.e("Chhonker", "Logout");
        int userType = prefModel.getUserType();
        //   mgoogleSignInHelper.signOut();
        AuroAppPref.INSTANCE.clearPref();
        funnelTeacherLogOut();
        Intent intent = new Intent(getActivity(), SplashScreenAnimationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finishAffinity();

    }
    private void funnelTeacherLogOut(){
        AnalyticsRegistry.INSTANCE.getModelInstance().trackTeacherLogOut();
    }
    private void funnelTeacherSubmitProfile(){
        AnalyticsRegistry.INSTANCE.getModelInstance().trackTeacherProfileSubmit();
    }
}
