package com.attribes.push2beat.network.interfaces;

import com.attribes.push2beat.models.Response.UserList.Datum;

import java.util.List;

/**
 * Created by android on 12/11/16.
 */
public interface ArrivalListener {
    void onDataRecieved(List<Datum> data);
    void onEmptyData(String msg);
}
