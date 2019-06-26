package com.work.sqlServerProject.model;

/**
 * Created by a.shcherbakov on 26.06.2019.
 */
public class TestPoint {
    private double longitude;
    private double latytude;

    public TestPoint(double longitude, double latytude) {
        this.longitude = longitude;
        this.latytude = latytude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatytude() {
        return latytude;
    }

    public void setLatytude(double latytude) {
        this.latytude = latytude;
    }
}
