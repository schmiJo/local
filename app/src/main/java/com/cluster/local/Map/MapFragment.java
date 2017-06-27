package com.cluster.local.Map;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cluster.local.Map.ExternalAPIs.GooglePlacesApiRequest;
import com.cluster.local.Map.Marker.Place;
import com.cluster.local.R;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.UiSettings;

import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {


    public MapFragment() {
        // Required empty public constructor
    }

    MapView mapView;
    MapboxMap mapboxMap;
    LinkedList<MarkerOptions> markerQueue;
    LinkedList<Marker> addedMarkers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) rootView.findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                setMapboxMap(mapboxMap);
                //Set uiSettings
                UiSettings uiSettings = mapboxMap.getUiSettings();
                uiSettings.setAttributionEnabled(false);
                uiSettings.setLogoEnabled(false);
                uiSettings.setCompassEnabled(false);
                uiSettings.setRotateGesturesEnabled(false);


                final GooglePlacesApiRequest request = new GooglePlacesApiRequest(getContext(), new GooglePlacesApiRequest.GooglePlacesApiCallback() {
                    @Override
                    public void onSuccess(Place result) {
                        getMapboxMap().addMarker(new MarkerOptions()
                                .position(result.getPosition())
                                .title(result.getName())
                        );
                    }

                    @Override
                    public void onFailure(byte errorCode) {

                    }
                });
                request.sendNearbyRequest(new LatLng(49.013679, 12.097980), 5000, GooglePlacesApiRequest.PLACE_TYPE_RESTAURANT);


            }
        });



        return rootView;
    }

    private void setMapboxMap(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }

    public MapboxMap getMapboxMap() {
        return mapboxMap;
    }

    public void addMarker(MarkerOptions markerOptions) {
        markerQueue.addFirst(markerOptions);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


}
