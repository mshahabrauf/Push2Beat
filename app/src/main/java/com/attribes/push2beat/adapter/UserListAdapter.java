package com.attribes.push2beat.adapter;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.CatchMeAdapterInterface;
import com.attribes.push2beat.adapter.viewholders.UserListHolder;
import com.attribes.push2beat.models.Response.UserList.Datum;
import com.google.common.eventbus.EventBus;

import java.util.List;

/**
 * Created by android on 12/12/16.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListHolder> {

    EventBus bus;

    public List<Datum> mData;
    private Context mContext;

    private CatchMeAdapterInterface listener;

    public UserListAdapter(List<Datum> data,CatchMeAdapterInterface mapInterface)
    {
        mData = data;
        listener = mapInterface;
    }

    @Override
    public UserListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View layout = LayoutInflater.from(mContext).inflate(R.layout.user_list_item,parent,false);
        UserListHolder holder = new UserListHolder(layout);
        return  holder;
    }

    @Override
    public void onBindViewHolder(UserListHolder holder, int position) {
        holder.fullName.setText(mData.get(position).getUser_name()+" "+mData.get(position).getUser_lastname());
        holder.distance.setText(calulateDistance(mData.get(position).getLat(),mData.get(position).getLng()));
        holder.startBtn.setOnClickListener(new startButtonListener(holder,position));

        //Todo Display profile picture if available
        //holder.profile_image.setImageURI(Uri.parse(mData.get(position).getProfile_image()));

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    /**
     * This method Caluclating distance from the current Location
     * @param lat
     * @param lng
     * @return
     */
    private String calulateDistance(String lat, String lng) {
        Location userLocation = new Location("opponentLocation");
        userLocation.setLatitude(Double.parseDouble(lat));
        userLocation.setLongitude(Double.parseDouble(lng));
        int distance = (int) userLocation.distanceTo(Common.getInstance().getLocation());
        return String.valueOf(distance)+"m";
    }



    private class startButtonListener implements View.OnClickListener {
        private int position;
        private UserListHolder holder;

        public startButtonListener(UserListHolder holder, int position) {
            this.position = position;
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {

        listener.onstartCallback(position);

        }
    }

}
