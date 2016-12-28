package com.attribes.push2beat.models.Response.PushFireBase;

/**
 * Created by Maaz on 11/22/2016.
 */
public class Data {

    public String username;
    public String token;
    public int status;  // If push from sender then it is 0 or if reciver reply back then it is 1

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public int getStatus() {
        return status;
    }

    /**
     * If push from sender then it is 0 or if reciver reply back then it is 1
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
