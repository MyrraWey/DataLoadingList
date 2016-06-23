package com.muravyovdmitr.loadinglist.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.muravyovdmitr.loadinglist.R;
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
                Builder dialog = getNegativeDataLoadingDialog();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initData() {
        mItemsManager = new SimpleItemsManager();
        mItems = mItemsManager.getItems();
        mItemsListAdapter = new SimpleItemsListAdapter(mItems);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SimpeItemsListDecoration());
        mRecyclerView.setAdapter(mItemsListAdapter);
    }

    private Builder getNegativeDataLoadingDialog() {
        return new Builder(this).setTitle("Any selected items")
                .setMessage("Select items for updating")
                .setPositiveButton(android.R.string.ok, null);
    }
}
