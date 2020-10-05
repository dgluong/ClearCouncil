package ca.bcit.clearcouncil;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import ca.bcit.clearcouncil.fragments.CouncilFragment;
import ca.bcit.clearcouncil.fragments.DashboardFragment;
import ca.bcit.clearcouncil.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new DashboardFragment());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(this);
    }


    //Load's the fragment depending on what's clicked
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    //Navigation item selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        BottomNavigationView navigation = findViewById(R.id.nav_view);
        int currentItemId = navigation.getSelectedItemId();
        if (menuItem.getItemId() == currentItemId) {
            return false;
        } else {
            switch (menuItem.getItemId()) {
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    break;

                case R.id.navigation_search:
                    fragment = new SearchFragment();
                    break;

                case R.id.navigation_council:
                    fragment = new CouncilFragment();
                    break;

            }

            return loadFragment(fragment);
        }
    }
}
