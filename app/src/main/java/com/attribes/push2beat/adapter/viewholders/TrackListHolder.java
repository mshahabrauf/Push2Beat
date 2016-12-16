package com.attribes.push2beat.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.attribes.push2beat.R;

/**
 * Created by android on 12/17/16.
 */

public class TrackListHolder extends RecyclerView.ViewHolder {

    public TextView trackName;
    public TextView calories;
    public Button startBtn;

    public TrackListHolder(View itemView) {
        super(itemView);
        trackName = (TextView) itemView.findViewById(R.id.track_name);
        calories = (TextView) itemView.findViewById(R.id.burn_calories);
        startBtn = (Button) itemView.findViewById(R.id.start_btn);

    }
}
