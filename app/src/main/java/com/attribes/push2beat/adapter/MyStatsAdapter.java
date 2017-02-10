package com.attribes.push2beat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.RecyclerAdapterInterface;
import com.attribes.push2beat.adapter.viewholders.MyStatsListHolder;
import com.attribes.push2beat.adapter.viewholders.StatsTrackHolder;
import com.attribes.push2beat.models.Response.MyStatsList.Datum;
import com.attribes.push2beat.models.Response.MyStatsList.Track;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maaz on 12/23/2016.
 */
public class MyStatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<Datum> mData;
    public List<Track> mTracks;
    private Context mContext;
    protected final int VIEW_TYPE_STATS = 0;
    protected final int VIEW_TYPE_TRACKS = 1;
    private RecyclerAdapterInterface listener;
    private String statsTitle = "";
    private List<LatLng> traker = new ArrayList<>();



    public MyStatsAdapter(List<Datum> mData, List<Track> mTracks) {
        this.mData = mData;
        this.mTracks = mTracks;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_STATS) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mystats_listitems, parent, false);
           // return new MyStatsListHolder(v);
             MyStatsListHolder myStatsListHolder=new MyStatsListHolder(v);
            return myStatsListHolder;

        } else if (viewType == VIEW_TYPE_TRACKS) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_stats_track_detail_item, parent, false);
            return new StatsTrackHolder(v);
        }
        return null;


       // mContext = parent.getContext();
       // View layout = LayoutInflater.from(mContext).inflate(R.layout.mystats_listitems,parent,false);
        // MyStatsListHolder holder = new MyStatsListHolder(layout);
        //return  holder;
    }

    @Override
    public int getItemViewType(int position) {
        /* It is for the recognition of
         * which view would be embedded */
        if(position >= mData.size())
        {
            return VIEW_TYPE_TRACKS;
        }
        else {
            return VIEW_TYPE_STATS;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {


        if(position >= mData.size())
        {
            StatsTrackHolder holder1 = (StatsTrackHolder) holder;



            if(position - mData.size() == 0)
            {
                holder1.title.setVisibility(View.VISIBLE);
            }



            traker = Common.getInstance().convertStringIntoLatlng(mTracks.get(position-mData.size()).getTrack_path());
            holder1.traker = traker;
        //    holder1.showRoute(traker);
            holder1.calories.setText(mTracks.get(position-mData.size()).getCaleries_burnt());

            double dist = Double.parseDouble(mTracks.get(position-mData.size()).getDistance());
            holder1.distance.setText(String.valueOf(Math.round(dist * 100) / 100)+"km");
            holder1.averageSpeed.setText(mTracks.get(position-mData.size()).getAverage_speed()+"km/hr");
            holder1.topSpeed.setText(mTracks.get(position-mData.size()).getTop_speed()+"km/hr");



        }
        else
        {
            MyStatsListHolder holder1 = (MyStatsListHolder) holder;

            int cal = (int) Double.parseDouble(mData.get(position).getCaleries_current());

            holder1.mystats_cal_current.setText("" + cal);
            holder1.mystats_cal_goal.setText(mData.get(position).getCaleries_goal());
            holder1.mystats_distance_current.setText(roundOffDecimals(mData.get(position).getDistance_current()) + " km");
            holder1.mystats_distance_goal.setText(mData.get(position).getDistance_goal());
            holder1.mystats_type.setText(setStatsTitle(mData.get(position).getStat_type()));

            if(mData.get(position).getProfile_image() != "") {
             //   holder1.profile_image.setImageURI(Uri.parse(mData.get(position).getProfile_image()));

            }
        }


    }



    public String roundOffDecimals(String value)
    {
        double roundOff = Math.round(Double.valueOf(value) * 100.0) / 100.0;
        return ""+ roundOff;

    }


    @Override
    public int getItemCount() {
        return (mData.size())+ mTracks.size();
    }

    public String setStatsTitle(String typeCode) {

        switch (typeCode)
        {
            case "1":
                statsTitle = "All Tracks";
                break;
            case "2":
                statsTitle = "Ghost Riders";
                break;
            case "3":
                statsTitle = "Catch Me if you can";
                break;

        }
        return statsTitle;
    }
}
