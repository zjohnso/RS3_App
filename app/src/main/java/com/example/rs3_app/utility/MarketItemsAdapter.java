package com.example.rs3_app.utility;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rs3_app.R;
import com.example.rs3_app.objects.TradableItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MarketItemsAdapter extends RecyclerView.Adapter<MarketItemsAdapter.ViewHolder> implements Filterable {

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onFavoriteClick(int position);
    }

    private OnItemClickListener listener;
    private List<TradableItem> items;
    private List<TradableItem> itemsFull;
    private Context c;
    LayoutInflater inflater;

    public MarketItemsAdapter(Context c, List<TradableItem> items) {
        this.items = items;
        itemsFull = new ArrayList<>(items);
        inflater = LayoutInflater.from(c);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.market_item_card, parent, false);
        c = v.getContext();
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TradableItem item = items.get(position);
        final int index = position;
        holder.itemName.setText(item.getName());
        String icUrl = "https://secure.runescape.com/m=itemdb_rs/1595239532091_obj_sprite.gif?id=";
        Picasso.get().load(icUrl + item.getId()).centerInside().fit().into(holder.itemIcon);
        if (item.getFavorite()) {
            holder.favorite.setImageResource(R.drawable.ic_favorite_sel);
            int tint = ContextCompat.getColor(c, R.color.goldAccent);
            ImageViewCompat.setImageTintList(holder.favorite, ColorStateList.valueOf(tint));
        } else {
            holder.favorite.setImageResource(R.drawable.ic_favorite_unsel);
            int tint = ContextCompat.getColor(c, R.color.menuUnselectedText);
            ImageViewCompat.setImageTintList(holder.favorite, ColorStateList.valueOf(tint));
        }

        boolean isExpanded = item.isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {
        return marketItemsFilter;
    }

    private Filter marketItemsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TradableItem> filteredItems = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredItems.addAll(itemsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (TradableItem item : itemsFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredItems.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredItems;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            items.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        ImageView itemIcon;
        ImageView favorite;
        ConstraintLayout expandableLayout;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.market_item_name);
            itemIcon = itemView.findViewById(R.id.market_item_icon);
            favorite = itemView.findViewById(R.id.favorite_selector);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onFavoriteClick(position);
                        }
                    }
                }
            });
        }
    }

}
