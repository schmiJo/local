package com.cluster.local.Map.ExternalAPIs;

import android.content.Context;
import android.os.Bundle;

import com.cluster.local.Map.MapUtils;
import com.cluster.local.Map.Marker.MarkerIconUtility;
import com.cluster.local.Map.Marker.Place;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.LinkedList;


/**
 * This class will automatically load all Places around the camera target
 */

public class PlacesLoader {

    private GooglePlacesApiRequest apiRequest;
    private LinkedList<Region> loadedRegions;
    private static final String BUNDLE_KEY = "Region";


    public PlacesLoader(Context context, Bundle savedInstanceState, final MapboxMap map, final MarkerIconUtility markerIconUtility) {
        loadedRegions = new LinkedList<>();
        onCreate(savedInstanceState);

        apiRequest = new GooglePlacesApiRequest(context, new GooglePlacesApiRequest.GooglePlacesApiCallback() {
            @Override
            public void onSuccess(Place result) {
                map.addMarker(new MarkerOptions()
                        .position(result.getPosition())
                        .title(result.getName())
                        .icon(markerIconUtility.getIcon(result.getTypes())));


            }

            @Override
            public void onFailure(byte errorCode) {

            }
        });
    }

    private void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) return;
        Region lastUserRegion = savedInstanceState.getParcelable(BUNDLE_KEY);
        loadedRegions.add(lastUserRegion);
    }

    public void onSaveInstanceState(Bundle outState) {
        if (loadedRegions != null&&loadedRegions.size()>0) {
            outState.putParcelable(BUNDLE_KEY, loadedRegions.getFirst());
        }
    }

    public void notifyCameraChange(CameraPosition newCameraPos) {
        //Check the zoom level
        if (newCameraPos.zoom < 10) {
            deleteAllMarkersAndRegions();
        }
        //check if the requested region target is inside the last requested region
        if (isTargetInLastLoadedRegion(newCameraPos.target)) {
            return;
        }
        if (isTargetInAnotherLoadedRegion(newCameraPos.target)) {
            return;
        }
        Region newRegion = new Region(newCameraPos.target, 5000);
        apiRequest.sendNearbyRequest(newRegion);

    }

    private boolean isTargetInLastLoadedRegion(LatLng target) {
        if (loadedRegions.size() == 0) return false;
        return (MapUtils.distanceBetweenLatLng(loadedRegions.getFirst().getMiddle(), target)
                < loadedRegions.getFirst().getRadius());

    }


    private boolean isTargetInAnotherLoadedRegion(LatLng target) {

        for (int i = 1; i < loadedRegions.size(); i++) {
            if (MapUtils.distanceBetweenLatLng(loadedRegions.get(i).getMiddle(), target)
                    < loadedRegions.get(i).getRadius()) {
                return true;
            }
        }
        return false;
    }


    private void deleteAllMarkersAndRegions() {

    }


}
