package com.example.rs3_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.rs3_app.R;
import com.example.rs3_app.objects.TradableItem;
import com.example.rs3_app.utility.APIHandler;
import com.example.rs3_app.utility.DataHandler;
import com.example.rs3_app.utility.MarketItemsAdapter;

import java.util.List;

public class MarketFragment extends Fragment {

    private RecyclerView mktItemList;
    private MarketItemsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_market, container, false);
        setHasOptionsMenu(true);
        mktItemList = v.findViewById(R.id.market_items);

        final List<TradableItem> tradableItems = DataHandler.getItemDB();

        adapter = new MarketItemsAdapter(v.getContext(), tradableItems);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        mktItemList.setLayoutManager(linearLayoutManager);
        mktItemList.setAdapter(adapter);

        ((SimpleItemAnimator) mktItemList.getItemAnimator()).setSupportsChangeAnimations(false);

        adapter.setOnItemClickListener(new MarketItemsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TradableItem item = tradableItems.get(position);
                item.setExpanded(!tradableItems.get(position).isExpanded());
                if (!item.hasGraphData()) {
                    APIHandler.loadGraphData(getSelf(), item, position);
                }
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onFavoriteClick(int position) {
                TradableItem favItem = tradableItems.get(position);
                favItem.toggleFavorite();
                adapter.notifyItemChanged(position);
            }
        });

        return v;
    }

    private MarketFragment getSelf() {
        return this;
    }

    public void notifyChartDataLoaded(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
