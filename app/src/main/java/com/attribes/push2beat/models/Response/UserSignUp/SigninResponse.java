package com.attribes.push2beat.models.Response.UserSignUp;

/**
 * Created by Talha Ghaffar on 12/14/2016.
 */
public class SigninResponse {
    public Integer code;
    public String msg;
    public LoginData data;

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

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }
}
