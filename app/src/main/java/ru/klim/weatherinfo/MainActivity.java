package ru.klim.weatherinfo;

import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;

import ru.klim.weatherinfo.utils.LocationUtils;
import ru.klim.weatherinfo.utils.PermissionsUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FusedLocationProviderClient fusedLocationClient;

    private FragmentManager fm;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        new LocationUtils().locationEnabled(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        new PermissionsUtils().checkPermissions(this);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.conteiner);
        if (fragment == null) {
            fragment = new TodayWeatherFragment();
            addFragment(fragment);
        }
    }

    public Location getLocation(){
        return mLocation;
    }

    public void addFragment(Fragment fragment) {
        fm.beginTransaction()
                .add(R.id.conteiner, fragment).commit();
    }

    public void replaceFragment(Fragment fragment){
        fm.beginTransaction()
                .replace(R.id.conteiner,fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_current:
                replaceFragment(new TodayWeatherFragment());
                break;
            case R.id.nav_week:
                replaceFragment(new WeekWeatherFragment());
                break;
            case R.id.nav_state:
                replaceFragment(new DeviceInfoFragment());
                break;
            case R.id.nav_setting:
                replaceFragment(new SettingFragment());
                break;
            case R.id.nav_exit: finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}