package com.attribes.push2beat.models.Response.PushFireBase;

/**
 * Created by Maaz on 11/22/2016.
 */
public class PushData {

    public Data data;
    public String to;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
