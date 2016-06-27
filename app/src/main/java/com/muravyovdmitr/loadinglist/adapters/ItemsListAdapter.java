package com.muravyovdmitr.loadinglist.adapters;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muravyovdmitr.loadinglist.R;
import com.muravyovdmitr.loadinglist.adapters.holders.ItemsListViewHolder;
import com.muravyovdmitr.loadinglist.data.Item;
import com.muravyovdmitr.loadinglist.utils.LoadingListApplication;

import java.util.List;

/**
 * Created by Dima Muravyov on 23.06.2016.
 */
public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListViewHolder> {
    private static final int ITEM_UPDATED = 1;
    private static final int RELOAD_ITEM = 2;

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

    private final Handler loadItem = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int itemPosition = msg.arg1;

            switch (msg.what) {
                case ITEM_UPDATED:
                    mItems.get(itemPosition).setLoad(true);
                    mLoadingItems.delete(itemPosition);
                    notifyItemChanged(itemPosition);
                    return true;
                case RELOAD_ITEM:
                    ItemsListAdapter.this.getReloadDialog(itemPosition).show();
                    return true;
                default:
                    return false;
            }
        }
    });

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

    public void startLoadingSelectedItems() {
        mLoadingItems = mSelectedItems;
        clearSelection();

        for (int i = 0; i < mLoadingItems.size(); i++) {
            loadItem(mLoadingItems.keyAt(i));
        }
    }

    public void clearSelection() {
        int[] selectedPositions = getSelectedItemsPositions();

        mSelectedItems = new SparseBooleanArray();

        for (int i : selectedPositions) {
            notifyItemChanged(i);
        }
    }

    private void loadItem(final int itemPosition) {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                Item item = mItems.get(itemPosition);
                if (item.isLoad()) {
                    loadItem.sendMessage(loadItem.obtainMessage(RELOAD_ITEM, itemPosition, 0));
                } else {
                    try {
                        Thread.sleep(item.getLoadingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    loadItem.sendMessage(loadItem.obtainMessage(ITEM_UPDATED, itemPosition, 0));
                }
            }
        }, "Load Item#" + itemPosition)).start();
    }

    private AlertDialog.Builder getReloadDialog(final int itemPosition) {
        return new AlertDialog.Builder(LoadingListApplication.getInstance())
                .setTitle("Item already loaded")
                .setMessage("Do you want to reload item #" + itemPosition)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadItem(itemPosition);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLoadingItems.delete(itemPosition);
                        notifyItemChanged(itemPosition);
                    }
                });
    }
}
