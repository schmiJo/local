package com.cluster.local.Map.Marker;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;


/**
 * Created by Jonas on 6/25/2017.
 */

public  class Place {

    private LatLng position;
    private String name;
    private String placeID;
    private String[] types;

    public Place() {
    }

    public Place(LatLng position, String name) {
        this.position = position;
        this.name = name;
    }


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

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }
}
