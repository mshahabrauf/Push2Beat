package com.attribes.push2beat.network.interfaces;

import com.attribes.push2beat.models.Response.MyStatsList.Datum;
import com.attribes.push2beat.models.Response.MyStatsList.Track;

import java.util.List;

/**
 * Created by Maaz on 12/23/2016.
 */
public interface MyStatsDataArrivalListner {

    void onDataRecieved(List<Datum> data, List<Track> track);
    void onEmptyData(String msg);
    void onFailure(String message);

}
