package com.fit2081.warehouseinventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.fit2081.warehouseinventoryapp.provider.Item;
import com.fit2081.warehouseinventoryapp.provider.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "LIFE_CYCLE_TRACING";
    public static final String SP_FILE_NAME = "Testing01PreferencesFile";

    private ItemViewModel mItemViewModel;
    RecyclerAdapter adapter;

    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    FloatingActionButton fabBtn;
    View v;
    View constraintLayout;

    EditText itemName;
    EditText quantity;
    EditText cost;
    EditText description;
    ToggleButton tg;

    private String itemNameText;
    private int quantityNum;
    private float costNum;
    private String descriptionText;
    private boolean toggleValue;
    private int xDown;
    private int yDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);

        // Find views and assign to variables.
        itemName = findViewById(R.id.itemNameAns);
        quantity = findViewById(R.id.quantityAns);
        cost = findViewById(R.id.costAns);
        description = findViewById(R.id.descriptionAns);
        tg = findViewById(R.id.frozenItemAns);
        // Import toolbar from androidx- the latest jet pack library.
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        fabBtn = findViewById(R.id.floatingActionButton);
        // Declare constraint layout to detect gestures.
        constraintLayout = findViewById(R.id.constraintLayout);

        // Make our toolbar as our action bar.
        setSupportActionBar(toolbar);

        // Open navigation view.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        // Listen to all the changes that might happen to the hamburger icon.
        drawerLayout.addDrawerListener(toggle);
        // Sync state between the draw and the hamburger icon.
        toggle.syncState();

        // Create a new listener in order to listen to the clicks in the navigation view.
        navigationView.setNavigationItemSelectedListener(new MyNavListener());

        // Create listener for floating action button.
        fabBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Item newItem = new Item(itemNameText, quantityNum, costNum, descriptionText, toggleValue);
                mItemViewModel.insertItemVM(newItem);

                // Create Snackbar for when FAB button is clicked.
                Snackbar.make(v, "Item Added!", Snackbar.LENGTH_LONG).show();
            }
        });

        /* Request permissions to access SMS */
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        /* Create a local broadcast receiver that listens to the messages that come from
           SMSReceiver.
         */
        SMSOrderBroadcastReceiver smsOrderBroadcastReceiver = new SMSOrderBroadcastReceiver();

        /*
        Register the broadcast handler with the intent filter that is declared in the class
        SMSReceiver.
         */
        registerReceiver(smsOrderBroadcastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));

        // Initiate adapter.
        adapter = new RecyclerAdapter();

        // Retrieve the database.
        mItemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        // newData contains data from database
        mItemViewModel.getAllItemsVM().observe(this, newData -> {
            adapter.setData(newData);
            adapter.notifyDataSetChanged();
        });

        // Detect gestures on the layout.
        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // get type of click
                int action = event.getActionMasked();

                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        // get initial x and y coordinates
                        xDown = (int) event.getX();
                        yDown = (int) event.getY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        return true;
                    case MotionEvent.ACTION_UP:
                        // provide tolerance for y coordinates
                        if (Math.abs(yDown - event.getY()) < 40) {
                            if (xDown - event.getX() < 0) {
                                // this is left to right gesture
                                onAddItemClick(v);
                            } else {
                                // this is right to left gesture
                                onClearItemsClick(v);
                            }
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        Log.i(TAG, "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        restoreSharedPreferences();
        setPersistentData();

        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveVariables();
        saveSharedPreferences();

        Log.i(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle inState) {
        super.onRestoreInstanceState(inState);

        Log.i(TAG, "onRestoreInstanceState");
    }

    private void saveVariables() {
        itemNameText = itemName.getText().toString();
        quantityNum = Integer.parseInt(quantity.getText().toString());
        costNum = Float.parseFloat(cost.getText().toString());
        descriptionText = description.getText().toString();

        if (tg.isChecked()) {
            toggleValue = true;
        } else {
            toggleValue = false;
        }
    }

    private void setPersistentData() {
        itemName.setText(itemNameText);
        quantity.setText(String.valueOf(quantityNum));
        cost.setText(String.valueOf(costNum));
        description.setText(descriptionText);

        if (toggleValue) {
            tg.setChecked(true);
        } else {
            tg.setChecked(false);
        }
    }

    private void saveSharedPreferences(){
        SharedPreferences sp = getSharedPreferences(SP_FILE_NAME, 0);
        SharedPreferences.Editor myEditor = sp.edit();

        myEditor.putString("item", itemNameText);
        myEditor.putInt("quantity", quantityNum);
        myEditor.putFloat("cost", costNum);
        myEditor.putString("description", descriptionText);
        myEditor.putBoolean("toggle", toggleValue);

        myEditor.apply();
    }

    private void restoreSharedPreferences(){
        SharedPreferences sp = getSharedPreferences(SP_FILE_NAME, 0);

        itemNameText = sp.getString("item", "");
        quantityNum = sp.getInt("quantity", 0);
        costNum = sp.getFloat("cost", 0);
        descriptionText = sp.getString("description", "");
        toggleValue = sp.getBoolean("toggle", false);

    }


    public void onClearItemsClick(View view) {
        itemNameText = "";
        quantityNum = 0;
        costNum = 0;
        descriptionText = "";
        toggleValue = false;

        setPersistentData();

        itemName.requestFocus();
    }

    public void onAddItemClick(View view) {
        CharSequence text = "New item ( " + itemName.getText() + " ) has been added.";

        saveVariables();
        Item newItem = new Item(itemNameText, quantityNum, costNum, descriptionText, toggleValue);
        mItemViewModel.insertItemVM(newItem);

        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 190);
        toast.show();

    }

    public void onDeleteAllItemsClick(View view) {
        mItemViewModel.deleteAllItemsVM();
        Toast toast = Toast.makeText(this, "All items successfully deleted!",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 190);
        toast.show();
    }

    class SMSOrderBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Retrieve the message from the intent.
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);

            /*
            Use String Tokenizer to parse the incoming message- text fields are separated
            by semicolons.
             */

            StringTokenizer sT = new StringTokenizer(msg, ";");
            String itemNameMessage = sT.nextToken();
            int quantityMessage = Integer.parseInt(sT.nextToken());
            float costMessage = Float.parseFloat(sT.nextToken());
            String descriptionMessage = sT.nextToken();
            boolean toggleValueMessage = Boolean.parseBoolean(sT.nextToken());

            // Update UI.
            itemName.setText(itemNameMessage);
            quantity.setText(String.valueOf(quantityMessage));
            cost.setText(String.valueOf(costMessage));
            description.setText(descriptionMessage);

            if (toggleValueMessage) {
                tg.setChecked(true);
            } else {
                tg.setChecked(false);
            }

        }
    }

    // Create options menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    // Specify behaviour of what items in options menu should do when clicked.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.addItemOptMenu:
                onAddItemClick(v);
                break;
            case R.id.clearItemOptMenu:
                onClearItemsClick(v);
                break;
        }
        return true;
    }

    // Class to listen to the clicks on the navigation view.
    class MyNavListener implements NavigationView.OnNavigationItemSelectedListener {

        // This method will be invoked every time you press a button in the navigation drawer.
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.addItemNav) {
                onAddItemClick(v);
            } else if (id == R.id.clearFieldsNav) {
                onClearItemsClick(v);
            } else if (id == R.id.listAllItemsNav) {
                Intent i = new Intent(getApplicationContext(), ItemListActivity.class);
                startActivity(i);
            } else if (id == R.id.deleteAllItemsNav) {
                onDeleteAllItemsClick(v);
            }
            // drawerLayout.closeDrawers();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
    }
}
