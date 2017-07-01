package com.cluster.local;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.cluster.local.Map.ExternalAPIs.LocationService;
import com.cluster.local.Map.MapFragment;
import com.cluster.local.SpinnerView.SpinnerClose;
import com.cluster.local.SpinnerView.SpinnerView;
import com.mapbox.mapboxsdk.Mapbox;


public class MainActivity extends AppCompatActivity implements  SpinnerView.OnOpenListener{


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
        if(spinnerView.getExtendState() == SpinnerView.EXTEND_OPEN){
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

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission was granted
                    Log.d("debug", "Permission was granted");
                    mapFragment.enableMap();
                } else {
                    //Permission was denied
                    Log.d("debug", "Permission was denied");
                    mapFragment.disableMap();
                }

                break;


            case LocationService.PERMISSION_GRANTED_COARSE_LOCATION:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission was granted

                } else {
                    //Permission was denied
                }

                break;
        }
    }

    public MapFragment getMapFragment() {
        return mapFragment;
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
