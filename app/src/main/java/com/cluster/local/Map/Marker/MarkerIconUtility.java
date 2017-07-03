package com.cluster.local.Map.Marker;

import com.cluster.local.R;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;


public class MarkerIconUtility {

    private Icon restaurant;
    private Icon bar;
    private Icon club;
    private Icon user;


    public MarkerIconUtility(IconFactory iconFactory) {
        restaurant = iconFactory.fromResource(R.drawable.burger);
        bar = iconFactory.fromResource(R.drawable.beer);
        club = iconFactory.fromResource(R.drawable.cocktail);
        user = iconFactory.defaultMarker();
    }

    public Icon getUserIcon() {
        return user;
    }

    public Icon getIcon(String[] types) {
        return getIcon(types, 0);
    }

    private Icon getIcon(String[] types, int i) {
        switch (types[i]) {

            case "accounting":
                break;
            case "airport":
                break;
            case "amusement_park":
                break;
            case "aquarium":
                break;
            case "art_gallery":
                break;
            case "atm":
                break;
            case "bakery":
                break;
            case "bank":
                break;
            case "bar":
                return bar;
            case "beauty_salon":
                break;
            case "bicycle_store":
                break;
            case "book_store":
                break;
            case "bowling_alley":
                break;
            case "bus_station":
                break;
            case "cafe":
                break;
            case "campground":
                break;
            case "car_dealer":
                break;
            case "car_rental":
                break;
            case "car_repair":
                break;
            case "car_wash":
                break;
            case "casino":
                break;
            case "cemetery":
                break;
            case "church":
                break;
            case "city_hall":
                break;
            case "clothing_store":
                break;
            case "convenience_store":
                break;
            case "courthouse":
                break;
            case "dentist":
                break;
            case "department_store":
                break;
            case "doctor":
                break;
            case "electrician":
                break;
            case "electronics_store":
                break;
            case "embassy":
                break;
            case "fire_station":
                break;
            case "florist":
                break;
            case "funeral_home":
                break;
            case "furniture_store":
                break;
            case "gas_station":
                break;
            case "gym":
                break;
            case "hair_care":
                break;
            case "hardware_store":
                break;
            case "hindu_temple":
                break;
            case "home_goods_store":
                break;
            case "hospital":
                break;
            case "insurance_agency":
                break;
            case "jewelry_store":
                break;
            case "laundry":
                break;
            case "lawyer":
                break;
            case "library":
                break;
            case "liquor_store":
                break;
            case "local_government_office":
                break;
            case "locksmith":
                break;
            case "lodging":
                break;
            case "meal_delivery":
                break;
            case "meal_takeaway":
                break;
            case "mosque":
                break;
            case "movie_rental":
                break;
            case "movie_theater":
                break;
            case "moving_company":
                break;
            case "museum":
                break;
            case "night_club":
                return club;
            case "painter":
                break;
            case "park":
                break;
            case "parking":
                break;
            case "pet_store":
                break;
            case "pharmacy":
                break;
            case "physiotherapist":
                break;
            case "plumber":
                break;
            case "police":
                break;
            case "post_office":
                break;
            case "real_estate_agency":
                break;
            case "restaurant":
                return restaurant;
            case "roofing_contractor":
                break;
            case "rv_park":
                break;
            case "school":
                break;
            case "shoe_store":
                break;
            case "shopping_mall":
                break;
            case "spa":
                break;
            case "stadium":
                break;
            case "storage":
                break;
            case "store":
                break;
            case "subway_station":
                break;
            case "synagogue":
                break;
            case "taxi_stand":
                break;
            case "train_station":
                break;
            case "transit_station":
                break;
            case "travel_agency":
                break;
            case "university":
                break;
            case "veterinary_care":
                break;
            case "zoo":
                break;
            default:
                break;


        }
        if (i <= 2) {
            return getIcon(types, ++i);
        } else {
            return null;
        }
    }
}
