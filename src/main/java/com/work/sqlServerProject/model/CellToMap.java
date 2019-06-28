package com.work.sqlServerProject.model;

/**
 * Created by a.shcherbakov on 28.06.2019.
 */
public class CellToMap {
    private double longitude;
    private double latitude;
    private String ci;

    public CellToMap(double longityde, double latitude, int ci) {
        this.longitude = longityde;
        this.latitude = latitude;
        this.ci = ci+"";
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

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }
}
