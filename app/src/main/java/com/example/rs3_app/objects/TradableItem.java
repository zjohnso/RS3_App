package com.example.rs3_app.objects;

import android.content.Context;
import android.graphics.DashPathEffect;

import androidx.core.content.ContextCompat;

import com.example.rs3_app.R;
import com.example.rs3_app.utility.DataHandler;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TradableItem {

    private int id;
    private String name;
    private String category;
    private String description;
    private boolean favorite;
    private boolean expanded;
    private boolean firstGraphLoad;

    private List<Integer> prices;
    private List<Date> dates;
    private List<Integer> pricesAvg;

    public TradableItem(int id, String name, String category, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        favorite = false;
        expanded = false;
        firstGraphLoad = true;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LineDataSet getLineDataMonth(Context c) {
        ArrayList<Entry> yValues = new ArrayList<>();
        List<Integer> prices = getPricesMonth();
        int entries = prices.size();
        for (int i = 0; i < entries; i++) {
            yValues.add(new Entry(i, prices.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(yValues, "Actual Price");
        dataSet.setColor(ContextCompat.getColor(c, R.color.goldAccent));
        dataSet.setCircleColor(ContextCompat.getColor(c, R.color.menuUnselectedText));
        dataSet.setCircleHoleColor(ContextCompat.getColor(c, R.color.trim));
        dataSet.setHighLightColor(ContextCompat.getColor(c, R.color.menuUnselectedText));
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setDrawValues(false);

        return dataSet;
    }

    public LineDataSet getLineDataAvgMonth(Context c) {
        ArrayList<Entry> yValues = new ArrayList<>();
        List<Integer> prices = getPricesAvgMonth();
        int entries = prices.size();
        for (int i = 0; i < entries; i++) {
            yValues.add(new Entry(i, prices.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(yValues, "Average Price");
        dataSet.setColor(ContextCompat.getColor(c, R.color.darkerGoldAccent));
        dataSet.setDrawCircles(false);
        dataSet.enableDashedLine(20, 10, 0);
        dataSet.setHighLightColor(ContextCompat.getColor(c, R.color.menuUnselectedText));
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setDrawValues(false);

        return dataSet;
    }

    public List<Date> getDatesMonth() {
        return dates.subList(dates.size() - 31, dates.size());
    }

    public List<Integer> getPricesMonth() {
        return prices.subList(prices.size() - 31, prices.size());
    }

    public List<Integer> getPricesAvgMonth() {
        return pricesAvg.subList(pricesAvg.size() - 31, pricesAvg.size());
    }

    public boolean isFirstGraphLoad() {
        return firstGraphLoad;
    }

    public void setFirstGraphLoad(boolean firstGraphLoad) {
        this.firstGraphLoad = firstGraphLoad;
    }

    public void setChartData(List<String> timeStamps, List<String> timeStampsAvg,
                             List<Integer> prices, List<Integer> pricesAvg) {
        this.prices = prices;
        this.pricesAvg = pricesAvg;

        dates = new ArrayList<>();
        for (String stamp : timeStamps) {
            long ts = Long.parseLong(stamp);
            Date d = new Date(ts);
            dates.add(d);
        }
    }

    public boolean hasGraphData() {
        return !(dates == null);
    }

    public List<Integer> getPrices() {
        return prices;
    }

    public List<Integer> getPricesAvg() {
        return pricesAvg;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void toggleFavorite() {
        if (!favorite) {
            favorite = true;
            DataHandler.addFavoriteItem(this);
        } else {
            DataHandler.removeFavoriteItem(this);
            favorite = false;
        }
    }

    public boolean getFavorite() {
        return favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradableItem that = (TradableItem) o;
        return id == that.id &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
