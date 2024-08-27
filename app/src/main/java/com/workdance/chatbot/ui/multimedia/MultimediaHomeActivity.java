package com.workdance.chatbot.ui.multimedia;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.chatbot.R;
import com.workdance.core.BaseActivity;
import com.workdance.chatbot.databinding.ActivityMmHomeBinding;
import com.workdance.chatbot.model.PlayScene;

import java.util.ArrayList;
import java.util.List;

public class MultimediaHomeActivity extends BaseActivity {
    private ActivityMmHomeBinding binding;
    private RecyclerView mRecyclerView;
    private final List<Item> mItems = new ArrayList<>();
    private MultimediaHomeActivity mActivity;

    public static void intentInto(Activity activity) {
        Intent intent = new Intent(activity, MultimediaHomeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int toolbarDisplayType() {
        return BaseActivity.TOOLBAR_HIDDEN;
    }

    @Override
    protected void beforeViews() {
        binding = ActivityMmHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        this.addStatusBarHeight(binding.actionBack);
        mActivity = this;
        mRecyclerView = binding.recyclerView;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_mm_list_item, parent, false);
                return new RecyclerView.ViewHolder(rootView) { /* ignore */
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                Item item = mItems.get(position);
                TextView title = holder.itemView.findViewById(R.id.title);
                ImageView image = holder.itemView.findViewById(R.id.image);

                title.setText(item.title);
                if (item.drawable != 0) {
                    image.setImageDrawable(ResourcesCompat.getDrawable(image.getResources(), item.drawable, null));
                } else {
                    image.setImageDrawable(null);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       switch (item.type) {
                           case Item.TYPE_PLAY_SCENE:
                               VideoActivity.intentInto(mActivity, item.playScene);
                               break;
                           case Item.TYPE_MINI_DRAMA:
                               DramaMainActivity.intentInto(mActivity);
                               break;
                           case Item.TYPE_SETTINGS:
                               break;
                       }
                   }
                });

            }

            @Override
            public int getItemCount() {
                return mItems.size();
            }
        });

        mItems.clear();
        mItems.addAll(createItems());
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private static List<Item> createItems() {
        final List<Item> items = new ArrayList<>();
        items.add(new Item(R.string.mm_mini_drama, R.drawable.vevod_main_list_item_mini_drama_ic, Item.TYPE_MINI_DRAMA,
                PlayScene.SCENE_UNKNOWN));
        items.add(new Item(R.string.mm_short_video, R.drawable.vevod_main_scene_list_item_short_ic, Item.TYPE_PLAY_SCENE,
                PlayScene.SCENE_SHORT));
        items.add(new Item(R.string.mm_feed_video, R.drawable.vevod_main_scene_list_item_feed_ic, Item.TYPE_PLAY_SCENE,
                PlayScene.SCENE_FEED));
        items.add(new Item(R.string.mm_long_video, R.drawable.vevod_main_scene_list_item_long_ic, Item.TYPE_PLAY_SCENE,
                PlayScene.SCENE_LONG));
        return items;
    }

    @Override
    protected void bindEvents() {
        super.bindEvents();
        View back = binding.actionBack;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    static class Item {
        static final int TYPE_PLAY_SCENE = 0;
        static final int TYPE_MINI_DRAMA = 1;
        static final int TYPE_SETTINGS = 2;

        @StringRes
        int title;
        @DrawableRes
        int drawable;
        int playScene;
        int type;

        Item(int name, int drawable, int type, int playScene) {
            this.title = name;
            this.drawable = drawable;
            this.type = type;
            this.playScene = playScene;
        }
    }
}
