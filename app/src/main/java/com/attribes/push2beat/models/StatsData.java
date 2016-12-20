package com.attribes.push2beat.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by android on 12/20/16.
 */

public class StatsData {
  private String userId;
  private double traveledDistance;
  private double topSpeed;
   private double averageSpeed;
    private int calories;
    private List<LatLng> path;


    public double getTraveledDistance() {
        return traveledDistance;
    }

    public void setTraveledDistance(double traveledDistance) {
        this.traveledDistance = traveledDistance;
    }

    public double getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(double topSpeed) {
        this.topSpeed = topSpeed;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public List<LatLng> getPath() {
        return path;
    }

    public void setPath(List<LatLng> path) {
        this.path = path;
    }
}
