package com.urbantechies.fetch_me_up.drivers;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.urbantechies.fetch_me_up.R;
import com.urbantechies.fetch_me_up.UserClient;
import com.urbantechies.fetch_me_up.model.User;
import com.urbantechies.fetch_me_up.model.UserLocation;

import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseFirestore mDb;
    private UserLocation mUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mDb = FirebaseFirestore.getInstance();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
        //    getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
         //           new MainMapFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }



    }

    private void setDriverOnline(User user) {

        DocumentReference driverRef = mDb.collection(getString(R.string.collection_user_online))
                .document(FirebaseAuth.getInstance().getUid());

        driverRef.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onComplete: Driver is online!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

    private void setDriverOffline() {

        DocumentReference driverRef = mDb.collection(getString(R.string.collection_driver_online))
                .document(FirebaseAuth.getInstance().getUid());

        driverRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }



    private void getUserDetails() {
        if (mUserLocation == null) {
            mUserLocation = new UserLocation();

            DocumentReference userRef = mDb.collection(getString(R.string.collection_drivers))
                    .document(FirebaseAuth.getInstance().getUid());

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: successfly get the user details");

                        User user = task.getResult().toObject(User.class);
                        mUserLocation.setUser(user);
                        ((UserClient) getApplicationContext()).setUser(user);
                        setDriverOnline(user);
                      //  getLastKnownLocation();
                    }
                }
            });
        } else {
          //  getLastKnownLocation();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out: {
                signOut();
                return true;
            }
            case R.id.action_profile: {
                //    startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }

    }


    private void signOut() {
       // setDriverOffline();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, logindriver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new RunMap();
                            break;
                        case R.id.nav_request:
                            selectedFragment = new PassengerRequestFragment();
                            break;
                        case R.id.nav_custom_ride:
                            selectedFragment = new CustomRideFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                            selectedFragment).commit();

                    return true;
                }
            };


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_inbox:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                        new InboxFragment()).commit();
                break;
            case R.id.nav_trip:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                        new MyTripFragment()).commit();
                break;
            case R.id.nav_wallet:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                        new WalletFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
}
