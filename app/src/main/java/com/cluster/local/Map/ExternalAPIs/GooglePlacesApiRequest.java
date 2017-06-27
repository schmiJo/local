package com.cluster.local.Map.ExternalAPIs;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cluster.local.Map.Marker.Bar;
import com.cluster.local.Map.Marker.Club;
import com.cluster.local.Map.Marker.Place;
import com.cluster.local.Map.Marker.Restaurant;
import com.cluster.local.Network.HTTPDataAdapter;
import com.cluster.local.R;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.cluster.local.Network.HTTPDataAdapter.EMPTY_RESULT;
import static com.cluster.local.Network.HTTPDataAdapter.INVALID_REQUEST;
import static com.cluster.local.Network.HTTPDataAdapter.OVER_QUERY_LIMIT;
import static com.cluster.local.Network.HTTPDataAdapter.REQUEST_DENIED;
import static com.cluster.local.Network.HTTPDataAdapter.UNKNOWN_ERROR;
import static com.cluster.local.Network.HTTPDataAdapter.ZERO_RESULTS;


public class GooglePlacesApiRequest {

    private static final String TAG = "GooglePlacesApiRequest";

    private GooglePlacesApiCallback callback;

    private static final String PREFIX = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static String KEY = "&key=";

    public static final String PLACE_TYPE_BAR = "bar";
    public static final String PLACE_TYPE_RESTAURANT = "restaurant";
    public static final String PLACE_TYPE_CLUB = "night_club";


    public GooglePlacesApiRequest(Context context, GooglePlacesApiCallback callback) {
        this.callback = callback;
        if (KEY.length() <= 5) {
            KEY += context.getString(R.string.google_web_api_key);
        }
    }

    public void sendNearbyRequest(LatLng coordinates, int radius, String placeType) {
        String type = "";
        if (!placeType.equals("")) {
            type = "&types=" + placeType;
        }
        new PlacesAPIRequest().execute("location=" + coordinates.getLatitude() + "," + coordinates.getLongitude() + type + "&radius=" + radius);
    }


    private class PlacesAPIRequest extends AsyncTask<String, Void, String> {


        private PlacesAPIRequest() {

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String response;

            HTTPDataAdapter httpDataAdapter = new HTTPDataAdapter();
            String url = PREFIX + params[0] + KEY;
            Log.d("debug", "URL: " + url);
            response = httpDataAdapter.getHTTPSData(url);
            return response;
        }

        @Override
        protected void onPostExecute(String requestResult) {
            if (requestResult.equals("")) {
                callback.onFailure(EMPTY_RESULT);
            } else {
                try {
                    JSONObject requestResultJSON = new JSONObject(requestResult);

                    //Test if the Geocoding Request was successful
                    boolean statusIsOk = checkStatusCode(requestResultJSON.getString("status"));

                    if (statusIsOk) {
                        //if so read the results
                        JSONArray results = requestResultJSON.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++) {
                            callback.onSuccess(encodeJSON((JSONObject) results.get(i)));
                        }


                    }

                } catch (JSONException e) {
                    callback.onFailure(UNKNOWN_ERROR);
                    e.printStackTrace();
                }
            }
        }

        private Place encodeJSON(JSONObject item) throws JSONException {
            JSONObject locationJSON = item.getJSONObject("geometry").getJSONObject("location");
            LatLng location = new LatLng(locationJSON.getDouble("lat"), locationJSON.getDouble("lng"));
            String name = item.getString("name");
            String placeId = item.getString("id");

            JSONArray typesJSON = item.getJSONArray("types");
            String[] types = new String[typesJSON.length()];
            for (int i = 0; i < typesJSON.length(); i++) {
                types[i] = typesJSON.get(i).toString();
            }

            Place place = getMarkerType(types, 0);
            place.setPosition(location);
            place.setName(name);
            place.setPlaceID(placeId);


            return place;
        }
    }

    public Place getMarkerType(String[] types, int i) {
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
                return new Bar();
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
                return new Club();
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
                return new Restaurant();
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
            return getMarkerType(types, i++);
        } else {
            return null;
        }
    }


    public interface GooglePlacesApiCallback {

        void onSuccess(Place result);
        void onFailure(byte errorCode);
    }


    private boolean checkStatusCode(String status) {
        switch (status) {
            case "OK":
                Log.i(TAG, "Request successful, reading Locations");
                return true;
            case "ZERO_RESULTS":
                Log.w(TAG, "No results were found!");
                callback.onFailure(ZERO_RESULTS);
                break;
            case "OVER_QUERY_LIMIT":
                Log.e(TAG, "Unknown error occurred while reading Google Places Request!");
                callback.onFailure(OVER_QUERY_LIMIT);
                break;
            case "REQUEST_DENIED":
                Log.e(TAG, "Permission was not granted!");
                callback.onFailure(REQUEST_DENIED);
                break;
            case "INVALID_REQUEST":
                Log.e(TAG, "Invalid Request!");
                callback.onFailure(INVALID_REQUEST);
                break;
            case "UNKNOWN_ERROR":
                Log.e(TAG, "Unknown error occurred while reading Google Places Request!");
                callback.onFailure(UNKNOWN_ERROR);
                break;
            default:
                Log.e(TAG, "Can not resolve Status from Google Places API! STATUS : " + status);
                callback.onFailure(UNKNOWN_ERROR);
        }
        return false;
    }
}

