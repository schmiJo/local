package com.cluster.local.Map.Marker;

import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Arrays;


public class Place implements Parcelable{

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


    protected Place(Parcel in) {
        position = in.readParcelable(LatLng.class.getClassLoader());
        name = in.readString();
        placeID = in.readString();
        types = in.createStringArray();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

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

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", position=" + position.getLatitude() + ", " + position.getLongitude() + '\'' +
                ", types=" + Arrays.toString(types) + '\'' +
                ", placeID='" + placeID +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(position, flags);
        dest.writeString(name);
        dest.writeString(placeID);
        dest.writeStringArray(types);
    }
}
