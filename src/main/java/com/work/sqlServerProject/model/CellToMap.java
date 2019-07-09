package com.work.sqlServerProject.model;

/**
 * Created by a.shcherbakov on 28.06.2019.
 */
public class CellToMap {
    private String ci;
    private String color;
    private int azimuth;

    public CellToMap(int ci, int azimuth, String color) {
        this.azimuth=azimuth;
        this.ci = ci+"";
        this.color=color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(int azimuth) {
        this.azimuth = azimuth;
    }


    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }
}
