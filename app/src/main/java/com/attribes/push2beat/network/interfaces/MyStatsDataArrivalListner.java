package com.attribes.push2beat.network.interfaces;

import com.attribes.push2beat.models.Response.MyStatsList.Datum;

import java.util.List;

/**
 * Created by Maaz on 12/23/2016.
 */
public interface MyStatsDataArrivalListner {

    void onDataRecieved(List<Datum> data);
    void onEmptyData(String msg);
}
