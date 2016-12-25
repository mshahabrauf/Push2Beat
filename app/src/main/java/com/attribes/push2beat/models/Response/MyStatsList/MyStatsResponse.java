package com.attribes.push2beat.models.Response.MyStatsList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maaz on 12/23/2016.
 */
public class MyStatsResponse {

    private Integer code;
    private String msg;
    private List<Datum> data = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
