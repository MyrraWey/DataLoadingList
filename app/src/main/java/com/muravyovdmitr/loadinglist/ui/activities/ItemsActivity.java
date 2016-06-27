package com.muravyovdmitr.loadinglist.ui.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.muravyovdmitr.loadinglist.R;
import com.muravyovdmitr.loadinglist.adapters.ItemsListAdapter;
import com.muravyovdmitr.loadinglist.adapters.MenuInvalidator;
import com.muravyovdmitr.loadinglist.adapters.decorations.ItemsListDecoration;
import com.muravyovdmitr.loadinglist.data.Item;
import com.muravyovdmitr.loadinglist.data.ItemsManager;

import java.util.List;
import java.util.Locale;

public class ItemsActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;

    private ItemsManager mItemsManager;
    private List<Item> mItems;
    private ItemsListAdapter mItemsListAdapter;

    private final MenuInvalidator mMenuInvalidator = new MenuInvalidator() {
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
                Builder dialog = getDataLoadingDialog();
                dialog.show();
                return true;
            case R.id.menu_items_list_release:
            case R.id.menu_items_list_deselect_all:
                mItemsListAdapter.clearSelection();
                invalidateOptionsMenu();
                return true;
            case R.id.menu_items_list_select_all:
                mItemsListAdapter.selectAll();
                invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        boolean isAnyItemsSelected = mItemsListAdapter.isAnyItemSelected();
        menu.findItem(R.id.menu_items_list_release).setVisible(isAnyItemsSelected);
        menu.findItem(R.id.menu_items_list_load).setVisible(isAnyItemsSelected);

        return true;
    }

    private void initData() {
        mItemsManager = new ItemsManager();
        mItems = mItemsManager.getItems();
        mItemsListAdapter = new ItemsListAdapter(mItems);
        mItemsListAdapter.setMenuInvalidator(mMenuInvalidator);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new ItemsListDecoration());
        mRecyclerView.setAdapter(mItemsListAdapter);
    }

    private Builder getDataLoadingDialog() {
        String format = "Do you really want to update all selected items (%d)?";

        return new Builder(this).setTitle("Update items")
                .setMessage(String.format(
                        Locale.getDefault(),
                        format,
                        mItemsListAdapter.getSelectedItemsPositions().length
                ))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mItemsListAdapter.startLoadingSelectedItems();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
    }
}
