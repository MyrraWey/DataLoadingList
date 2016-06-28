package com.muravyovdmitr.loadinglist.adapters.holders;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.muravyovdmitr.loadinglist.R;
import com.muravyovdmitr.loadinglist.adapters.ItemLongClickListener;
import com.muravyovdmitr.loadinglist.data.Item;

/**
 * Created by Dima Muravyov on 23.06.2016.
 */
public class ItemsListViewHolder extends RecyclerView.ViewHolder {
    private TextView mItemTitle;
    private ProgressBar mItemProgress;
    private ImageView mItemImage;

    private Context mContext;
    private Item mItem;
    private ItemLongClickListener mItemLongClickListener;
    private boolean isLoading;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            itemView.setSelected(!itemView.isSelected());

            changeBackgroundAccordingToSelection(itemView.isSelected());

            if (mItemLongClickListener != null) {
                mItemLongClickListener.OnLongClick(getAdapterPosition(), itemView.isSelected());
            }
        }
    };

    private final View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if(isLoading){
                return true;
            }

            mItem.setLoad(!mItem.isLoad());
            changeItemStatusImage(mItem.isLoad());

            return true;
        }
    };

    public ItemsListViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(mOnClickListener);
        itemView.setOnLongClickListener(mOnLongClickListener);

        mContext = itemView.getContext();

        mItemTitle = (TextView) itemView.findViewById(R.id.holder_list_item_title);
        mItemProgress = (ProgressBar) itemView.findViewById(R.id.holder_list_item_progress);
        mItemImage = (ImageView) itemView.findViewById(R.id.holder_list_item_image);
    }

    public void bind(Item item, boolean isSelected) {
        itemView.setSelected(isSelected);
        changeBackgroundAccordingToSelection(isSelected);

        mItem = item;

        mItemTitle.setText(mItem.getTittle());
        changeItemStatusImage(mItem.isLoad());
    }

    public void changeItemStatusImage(boolean isLoad) {
        isLoading = false;
        int imageId = isLoad ? R.drawable.item_is_loaded : R.drawable.item_is_not_loaded;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mItemImage.setImageDrawable(mContext.getResources().getDrawable(imageId));
        } else {
            mItemImage.setImageDrawable(mContext.getDrawable(imageId));
        }

        mItemProgress.setVisibility(View.INVISIBLE);
        mItemImage.setVisibility(View.VISIBLE);
    }

    public void setItemLongClickListener(ItemLongClickListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }

    private void changeBackgroundAccordingToSelection(boolean isSelected) {
        if (isSelected) {
            itemView.setBackgroundResource(R.color.holder_list_item_background_selected);
        } else {
            itemView.setBackgroundResource(R.color.holder_list_item_background);
        }
    }

    public void loadItem() {
        isLoading = true;
        mItemProgress.setIndeterminate(true);
        mItemProgress.setVisibility(View.VISIBLE);
        mItemImage.setVisibility(View.INVISIBLE);
    }
}
