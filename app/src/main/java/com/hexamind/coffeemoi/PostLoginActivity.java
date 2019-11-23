package com.hexamind.coffeemoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class PostLoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navView;
    String username;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);

        drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        username = getIntent().getStringExtra(Constants.USERS_INTENT);
        getSupportActionBar().setTitle("Profile");
        prefs = getSharedPreferences(Constants.USER_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.USERS_INTENT, username);
        editor.apply();
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(this);

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        ProfileFragment profileFragment = new ProfileFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        profileFragment.setArguments(bundle);
        transaction.add(R.id.frameLayout, profileFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item))  {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (id) {
            case R.id.action_profile:
                transaction.replace(R.id.frameLayout, new ProfileFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                getSupportActionBar().setTitle("Profile");
                break;
            case R.id.action_create_coffee:
                transaction.replace(R.id.frameLayout, new MakeCoffeeFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                getSupportActionBar().setTitle("Create Coffee");
                break;
            case R.id.action_past_orders:
                transaction.replace(R.id.frameLayout, new ProfileFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                getSupportActionBar().setTitle("View Past Orders");
                break;
            case R.id.action_exit:
                Snackbar.make(drawer, "Thank you for using the service", Snackbar.LENGTH_SHORT).show();
                finish();
                break;
        }

        drawer.closeDrawers();
        return true;
    }
}
