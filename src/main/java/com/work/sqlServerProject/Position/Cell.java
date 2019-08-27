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
    private List<Point> pointsInCell=new ArrayList<>();
    private double maxDistance;
    private double minDistance;
    private int sectorOfFinding;
    private Integer numID;
    private String about;
    private boolean ok;
    private int bestCellID;

    private int best1;
    private int best2;

    private boolean ok1;
    private boolean ok2;

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
        if (m==1){
            this.next=this;
            this.previous=this;
        }
        else
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
        //leftBorderAzimuth=((this.azimuth+previous.azimuth+sectorOfFinding)%360)/2;
        //rightBorderAzimuth=((this.azimuth+next.azimuth-sectorOfFinding)%360)/2;
        leftBorderAzimuth=((this.azimuth-sectorOfFinding/2)%360);
        if (leftBorderAzimuth<0){
            leftBorderAzimuth=360+leftBorderAzimuth;
        }
        rightBorderAzimuth=((this.azimuth+sectorOfFinding/2)%360);
    }

    public void setPointsInCell(List<Point>source, List<Point>target, int leftBorderAzimuth,int rightBorderAzimuth){
        double commonDist=0.0;
        int count=0;
        for (Point p : source){
            double dist = HelperCell.toDist(this.lalitude,this.longitude,p.getLatitude(),p.getLongitude());
            double az = toAzimuth(dist, p);
            if (dist<=maxDistance && dist>=minDistance){
                if (rightBorderAzimuth>leftBorderAzimuth) {
                    if (az > leftBorderAzimuth && az < rightBorderAzimuth) {
                        checkNotNullAndAdd(p, target);
                    }
                }
                else
                    if ((az>=0 && az<rightBorderAzimuth) || (az<360 && az>leftBorderAzimuth)){
                        checkNotNullAndAdd(p, target);
                    }
            }
        }
    }


    public void checkNotNullAndAdd(Point p, List<Point>target){
        if (this.system.equals("GSM")){
            String key = "GSM "+this.band;
            if (p.getMainMap().get(key)!=null){
                target.add(p);
            }
        }
        else
        if (this.system.equals("UMTS")){
            String key = this.system+" "+this.channel;
            if (p.getMainMap().get(key)!=null){
                target.add(p);
            }
        }
        else
        if (this.system.equals("LTE")){
            String key = this.system+" "+this.channel;
            if (p.getMainMap().get(key)!=null){
                target.add(p);
            }
        }

    }

    public double toAzimuth(Double dist, Point point){
        double x= HelperCell.toDist(this.lalitude, this.longitude, this.lalitude, point.getLongitude());
        double h= dist;
        double azimuth=Math.asin(x/h)*180/HelperCell.pi;
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



    public Cell(CellInfo cellInfo, int distance) {
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
        this.maxDistance=distance;
        this.minDistance=10;
        this.sectorOfFinding=90;
        if (system.equals("GSM")){
            numID=HelperCell.numIds.get(system+" "+band);
            about=system+" "+band;
        }
        else
        if (system.equals("UMTS")){
            numID=HelperCell.numIds.get(system+" "+channel);
            about=system+" "+channel;
        }
        else
        if (system.equals("LTE")){
            numID=HelperCell.numIds.get(system+" "+channel);
            about=system+" "+channel;
        }
    }

    public int getBestCellID() {
        return bestCellID;
    }

    public void setBestCellID(int bestCellID) {
        this.bestCellID = bestCellID;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    public int getSectorOfFinding() {
        return sectorOfFinding;
    }

    public void setSectorOfFinding(int sectorOfFinding) {
        this.sectorOfFinding = sectorOfFinding;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Integer getNumID() {
        return numID;
    }

    public void setNumID(Integer numID) {
        this.numID = numID;
    }

    public double getDistance() {
        return maxDistance;
    }

    public void setDistance(double distance) {
        this.maxDistance = distance;
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

    public int getBest1() {
        return best1;
    }

    public void setBest1(int best1) {
        this.best1 = best1;
    }

    public int getBest2() {
        return best2;
    }

    public void setBest2(int best2) {
        this.best2 = best2;
    }

    public boolean isOk1() {
        return ok1;
    }

    public void setOk1(boolean ok1) {
        this.ok1 = ok1;
    }

    public boolean isOk2() {
        return ok2;
    }

    public void setOk2(boolean ok2) {
        this.ok2 = ok2;
    }
}
