package com.attribes.push2beat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.adapter.viewholders.StatsTrackHolder;
import com.attribes.push2beat.models.Response.MyStatsList.Track;
import com.google.android.gms.maps.GoogleMap;
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

public class TracKStatsAdapter extends RecyclerView.Adapter<StatsTrackHolder> {

    public List<Track> tracks;
    Context context;
    private Polyline line;
    private Marker start;
    private Marker end;
    GoogleMap map;


    public TracKStatsAdapter(List<Track> track) {
        tracks =  track;
    }


    @Override
    public StatsTrackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View layout = LayoutInflater.from(context).inflate(R.layout.my_stats_track_detail_item,parent,false);
        StatsTrackHolder holder = new StatsTrackHolder(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(StatsTrackHolder holder, int position) {

        map = holder.gMap;
        List<LatLng> traker = new ArrayList<>();
        traker = Common.getInstance().convertStringIntoLatlng(tracks.get(position).getTrack_path());
        holder.calories.setText(tracks.get(position).getCaleries_burnt());

        double dist = Double.parseDouble(tracks.get(position).getDistance());
        holder.distance.setText(String.valueOf(Math.round(dist * 100) / 100)+"km");
        holder.averageSpeed.setText(tracks.get(position).getAverage_speed()+"km/hr");
        holder.topSpeed.setText(tracks.get(position).getTop_speed()+"km/hr");

      if(map!=null) {
          showRoute(traker);
      }



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
        //   moveMapCamera(track.get(0).latitude,track.get(0).longitude);
    }


    private void addTrackMarker(List<LatLng> track) {
        if(track.size() > 0) {
            start = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(track.get(0)));
            end = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(track.get(track.size() - 1)));
        }
    }

//    @Override
//    public void onViewRecycled(StatsTrackHolder holder) {
//        if(holder.gMap != null) {
//            holder.gMap.clear();
//            holder.gMap.setMapType(GoogleMap.MAP_TYPE_NONE);
//        }
//
//
//    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }


}
