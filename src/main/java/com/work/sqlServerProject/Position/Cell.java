package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.Point;

import java.util.*;

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
    private Cell previous;
    private Cell next;
    private int leftBorderAzimuth;
    private int rightBorderAzimuth;
    private int channel;
    private TreeSet<Cell> cellsInBand;
    private List<Point> pointsInCell;
    private double distance;

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


    public void setPreviousAndNextCells(){
        List<Cell>list = new ArrayList<>();
        int m=0;
        int indexOfSelfCell=0;
        for (Cell c : cellsInBand){
            list.add(c);
            if (c.azimuth==this.azimuth){
                indexOfSelfCell=m;
            }
            m++;
        }
        if (indexOfSelfCell==m-1){
            this.next=list.get(0);
            this.previous=list.get(indexOfSelfCell-1);
        }
        else
        if (indexOfSelfCell==0){
            this.next=list.get(indexOfSelfCell+1);
            this.previous=list.get(m-1);
        }
        else
        {
            this.next=list.get(indexOfSelfCell+1);
            this.previous=list.get(indexOfSelfCell-1);
        }
        leftBorderAzimuth=((this.azimuth+previous.azimuth)%360)/2;
        rightBorderAzimuth=((this.azimuth+next.azimuth)%360)/2;
    }

    public void setPointsInCellFromNBF(List<Point>allPointsFromNBF){
        pointsInCell=new ArrayList<>();
        for (Point p : allPointsFromNBF){
            double dist = toDist(this.lalitude,this.longitude,p.getLatitude(),p.getLongitude());
            double az = toAzimuth(dist, p);
            if (dist<=distance){
                if (rightBorderAzimuth>leftBorderAzimuth) {
                    if (az > leftBorderAzimuth && az < rightBorderAzimuth) {
                        System.out.println(dist + " " + az + " " + p.getLatitude() + " " + p.getLongitude());
                        pointsInCell.add(p);
                    }
                }
                else
                    if ((az>=0 && az<rightBorderAzimuth) || (az<360 && az>leftBorderAzimuth)){
                        //System.out.println(dist + " " + az + " " + p.getLatitude() + " " + p.getLongitude());
                        pointsInCell.add(p);
                    }
            }
        }
    }

    final static double pi=3.1415926535898;
    final static double EarthRadius= 6372795.0;

    public static double toDist(double latA, double lonA, double latB, double lonB){
        double latArad=latA*pi/180;
        double latBrad=latB*pi/180;
        double lonArad=lonA*pi/180;
        double lonBrad=lonB*pi/180;

        double cl1= Math.cos(latArad);
        double cl2= Math.cos(latBrad);
        double sl1=Math.sin(latArad);
        double sl2=Math.sin(latBrad);
        double delta = lonBrad-lonArad;
        double cdelta = Math.cos(delta);
        double sdelta = Math.sin(delta);

        double y = Math.sqrt(Math.pow(cl2 * sdelta, 2) + Math.pow(cl1 * sl2 - sl1 * cl2 * cdelta, 2));
        double x = sl1 * sl2 + cl1 * cl2 * cdelta;

        double ad = Math.atan2(y,x);
        double dist=ad*EarthRadius;
        return dist;
    }

    public double toAzimuth(Double dist, Point point){
        double x= toDist(this.lalitude, this.longitude, this.lalitude, point.getLongitude());
        double h= dist;
        double azimuth=Math.asin(x/h)*180/pi;
        double lonDif=point.getLongitude()-this.longitude;
        double latDif=point.getLatitude()-this.lalitude;
        if (lonDif>0 && latDif>0){
            return azimuth;
        }
        else
        if (lonDif>0 && latDif<0){
            return 180-azimuth;
        }
        else
        if (lonDif<0 && latDif<0){
            return 180+azimuth;
        }
        else
        return 360-azimuth;
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
        this.distance=2000; //2 км
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<Point> getPointsInCell() {
        return pointsInCell;
    }

    public void setPointsInCell(List<Point> pointsInCell) {
        this.pointsInCell = pointsInCell;
    }

    public TreeSet<Cell> getCellsInBand() {
        return cellsInBand;
    }

    public void setCellsInBand(TreeSet<Cell> cellsInBand) {
        this.cellsInBand = cellsInBand;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public Cell getPrevious() {
        return previous;
    }

    public void setPrevious(Cell previous) {
        this.previous = previous;
    }

    public Cell getNext() {
        return next;
    }

    public void setNext(Cell next) {
        this.next = next;
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
