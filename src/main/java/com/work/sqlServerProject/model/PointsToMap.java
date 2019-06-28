package com.work.sqlServerProject.model;

/**
 * Created by a.shcherbakov on 28.06.2019.
 */
public class PointsToMap {
    private double longitude;
    private double latitude;

    public PointsToMap(double longituge, double latitude) {
        this.longitude = longituge;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "PointsToMap{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
