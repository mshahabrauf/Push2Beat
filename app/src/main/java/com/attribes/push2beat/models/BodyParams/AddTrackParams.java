package com.attribes.push2beat.models.BodyParams;

/**
 * Created by android on 12/9/16.
 */

public class AddTrackParams {
    private String track_name;
    private int genrated_by;
    private double distance;
    private  int track_time;
    private int caleries_burnt;
    private double start_latitude;
    private double start_longitude;
    private  double end_latitude;
    private double end_longitude;
    private  String track_path;
    private int track_type;

    public String getTrack_name() {
        return track_name;
    }

    public int getGenrated_by() {
        return genrated_by;
    }

    public double getDistance() {
        return distance;
    }

    public int getTrack_time() {
        return track_time;
    }

    public int getCaleries_burnt() {
        return caleries_burnt;
    }

    public double getStart_latitude() {
        return start_latitude;
    }

    public double getStart_longitude() {
        return start_longitude;
    }

    public double getEnd_latitude() {
        return end_latitude;
    }

    public double getEnd_longitude() {
        return end_longitude;
    }

    public String getTrack_path() {
        return track_path;
    }

    public int getTrack_type() {
        return track_type;
    }

    public void setTrack_name(String track_name) {
        this.track_name = track_name;
    }

    public void setGenrated_by(int genrated_by) {
        this.genrated_by = genrated_by;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setTrack_time(int track_time) {
        this.track_time = track_time;
    }

    public void setCaleries_burnt(int caleries_burnt) {
        this.caleries_burnt = caleries_burnt;
    }

    public void setStart_latitude(double start_latitude) {
        this.start_latitude = start_latitude;
    }

    public void setStart_longitude(double start_longitude) {this.start_longitude = start_longitude;}

    public void setEnd_latitude(double end_latitude) {
        this.end_latitude = end_latitude;
    }

    public void setEnd_longitude(double end_longitude) {
        this.end_longitude = end_longitude;
    }

    public void setTrack_path(String track_path) {
        this.track_path = track_path;
    }

    public void setTrack_type(int track_type) {
        this.track_type = track_type;
    }
}
