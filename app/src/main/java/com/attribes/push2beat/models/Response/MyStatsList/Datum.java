package com.attribes.push2beat.models.Response.MyStatsList;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maaz on 12/23/2016.
 */
public class Datum {

    private String first_name;
    private String profile_image;
    private String user_id;
    private String caleries_goal;
    private String caleries_current;
    private String distance_goal;
    private String distance_current;
    private String stat_type;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCaleries_goal() {
        return caleries_goal;
    }

    public void setCaleries_goal(String caleries_goal) {
        this.caleries_goal = caleries_goal;
    }

    public String getCaleries_current() {
        return caleries_current;
    }

    public void setCaleries_current(String caleries_current) {
        this.caleries_current = caleries_current;
    }

    public String getDistance_goal() {
        return distance_goal;
    }

    public void setDistance_goal(String distance_goal) {
        this.distance_goal = distance_goal;
    }

    public String getDistance_current() {
        return distance_current;
    }

    public void setDistance_current(String distance_current) {
        this.distance_current = distance_current;
    }

    public String getStat_type() {
        return stat_type;
    }

    public void setStat_type(String stat_type) {
        this.stat_type = stat_type;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}