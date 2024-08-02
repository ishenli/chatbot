package com.workdance.imagepicker.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.workdance.imagepicker.R;
import com.workdance.imagepicker.Utils;
import com.workdance.imagepicker.model.ImageFolder;

import java.util.ArrayList;
import java.util.List;

public class ImageFolderAdapter extends BaseAdapter {
    private final Activity mActivity;
    private final LayoutInflater mInflater;
    private final int mImageSize;
    private List<ImageFolder> mImageFolders;
    private int lastSelected = 0;

    public ImageFolderAdapter(Activity activity, List<ImageFolder> imageFolders) {
        mActivity = activity;
        if (imageFolders != null && !imageFolders.isEmpty()) {
            this.mImageFolders = imageFolders;
        } else {
            this.mImageFolders = new ArrayList<>();
        }
        mImageSize = Utils.getImageItemWidth(mActivity);
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mImageFolders.size();
    }

    @Override
    public Object getItem(int i) {
        return mImageFolders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;
        if (contentView == null) {
            contentView = mInflater.inflate(R.layout.adapter_folder_list_item, parent, false);
            holder = new ViewHolder(contentView);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        ImageFolder imageFolder = mImageFolders.get(position);
        holder.folderName.setText(imageFolder.getName());
        holder.imageCount.setText(imageFolder.getImages().size() + "张");

        // 加载图片到九宫格
        Glide.with(contentView.getContext()).load(Uri.parse("file://" + imageFolder.getCover().getPath()).toString()).into(holder.cover);

        if (lastSelected == position) {
            holder.folderCheck.setImageResource(R.mipmap.list_selected);
        } else {
            holder.folderCheck.setImageResource(R.mipmap.list_unselected);
        }

        return contentView;
    }

    public void refreshData(List<ImageFolder> imageFolders) {
        if (imageFolders != null && !imageFolders.isEmpty()) {
            this.mImageFolders = imageFolders;
        } else {
            this.mImageFolders.clear();
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView cover;
        TextView folderName;
        TextView imageCount;
        ImageView folderCheck;

        public ViewHolder(View view) {
            cover = (ImageView) view.findViewById(R.id.iv_cover);
            folderName = (TextView) view.findViewById(R.id.tv_folder_name);
            imageCount = (TextView) view.findViewById(R.id.tv_image_count);
            folderCheck = (ImageView) view.findViewById(R.id.iv_folder_check);
            view.setTag(this);
        }
    }
}
