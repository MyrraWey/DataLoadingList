package com.muravyovdmitr.loadinglist.adapters.holders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.muravyovdmitr.loadinglist.R;
import com.muravyovdmitr.loadinglist.data.SimpleItem;

/**
 * Created by Dima Muravyov on 23.06.2016.
 */
public class SimpleItemsListViewHolder extends RecyclerView.ViewHolder {
    private TextView mItemTitle;
    private ProgressBar mItemProgress;
    private ImageView mItemImage;

    private Context mContext;
    private SimpleItem mItem;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mItem.setLoad(!mItem.isLoad());
            changeItemStatusImage(mItem.isLoad());
        }
    };

    public SimpleItemsListViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(mOnClickListener);

        mContext = itemView.getContext();

        mItemTitle = (TextView) itemView.findViewById(R.id.holder_list_item_title);
        mItemProgress = (ProgressBar) itemView.findViewById(R.id.holder_list_item_progress);
        mItemImage = (ImageView) itemView.findViewById(R.id.holder_list_item_image);
    }

    public void bind(SimpleItem item) {
        mItem = item;
        itemView.setActivated(true);

        mItemTitle.setText(mItem.getTittle());
        changeItemStatusImage(mItem.isLoad());
    }

    public void changeItemStatusImage(boolean isLoad) {
        int imageId = isLoad ? R.drawable.item_is_loaded : R.drawable.item_is_not_loaded;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mItemImage.setImageDrawable(mContext.getResources().getDrawable(imageId));
        } else {
            mItemImage.setImageDrawable(mContext.getDrawable(imageId));
        }

        mItemProgress.setVisibility(View.INVISIBLE);
        mItemImage.setVisibility(View.VISIBLE);
    }
}
