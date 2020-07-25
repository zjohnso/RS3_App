package com.example.rs3_app.objects;

import com.example.rs3_app.utility.DataHandler;

import java.util.List;
import java.util.Objects;

public class TradableItem {

    private int id;
    private String name;
    private boolean favorite;
    private boolean expanded;

    private List<String> timeStamps;
    private List<Integer> prices;
    private List<String> timeStampsAvg;
    private List<Integer> pricesAvg;

    public TradableItem(int id, String name) {
        this.id = id;
        this.name = name;
        favorite = false;
        expanded = false;
    }

    public void setChartData(List<String> timeStamps, List<String> timeStampsAvg,
                             List<Integer> prices, List<Integer> pricesAvg) {
        this.prices = prices;
        this.pricesAvg = pricesAvg;
        this.timeStamps = timeStamps;
        this.timeStampsAvg = timeStampsAvg;
    }

    public boolean hasGraphData() {
        return !(timeStamps == null);
    }

    public List<Integer> getPrices() {
        return prices;
    }

    public List<Integer> getPricesAvg() {
        return pricesAvg;
    }

    public List<String> getTimeStamps() {
        return timeStamps;
    }

    public List<String> getTimeStampsAvg() {
        return timeStampsAvg;
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
