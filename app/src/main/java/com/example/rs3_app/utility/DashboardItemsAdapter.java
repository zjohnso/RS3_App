package com.example.rs3_app.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rs3_app.R;
import com.example.rs3_app.objects.TradableItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DashboardItemsAdapter extends RecyclerView.Adapter<DashboardItemsAdapter.ViewHolder> {

    private List<TradableItem> items;
    LayoutInflater inflater;

    public DashboardItemsAdapter(Context c, List<TradableItem> items) {
        this.items = items;
        inflater = LayoutInflater.from(c);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.db_item_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemName.setText(items.get(position).getName());
        String icUrl = "https://secure.runescape.com/m=itemdb_rs/1595239532091_obj_sprite.gif?id=";
        int itemId = items.get(position).getId();
        Picasso.get().load(icUrl + itemId).centerInside().fit().into(holder.itemIcon);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        ImageView itemIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.db_item_name);
            itemIcon = itemView.findViewById(R.id.db_item_icon);
        }
    }

}
