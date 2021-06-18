package com.example.aplikasisaya;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class MapFragment extends Fragment {
    private static final int UPDATE_INTERVAL = 30000;
    private static final int FASTEST_UPDATE_INTERVAL = 10000;
    MapView map = null;
    Marker lokasisaya;
    ArrayList<MyListData> myListData = new ArrayList<>();
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        // Inflate the layout for this fragment
        Configuration.getInstance().load(view.getContext(), PreferenceManager.getDefaultSharedPreferences(view.getContext()));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        map = view.findViewById(R.id.map);
        map.setClickable(true);
        map.setMultiTouchControls(true);
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.getController().setZoom(12.0);
        GeoPoint center = new GeoPoint(-7.310885, 112.728747);
        map.getController().setCenter(center);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.e("OSM", "Masuk bro...");
                if (locationResult == null) {

                    Log.e("OSM", "Macet bro...");
                    return;
                }
                GeoPoint center = new GeoPoint(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());

                map.getOverlays().remove(lokasisaya);
                lokasisaya = new Marker(map);
                lokasisaya.setPosition(center);
                map.getOverlays().add(lokasisaya);
                lokasisaya.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                lokasisaya.setIcon(getResources().getDrawable(R.drawable.ic_koordinat));
                lokasisaya.setTitle("Anda di sini");
                lokasisaya.showInfoWindow();
            }
        };
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest = locationRequest;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        Log.e("OSM", "Setting up location services...");
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }

        new GetGempas().execute();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        map.onPause();
    }

    @Override
    public void onDestroy() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        super.onDestroy();
    }

    private void setMarker() {
        for (MyListData data : myListData) {
            Marker marker = new Marker(map);
            marker.setPosition(data.getPosisi());
            map.getOverlays().add(marker);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            marker.setIcon(getResources().getDrawable(R.drawable.ic_lokasi));
            marker.setTitle(data.getTanggal() + " " + data.getJam());
            marker.setSnippet("Magnitude: " + data.getMagnitude() + " Kedalaman: " + data.getKedalaman());
            marker.setSubDescription("Posisi: " + data.getLintang() + "," + data.getBujur() + "<br>" + "Wilayah: " + data.getWilayah() + "<br>" + "Dirasakan: " + data.getDirasakan());
        }
    }

    private class GetGempas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            myListData = new ArrayList<>();
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://data.bmkg.go.id/DataMKG/TEWS/gempadirasakan.json";
            String jsonStr = sh.makeServiceCall(url);

            Log.e("Donlot json", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject Infogempa = jsonObj.getJSONObject("Infogempa");
                    JSONArray gempas = Infogempa.getJSONArray("gempa");

                    // looping through All Contacts
                    for (int i = 0; i < gempas.length(); i++) {
                        JSONObject g = gempas.getJSONObject(i);

                        String tanggal = g.getString("Tanggal");
                        String jam = g.getString("Jam");
                        String lintang = g.getString("Lintang");
                        String bujur = g.getString("Bujur");
                        String magnitude = g.getString("Magnitude");
                        String kedalaman = g.getString("Kedalaman");
                        String wilayah = g.getString("Wilayah");
                        String dirasakan = g.getString("Dirasakan");
                        String koordinat = g.getString("Coordinates");

                        MyListData gempa = new MyListData(tanggal, jam, lintang, bujur, magnitude, kedalaman, wilayah, dirasakan, koordinat);

                        myListData.add(gempa);

                        Log.e("Donlot json", "Json total : " + myListData.size());
                    }
                } catch (final JSONException e) {
                    Log.e("Donlot json", "Json parsing error: " + e.getMessage());
                    Toast.makeText(getContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                }

            } else {
                Log.e("Donlot json", "Couldn't get json from server.");
                Toast.makeText(getContext(),
                        "Couldn't get json from server. Check LogCat for possible errors!",
                        Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            setMarker();
        }
    }
}