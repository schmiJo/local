package com.cluster.local;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cluster.local.Map.ExternalAPIs.LocationService;
import com.cluster.local.Map.MapFragment;
import com.cluster.local.SpinnerView.SpinnerClose;
import com.cluster.local.SpinnerView.SpinnerView;
import com.google.android.gms.location.LocationSettingsStates;
import com.mapbox.mapboxsdk.Mapbox;

import static com.cluster.local.Map.ExternalAPIs.LocationService.REQUEST_CHECK_SETTINGS;


public class MainActivity extends AppCompatActivity implements SpinnerView.OnOpenListener {


    private MapFragment mapFragment;


    private SpinnerView spinnerView;
    private SpinnerClose spinnerClose;

    @SuppressWarnings("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Mapbox.getInstance(this, getString(R.string.mapbox_api_key));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);


        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        spinnerView = (SpinnerView) findViewById(R.id.spinnerView);
        spinnerClose = (SpinnerClose) findViewById(R.id.spinnerClose);
    }

    @Override
    public void onBackPressed() {
        if (spinnerView.getExtendState() == SpinnerView.EXTEND_OPEN) {
            spinnerView.setDownStamp();
            spinnerView.close();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void close() {
        spinnerClose.hide();
    }

    @Override
    public void open() {
        spinnerClose.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LocationService.PERMISSION_GRANTED_FINE_LOCATION:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission was granted
                    Log.d("debug", "Permission FINE_LOCATION was granted");
                    mapFragment.getLocationService().setLocationPermissionGranted(true);
                    //If the user has activate the Permission ask him if he wants to enable Location determination
                    mapFragment.getLocationService().checkSettings();
                } else {
                    //Permission was denied
                    Log.d("debug", "Permission FINE_LOCATION was denied");
                    mapFragment.getLocationService().setLocationPermissionGranted(false);
                }

                break;


            case LocationService.PERMISSION_GRANTED_COARSE_LOCATION:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission was granted
                    Log.d("debug", "Permission COARSE_LOCATION was granted");
                    //mapFragment.getLocationService().setLocationSettingsEnabled(true);
                } else {
                    //Permission was denied
                    Log.d("debug", "Permission COARSE_LOCATION was denied");
                    //mapFragment.getLocationService().setLocationSettingsEnabled(false);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        mapFragment.getLocationService().setLocationSettingsEnabled(true);
                        mapFragment.getLocationService().startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        mapFragment.getLocationService().setLocationSettingsEnabled(false);
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public MapFragment getMapFragment() {
        return mapFragment;
    }

    public void showSpinner(boolean value) {
        if (value) {
            spinnerView.setVisibility(View.VISIBLE);
            spinnerClose.setVisibility(View.VISIBLE);
        } else {
            spinnerView.setVisibility(View.INVISIBLE);
            spinnerClose.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
