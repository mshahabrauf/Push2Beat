package com.attribes.push2beat.models.Response.UserList;

/**
 * Author: Uzair Qureshi
 * Date:  2/22/17.
 * Description:
 */

public class UpdateLocationResponse
{
    private String[] data;
    private String code;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }


}
