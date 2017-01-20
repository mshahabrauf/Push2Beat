package com.attribes.push2beat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.RecyclerAdapterInterface;
import com.attribes.push2beat.adapter.viewholders.MyStatsListHolder;
import com.attribes.push2beat.models.Response.MyStatsList.Datum;

import java.util.List;

/**
 * Created by Maaz on 12/23/2016.
 */
public class MyStatsAdapter extends RecyclerView.Adapter<MyStatsListHolder>{

    public List<Datum> mData;
    private Context mContext;
    private RecyclerAdapterInterface listener;

    public MyStatsAdapter(List<Datum> mData, RecyclerAdapterInterface listener) {
        this.mData = mData;
        this.listener = listener;
    }

    @Override
    public MyStatsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View layout = LayoutInflater.from(mContext).inflate(R.layout.mystats_listitems,parent,false);
        MyStatsListHolder holder = new MyStatsListHolder(layout);
        return  holder;
    }

    @Override
    public void onBindViewHolder(MyStatsListHolder holder, int position) {

        holder.mystats_cal_current.setText(mData.get(position).getCaleries_current());
        holder.mystats_cal_goal.setText(mData.get(position).getCaleries_goal());
        holder.mystats_distance_current.setText(roundOffDecimals(mData.get(position).getDistance_current()));
        holder.mystats_distance_goal.setText(mData.get(position).getDistance_goal());

//        if(mData.get(position).getProfile_image() != "") {
//            holder.profile_image.setImageURI(Uri.parse(mData.get(position).getProfile_image()));
//
//        }


        //Todo Display profile picture if available

    }


    public String roundOffDecimals(String value)
    {
        double roundOff = Math.round(Double.valueOf(value) * 100.0) / 100.0;
        return ""+ roundOff;

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

}
