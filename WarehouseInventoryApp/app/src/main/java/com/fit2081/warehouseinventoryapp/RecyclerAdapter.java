package com.fit2081.warehouseinventoryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.warehouseinventoryapp.provider.Item;
import com.fit2081.warehouseinventoryapp.provider.ItemViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    List<Item> data = new ArrayList<>();
    ItemCardListener itemCardListener;

    public RecyclerAdapter() {}

    // to retrieve data from database
    public void setData(List<Item> data) {
        this.data = data;
    }

    public void setItemCardListener(ItemCardListener cardListener) {
        this.itemCardListener = cardListener;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate card layout.
        // Param 1: Put our view in the card layout.
        // Param 2: The ViewGroup itself.
        // Param 3: We are not attaching it immediately, only when the application starts.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_card_view,
                parent, false);
        // Create a new viewHolder that will bind all the necessary view components in this
        // card layout.
        ViewHolder viewHolder = new ViewHolder(v, itemCardListener);

        // Return this viewHolder which will be passed in as the parameter in onBindViewHolder().
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.itemCard.setText(data.get(position).getItemNameText());
        holder.costCard.setText(String.valueOf(data.get(position).getCostNum()));
        holder.quantityCard.setText(String.valueOf(data.get(position).getQuantityNum()));
        holder.descriptionCard.setText(data.get(position).getDescriptionText());
        holder.freezeCard.setText(String.valueOf(data.get(position).isToggleValue()));

        final int fPosition = position;
        // To make the card view clickable.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(v, "Item at position " + fPosition + " was clicked!",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView itemCard;
        TextView costCard;
        TextView quantityCard;
        TextView descriptionCard;
        TextView freezeCard;
        Button deleteBtnCard;

        ItemCardListener itemCardListener;

        public ViewHolder(@NonNull View itemView, ItemCardListener itemCardListener) {
            super(itemView);
            this.itemView = itemView;
            itemCard = itemView.findViewById(R.id.itemCard);
            costCard = itemView.findViewById(R.id.costCard);
            quantityCard = itemView.findViewById(R.id.quantityCard);
            descriptionCard = itemView.findViewById(R.id.descriptionCard);
            freezeCard = itemView.findViewById(R.id.freezeCard);
            deleteBtnCard = itemView.findViewById(R.id.deleteItemCard);
            this.itemCardListener = itemCardListener;

            deleteBtnCard.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    itemCardListener.onDeleteCardClick(v, getAdapterPosition());
                }
            });

        }
    }

    public interface ItemCardListener {
        void onDeleteCardClick(View v, int position);
    }
}
