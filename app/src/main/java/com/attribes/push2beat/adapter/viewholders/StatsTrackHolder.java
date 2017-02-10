package com.attribes.push2beat.adapter.viewholders;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.attribes.push2beat.R;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2/8/17.
 */

public class StatsTrackHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback{

    public TextView title;
    public MapView map;
    public GoogleMap gMap;
    public TextView calories;
    public TextView distance;
    public TextView topSpeed;
    public TextView averageSpeed;
    public List<LatLng> traker;

    private Polyline line;
    private Marker start;
    private Marker end;





    public StatsTrackHolder(View itemView) {
        super(itemView);
        map = (MapView) itemView.findViewById(R.id.mapView);
        calories = (TextView) itemView.findViewById(R.id.stats_calburnt_tv);
        distance = (TextView) itemView.findViewById(R.id.stats_distancetravelled_tv);
        title = (TextView) itemView.findViewById(R.id.titler);
        topSpeed = (TextView) itemView.findViewById(R.id.stats_topspeed_tv);
        averageSpeed = (TextView) itemView.findViewById(R.id.stats_averagespeed_tv);
        traker = new ArrayList<>();

        if (map != null)
        {
            map.onCreate(null);
            map.onResume();
            map.getMapAsync(this);
            map.setClickable(false);
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        showRoute(traker);

    }



    public void showRoute(List<LatLng> track) {


        if(track != null && gMap != null){
            for (int i = 0; i < track.size() - 1; i++) {
                LatLng src = track.get(i);
                LatLng dest = track.get(i + 1);

                line = gMap.addPolyline(
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
    private void addTrackMarker(List<LatLng> track) {
        if(track.size() > 0) {
            start = gMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(track.get(0)));
            end = gMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(track.get(track.size() - 1)));
        }
    }



    public void moveMapCamera(double latitude, double longitude) {
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 16.0f));
    }


}
