package com.attribes.push2beat.models.Response.MyStatsList;

import java.util.List;

/**
 * Created by Maaz on 12/23/2016.
 */
public class MyStatsResponse {

    private Integer code;
    private String msg;
    private List<Datum> data = null;
    private List<Track> tracks = null;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
