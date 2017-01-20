package com.attribes.push2beat.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.attribes.push2beat.R;

/**
 * Created by Maaz on 12/24/2016.
 */
public class MyStatsListHolder extends RecyclerView.ViewHolder {

    public TextView mystats_cal_goal;
    public TextView mystats_distance_goal;
    public TextView mystats_cal_current;
    public TextView mystats_distance_current;


    public MyStatsListHolder(View itemView) {
        super(itemView);

            mystats_cal_goal = (TextView) itemView.findViewById(R.id.mystats_calories_goal);
            mystats_distance_goal  = (TextView) itemView.findViewById(R.id.mystats_distance_goal);
            mystats_cal_current  = (TextView) itemView.findViewById(R.id.mystats_calorietv);
            mystats_distance_current  = (TextView) itemView.findViewById(R.id.mystats_distancetv);
    }
}
