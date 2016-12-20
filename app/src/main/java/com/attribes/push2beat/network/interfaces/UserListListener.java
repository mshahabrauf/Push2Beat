package com.attribes.push2beat.network.interfaces;

import com.attribes.push2beat.models.Response.Datum;

import java.util.List;

/**
 * Created by Maaz on 12/13/2016.
 */
public interface UserListListener {
    public void onDataRecieved(List<Datum> data);
}
