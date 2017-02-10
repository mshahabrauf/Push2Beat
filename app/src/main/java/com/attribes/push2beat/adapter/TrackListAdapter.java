package com.attribes.push2beat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.RecyclerAdapterInterface;
import com.attribes.push2beat.adapter.viewholders.TrackListHolder;
import com.attribes.push2beat.models.Response.TrackList.Datum;

import java.util.List;

/**
 * Created by android on 12/17/16.
 */

public class TrackListAdapter extends RecyclerView.Adapter<TrackListHolder> {
    private Context mContext;
    private List<Datum> data;
    private RecyclerAdapterInterface listener;





    public TrackListAdapter(List<Datum> data,RecyclerAdapterInterface listener)
    {
        this.data = data;
        this.listener = listener;
    }


    @Override
    public TrackListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.track_list_item,parent,false);
        TrackListHolder holder = new TrackListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TrackListHolder holder, int position) {

        holder.calories.setText(data.get(position).getCaleries_burnt());
        holder.trackName.setText(data.get(position).getTrack_name());
        holder.startBtn.setOnClickListener(new OnStartListener(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class OnStartListener implements View.OnClickListener {
        private int position;

        public OnStartListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            listener.onstartCallback(position);
        }
    }
}
