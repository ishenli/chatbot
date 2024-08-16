package com.workdance.chatbot.ui.multimedia.drama;

import static com.workdance.core.util.DisplayModeHelper.calDisplayAspectRatio;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.workdance.chatbot.R;
import com.workdance.chatbot.ui.multimedia.model.DramaInfo;
import com.workdance.core.util.DisplayModeHelper;

import java.util.ArrayList;
import java.util.List;

public class DramaGridCoverAdapter extends RecyclerView.Adapter<DramaGridCoverAdapter.ViewHolder> {
    private final List<DramaInfo> mItems = new ArrayList<>();

    public void setItems(List<DramaInfo> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void appendItems(List<DramaInfo> items) {
        if (items != null && !items.isEmpty()) {
            int count = mItems.size();
            mItems.addAll(items);
            if (count > 0) {
                notifyItemRangeInserted(count, mItems.size());
            } else {
                notifyDataSetChanged();
            }
        }
    }

    @NonNull
    @Override
    public DramaGridCoverAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull DramaGridCoverAdapter.ViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    protected DramaInfo getItem(int position) {
        return mItems.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final DisplayModeHelper displayModeHelper;

        static ViewHolder create(ViewGroup parent) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_drama_grid_cover_item, parent, false));
        }

        public final ImageView cover;
        public final TextView title;
        public final TextView desc;
        public final TextView playCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.cover);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            playCount = itemView.findViewById(R.id.playCount);

            displayModeHelper = new DisplayModeHelper();
            displayModeHelper.setDisplayMode(DisplayModeHelper.DISPLAY_MODE_ASPECT_FILL);
            displayModeHelper.setDisplayView(cover);
            displayModeHelper.setContainerView((FrameLayout) cover.getParent());
        }

        public void bind(DramaInfo drama) {
            title.setText(drama.dramaTitle);
            // 一共多少集
            desc.setText(String.format(desc.getResources().getString(R.string.vevod_mini_drama_grid_cover_item_total_desc), drama.totalEpisodeNumber));

            // 渲染封面
            Glide.with(cover).load(drama.coverUrl).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    displayModeHelper.setDisplayAspectRatio(calDisplayAspectRatio(resource.getIntrinsicWidth(), resource.getIntrinsicHeight(), 0));
                    return false;
                }
            }).into(cover);
        }
    }
}
