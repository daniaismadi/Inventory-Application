package com.fit2081.warehouseinventoryapp.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {

    private ItemRepository mItemRepository;
    private LiveData<List<Item>> mAllItems;

    public ItemViewModel(@NonNull Application app) {
        super(app);

        mItemRepository = new ItemRepository(app);
        mAllItems = mItemRepository.getAllItemsRepo();
    }

    public LiveData<List<Item>> getAllItemsVM() {
        return mAllItems;
    }

    public void insertItemVM(Item item) {
        mItemRepository.insertItemRepo(item);
    }

    public void deleteAllItemsVM() {
        mItemRepository.deleteAllItemsRepo();
    }

    public List<Item> getItemByIdVM(int id) {
        return mItemRepository.getItemById(id);
    }

    public void deleteItemByIdVM(int id) {
        mItemRepository.deleteItemById(id);
    }

}
