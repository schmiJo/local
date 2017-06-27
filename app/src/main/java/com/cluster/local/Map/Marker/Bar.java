package com.cluster.local.Map.Marker;

import com.cluster.local.R;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;



public class Bar extends Place {

    private static Icon icon;

    public Bar() {
    }

    public Bar(IconFactory iconFactory) {
        if(icon == null){
            icon = iconFactory.fromResource(R.drawable.beer);
        }
    }

    public Bar(LatLng position, String title) {
        super(position, title);
    }

    @Override
    public MarkerOptions asMarkerOptions() {
        return new MarkerOptions().position(getPosition()).title(getName()).icon(icon);
    }



    @Override
    public String toString() {
        return "BAR: location: " + super.getPosition().toString();
    }
}
