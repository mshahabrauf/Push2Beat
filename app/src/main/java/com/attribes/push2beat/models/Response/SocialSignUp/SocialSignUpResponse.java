package com.attribes.push2beat.models.Response.SocialSignUp;

/**
 * Created by Talha Ghaffar on 12/28/2016.
 */
public class SocialSignUpResponse {

    private Integer code;
    private String msg;
   private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}