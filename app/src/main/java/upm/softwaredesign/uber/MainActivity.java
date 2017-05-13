package upm.softwaredesign.uber;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.Message;
import upm.softwaredesign.uber.fragments.MapViewFragment;
import upm.softwaredesign.uber.fragments.SelectLocationFragment;
import upm.softwaredesign.uber.fragments.TripStatusDialogFragment;
import upm.softwaredesign.uber.utilities.Constants;
import upm.softwaredesign.uber.utilities.HttpManager;

import static upm.softwaredesign.uber.R.id.nav_view;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SelectLocationFragment.OnFragmentInteractionListener {

    public static MainActivity mainInstance;
    private SelectLocationFragment mSelectLocationFragment;
    private MapViewFragment mMapViewFratment;
    private FloatingActionButton mFloatingActionButton;
    private NavigationView navigationView;

    private boolean requestedTripInProgress;
    private Double currentCarLocationLatitude;
    private Double currentCarLocationLongitude;

    private String mTripId;
    private String mCarId;
    private String mTripStatus;
    private AblyRealtime mRealtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainInstance = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestedTripInProgress = false;
        mFloatingActionButton   = (FloatingActionButton) findViewById(R.id.fab);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getFragmentManager();
        mSelectLocationFragment = new SelectLocationFragment();
        mMapViewFratment = new MapViewFragment();
        fm.beginTransaction().replace(R.id.content_frame, mMapViewFratment).commit();
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
                    mCarId = bundle.getString(Constants.CAR_ID);
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

    public void showAccountName(String firstName,String lastName) {
        View header = navigationView.getHeaderView(0);
        TextView nav_name = (TextView)header.findViewById(R.id.profile_name);
        nav_name.setText(firstName+" "+lastName);
    }

    private void showTripStatus(String tripID, final String tripStatus) {

        FragmentManager fm = getFragmentManager();
        final TripStatusDialogFragment tripStatusDialogFragment = TripStatusDialogFragment.newInstance(tripID, tripStatus);
        tripStatusDialogFragment.show(fm, "trip_status_tag");

        if (tripStatus.equalsIgnoreCase("new")) {
            requestedTripInProgress = true;
            mFloatingActionButton.hide();
        }

        //Toast.makeText(this, "Your trip ID is: " + tripID.toString() ,Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Your trip status is: " + tripStatus ,Toast.LENGTH_SHORT).show();

        try {
            mRealtime = new AblyRealtime(Constants.ALBY_API_KEY);
        } catch (AblyException e) {
            e.printStackTrace();
        }

        // TRIP STATUS CHANNEL
        String tripChannel = "/trip/" + tripID;
        Channel channel = mRealtime.channels.get(tripChannel);
        try {
            channel.subscribe("statusChange", new Channel.MessageListener() {
                @Override
                public void onMessage(final Message messages) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getBaseContext(), "Message received: " + messages.data, Toast.LENGTH_LONG).show();
                            mTripStatus = messages.data.toString();
                            //Toast.makeText(MainActivity.this, "Your trip status is: " + mTripStatus ,Toast.LENGTH_SHORT).show();

                            try {
                                JSONObject tripStatusJSONObject = new JSONObject(mTripStatus);
                                mTripStatus = (String) tripStatusJSONObject.get("status");
                                if (mTripStatus.equalsIgnoreCase("ended") ) {
                                    requestedTripInProgress = false;
                                    mFloatingActionButton.show();
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                            tripStatusDialogFragment.updateTripStatus(mTripStatus);
                        }
                    });

                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // CAR LOCATION CHANNEL
        String carChannelName = "/car/" + mCarId;
        Channel carChannel = mRealtime.channels.get(carChannelName);
        try {
            carChannel.subscribe("locationChange", new Channel.MessageListener() {
                @Override
                public void onMessage(final Message messages) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String carLocationJson = messages.data.toString();
                            try {
                                JSONObject carLocationJsonObject = new JSONObject(carLocationJson);
                                currentCarLocationLatitude = (Double) carLocationJsonObject.get("lat");
                                currentCarLocationLongitude = (Double) carLocationJsonObject.get("lon");
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(MainActivity.this, "Car latitude: " + currentCarLocationLatitude + "\n" +
                                    "Car longitude: " + currentCarLocationLongitude,Toast.LENGTH_SHORT).show();

                            mMapViewFratment.updateCarMarkerOnMap(currentCarLocationLatitude, currentCarLocationLongitude);

                        }
                    });

                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
