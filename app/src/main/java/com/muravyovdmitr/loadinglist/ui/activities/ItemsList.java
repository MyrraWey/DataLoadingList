package com.muravyovdmitr.loadinglist.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.muravyovdmitr.loadinglist.R;
import com.muravyovdmitr.loadinglist.adapters.InvalidateMenuFromAdapter;
import com.muravyovdmitr.loadinglist.adapters.SimpleItemsListAdapter;
import com.muravyovdmitr.loadinglist.adapters.decorations.SimpeItemsListDecoration;
import com.muravyovdmitr.loadinglist.data.SimpleItem;
import com.muravyovdmitr.loadinglist.data.SimpleItemsManager;

import java.util.List;

public class ItemsList extends AppCompatActivity {
    RecyclerView mRecyclerView;

    private SimpleItemsManager mItemsManager;
    private List<SimpleItem> mItems;
    private SimpleItemsListAdapter mItemsListAdapter;

    private final InvalidateMenuFromAdapter mInvalidateMenuFromAdapter = new InvalidateMenuFromAdapter() {
        @Override
        public void invalidateMenu() {
            invalidateOptionsMenu();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_items_recycler_view);

        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.items_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_items_list_load:
                Builder dialog = mItemsListAdapter.isAnyItemSelected() ? getDataLoadingDialog()
                        : getNegativeDataLoadingDialog();
                dialog.show();
                return true;
            case R.id.menu_items_list_release:
                mItemsListAdapter.clearSelection();
                item.setVisible(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.menu_items_list_release).setVisible(mItemsListAdapter.isAnyItemSelected());

        return true;
    }

    private void initData() {
        mItemsManager = new SimpleItemsManager();
        mItems = mItemsManager.getItems();
        mItemsListAdapter = new SimpleItemsListAdapter(mItems);
        mItemsListAdapter.setInvalidateMenuFromAdapter(mInvalidateMenuFromAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SimpeItemsListDecoration());
        mRecyclerView.setAdapter(mItemsListAdapter);
    }

    private Builder getNegativeDataLoadingDialog() {
        return new Builder(this).setTitle("Any selected items")
                .setMessage("Select items before updating updating")
                .setPositiveButton(android.R.string.ok, null);
    }

    private Builder getDataLoadingDialog() {
        String text = "Items to update: ";
        for (int i : mItemsListAdapter.getSelectedItemsPositions()) {
            text += i;
        }

        return new Builder(this).setTitle("Update items")
                .setMessage(text)
                .setPositiveButton(android.R.string.ok, null);
    }
}
