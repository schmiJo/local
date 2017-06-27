package com.cluster.local;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cluster.local.Map.ExternalAPIs.GooglePlacesApiRequest;
import com.cluster.local.Map.Marker.Place;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;


public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Mapbox.getInstance(this, getString(R.string.mapbox_api_key));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(CheckGooglePlayServices()){
           /* GooglePlacesApiRequest request = new GooglePlacesApiRequest(getApplicationContext(), new GooglePlacesApiRequest.GooglePlacesApiCallback() {
                @Override
                public void onSuccess(Place result) {
                    Log.d("debug", "Place encooded: " + result.toString());
                }

                @Override
                public void onFailure(byte errorCode) {
                    Log.e("debug", "OnFailure Called Code: " + errorCode);
                }
            });
            request.sendNearbyRequest(new LatLng(49.020126, 12.095757),5000,
                   //GooglePlacesApiRequest.PLACE_TYPE_CLUB,
                   GooglePlacesApiRequest.PLACE_TYPE_BAR
                   // GooglePlacesApiRequest.PLACE_TYPE_RESTAURANT
            );*/
        }



    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }



}
