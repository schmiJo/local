package com.cluster.local.Map.Marker;

import com.cluster.local.R;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;


/**
 * Created by Jonas on 6/25/2017.
 */

public class Club extends Place {

    private static Icon icon;

    public Club() {
    }

    public Club(IconFactory iconFactory) {
        if(icon == null){
            icon = iconFactory.fromResource(R.drawable.cocktail);
        }
    }

    public Club(LatLng position, String title) {
        super(position, title);
    }

    @Override
    public MarkerOptions asMarkerOptions() {
        return new MarkerOptions().position(getPosition()).title(getName()).icon(icon);
    }


}