package com.auro.application.teacher.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.ResponseApi;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.home.data.model.AuroScholarDataModel;
import com.auro.application.home.data.model.response.DynamiclinkResModel;
import com.auro.application.teacher.data.model.request.TeacherAddStudentInGroupReqModel;
import com.auro.application.teacher.data.model.request.TeacherCreateGroupReqModel;
import com.auro.application.teacher.data.model.request.TeacherDasboardSummaryResModel;
import com.auro.application.teacher.data.model.request.TeacherUserIdReq;
import com.auro.application.teacher.data.model.response.MyProfileResModel;
import com.auro.application.teacher.domain.TeacherDbUseCase;
import com.auro.application.teacher.domain.TeacherRemoteUseCase;
import com.auro.application.teacher.domain.TeacherUseCase;
import com.auro.application.util.AppLogger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class StudentPassportViewModel extends ViewModel {
    CompositeDisposable compositeDisposable;
    MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();

    public TeacherUseCase teacherUseCase;
    TeacherRemoteUseCase teacherRemoteUseCase;
    TeacherDbUseCase teacherDbUseCase;

    public StudentPassportViewModel(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        this.teacherDbUseCase = teacherDbUseCase;
        this.teacherUseCase = teacherUseCase;
        this.teacherRemoteUseCase = teacherRemoteUseCase;
    }

    public void checkInternet(Object object, Status status) {
        Disposable disposable = teacherRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                switch (status) {
                    case TEACHER_STUDENTPASSPORT:
                        getTeacherClassApi((TeacherUserIdReq) object);
                        break;

                    case GET_PROFILE_TEACHER_API:
                        getTeacherProfileDataApi(AuroAppPref.INSTANCE.getModelInstance().getStudentData().getUserId());
                        break;
                    case DYNAMIC_LINK_API:
                        AppLogger.e("callRefferApi","step 2");
                        getDynamicDataApiTeacher((DynamiclinkResModel) object);
                        break;
                }
            } else {
                AppLogger.v("InfoScreen", " callTeacher  serviceLiveData");
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, AuroApp.getAppContext().getResources().getString(R.string.internet_check), Status.NO_INTERNET));
            }

        });
        getCompositeDisposable().add(disposable);

    }
    private void getDynamicDataApi(DynamiclinkResModel model) {
        AppLogger.e("callRefferApi","step 3");
        getCompositeDisposable()
                .add(teacherRemoteUseCase.getDynamicDataApi(model)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           AppLogger.e("callRefferApi","step 4");
                                           serviceLiveData.setValue(responseApi);
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.e("callRefferApi","step 5"+ throwable.getMessage());
                                        defaultError();
                                    }
                                }));

    }
    private void getDynamicDataApiTeacher(DynamiclinkResModel model) {
        AppLogger.e("callRefferApi","step 3");
        getCompositeDisposable()
                .add(teacherRemoteUseCase.getDynamicDataApiTeacher(model)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           AppLogger.e("callRefferApi","step 4");
                                           serviceLiveData.setValue(responseApi);
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.e("callRefferApi","step 5"+ throwable.getMessage());
                                        defaultError();
                                    }
                                }));

    }

    private void getTeacherProfileDataApi(String userId) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.getProfileTeacherApi(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                // serviceLiveData.setValue(ResponseApi.loading(Status.GET_PROFILE_TEACHER_API));
                            }
                        })
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           AppLogger.v("GetProfiler","step 0.1-");
                                           serviceLiveData.setValue(responseApi);
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.v("GetProfiler","step 0.3-"+throwable.getMessage());
                                        defaultError();
                                    }
                                }));

    }

    private void getTeacherClassApi(TeacherUserIdReq auroScholarDataModel) {
        getCompositeDisposable()
                .add(teacherRemoteUseCase.teacherClassRoom(auroScholarDataModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                //serviceLiveData.setValue(ResponseApi.loading(null));
                            }
                        })
                        .subscribe(new Consumer<ResponseApi>() {
                                       @Override
                                       public void accept(ResponseApi responseApi) throws Exception {
                                           serviceLiveData.setValue(responseApi);
                                       }
                                   },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        AppLogger.e("chhonker exception","--"+throwable.getMessage());
                                        defaultError();
                                    }
                                }));

    }


    private void defaultError() {
        serviceLiveData.setValue(new ResponseApi(Status.FAIL, AuroApp.getAppContext().getResources().getString(R.string.default_error), null));
    }

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }

    public LiveData<ResponseApi> serviceLiveData() {

        return serviceLiveData;

    }

}
