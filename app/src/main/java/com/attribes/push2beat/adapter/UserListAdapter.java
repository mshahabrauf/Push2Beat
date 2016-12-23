package com.attribes.push2beat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.RecyclerAdapterInterface;
import com.attribes.push2beat.adapter.viewholders.UserListHolder;
import com.attribes.push2beat.models.Response.MyProfileResponse;
import com.attribes.push2beat.models.Response.PushFireBase.Data;
import com.attribes.push2beat.models.Response.PushFireBase.PushData;
import com.attribes.push2beat.models.Response.UserList.Datum;
import com.attribes.push2beat.network.DAL.GetProfileDAL;
import com.attribes.push2beat.network.DAL.SendPush;
import com.attribes.push2beat.network.interfaces.ProfileDataArrivalListner;

import java.util.List;

/**
 * Created by android on 12/12/16.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListHolder> {



    public List<Datum> mData;
    private Context mContext;
    private RecyclerAdapterInterface listener;

    public UserListAdapter(List<Datum> data,RecyclerAdapterInterface mapInterface)
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
        holder.distance.setText(Common.getInstance().calulateDistance(mData.get(position).getLat(),mData.get(position).getLng()));
        holder.startBtn.setOnClickListener(new startButtonListener(position));

        //Todo Display profile picture if available
        //holder.profile_image.setImageURI(Uri.parse(mData.get(position).getProfile_image()));

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }




    private class startButtonListener implements View.OnClickListener {
        private int position;

        public startButtonListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {

            listener.onstartCallback(position);
            GetProfileDAL.getProfileData(mData.get(position).getId(), new ProfileDataArrivalListner() {
                @Override
                public void onDataRecieved(MyProfileResponse.Data data) {
                    PushData push = new PushData();
                    Data dataa = new Data();
                    dataa.setMessage("ye le beta chal");
                    push.setData(dataa);
              //      if (data.getDevice_token().equals("") == false) {
                    push.setTo(data.getDevice_token());
                //    }
                    SendPush.sendPushToUser(push);

                }

                @Override
                public void onEmptyData(String msg) {

                }
            });
//

        }
    }

}
