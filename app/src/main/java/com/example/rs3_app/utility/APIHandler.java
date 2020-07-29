package com.example.rs3_app.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.widget.ImageView;

import com.example.rs3_app.MainActivity;
import com.example.rs3_app.fragments.MarketFragment;
import com.example.rs3_app.objects.TradableItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class APIHandler {

    public static void getItemDatabase(Context context) {
        new GetItemDatabase(context).execute();
    }

    public static void loadGraphData(MarketFragment fragment, TradableItem item, int position) {
        new LoadGraphData(fragment, item, position).execute();
    }

    private static class LoadGraphData extends AsyncTask<String, Void, Void> {
        private MarketFragment fragment;
        private TradableItem item;
        private int position;

        LoadGraphData(MarketFragment fragment, TradableItem item, int position) {
            this.fragment = fragment;
            this.item = item;
            this.position = position;
        }

        @Override
        protected Void doInBackground(String... urls) {
            StringBuilder content = new StringBuilder();
            List<String> timeStamps = new ArrayList<>();
            List<Integer> price = new ArrayList<>();
            List<String> timeStampsAvg = new ArrayList<>();
            List<Integer> priceAvg = new ArrayList<>();
            try {
                String url = "https://secure.runescape.com/m=itemdb_rs/api/graph/" + item.getId() + ".json";
                URL itemGraphData = new URL(url);
                BufferedReader itemReader = new BufferedReader(new InputStreamReader(itemGraphData.openStream()));
                String line;
                while ((line = itemReader.readLine()) != null)
                {
                    content.append(line).append("\n");
                }
                itemReader.close();

                JsonReader reader = new JsonReader(new StringReader(content.toString()));
                reader.beginObject();
                reader.nextName();
                reader.beginObject();
                while (reader.hasNext()) {
                    timeStamps.add(reader.nextName());
                    price.add(reader.nextInt());
                }
                reader.endObject();
                reader.nextName();
                reader.beginObject();
                while (reader.hasNext()) {
                    timeStampsAvg.add(reader.nextName());
                    priceAvg.add(reader.nextInt());
                }
                reader.endObject();
                reader.endObject();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            item.setChartData(timeStamps, timeStampsAvg, price, priceAvg);
            return null;
        }

        protected void onPostExecute(Void nothing) {
            fragment.notifyChartDataLoaded(position);
        }
    }

    private static class GetItemDatabase extends AsyncTask<String, Void, List<TradableItem>> {
        private WeakReference<Context> c;

        GetItemDatabase(Context context) {
            c = new WeakReference<>(context);
        }

        @Override
        protected List<TradableItem> doInBackground(String... urls) {
            StringBuilder content = new StringBuilder();
            List<TradableItem> items = new ArrayList<>();
            try {
                URL itemDB = new URL("https://raw.githubusercontent.com/zjohnso/rs3-ge-app/master/Python/RS3_Items_Detail.json");
                BufferedReader itemReader = new BufferedReader(new InputStreamReader(itemDB.openStream()));
                String line;
                while ((line = itemReader.readLine()) != null)
                {
                    content.append(line).append("\n");
                }
                itemReader.close();

                JsonReader reader = new JsonReader(new StringReader(content.toString()));
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    reader.nextName();
                    int id = reader.nextInt();
                    reader.nextName();
                    String name = reader.nextString();
                    reader.nextName();
                    String category = reader.nextString();
                    reader.nextName();
                    String description = reader.nextString();
                    reader.endObject();
                    items.add(new TradableItem(id, name, category, description));
                }
                reader.endArray();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return items;
        }

        protected void onPostExecute(List<TradableItem> items) {
            DataHandler.storeItemDB(items);
            ((MainActivity) c.get()).doneLoadingToast();
        }
    }

}
