package com.example.rs3_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rs3_app.fragments.BestiaryFragment;
import com.example.rs3_app.fragments.DashboardFragment;
import com.example.rs3_app.fragments.FriendsFragment;
import com.example.rs3_app.fragments.MarketFragment;
import com.example.rs3_app.fragments.ProfileFragment;
import com.example.rs3_app.fragments.ProfitFragment;
import com.example.rs3_app.utility.APIHandler;
import com.example.rs3_app.utility.DataHandler;
import com.example.rs3_app.utility.SkillGridAdapter;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private ImageView profilePic;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Temporary until data is stored
        APIHandler.getItemDatabase(this);
        DataHandler.storePlayerName("Shtacks");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(this);

        loadProfilePic(nv.getHeaderView(0));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DashboardFragment()).commit();
            nv.setCheckedItem(R.id.nav_dashboard);
        }

    }

    private void loadProfilePic(View header) {
        profilePic = header.findViewById(R.id.header_pic);
        String playerName = DataHandler.getPlayerName();
        String imageUri = "https://secure.runescape.com/m=avatar-rs/" + playerName + "/chat.png";
        Picasso.get().load(imageUri).fit().into(profilePic);
    }

    public void doneLoadingToast() {
        Toast.makeText(this, "Done loading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DashboardFragment()).commit();
                toolbar.setTitle("Dashboard");
                break;
            case R.id.nav_bestiary:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BestiaryFragment()).commit();
                toolbar.setTitle("Bestiary");
                break;
            case R.id.nav_profit:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfitFragment()).commit();
                toolbar.setTitle("Profit Calculator"); 
                break;
            case R.id.nav_friends:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FriendsFragment()).commit();
                toolbar.setTitle("Friends");
                break;
            case R.id.nav_market:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MarketFragment()).commit();
                toolbar.setTitle("Market");
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                toolbar.setTitle("Profile");
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
