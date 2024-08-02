package com.workdance.imagepicker.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.workdance.imagepicker.ImageDataSource;
import com.workdance.imagepicker.ImagePickStore;
import com.workdance.imagepicker.ImagePicker;
import com.workdance.imagepicker.R;
import com.workdance.imagepicker.Utils;
import com.workdance.imagepicker.adapter.ImageFolderAdapter;
import com.workdance.imagepicker.adapter.ImageGridAdapter;
import com.workdance.imagepicker.databinding.ActivityImageGridBinding;
import com.workdance.imagepicker.databinding.IncludeTopBarBinding;
import com.workdance.imagepicker.model.ImageFolder;
import com.workdance.imagepicker.model.ImageItem;

import java.util.List;

public class ImageGridActivity extends ImageBaseActivity implements View.OnClickListener, ImageDataSource.OnImageLoadListener, ImageGridAdapter.OnImageItemClickListener {
    private static final String TAG = "ImageGridActivity";
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;

    private boolean multiMode;
    private int limit;
    private boolean showCamera;
    private ImagePickStore store;
    private ActivityImageGridBinding binding;
    private IncludeTopBarBinding topBarBinding;

    private boolean isFullAccessGranted = false;
    private List<ImageFolder> mImageFolders;
    private ImageDataSource imageDataSource;
    private ImageGridAdapter mImageGridAdapter;
    private String takePhotoOutputPath;


    private GridView mGridView;  //图片展示控件
    private ImageFolderAdapter mImageFolderAdapter;    //图片文件夹的适配器
    private Button mBtnOk;
    private Button mBtnDir;
    private Button mBtnPre;
    private View mFooterBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        store = ImagePickStore.getInstance();
        store.clearSelectedImages();

        Intent intent = getIntent();
        multiMode = intent.getBooleanExtra("multiMode", false);
        limit = intent.getIntExtra("limit", 9);
        showCamera = intent.getBooleanExtra("showCamera", false);

        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(this);
        mBtnDir = (Button) findViewById(R.id.btn_dir);
        mBtnDir.setOnClickListener(this);
        mBtnPre = (Button) findViewById(R.id.btn_preview);
        mBtnPre.setOnClickListener(this);
        mGridView = (GridView) findViewById(R.id.gridview);
        mFooterBar = findViewById(R.id.footer_bar);

        if (multiMode) {
            mBtnOk.setVisibility(View.VISIBLE);
            mBtnPre.setVisibility(View.VISIBLE);
        } else {
            mBtnOk.setVisibility(View.GONE);
            mBtnPre.setVisibility(View.GONE);
        }

        // 初始化各种适配器
        mImageGridAdapter = new ImageGridAdapter(this, showCamera, multiMode, limit);
        mImageFolderAdapter = new ImageFolderAdapter(this, null);
        imageDataSource = new ImageDataSource(this, null, this);

        // 请求各种权限
        String[] permissions = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED)) {
            // Full access on Android 13 (API level 33) or higher
            isFullAccessGranted = true;
        } else {
            // Access denied or partial access granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions = new String[]{
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                };
            } else {
                permissions = new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                };
            }
        }

        if (!isFullAccessGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(permissions, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.takePhoto(this, takePhotoOutputPath, ImagePicker.REQUEST_CODE_TAKE);
            } else {
                Toast.makeText(this, "权限被禁止，无法打开相机", Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void onClick(View view) {
        // 根据每个点击元素来判断行为
        int id = view.getId();
        if (id == R.id.btn_ok) {
            finishImagePick();
        } else if (id == R.id.btn_dir) {
            if (mImageFolders == null) {
                Log.i(TAG, "你的手机没有图片");
                return;
            }


        } else if (id == R.id.btn_preview) {
            Log.i(TAG, "预览图片");
        } else if (id == R.id.btn_back) {
            finish();
        }
    }

    /**
     * datasource 的图片回调接口
     * @param imageFolders
     */
    @Override
    public void onImageLoad(List<ImageFolder> imageFolders) {
        this.mImageFolders = imageFolders;
        store.setImageFolders(imageFolders);
        if (imageFolders.isEmpty()) {
            mImageGridAdapter.refreshData(null);
        } else {
            mImageGridAdapter.refreshData(imageFolders.get(0).getImages());
        }

        mImageGridAdapter.setListener(this);
        mGridView.setAdapter(mImageGridAdapter);
        mImageFolderAdapter.refreshData(imageFolders);

    }

    public void takePhoto() {

    }

    @Override
    public void onImageItemClick(View view, ImageItem imageItem, int position) {
        position = showCamera ? position - 1 : position;
        if (multiMode) {
            Intent intent = new Intent(ImageGridActivity.this, ImagePreviewActivity.class);
            intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
            startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);
        } else {
            store.addSelectedImageItem(store.getCurrentImageFolderItems().get(position), true);
            finishImagePick();
        }
    }

    private void finishImagePick() {
        Intent intent = new Intent();
        intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, store.getSelectedImages());
        intent.putExtra(ImagePicker.EXTRA_COMPRESS, store.isCompress());
        setResult(Activity.RESULT_OK, intent);   //单选不需要裁剪，返回数据
        finish();
    }

    public void updatePickStatus() {
        if (store.getSelectImageCount() > 0) {
            mBtnOk.setText(getString(R.string.select_complete, store.getSelectImageCount(), limit));
            mBtnOk.setEnabled(true);
            mBtnPre.setEnabled(true);
        } else {
            mBtnOk.setText(getString(R.string.complete));
            mBtnOk.setEnabled(false);
            mBtnPre.setEnabled(false);
        }
        mBtnPre.setText(getResources().getString(R.string.preview_count, store.getSelectImageCount()));
        mImageGridAdapter.notifyDataSetChanged();
    }
}
