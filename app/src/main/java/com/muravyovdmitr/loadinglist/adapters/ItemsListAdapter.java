package com.muravyovdmitr.loadinglist.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muravyovdmitr.loadinglist.R;
import com.muravyovdmitr.loadinglist.adapters.holders.ItemsListViewHolder;
import com.muravyovdmitr.loadinglist.data.Item;

import java.util.List;

/**
 * Created by Dima Muravyov on 23.06.2016.
 */
public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListViewHolder> {
    private List<Item> mItems;
    private SparseBooleanArray mSelectedItems;
    private SparseBooleanArray mLoadingItems;
    private MenuInvalidator mMenuInvalidator;

    public ItemsListAdapter(List<Item> items) {
        mItems = items;

        mSelectedItems = new SparseBooleanArray();
        mLoadingItems = new SparseBooleanArray();
    }

    private final ItemLongClickListener mItemLongClickListener = new ItemLongClickListener() {
        @Override
        public void OnLongClick(int position, boolean isSelected) {
            if (isSelected) {
                mSelectedItems.append(position, isSelected);
            } else {
                mSelectedItems.delete(position);
            }

            if (mMenuInvalidator != null) {
                mMenuInvalidator.invalidateMenu();
            }
        }
    };

    @Override
    public ItemsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.holder_list_item, parent, false);

        ItemsListViewHolder itemsListViewHolder = new ItemsListViewHolder(view);
        itemsListViewHolder.setItemLongClickListener(mItemLongClickListener);

        return itemsListViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemsListViewHolder holder, int position) {
        boolean isItemSelected = mSelectedItems.get(position, false);
        holder.bind(mItems.get(position), isItemSelected);
        if (mLoadingItems.get(position, false)) {
            holder.loadItem();
            mLoadingItems.delete(position);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setMenuInvalidator(MenuInvalidator menuInvalidator) {
        mMenuInvalidator = menuInvalidator;
    }

    public boolean isAnyItemSelected() {
        return mSelectedItems.size() > 0;
    }

    public int[] getSelectedItemsPositions() {
        int[] positions = new int[mSelectedItems.size()];

        for (int i = 0; i < mSelectedItems.size(); i++) {
            positions[i] = mSelectedItems.keyAt(i);
        }

        return positions;
    }

    public void selectAll() {
        mSelectedItems = new SparseBooleanArray(mItems.size());
        for (int i = 0; i < mItems.size(); i++) {
            mSelectedItems.put(i, true);
        }

        notifyItemRangeChanged(0, mSelectedItems.size());
    }

    public void loadItems() {
        mLoadingItems = mSelectedItems;
        clearSelection();

        for (int i = 0; i < mLoadingItems.size(); i++) {
            notifyItemChanged(mLoadingItems.keyAt(i));
        }
    }

    public void clearSelection() {
        int[] selectedPositions = getSelectedItemsPositions();

        mSelectedItems = new SparseBooleanArray();

        for (int i : selectedPositions) {
            notifyItemChanged(i);
        }
    }
}
