package com.attribes.push2beat.models.Response.UserList;

import java.util.List;

/**
 * Created by android on 12/11/16.
 */

public class ListOfUserResponse {

    public Integer code;

    public String msg;

    public List<Datum> data = null;

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
}
