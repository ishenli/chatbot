// package com.workdance.multimedia.editor;
//
// import android.Manifest;
// import android.content.pm.PackageManager;
// import android.os.Bundle;
// import android.view.LayoutInflater;
// import android.view.View;
// import android.view.ViewGroup;
// import android.widget.FrameLayout;
//
// import androidx.core.content.ContextCompat;
//
// import com.workdance.core.BaseFragment;
// import com.workdance.multimedia.R;
//
// public class CaptureFragment extends BaseFragment {
//     private static final String TAG = "CaptureFragment";
//     private final String cameraPermission = Manifest.permission.CAMERA;
//     private FrameLayout mCameraContainer;
//     private View mRoot;
//     private View mCancel;
//     private FrameLayout mContentRoot;
//     private View mControlPanel;
//     private View mSwitchCamera;
//     private View mSwitchFlash;
//     private View mSwitchBeauty;
//     String[] permissions = new String[2];
//     public static CaptureFragment newInstance(Bundle extras) {
//         CaptureFragment ret = new CaptureFragment();
//         Bundle args = new Bundle();
//         args.putAll(extras);
//         ret.setArguments(args);
//         return ret;
//     }
//
//     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//         super.onCreateView(inflater, container, savedInstanceState);
//         mRoot = inflater.inflate(R.layout.fragment_creator_capture, null);
//         initViews();
//         checkPermissionAndOpenCamera();
//         return mRoot;
//     }
//
//     private void initViews() {
//         mContentRoot = mRoot.findViewById(R.id.fl_content_root);
//     }
//
//     private void checkPermissionAndOpenCamera() {
//         boolean hasCameraPermission = ContextCompat.checkSelfPermission(getContext(), cameraPermission) == PackageManager.PERMISSION_GRANTED;
//         if (hasCameraPermission) {
//             mContentRoot.setVisibility(View.VISIBLE);
//             openCamera();
//         } else {
//             mContentRoot.setVisibility(View.GONE);
//             // 先申请一下权限
//             // requestPermission(new String[]{cameraPermission},REQUEST_CODE_CAPTURE_PERMISSION);
//         }
//     }
//
//     private void openCamera() {
//     }
//
// }
