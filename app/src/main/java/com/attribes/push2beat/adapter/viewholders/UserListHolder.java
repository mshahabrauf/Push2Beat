package com.attribes.push2beat.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.attribes.push2beat.R;

/**
 * Created by android on 12/12/16.
 */

public class UserListHolder extends RecyclerView.ViewHolder{

    public ImageView profileImage;
    public TextView fullName;
    public TextView distance;
    public Button startBtn;


    public UserListHolder(View itemView) {
        super(itemView);
        profileImage = (ImageView) itemView.findViewById(R.id.profile_image);
        fullName = (TextView) itemView.findViewById(R.id.user_name);
        distance = (TextView) itemView.findViewById(R.id.distance);
        startBtn = (Button) itemView.findViewById(R.id.start_btn);

    }
}
