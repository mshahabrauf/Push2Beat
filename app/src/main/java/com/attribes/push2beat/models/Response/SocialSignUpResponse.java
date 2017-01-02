package com.attribes.push2beat.models.Response;

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
    public class Data {

        private Integer u_id;

        public Integer getU_id() {
            return u_id;
        }

        public void setU_id(Integer u_id) {
            this.u_id = u_id;
        }

    }

}