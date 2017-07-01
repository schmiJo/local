package com.cluster.local.Map.ExternalAPIs;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cluster.local.Map.Marker.Place;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;


public class Region implements Parcelable{

    private LatLng middle;
    private float radius;
    private List<Place> places;


    public Region(@NonNull LatLng middle, float radius) {
        this.middle = middle;
        this.radius = radius;
        places = new ArrayList<>();
    }


    protected Region(Parcel in) {
        middle = in.readParcelable(LatLng.class.getClassLoader());
        radius = in.readFloat();
        places = in.createTypedArrayList(Place.CREATOR);
    }

    public static final Creator<Region> CREATOR = new Creator<Region>() {
        @Override
        public Region createFromParcel(Parcel in) {
            return new Region(in);
        }

        @Override
        public Region[] newArray(int size) {
            return new Region[size];
        }
    };

    public void addPlace(Place place) {
        places.add(place);
    }

    public List<Place> getPlaces() {
        return places;
    }

    public LatLng getMiddle() {
        return middle;
    }

    public float getRadius() {
        return radius;
    }


    @Override
    public boolean equals(Object obj) {

        if (obj == null || getClass() != obj.getClass()) return false;

        Region that = (Region) obj;

        return (this.middle.equals(that.middle) && (that.radius == this.radius));

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(middle, flags);
        dest.writeFloat(radius);
        for(Place place : places){
            dest.writeParcelable(place, flags);
        }
    }
}
