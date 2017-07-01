package com.cluster.local.Map;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cluster.local.Map.ExternalAPIs.LocationService;
import com.cluster.local.Map.ExternalAPIs.PlacesLoader;
import com.cluster.local.Map.Marker.MarkerIconUtility;
import com.cluster.local.R;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.UiSettings;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {


    public MapFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "Map";

    private MapView mapView;
    private MapboxMap mapboxMap;

    private LocationService locationService;

    private MarkerIconUtility markerIconUtility;
    private PlacesLoader placesLoader;
    private OverLayFragment overLayFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) rootView.findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        locationService = new LocationService(this);

        markerIconUtility = new MarkerIconUtility(IconFactory.getInstance(getContext()));

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                setMapboxMap(mapboxMap);

                //Set uiSettings
                UiSettings uiSettings = mapboxMap.getUiSettings();
                uiSettings.setAttributionEnabled(false);
                uiSettings.setLogoEnabled(false);
                uiSettings.setCompassEnabled(true);
                uiSettings.setRotateGesturesEnabled(true);

                placesLoader = new PlacesLoader(getContext(), savedInstanceState, mapboxMap, markerIconUtility);


                mapboxMap.setOnCameraChangeListener(new MapboxMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition position) {
                        //             placesLoader.notifyCameraChange(position);
                    }
                });
            }
        });


        return rootView;
    }

    public LocationService getLocationService() {
        return locationService;
    }

    public void setMapboxMap(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }

    public void enableMap() {
        //Boolean is updated in case if the LocationService have launched a permission dialog and the callback comes back from the Main activity
        locationService.setPermissionGranted(true);
        //Kill the overLayFragment
        if (overLayFragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(overLayFragment).commit();
            overLayFragment.onDestroy();
            overLayFragment = null;
        }
    }

    public void disableMap() {
        //Boolean is updated in case if the LocationService have launched a permission dialog and the callback comes back from the Main activity
        locationService.setPermissionGranted(false);
        //Create new Fragment which lays over the map, to lock it
        overLayFragment = new OverLayFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.overLayPlaceHolder, overLayFragment).commit();
        //Make a fly-out animation if the map was disabled
        if (mapboxMap != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .bearing(0)
                    .tilt(0)
                    .zoom(1)
                    .build();
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        locationService.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        locationService.onResume();
        //Check if the location was granted for the case the user came back from the settings
        if (!locationService.isPermissionGranted()) {
            locationService.requestLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        locationService.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        locationService.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        locationService.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        placesLoader.onSaveInstanceState(outState);
    }


}
