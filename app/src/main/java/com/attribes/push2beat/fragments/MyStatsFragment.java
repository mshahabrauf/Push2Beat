package com.attribes.push2beat.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.attribes.push2beat.R;
import com.attribes.push2beat.databinding.FragmentMyStatsBinding;
import com.attribes.push2beat.utils.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Maaz on 12/11/2016.
 */
public class MyStatsFragment extends android.support.v4.app.Fragment  {

     FragmentMyStatsBinding mtBinding;
     private MapView mapView;
     private GoogleMap map;
    private Location startLocation;

    public MyStatsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mtBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_stats,container,false);
        View view = mtBinding.getRoot();
        initMap(savedInstanceState);
        return view;
    }

    private void initMap(Bundle savedInstanceState) {
        mapView = mtBinding.statsMapView;
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(new MapReadyCallback());
    }

    private void moveMapCamera(double latitude, double longitude) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 16.0f));
    }

    private class MapReadyCallback implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION);
                return;
            }
            map.setMyLocationEnabled(true);
            if(startLocation != null)
            {
                moveMapCamera(startLocation.getLatitude(), startLocation.getLongitude());
            }
        }
    }
}
