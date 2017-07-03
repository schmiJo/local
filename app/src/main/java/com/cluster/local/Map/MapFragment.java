package com.cluster.local.Map;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cluster.local.Map.ExternalAPIs.LocationService;
import com.cluster.local.Map.ExternalAPIs.PlacesLoader;
import com.cluster.local.Map.Marker.MarkerIconUtility;
import com.cluster.local.R;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
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
    private Compass compass;

    private MarkerIconUtility markerIconUtility;
    private PlacesLoader placesLoader;
    private OverLayFragment overLayFragment;

    private MarkerOptions userMarkerOptions;
    private Marker userMarker;

    private boolean isMapEnabled = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        compass = (Compass) rootView.findViewById(R.id.compass);

        compass.setOnClickListener(new Compass.OnCompassClickListener() {
            @Override
            public void onClick() {
                if (mapboxMap != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .bearing(0)
                            .build();
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1500, null);

                }
            }
        });

        mapView = (MapView) rootView.findViewById(R.id.mapView);
        markerIconUtility = new MarkerIconUtility(IconFactory.getInstance(getContext()));
        mapView.onCreate(savedInstanceState);

        locationService = new LocationService(this, new LocationService.OnLocationUpdateListener() {

            @Override
            public void onLocationUpdate(Location location) {
                if (!isMapEnabled) {
                    enableMap();
                }
                if (mapboxMap != null) {
                    if (userMarkerOptions == null) {
                        //Create user Marker
                        userMarkerOptions = new MarkerOptions();
                        userMarkerOptions.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                        userMarkerOptions.icon(markerIconUtility.getUserIcon());
                        userMarker = mapboxMap.addMarker(userMarkerOptions);
                    } else {
                        //Change Position of the Marker
                        mapboxMap.removeMarker(userMarker);
                        userMarkerOptions.getPosition().setLatitude(location.getLatitude());
                        userMarkerOptions.getPosition().setLongitude(location.getLongitude());
                        userMarker = mapboxMap.addMarker(userMarkerOptions);
                    }
                }

            }
        });


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                setMapboxMap(mapboxMap);

                //Set uiSettings
                UiSettings uiSettings = mapboxMap.getUiSettings();
                uiSettings.setAttributionEnabled(false);
                uiSettings.setLogoEnabled(false);
                uiSettings.setCompassEnabled(false);
                uiSettings.setRotateGesturesEnabled(true);

                placesLoader = new PlacesLoader(getContext(), savedInstanceState, mapboxMap, markerIconUtility);


                mapboxMap.setOnCameraChangeListener(new MapboxMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition position) {
                        //             placesLoader.notifyCameraChange(position);
                        compass.update((float)position.bearing);
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

    public boolean isMapEnabled() {
        return isMapEnabled;
    }

    public void enableMap() {
        isMapEnabled = true;
        //Kill the overLayFragment
        if (overLayFragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(overLayFragment).commit();
            overLayFragment.onDestroy();
            overLayFragment = null;
        }
        Log.i(TAG, "Map was enabled");
    }

    public void disableMap() {
        isMapEnabled = false;
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
        Log.w(TAG, "Map was disabled");
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
