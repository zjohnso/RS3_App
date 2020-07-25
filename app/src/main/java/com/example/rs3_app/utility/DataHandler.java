package com.example.rs3_app.utility;

import com.example.rs3_app.objects.TradableItem;

import java.util.ArrayList;
import java.util.List;

public class DataHandler {

    private static List<TradableItem> itemDB;
    private static List<TradableItem> favoriteItems = new ArrayList<>();
    private static String playerName;

    public static void storePlayerName(String name) {
        playerName = name;
    }

    public static String getPlayerName() {
        return playerName;
    }

    public static void removeFavoriteItem(TradableItem item) {
        favoriteItems.remove(item);
    }

    public static void storeItemDB(List<TradableItem> items) {
        itemDB = items;
    }

    public static void addFavoriteItem(TradableItem item) {
        favoriteItems.add(item);
    }

    public static List<TradableItem> getFavoriteItems() {
        return favoriteItems;
    }

    public static List<TradableItem> getItemDB() {
        return itemDB;
    }

}
