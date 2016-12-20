package com.attribes.push2beat.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.databinding.FragmentMapBinding;
import com.attribes.push2beat.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.*;

import java.util.List;

import static com.google.android.gms.location.LocationRequest.create;

/**
 * Created by android on 12/8/16.
 */

public class mapFragment extends android.support.v4.app.Fragment {


    public mapFragment() {}

    @SuppressLint("ValidFragment")
    public mapFragment(Location location)
    {
        startLocation = location;
    }

    private GoogleApiClient apiClient;
    private MapView mapView;
    private FragmentMapBinding binding;
    private GoogleMap map;
    private LocationRequest request;
    private Location startLocation;
    private Location prev;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        View view = binding.getRoot();
        initMap(savedInstanceState);
        return view;
    }




    /**
     * Initialize Map Object
     * @param savedInstanceState
     */
    private void initMap(Bundle savedInstanceState) {
        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(new MapReadyCallback());
    }


    /**
     * To move Map Camera
     * @param latitude
     * @param longitude
     */
    public void moveMapCamera(double latitude, double longitude) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 16.0f));
    }







    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION);
            }
        }
    }

    public void showRoute(List<LatLng> track) {
        for (int i = 0; i < track.size() - 1; i++) {
            LatLng src = track.get(i);
            LatLng dest = track.get(i + 1);

            Polyline line = map.addPolyline(
                    new PolylineOptions().add(
                            new LatLng(src.latitude, src.longitude),
                            new LatLng(dest.latitude,dest.longitude)
                    ).width(4).color(Color.RED).geodesic(true)
            );
        }
        addMarker(track);
    }

    private void addMarker(List<LatLng> track) {
        //  mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.masjid)).position(location).title(masjid.getName())).setTag(masjid);
        map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(track.get(0)));
        map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(track.get(track.size()-1)));

    }


    private class MapReadyCallback implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION);
                return;
            }
            map.setMyLocationEnabled(true);
            //moveMapCamera(startLocation.getLatitude(), startLocation.getLongitude());
        }
    }

}