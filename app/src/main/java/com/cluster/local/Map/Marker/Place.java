package com.cluster.local.Map.Marker;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;


/**
 * Created by Jonas on 6/25/2017.
 */

public abstract class Place {

    private LatLng position;
    private String name;
    private String placeID;

    public Place() {
    }

    public Place(LatLng position, String name) {
        this.position = position;
        this.name = name;
    }

    public abstract MarkerOptions asMarkerOptions();

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }


}
