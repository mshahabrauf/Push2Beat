package com.attribes.push2beat.models.Response.UserSignUp;


/**
 * Created by Talha Ghaffar on 12/13/2016.
 */
public class SignupResponse {


        public Integer code;
        public String msg;
        public Data data;

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