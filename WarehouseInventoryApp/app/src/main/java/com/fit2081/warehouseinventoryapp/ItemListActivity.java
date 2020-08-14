package com.fit2081.warehouseinventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.fit2081.warehouseinventoryapp.provider.Item;
import com.fit2081.warehouseinventoryapp.provider.ItemViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity implements RecyclerAdapter.ItemCardListener {
    public static final String TAG = "LIFE_CYCLE_TRACING";
    public static final String SP_FILE_NAME = "Testing01PreferencesFile";

    private ItemViewModel mItemViewModel;

    Toolbar toolbar;

    RecyclerView recyclerView;
    // Responsible to manage the layout of the recycler view.
    RecyclerView.LayoutManager layoutManager;
    // Adapter to adapt data into RecyclerView.
    RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        toolbar = findViewById(R.id.toolbar2);

        // Make our toolbar as our action bar.
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);

        // Configure layout for recycler view.
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Adapt data list.
        adapter = new RecyclerAdapter();
        // Set listener.
        adapter.setItemCardListener(this);
        // Set adapter to recycler view.
        recyclerView.setAdapter(adapter);

        // Retrieve the database.
        mItemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        // newData contains data from database
        mItemViewModel.getAllItemsVM().observe(this, newData -> {
            adapter.setData(newData);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onDeleteCardClick(View v, int position) {

        Item item = adapter.data.get(position);
        int key = item.getItemID();

        // Remove from card view.
        adapter.data.remove(position);
        adapter.notifyDataSetChanged();

        // Remove from database.
        mItemViewModel.deleteItemByIdVM(key);

        // Show SnackBar.
        Snackbar.make(v, item.getItemNameText() + " was deleted.", Snackbar.LENGTH_LONG).show();
    }
}
