package com.attribes.push2beat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Alerts;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.Utils.RecyclerAdapterInterface;
import com.attribes.push2beat.adapter.viewholders.UserListHolder;
import com.attribes.push2beat.interfaces.MyCallBacks;
import com.attribes.push2beat.models.Response.PushFireBase.PushResponse;
import com.attribes.push2beat.models.Response.UserList.Datum;
import com.attribes.push2beat.network.DAL.ChallengeDAL;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by android on 12/12/16.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListHolder> {



    public List<Datum> mData;
    private Context mContext;


    public UserListAdapter(List<Datum> data)
    {
        mData = data;

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

        holder.fullName.setText(mData.get(position).getUser_name());
        holder.distance.setText(Common.getInstance().calulateDistance(mData.get(position).getLat(),mData.get(position).getLng()));
        holder.startBtn.setOnClickListener(new startButtonListener(position));

        if(mData.get(position).getProfile_image() != "") {
            Picasso.with(mContext).load(mData.get(position).getProfile_image()).placeholder(R.drawable.placeholder).into(holder.profileImage);
        }

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }




    private class startButtonListener implements View.OnClickListener {
        private int position;

        public startButtonListener(int position)
        {
            this.position = position;
        }

        @Override
        public void onClick(View view) {

            ChallengeDAL.challengeOpponent(mData.get(position).getId(),
                    DevicePreferences.getInstance().getuser().getId(), new MyCallBacks<PushResponse>()
                    {
                        @Override
                        public void onSuccess(PushResponse data)
                        {
                            Common.getInstance().setOpponentData(mData.get(position));//need some verification
                            Alerts.showDialoge(mContext,"Challenge Successfully Sent");

                        }

                        @Override
                        public void onFailure(String message)
                        {
                            Alerts.showDialoge(mContext,message);

                        }
                    });

        }
    }

}




