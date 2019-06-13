package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Cell {
    private String system;
    private int band;
    private CellInfo cellInfo;
    private int posname;
    private int ci;
    private double longitude;
    private double lalitude;
    private int azimuth;
    private Cell leftNeibor;
    private Cell rightNeibor;
    private int leftBorderAzimuth;
    private int rightBorderAzimuth;
    private int channel;

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    Comparator<Cell> comparator = new Comparator<Cell>() {
        @Override
        public int compare(Cell o1, Cell o2) {
            if (o1.azimuth>o2.azimuth)
                return 1;
            else
            if (o1.azimuth==o2.azimuth)
                return 0;
            else return -1;
        }
    };

    public TreeSet<Cell> getCellsInBand() {
        return cellsInBand;
    }

    public void setCellsInBand(TreeSet<Cell> cellsInBand) {
        this.cellsInBand = cellsInBand;
    }

    TreeSet<Cell> cellsInBand;

        public void setAllCellsInBand(List<Cell> cellsInBand) {
        for (Cell c : cellsInBand){
            if (c.system.equals(this.system) && c.band==this.band){
                if (c.system.equals("UMTS")){
                    if (c.channel==this.channel){
                        this.cellsInBand.add(c);
                    }
                }
                else
                    this.cellsInBand.add(c);
            }
        }
    }

    public void setLeftAndRightBorder(){


    }



    public Cell(CellInfo cellInfo) {
        this.cellInfo = cellInfo;
        this.posname=cellInfo.getSyte();
        this.ci=cellInfo.getCi();
        this.longitude=cellInfo.getLon();
        this.lalitude=cellInfo.getLat();
        this.azimuth=cellInfo.getDir();
        this.band=cellInfo.getBand();
        this.system=cellInfo.getSystem();
        this.cellsInBand=new TreeSet<>(comparator);
        this.channel=cellInfo.getCh();
    }



    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public int getBand() {
        return band;
    }

    public void setBand(int band) {
        this.band = band;
    }

    public Cell getLeftNeibor() {
        return leftNeibor;
    }

    public void setLeftNeibor(Cell leftNeibor) {
        this.leftNeibor = leftNeibor;
    }

    public Cell getRightNeibor() {
        return rightNeibor;
    }

    public void setRightNeibor(Cell rightNeibor) {
        this.rightNeibor = rightNeibor;
    }

    public int getLeftBorderAzimuth() {
        return leftBorderAzimuth;
    }

    public void setLeftBorderAzimuth(int leftBorderAzimuth) {
        this.leftBorderAzimuth = leftBorderAzimuth;
    }

    public int getRightBorderAzimuth() {
        return rightBorderAzimuth;
    }

    public void setRightBorderAzimuth(int rightBorderAzimuth) {
        this.rightBorderAzimuth = rightBorderAzimuth;
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
