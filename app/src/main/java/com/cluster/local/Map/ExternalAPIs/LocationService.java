package com.cluster.local.Map.ExternalAPIs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.cluster.local.Map.MapFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.geometry.LatLng;


@SuppressWarnings("MissingPermission")
public class LocationService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static String TAG = "LocationService";

    private MapFragment mapFragment;

    private GoogleApiClient googleApiClient;
    private LocationRequest defaultLocationRequest;
    private FusedLocationProviderApi locationProvider;

    private LatLng currentPosition;
    private boolean permissionGranted = false;

    //Callback ID's
    public static final int PERMISSION_GRANTED_FINE_LOCATION = 0x0000001;
    public static final int PERMISSION_GRANTED_COARSE_LOCATION = 0x0000002;


    public LocationService(MapFragment mapFragment) {
        this.mapFragment = mapFragment;

        //Set up the framework for the Google Location Services Api
        googleApiClient = new GoogleApiClient.Builder(mapFragment.getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //Configure the default location request
        defaultLocationRequest = new LocationRequest();
        defaultLocationRequest.setInterval(10000);
        defaultLocationRequest.setFastestInterval(5000);
        defaultLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //Get the location provider from the Google Play Services Api
        locationProvider = LocationServices.FusedLocationApi;
    }


    public void onStart() {
        googleApiClient.connect();
        //After that one of the Methods from the ConnectionCallbackListener should be called
    }

    public void onResume() {
        if (googleApiClient.isConnected() && permissionGranted) {
            requestLocationUpdates();
        }
    }

    public void onPause() {
        if (permissionGranted) {
            locationProvider.removeLocationUpdates(googleApiClient, this);
        }
    }

    public void onStop() {
        googleApiClient.disconnect();
    }

    public void onDestroy() {
        mapFragment = null;
        mapFragment = null;
        googleApiClient = null;
        defaultLocationRequest = null;
        locationProvider = null;
        currentPosition = null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
        Log.i(TAG, "Connection to the Google Api client was successful");
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "Connection to the Google Api client has broken");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Connection to the Google Api client failed!");
        Log.e(TAG, "Error message: " + connectionResult.getErrorMessage());
    }

    @Override
    public void onLocationChanged(Location location) {
        if (currentPosition == null) {
            currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            currentPosition.setLatitude(location.getLatitude());
            currentPosition.setLongitude(location.getLongitude());
        }
        Log.i(TAG, "Location update, new location: " + location.toString());
    }


    public void requestLocationUpdates() {
        Log.i(TAG, "Location updates were requested");
        if (googleApiClient.isConnected() && checkLocationPermissions()) {
            locationProvider.requestLocationUpdates(googleApiClient, defaultLocationRequest, this);
            permissionGranted = true;
            Log.i(TAG, "Location request starts");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mapFragment.getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_GRANTED_FINE_LOCATION);
                mapFragment.disableMap();
                permissionGranted = false;
                Log.w(TAG, "Permission was not given");
            } else {
                //on Lower APIs as 23 the permission is always granted
                permissionGranted = true;
                Log.i(TAG, "Location request starts");
            }
        }
    }


    private boolean checkLocationPermissions() {
        return ActivityCompat.checkSelfPermission(mapFragment.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mapFragment.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void setPermissionGranted(boolean value) {
        permissionGranted = value;
    }

    public boolean isPermissionGranted() {
        return permissionGranted;
    }
}
