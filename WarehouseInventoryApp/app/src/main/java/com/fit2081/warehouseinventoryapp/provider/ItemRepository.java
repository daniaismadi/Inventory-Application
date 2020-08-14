package com.fit2081.warehouseinventoryapp.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemRepository {
    // to execute all operations in the TaskDao to provide them to the higher level
    private ItemDao mItemDao;
    LiveData<List<Item>> mAllItems;

    public ItemRepository(Application app) {
        // get reference to database by using getDatabase() in ItemDatabase class
        ItemDatabase db = ItemDatabase.getDatabase(app);
        mItemDao = db.itemDao();
        mAllItems = mItemDao.getAllItems();
    }

    LiveData<List<Item>> getAllItemsRepo() {
        // get fresh data
        return mItemDao.getAllItems();
    }

    List<Item> getItemById(int id) {
        return mItemDao.getItem(id);
    }

    void insertItemRepo(Item task) {
        // use the pull of threads to execute this method to run in a different thread
        // protect integrity of data
        ItemDatabase.databaseWriteExecutor.execute(() -> mItemDao.insertItem(task));
    }

    void deleteAllItemsRepo() {
        // call the DAO method to delete all items
        ItemDatabase.databaseWriteExecutor.execute(() -> mItemDao.deleteAllItems());
    }

    void deleteItemById(int id) {
        ItemDatabase.databaseWriteExecutor.execute(() -> mItemDao.deleteItem(id));
    }
}
