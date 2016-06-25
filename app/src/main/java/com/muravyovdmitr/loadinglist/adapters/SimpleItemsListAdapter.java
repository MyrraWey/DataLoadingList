package com.muravyovdmitr.loadinglist.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muravyovdmitr.loadinglist.R;
import com.muravyovdmitr.loadinglist.adapters.holders.SimpleItemsListViewHolder;
import com.muravyovdmitr.loadinglist.data.SimpleItem;

import java.util.List;

/**
 * Created by Dima Muravyov on 23.06.2016.
 */
public class SimpleItemsListAdapter extends RecyclerView.Adapter<SimpleItemsListViewHolder> {
    private List<SimpleItem> mItems;
    private SparseBooleanArray mSelectedItems;
    private InvalidateMenuFromAdapter mInvalidateMenuFromAdapter;

    public SimpleItemsListAdapter(List<SimpleItem> items) {
        mItems = items;

        mSelectedItems = new SparseBooleanArray();
    }

    private final OnSimpleItemLongClick mOnSimpleItemLongClick = new OnSimpleItemLongClick() {
        @Override
        public void OnLongClick(int position, boolean isSelected) {
            if (isSelected) {
                mSelectedItems.append(position, isSelected);
            } else {
                mSelectedItems.delete(position);
            }

            if (mInvalidateMenuFromAdapter != null) {
                mInvalidateMenuFromAdapter.invalidateMenu();
            }
        }
    };

    @Override
    public SimpleItemsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.holder_list_item, parent, false);

        SimpleItemsListViewHolder simpleItemsListViewHolder = new SimpleItemsListViewHolder(view);
        simpleItemsListViewHolder.setOnSimpleItemLongClick(mOnSimpleItemLongClick);

        return simpleItemsListViewHolder;
    }

    @Override
    public void onBindViewHolder(SimpleItemsListViewHolder holder, int position) {
        boolean isItemSelected = mSelectedItems.get(position, false);
        holder.bind(mItems.get(position), isItemSelected);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setInvalidateMenuFromAdapter(InvalidateMenuFromAdapter invalidateMenuFromAdapter) {
        mInvalidateMenuFromAdapter = invalidateMenuFromAdapter;
    }

    public boolean isAnyItemSelected() {
        return mSelectedItems.size() > 0;
    }

    public int[] getSelectedItemsPositions(){
        int[] positions = new int[mSelectedItems.size()];

        for(int i=0;i<mSelectedItems.size();i++){
            positions[i] = mSelectedItems.keyAt(i);
        }

        return positions;
    }

    public void selectAll(){
        mSelectedItems = new SparseBooleanArray(mItems.size());
        for(int i=0;i<mItems.size();i++){
            mSelectedItems.put(i, true);
        }

        notifyItemRangeChanged(0, mSelectedItems.size());
    }

    public void clearSelection(){
        int[] selectedPositions = getSelectedItemsPositions();

        mSelectedItems = new SparseBooleanArray();

        for(int i : selectedPositions){
            notifyItemChanged(i);
        }
    }
}
