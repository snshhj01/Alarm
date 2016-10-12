package kr.co.miracom.alarm.external;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public class DirectionFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyDYPLgGTvuRDDHqsUZA9MD7VoHParkXGMQ";
    private DirectionFinderListener listener;
    private String origin;
    private String destination;
//    private Long arrivalTime;

    public DirectionFinder( DirectionFinderListener listener, String origin, String destination) {
        this.listener = listener;
        this.origin = origin;
        this.destination = destination;
//        this.arrivalTime = arrivalTime;
    }

    public void execute() throws UnsupportedEncodingException {
        listener.onDirectionFinderStart();
        new DownloadRawData().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        String urlDestination = URLEncoder.encode(destination, "utf-8");
//        String urlArrivalTime = URLEncoder.encode(String.valueOf(arrivalTime), "utf-8");

        String FullURL = DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&mode=transit&key=" + GOOGLE_API_KEY;
        // https://maps.googleapis.com/maps/api/directions/json?origin=37.518889,127.062387&destination=37.520657,127.121423&arrival_time=1480928400&mode=transit&key=AIzaSyDYPLgGTvuRDDHqsUZA9MD7VoHParkXGMQ
        Log.i("FullURL", FullURL);
        return FullURL;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                Log.i("response" , buffer.toString());
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSon(String data) throws JSONException {
        if (data == null)
            return;

        String duration = "";

//        List<Route> routes = new ArrayList<Route>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
//            Route route = new Route();

//            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");
            JSONObject jsonDepartureTime = jsonLeg.getJSONObject("departure_time");

//            route.distance = new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
//            route.duration = new Duration(jsonDuration.getString("text"), jsonDuration.getInt("value"));
//            route.endAddress = jsonLeg.getString("end_address");
//            route.startAddress = jsonLeg.getString("start_address");
//            route.startLocation = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
//            route.endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
//            route.points = decodePolyLine(overview_polylineJson.getString("points"));

//            routes.add(route);
            Log.i("distance", jsonDistance.getString("text"));
            Log.i("duration", jsonDuration.getString("text"));
            Log.i("departureTime", jsonDepartureTime.getString("text"));

            duration = jsonDuration.getString("value");
        }

        listener.onDirectionFinderSuccess(duration);
    }

//    private List<LatLng> decodePolyLine(final String poly) {
//        int len = poly.length();
//        int index = 0;
//        List<LatLng> decoded = new ArrayList<LatLng>();
//        int lat = 0;
//        int lng = 0;
//
//        while (index < len) {
//            int b;
//            int shift = 0;
//            int result = 0;
//            do {
//                b = poly.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = poly.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            decoded.add(new LatLng(
//                    lat / 100000d, lng / 100000d
//            ));
//        }
//
//        return decoded;
//    }
}
