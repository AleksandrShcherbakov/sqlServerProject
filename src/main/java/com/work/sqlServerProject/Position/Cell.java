package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Cell {
    private CellInfo cellInfo;
    private int posname;
    private int ci;
    private double longitude;
    private double lalitude;
    private int azimuth;

    public Cell(CellInfo cellInfo) {
        this.cellInfo = cellInfo;
        this.posname=cellInfo.getSyte();
        this.ci=cellInfo.getCi();
        this.longitude=cellInfo.getLon();
        this.lalitude=cellInfo.getLat();
        this.azimuth=cellInfo.getDir();
    }

    public int getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(int azimuth) {
        this.azimuth = azimuth;
    }

    public CellInfo getCellInfo() {
        return cellInfo;
    }

    public void setCellInfo(CellInfo cellInfo) {
        this.cellInfo = cellInfo;
    }

    public int getPosname() {
        return posname;
    }

    public void setPosname(int posname) {
        this.posname = posname;
    }

    public int getCi() {
        return ci;
    }

    public void setCi(int ci) {
        this.ci = ci;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLalitude() {
        return lalitude;
    }

    public void setLalitude(double lalitude) {
        this.lalitude = lalitude;
    }

}
