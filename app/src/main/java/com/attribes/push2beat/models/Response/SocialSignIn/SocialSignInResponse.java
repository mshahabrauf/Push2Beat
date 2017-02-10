package com.attribes.push2beat.models.Response.SocialSignIn;

import com.attribes.push2beat.models.BodyParams.SignInParams;

/**
 * Created by android on 1/13/17.
 */

public class SocialSignInResponse {



        private Integer code;
        private String msg;
        private SignInParams data;

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


    public SignInParams  getData() {
        return data;
    }

    public void setData(SignInParams data) {
        this.data = data;
    }
}
