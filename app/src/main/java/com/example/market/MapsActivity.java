package com.example.market;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.market.controllers.fragment.BillingFragment;
import com.example.market.controllers.fragment.OrderFragment;
import com.example.market.utils.ActivityHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int Request_User_Location_Code = 99;

    private static final String TAG = "MapsActivityTag";
    public static final int DEFAULT_MAP_ZOOM = 14;
    public static LatLng mUserLocation;
    private GoogleMap mMap;
    private FloatingActionButton mFabDon;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Boolean mIsFirstMethodCalled;
    private LatLng mPreviousUserLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mPreviousUserLatLng = getIntent().getParcelableExtra(ActivityHelper.EXTRA_USER_LATLNG);
        initView();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void initView() {
        mFabDon = findViewById(R.id.fab_done);
        mFabDon.setOnClickListener(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mIsFirstMethodCalled = true;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                        , Request_User_Location_Code);
            } else {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                        , Request_User_Location_Code);
            }
            return false;
        } else
            return true;
    }


    private String getUserAddress(LatLng userLocation) {
        String userAddress = getString(R.string.default_address_2);

        try {
            double latitude = userLocation.latitude;
            double longitude = userLocation.longitude;
            Geocoder geo = new Geocoder(MapsActivity.this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty()) {
                Log.i(TAG, "Waiting for location");
            } else {
                userAddress = addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality()
                        + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName() +
                        addresses.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());// getFromLocation() may sometimes fail
        } finally {
            return userAddress;
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_done:
                returnLocationAndAddress();
                break;

        }
    }


    private void returnLocationAndAddress() {
        mUserLocation = mMap.getCameraPosition().target;
        String userAddress = getUserAddress(mUserLocation);
        Intent intent = new Intent();
        intent.putExtra(BillingFragment.EXTRA_LOCATION, mUserLocation);
        intent.putExtra(BillingFragment.EXTRA_ADDRESS, userAddress);
        setResult(RESULT_OK, intent);


        this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {


            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (googleApiClient == null)
                            buildGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    String message = getString(R.string.permission_denied_text);
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
                return;

        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.requestLocationUpdates
                    (googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i(TAG, "onLocationChanged: latitude= " + location.getLatitude() + "& longitude= " +
                " " + location.getLatitude());

        lastLocation = location;
        double latitude = 0;
        double longitude = 0;
        LatLng latLng;
        if (!mIsFirstMethodCalled) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            if (mPreviousUserLatLng.latitude != 0 && mPreviousUserLatLng.longitude != 0) {
                latitude = mPreviousUserLatLng.latitude;
                longitude = mPreviousUserLatLng.longitude;
            }
            mIsFirstMethodCalled = false;
        }
        if (latitude != 0 && longitude != 0) {
            latLng = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_MAP_ZOOM));
        }
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        }
    }


}
