package com.attribes.push2beat.network.interfaces;

import com.attribes.push2beat.models.Response.TrackList.Datum;

import java.util.List;

/**
 * Created by android on 12/17/16.
 */

public interface TracksArrivalListener {

    void onDataRecieved(List<Datum> data);
    void onEmptyData(String msg);
}
