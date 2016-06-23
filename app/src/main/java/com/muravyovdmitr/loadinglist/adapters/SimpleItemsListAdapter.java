package com.muravyovdmitr.loadinglist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.muravyovdmitr.loadinglist.R;
import com.muravyovdmitr.loadinglist.adapters.holders.SimpleItemsListViewHolder;
import com.muravyovdmitr.loadinglist.data.SimpleItem;

import java.util.List;

/**
 * Created by Dima Muravyov on 23.06.2016.
 */
public class SimpleItemsListAdapter extends RecyclerView.Adapter<SimpleItemsListViewHolder> {
    private List<SimpleItem> mItems;

    public SimpleItemsListAdapter(List<SimpleItem> items){
        mItems = items;
    }

    @Override
    public SimpleItemsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.holder_list_item, parent, false);

        return new SimpleItemsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleItemsListViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
