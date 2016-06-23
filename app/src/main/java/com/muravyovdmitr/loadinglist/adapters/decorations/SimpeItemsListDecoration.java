package com.muravyovdmitr.loadinglist.adapters.decorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Dima Muravyov on 23.06.2016.
 */
public class SimpeItemsListDecoration extends RecyclerView.ItemDecoration {
    private final int mVerticalSpaceHeight = 16;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount()) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }
}
