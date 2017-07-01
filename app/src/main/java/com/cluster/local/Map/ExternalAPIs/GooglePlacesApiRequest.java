package com.cluster.local.Map.ExternalAPIs;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cluster.local.Map.Marker.Place;
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


/**
 * Class for sending a nearby search with the Google Places API
 */

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
        //Make sure that the api key won't be added twice
        if (KEY.length() <= 5) {
            KEY += context.getString(R.string.google_web_api_key);
        }
    }

    public void sendNearbyRequest(Region region){
        new PlacesAPIRequest().execute(
                "location=" + region.getMiddle().getLatitude() + "," + region.getMiddle().getLongitude() +
                        "&type=bar" +
                        "&radius=" + region.getRadius());

        new PlacesAPIRequest().execute(
                "location=" + region.getMiddle().getLatitude() + "," + region.getMiddle().getLongitude() +
                        "&type=restaurant" +
                        "&radius=" + region.getRadius());

        new PlacesAPIRequest().execute(
                "location=" + region.getMiddle().getLatitude() + "," + region.getMiddle().getLongitude() +
                        "&type=night_club" +
                        "&radius=" + region.getRadius());
    }


@Deprecated
    public void sendNearbyRequest(LatLng coordinates, int radius, String... placeTypes) {

        if (placeTypes[0].equals("")) {
            new PlacesAPIRequest().execute("location=" + coordinates.getLatitude() + "," + coordinates.getLongitude() + "&radius=" + radius);
        } else {
            for (String type : placeTypes) {
                String typeKey = "&types=" + type;
                new PlacesAPIRequest().execute("location=" + coordinates.getLatitude() + "," + coordinates.getLongitude() + typeKey + "&radius=" + radius);
            }
        }
    }


    /**
     * Helper class for communicating with the GooglePlaces Web API, for nearby searches
     */

    private class PlacesAPIRequest extends AsyncTask<String, Void, String> {


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

            Place place = new Place(location, name);

            place.setPlaceID(placeId);
            place.setTypes(types);

            Log.i(TAG, "New Place: " + place.toString());

            return place;
        }
    }


    public interface GooglePlacesApiCallback {

        void onSuccess(Place result);
        void onFailure(byte errorCode);
    }


    private boolean checkStatusCode(String status) {
        switch (status) {
            case "OK":
                Log.i(TAG, "Request successful, reading Places");
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

