package com.fit2081.warehouseinventoryapp.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {

    // LiveData makes output observable.
    @Query("select * from items")
    LiveData<List<Item>> getAllItems();

    @Query("select * from items where itemId=:id")
    List<Item> getItem(int id);

    @Insert
    void insertItem(Item item);

    @Query("delete from items where itemId=:id")
    void deleteItem(int id);

    // Delete everything in items table.
    @Query("delete from items")
    void deleteAllItems();

}
