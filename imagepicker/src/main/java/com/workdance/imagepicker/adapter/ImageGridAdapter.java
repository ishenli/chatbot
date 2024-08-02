package com.workdance.imagepicker.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.workdance.imagepicker.ImagePickStore;
import com.workdance.imagepicker.R;
import com.workdance.imagepicker.Utils;
import com.workdance.imagepicker.model.ImageItem;
import com.workdance.imagepicker.ui.ImageGridActivity;

import java.util.ArrayList;

import lombok.Setter;

public class ImageGridAdapter extends BaseAdapter {
    private static final int ITEM_TYPE_CAMERA = 0;  //第一个条目是相机
    private static final int ITEM_TYPE_NORMAL = 1;  //第一个条目不是相机

    private final Activity mActivity;
    private ArrayList<ImageItem> images;
    private final int mImageSize;
    private final ImagePickStore store;
    private final boolean showCamera;
    private final boolean multiMode;
    private final int limit;
    private final ArrayList<ImageItem> mSelectedImages;
    private View convertView;
    private ViewHolder holder;

    @Setter
    private OnImageItemClickListener listener;   //图片被点击的监听

    public ImageGridAdapter(Activity activity, boolean showCamera, boolean multiMode, int limit) {
        this.mActivity = activity;
        this.images = new ArrayList<>();
        mImageSize = Utils.getImageItemWidth(activity);
        store = ImagePickStore.getInstance();
        this.showCamera = showCamera;
        this.multiMode = multiMode;
        this.limit = limit;
        mSelectedImages = store.getPickedImages();
    }

    public void refreshData(ArrayList<ImageItem> images) {
        if (images == null || images.size() == 0) {
            this.images = new ArrayList<>();
        } else {
            this.images = images;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return this.showCamera ? images.size() + 1 : images.size();
    }

    @Override
    public Object getItem(int position) {
        if (showCamera) {
            if (position == 0) return null;
            return images.get(position - 1);
        } else {
            return images.get(position);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public int getItemViewType(int position) {
        if (showCamera) return position == 0 ? ITEM_TYPE_CAMERA : ITEM_TYPE_NORMAL;
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 实现一个宫格的元素
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_TYPE_CAMERA) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.adapter_camera_item, parent, false);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
            convertView.setTag(null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ImageGridActivity) mActivity).takePhoto();
                }
            });
        } else  {
            if (convertView == null) {
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.adapter_image_list_item, parent, false);
                convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize));
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ImageItem imageItem = (ImageItem) getItem(position);
            holder.ivThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onImageItemClick(holder.rootView, imageItem, position);
                    }
                }
            });
            // 绑定图片点击事件
            holder.cbCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.cbCheck.isChecked() && mSelectedImages.size() >= limit) {
                        Toast.makeText(mActivity, "最多选择" + limit + "张图片", Toast.LENGTH_SHORT);
                        holder.cbCheck.setChecked(false);
                        holder.mask.setVisibility(View.GONE);
                    } else {
                        store.addSelectedImageItem(imageItem, holder.cbCheck.isChecked());
                        holder.mask.setVisibility(View.VISIBLE);
                        ((ImageGridActivity) mActivity).updatePickStatus();

                    }
                }
            });

            // 处理多选的样式选择
            if (multiMode) {
                holder.cbCheck.setVisibility(View.VISIBLE);
                boolean checked = mSelectedImages.contains(imageItem);
                if (checked) {
                    holder.cbCheck.setChecked(true);
                    holder.mask.setVisibility(View.VISIBLE);
                } else {
                    holder.cbCheck.setChecked(false);
                    holder.mask.setVisibility(View.GONE);
                }
            } else {
                holder.cbCheck.setVisibility(View.GONE);
            }


            // 加载图片
            Glide.with(convertView.getContext()).load(Uri.parse("file://" + imageItem.getPath()).toString()).into(holder.ivThumb);
        }


        return convertView;
    }

    private class ViewHolder {
        public View rootView;
        public ImageView ivThumb;
        public View mask;
        public CheckBox cbCheck;

        public ViewHolder(View view) {
            rootView = view;
            ivThumb = (ImageView) view.findViewById(R.id.iv_thumb);
            mask = view.findViewById(R.id.mask);
            cbCheck = (CheckBox) view.findViewById(R.id.cb_check);
        }
    }

    public interface OnImageItemClickListener {
        void onImageItemClick(View view, ImageItem imageItem, int position);
    }
}
