package com.auro.application.home.presentation.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseFragment;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.common.CommonCallBackListner;
import com.auro.application.core.common.CommonDataModel;
import com.auro.application.core.common.Status;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.CameraFragmentLayoutBinding;
import com.auro.application.home.presentation.view.activity.HomeActivity;
import com.auro.application.util.AppLogger;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.camera.CameraOverlay;
import com.auro.application.util.camera.FaceOverlayGraphics;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;


public class CameraFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener {

    @Inject
    @Named("CameraFragment")
    ViewModelFactory viewModelFactory;
    String TAG = "CameraFragment";
    CameraSource mCameraSource;
    private static final int RC_HANDLE_GMS = 9001;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    int cameraID = 0;
    Camera.Parameters params;
    Camera camera;
    boolean isFlash = false;
    CameraFragmentLayoutBinding binding;
    public static boolean status;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        binding.setLifecycleOwner(this);

        if (hasFrontCamera()) {
            cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;

        }
        setRetainInstance(true);
        checkValueEverySecond();

        binding.textCamra.setText(AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails().getPleasePutYourFaceInsideTheBox());
        ViewUtil.setLanguageonUi(getActivity());
        return binding.getRoot();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    private void checkValueEverySecond() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status) {
                    binding.captureButtonSecondaryContainer.animate().alpha(1F).start();
                } else {
                    binding.captureButtonSecondaryContainer.animate().alpha(0F).start();
                }
                checkValueEverySecond();
            }
        }, 1000);

    }

    @Override
    protected void init() {
        setListener();
        askPermission();

    }

    private void askPermission() {
        String rationale = "Please provide camera permission so that you can ...";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");
        Permissions.check(getActivity(), PermissionUtil.mCameraPermissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                createCameraSource(cameraID);
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {

            }
        });
    }


    private void createCameraSource(int cameraID) {

        Context context = getActivity().getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();


        detector.setProcessor(
                new MultiProcessor.Builder<>(new CameraFragment.GraphicFaceTrackerFactory())
                        .build());

        if (!detector.isOperational()) {

            AppLogger.e(TAG, "Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(context, detector)
                .setFacing(cameraID)
                .setAutoFocusEnabled(true)
                .setRequestedFps(30.0f)
                .build();

        startCameraSource();
    }


    private void startCameraSource() {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getActivity().getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                binding.preview.start(mCameraSource, binding.faceOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.switch_orientation) {
            changeCamera();
        } else if (v.getId() == R.id.flash_toggle) {
            flashIsAvailable();

        } else if (v.getId() == R.id.stillshot) {
            clickPicture();
        }
    }

    private void changeCamera() {
        if (cameraID == Camera.CameraInfo.CAMERA_FACING_BACK) {
            cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        binding.faceOverlay.clear();
        mCameraSource.release();
        createCameraSource(cameraID);
    }


    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new CameraFragment.GraphicFaceTracker(binding.faceOverlay);
        }
    }

    private class GraphicFaceTracker extends Tracker<Face> {
        private final CameraOverlay mOverlay;
        private final FaceOverlayGraphics faceOverlayGraphics;

        GraphicFaceTracker(CameraOverlay overlay) {
            mOverlay = overlay;
            faceOverlayGraphics = new FaceOverlayGraphics(overlay);
        }


        @Override
        public void onNewItem(int faceId, Face item) {

            faceOverlayGraphics.setId(faceId);
            status = true;

        }

        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.add(faceOverlayGraphics);
            faceOverlayGraphics.updateFace(face);
        }

        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {

            mOverlay.remove(faceOverlayGraphics);

        }

        @Override
        public void onDone() {
            mOverlay.remove(faceOverlayGraphics);
            status = false;

        }
    }

    private void clickPicture() {
        mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes) {
                try {

                    Bitmap loadedImage = null;
                    loadedImage = BitmapFactory.decodeByteArray(bytes, 0,
                            bytes.length);
                    setUserPrefData(saveToInternalStorage(loadedImage)+"/profile.jpg");
                    loadImageFromStorage(saveToInternalStorage(loadedImage));



                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });

    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(AuroApp.getAppContext().getApplicationContext());

        File directory = cw.getDir("auroImageDir", Context.MODE_PRIVATE);

        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


    private void loadImageFromStorage(String path) {
        try {

            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            getActivity().getSupportFragmentManager().popBackStack();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean hasFrontCamera() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return true;
            }
        }
        return false;
    }


    private void flashIsAvailable() {
        boolean hasFlash = getActivity().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (hasFlash) {
            changeFlashStatus();
        }
    }


    public void changeFlashStatus() {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    camera = (Camera) field.get(mCameraSource);
                    if (camera != null) {
                        params = camera.getParameters();
                        if (!isFlash) {
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            binding.flashToggle.setImageDrawable(getActivity().getDrawable(R.drawable.ic_flash_on_black));
                            isFlash = true;
                        } else {
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            binding.flashToggle.setImageDrawable(getActivity().getDrawable(R.drawable.ic_flash_off_black));
                            isFlash = false;
                        }
                        camera.setParameters(params);
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
    }


    @Override
    protected void setToolbar() {
        /*Do work here*/
    }

    @Override
    protected void setListener() {
        binding.stillshot.setOnClickListener(this);
        binding.switchOrientation.setOnClickListener(this);
        binding.flashToggle.setOnClickListener(this);
    }


    @Override
    protected int getLayout() {
        return R.layout.camera_fragment_layout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setToolbar();
        setListener();
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        if (commonDataModel.getClickType() == Status.FACE_DETECTION_CALLBACK) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }


    private void releaseCamera() {
        binding.faceOverlay.clear();
        mCameraSource.release();
    }

    public static void setUserPrefData(String path) {
        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        prefModel.setUserKYCProfilePhotoPath(path);
        AuroAppPref.INSTANCE.setPref(prefModel);
    }
}
