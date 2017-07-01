package com.cluster.local.Map;

import com.mapbox.mapboxsdk.geometry.LatLng;


/**
 * Created by Jonas on 6/28/2017.
 */

public class MapUtils {

    public static float distanceBetweenLatLng(LatLng coord1, LatLng coord2){

        double R = 6371000; // metres
        double φ1 = Math.toRadians(coord1.getLatitude());
        double φ2 = Math.toRadians(coord2.getLatitude());
        double Δφ = Math.toRadians(coord2.getLatitude()-coord1.getLatitude());
        double Δλ = Math.toRadians(coord2.getLongitude()-coord1.getLongitude());

        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (float)(R * c);
    }
}
