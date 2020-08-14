package com.fit2081.warehouseinventoryapp.provider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// If there is a higher version, Android application will automatically replace lower version
// with higher version
@Database(entities={Item.class}, version=1)
public abstract class ItemDatabase extends RoomDatabase {

    public static final String ITEM_DATABASE_NAME = "item_database";
    public abstract ItemDao itemDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile ItemDatabase INSTANCE;

    // number of threads we use to execute everything in the database
    private static final int NUMBER_OF_THREADS = 4;

    // needed to manipulate database
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // return instance of this database, if database does not exist, create a database
    static ItemDatabase getDatabase(final Context context) {
        // database does not exist
        if (INSTANCE == null) {
            // lock access to this class to ensure only one thread inside this statement at any moment
            synchronized (ItemDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            // reference to class that refers to Room Database
                            ItemDatabase.class,
                            // item database name stored locally
                            ITEM_DATABASE_NAME).build();    // build generates tables and implements
                                                            // interface for DAO
                }
            }
        }

        return INSTANCE;
    }
}
