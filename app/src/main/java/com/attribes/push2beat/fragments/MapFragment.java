package com.attribes.push2beat.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.databinding.FragmentMapBinding;
import com.attribes.push2beat.models.Response.UserList.Datum;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.quickblox.core.QBEntityCallback;

import com.quickblox.core.account.model.QBAccountSettings;
import com.quickblox.core.exception.QBResponseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 12/8/16.
 */

public class MapFragment extends android.support.v4.app.Fragment {


    public MapFragment() {


    }

    @SuppressLint("ValidFragment")
    public MapFragment(Location location)
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
    private Marker start;
    private Marker end;
    private Polyline line;
    private MarkerOptions opponent;
    StatsFragment.MapListener maplistener;

    @SuppressLint("ValidFragment")
    public MapFragment(Location location, StatsFragment.MapListener mapListener) {
        maplistener = mapListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        View view = binding.getRoot();
        initMap(savedInstanceState);
        initQuickBlox();
        return view;
    }

    private void initQuickBlox() {
//        QBSettings.getInstance().init(getContext(), Constants.APP_ID, Constants.AUTH_KEY, Constants.AUTH_SECRET);
//        QBSettings.getInstance().setAccountKey(Constants.ACCOUNT_KEY);
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




    private void addTrackMarker(List<LatLng> track) {
        if(track.size() > 0) {
            start = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(track.get(0)));
            end = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(track.get(track.size() - 1)));
            }
    }




    private class MapReadyCallback implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            if(maplistener != null)
            {
            maplistener.onMapReady();
            }


            if(opponent != null)
            {
                map.addMarker(opponent);

            }

            if(startLocation!=null) {
                map.setMyLocationEnabled(true);
                moveMapCamera(startLocation.getLatitude(),startLocation.getLongitude());
            }
            else {
                startLocation = Common.getInstance().getLocation();
                map.setMyLocationEnabled(true);
                moveMapCamera(Common.getInstance().getLocation().getLatitude(),Common.getInstance().getLocation().getLongitude());
            }
        }
    }









    //================================================Public Methods===================================//











    public void removeTrackMarkers()
    {
        if(start != null)
        {
            start.remove();
            end.remove();
            line.remove();
        }
    }






    public void showUsers(List<Datum> data)
    {
        for(Datum datum:data)
        {
            LatLng userPosition =new LatLng(Double.parseDouble(datum.getLat()),Double.parseDouble(datum.getLng()));
            map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name)).position(userPosition)).setTag(datum);

        }

    }



    public void addOpponentMaker(double lat,double lng)
    {
        LatLng latLng = new LatLng(lat,lng);
        opponent = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name)).position(latLng);
      //  map.addMarker(opponent);

    }

    public void moveOpponent(double lat,double lng){
        LatLng latLng = new LatLng(lat,lng);
        opponent.position(latLng);

    }

    public void showRoute(List<LatLng> track) {
        if(track != null){
            for (int i = 0; i < track.size() - 1; i++) {
                LatLng src = track.get(i);
                LatLng dest = track.get(i + 1);

                line = map.addPolyline(
                        new PolylineOptions().add(
                                new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude,dest.longitude)
                        ).width(4).color(Color.RED).geodesic(true)
                );
            }
            addTrackMarker(track);
        }
        moveMapCamera(track.get(0).latitude,track.get(0).longitude);
    }

    /**
     * To move Map Camera
     * @param latitude
     * @param longitude
     */
    public void moveMapCamera(double latitude, double longitude) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 16.0f));
    }






}
