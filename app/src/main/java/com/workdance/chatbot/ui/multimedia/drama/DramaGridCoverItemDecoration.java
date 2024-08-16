package com.workdance.chatbot.ui.multimedia.drama;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.core.util.ViewUtils;

public class DramaGridCoverItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final int position = parent.getChildAdapterPosition(view);

        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();

        final int itemCount = parent.getAdapter().getItemCount();
        final int spanCount = layoutManager.getSpanCount();

        // Grid position. Imagine all items ordered in x/y axis
        int x = position % spanCount;
        int y = position / spanCount;

        int yCount = itemCount % 3 == 0 ? itemCount / 3 : itemCount / 3 + 1;

        // Saved size
        int _16dp = (int) ViewUtils.dip2Px(view.getContext(), 16);
        int _8dp = (int) ViewUtils.dip2Px(view.getContext(), 8);

        // Conditions in row and column
        if (x == 0) {
            outRect.left = _16dp;
            outRect.right = 0;
        } else if (x == spanCount - 1) {
            outRect.right = _16dp;
            outRect.left = 0;
        } else {
            outRect.left = _8dp;
            outRect.right = _8dp;
        }
        if (y == 0) {
            outRect.top = _16dp;
            outRect.bottom = _16dp/2;
        } else if (y == yCount - 1) {
            outRect.top = _16dp / 2;
            outRect.bottom = _16dp;
        } else {
            outRect.top = _16dp / 2;
            outRect.bottom = _16dp / 2;
        }

    }
}
