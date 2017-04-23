package upm.softwaredesign.uber;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import upm.softwaredesign.uber.fragments.MapViewFragment;
import upm.softwaredesign.uber.fragments.SelectLocationFragment;
import upm.softwaredesign.uber.fragments.TripStatusDialogFragment;
import upm.softwaredesign.uber.utilities.Constants;
import upm.softwaredesign.uber.utilities.HttpManager;

import static android.R.attr.fragment;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static upm.softwaredesign.uber.R.id.nav_view;
import static upm.softwaredesign.uber.R.id.textView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SelectLocationFragment.OnFragmentInteractionListener {

    private SelectLocationFragment mSelectLocationFragment;

    private String mTripId;
    private String mTripStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getFragmentManager();
        mSelectLocationFragment = new SelectLocationFragment();
        fm.beginTransaction().replace(R.id.content_frame, new MapViewFragment()).commit();
        fm.beginTransaction()
                .add(R.id.main_layout, mSelectLocationFragment, "select location fragment")
                .show(mSelectLocationFragment)
                .commit();

        FloatingActionButton menu = (FloatingActionButton)findViewById(R.id.floating_button_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(navigationView);
            }
        });

        //added onclick listener for profile image
        View header = navigationView.getHeaderView(0);
        View profileImage = header.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        //added onclick listener for profile name
        View profileName = header.findViewById(R.id.profile_name);
        profileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        EditText whereTo = (EditText) findViewById(R.id.destination_edit_text);
        whereTo.setKeyListener(null);

        //add onclick listener for selecting location
        View selectLocation = findViewById(R.id.destination_edit_text);
        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionLocationFragment();
            }
        });

        Intent tripStatusIntent = getIntent();
        if (tripStatusIntent.getFlags() == Constants.TRIP_STATUS_INTENT_FLAG) {
            if (tripStatusIntent != null) {
                Bundle bundle = tripStatusIntent.getExtras();
                if (bundle != null) {
                    mTripId = bundle.getString(Constants.TRIP_ID);
                    mTripStatus = bundle.getString(Constants.TRIP_STATUS);
                    showTripStatus(mTripId, mTripStatus);
                }
            }
        }
    }

    public void showSelectionLocationFragment() {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .addToBackStack(null)
                .show(mSelectLocationFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();
        int id = item.getItemId();
/*        if (id == R.id.nav_map) {
            fm.beginTransaction().replace(R.id.content_frame, new MapViewFragment()).commit();
        } else */
        if (id == R.id.nav_logout) {
            new HttpManager(this).sendLogout();
        } else if (id == R.id.nav_about) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showTripStatus(String tripID, String tripStatus) {
        FragmentManager fm = getFragmentManager();
        TripStatusDialogFragment tripStatusDialogFragment = TripStatusDialogFragment.newInstance(tripID, tripStatus);
        tripStatusDialogFragment.show(fm, "trip_status_tag");
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
