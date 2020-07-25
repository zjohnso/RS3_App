package com.example.rs3_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rs3_app.R;
import com.example.rs3_app.utility.DashboardItemsAdapter;
import com.example.rs3_app.utility.SkillGridAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView skillGrid;
    List<String> skillLevels;
    SkillGridAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        skillGrid = v.findViewById(R.id.skill_grid);

        skillLevels = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            skillLevels.add("120");
        }

        adapter = new SkillGridAdapter(v.getContext(), skillLevels);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(v.getContext(), 3, GridLayoutManager.VERTICAL, false);
        skillGrid.setLayoutManager(gridLayoutManager);
        skillGrid.setAdapter(adapter);

        return v;
    }
}
