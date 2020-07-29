package com.example.rs3_app.utility;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        holder.itemCategory.setText(item.getCategory());
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
        boolean hasChartData = item.hasGraphData();

        holder.chart1.setNoDataText("Loading data...");
        holder.chart1.setNoDataTextColor(ContextCompat.getColor(c, R.color.menuUnselectedText));
        if (hasChartData) {
            handleCharts(item, holder);
        } else {
            holder.chart1.setData(null);
        }
    }

    private void handleCharts(TradableItem item, final ViewHolder holder) {

        final List<Date> dates = item.getDatesMonth();
        XAxis x = holder.chart1.getXAxis();
        x.setLabelRotationAngle(45);
        x.setTextColor(ContextCompat.getColor(c, R.color.white));
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setLabelCount(10, false);
        x.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return new SimpleDateFormat("MMM dd", Locale.getDefault()).format(dates.get((int) value));
            }
        });

        Legend legend = holder.chart1.getLegend();
        legend.setTextColor(ContextCompat.getColor(c, R.color.subText));
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setForm(Legend.LegendForm.CIRCLE);

        holder.chart1.getAxisLeft().setTextColor(ContextCompat.getColor(c, R.color.white));
        holder.chart1.getAxisRight().setEnabled(false);
        holder.chart1.setDoubleTapToZoomEnabled(false);
        holder.chart1.setScaleEnabled(false);
        holder.chart1.setDescription(null);
        holder.chart1.setData(new LineData(item.getLineDataMonth(c), item.getLineDataAvgMonth(c)));

        if (item.isFirstGraphLoad()) {
            holder.chart1.animateX(1000);
            item.setFirstGraphLoad(false);
        } else {
            holder.chart1.setExtraBottomOffset(0);
            holder.chart1.invalidate();
        }

        holder.chart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float xVal = e.getX();
                Entry actualEntry = holder.chart1.getLineData().getDataSetByIndex(0).getEntryForXValue(xVal, 0);
                Entry avgEntry = holder.chart1.getLineData().getDataSetByIndex(1).getEntryForXValue(xVal, 0);
                String date = holder.chart1.getXAxis().getValueFormatter().getAxisLabel(xVal, holder.chart1.getXAxis());
                date = date + ":";
                String actualPrice = NumberFormat.getInstance().format((int) actualEntry.getY()) + " gp";
                String avgPrice = NumberFormat.getInstance().format((int) avgEntry.getY()) + " gp";
                holder.selPrice.setText(actualPrice);
                holder.selPriceAvg.setText(avgPrice);
                holder.selDate.setText(date);
            }

            @Override
            public void onNothingSelected() {
                holder.selPrice.setText(null);
                holder.selDate.setText(null);
                holder.selPriceAvg.setText(null);
            }
        });
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
        TextView itemCategory;
        ImageView itemIcon;
        ImageView favorite;
        LineChart chart1;
        TextView selPrice;
        TextView selDate;
        TextView selPriceAvg;
        ConstraintLayout expandableLayout;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.market_item_name);
            itemCategory = itemView.findViewById(R.id.market_item_category);
            itemIcon = itemView.findViewById(R.id.market_item_icon);
            favorite = itemView.findViewById(R.id.favorite_selector);
            selDate = itemView.findViewById(R.id.selected_date);
            selPrice = itemView.findViewById(R.id.selected_price);
            selPriceAvg = itemView.findViewById(R.id.selected_price_avg);
            chart1 = itemView.findViewById(R.id.chart1);
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
