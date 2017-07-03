package com.cluster.local.Map.ExternalAPIs;

import android.Manifest;
import android.content.IntentSender;
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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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
    private boolean settingsEnabled = false;

    private OnLocationUpdateListener callback;

    //Callback ID's
    public static final int PERMISSION_GRANTED_FINE_LOCATION = 0x0000001;
    public static final int PERMISSION_GRANTED_COARSE_LOCATION = 0x0000002;
    public static final int REQUEST_CHECK_SETTINGS = 0x0000003;


    public LocationService(MapFragment mapFragment, OnLocationUpdateListener callback) {
        this.mapFragment = mapFragment;
        this.callback = callback;

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


    @Override
    public void onConnected(@Nullable Bundle bundle) {
       update();
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
        //If the LocationService receives location updates it is sure that the permission is given
        //and the location determination is enabled in the settings
        setLocationPermissionGranted(true);
        setLocationSettingsEnabled(true);

        if (currentPosition == null) {
            currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            currentPosition.setLatitude(location.getLatitude());
            currentPosition.setLongitude(location.getLongitude());
        }
        callback.onLocationUpdate(location);
        Log.i(TAG, "Location update, new location: " + location.toString());
    }

    public void update(){
        if(checkPermissions()){
            checkSettings();
        }
    }


    private boolean checkPermissions() {
        permissionGranted = ActivityCompat.checkSelfPermission(mapFragment.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mapFragment.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if(permissionGranted){
            return permissionGranted;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mapFragment.getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_GRANTED_FINE_LOCATION);
        } else {
            Log.w(TAG, "Permissions should already be given... SDK<23");
        }
        return permissionGranted;
    }

    public void checkSettings() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(defaultLocationRequest);
        final PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        settingsEnabled = true;
                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            status.startResolutionForResult(mapFragment.getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "Couldn't start location enable dialog. ErrorMessage: " + e);
                        }
                        settingsEnabled = false;
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        Log.wtf(TAG, "Now way enabling the location settings");
                        settingsEnabled = false;
                        break;
                }
            }
        });
    }


    public void startLocationUpdates() {
        locationProvider.requestLocationUpdates(googleApiClient, defaultLocationRequest, this);
    }




    public void setLocationPermissionGranted(boolean value) {
        permissionGranted = value;
    }

    public void setLocationSettingsEnabled(boolean settingsEnabled) {
        this.settingsEnabled = settingsEnabled;
    }


    //Lifecycle Methods

    public void onStart() {
        googleApiClient.connect();
        //After that one of the Methods from the ConnectionCallbackListener should be called
    }

    public void onResume() {
        //Update the permission and settingsEnabled Boolean
        if (googleApiClient.isConnected()) {
            //Check if the booleans permissionGranted and settingsEnabled correspond to the locking state of the
            //Map. In case the values were changed from the solutions (dialogs)
           if(permissionGranted){
               if(settingsEnabled && !mapFragment.isMapEnabled()){
                   mapFragment.enableMap();
               }else if(!settingsEnabled && mapFragment.isMapEnabled()){
                   mapFragment.disableMap();
               }
           }else{
               mapFragment.disableMap();
           }
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


    //Callbacks


    public interface OnLocationUpdateListener {

        void onLocationUpdate(Location location);
    }


}
