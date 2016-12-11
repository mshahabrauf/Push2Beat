package com.attribes.push2beat.models.Response;

import java.util.List;

/**
 * Created by android on 12/9/16.
 */

public class AddTrackResponse {
    private Integer code;
    private String msg;
    private List<Object> data = null;

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

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
