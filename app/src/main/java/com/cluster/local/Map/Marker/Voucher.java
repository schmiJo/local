package com.cluster.local.Map.Marker;

import com.cluster.local.R;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;


/**
 * Created by Jonas on 6/25/2017.
 */

public class Voucher extends Place {

    private static int ICON_ID = R.drawable.gift;

    public Voucher(LatLng position, String title) {
        super(position, title);
    }

    @Override
    public MarkerOptions asMarkerOptions() {
        return new MarkerOptions();
    }

    public static int getIconId() {
        return ICON_ID;
    }
}
