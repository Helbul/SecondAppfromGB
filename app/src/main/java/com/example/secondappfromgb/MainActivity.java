package com.example.secondappfromgb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements ToolbarHolder, OnExitDialogListener{
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout = findViewById(R.id.drawer);

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_drawer_notes:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new NoteListFragment())
                                .addToBackStack("")
                                .commit();
                        drawerLayout.close();
                        return true;
                    case R.id.menu_drawer_about_us:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new AboutUsFragment())
                                .addToBackStack("")
                                .commit();
                        drawerLayout.close();
                        return true;
                    case R.id.menu_drawer_settings:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new SettingsFragment())
                                .addToBackStack("")
                                .commit();
                        drawerLayout.close();
                        return true;
                    case R.id.menu_drawer_exit:
                        showExitDialogFragment();
                        drawerLayout.close();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public void showExitDialogFragment(){
        ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
//        exitDialogFragment.setOnClickListener(new OnExitDialogListener() {
//            @Override
//            public void onExitDialogOk() {
//
//            }
//        });
        exitDialogFragment.show(getSupportFragmentManager(), ExitDialogFragment.TAG);
    }

    @Override
    public void onExitDialogOk() {
        Toast.makeText(getApplicationContext(), "Выход", Toast.LENGTH_LONG).show();
    }
}