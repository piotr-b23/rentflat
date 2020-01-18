package com.project.rentflat.ui;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.project.rentflat.R;
import com.project.rentflat.session.SessionManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static SessionManager sessionManager;
    public static String userId;
    public static String TOKEN;
    public static String userName;
    public static String serverIp = "http://192.168.1.11/";
    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_my_flats, R.id.nav_my_account,
                R.id.nav_my_rates, R.id.nav_messages)
                .setDrawerLayout(drawer)
                .build();

        if (sessionManager.isLogged()) {
            userId = user.get(SessionManager.ID);
            TOKEN = user.get(SessionManager.TOKEN);
            userName = user.get(SessionManager.NAME);

        } else {
            userId = null;
            TOKEN = null;
            userName = null;
        }


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}