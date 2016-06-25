package com.muravyovdmitr.loadinglist.adapters.holders;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.muravyovdmitr.loadinglist.R;
import com.muravyovdmitr.loadinglist.adapters.OnSimpleItemLongClick;
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
    private OnSimpleItemLongClick mOnSimpleItemLongClick;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            itemView.setSelected(!itemView.isSelected());

            changeBackgroundAccordingToSelection(itemView.isSelected());

            if (mOnSimpleItemLongClick != null) {
                mOnSimpleItemLongClick.OnLongClick(getAdapterPosition(), itemView.isSelected());
            }
        }
    };

    private final View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            mItem.setLoad(!mItem.isLoad());
            changeItemStatusImage(mItem.isLoad());

            return true;
        }
    };

    public SimpleItemsListViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(mOnClickListener);
        itemView.setOnLongClickListener(mOnLongClickListener);

        mContext = itemView.getContext();

        mItemTitle = (TextView) itemView.findViewById(R.id.holder_list_item_title);
        mItemProgress = (ProgressBar) itemView.findViewById(R.id.holder_list_item_progress);
        mItemImage = (ImageView) itemView.findViewById(R.id.holder_list_item_image);
    }

    public void bind(SimpleItem item, boolean isSelected) {
        itemView.setSelected(isSelected);
        changeBackgroundAccordingToSelection(isSelected);

        mItem = item;

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

    public void setOnSimpleItemLongClick(OnSimpleItemLongClick onSimpleItemLongClick){
        mOnSimpleItemLongClick = onSimpleItemLongClick;
    }

    private void changeBackgroundAccordingToSelection(boolean isSelected){
        if(isSelected){
            itemView.setBackgroundResource(R.color.holder_list_item_background_selected);
        } else {
            itemView.setBackgroundResource(R.color.holder_list_item_background);
        }
    }

    public void loadItem(){
        LoadItem loadItem = new LoadItem(mItem.getLoadingTime());
        loadItem.execute();
    }

    private class LoadItem extends AsyncTask<Void, Integer, Void> {
        private int mLoadingTime;

        public LoadItem(int loadTime){
            mLoadingTime = loadTime;

            mItemProgress.setMax(loadTime);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mItemImage.setVisibility(View.INVISIBLE);
            mItemProgress.setVisibility(View.VISIBLE);
            mItemProgress.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... params) {
            int currentLoadingTime = 0;
            while (currentLoadingTime++ < mLoadingTime){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress(currentLoadingTime);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mItemProgress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mItem.setLoad(true);
            changeItemStatusImage(true);
        }
    }
}
