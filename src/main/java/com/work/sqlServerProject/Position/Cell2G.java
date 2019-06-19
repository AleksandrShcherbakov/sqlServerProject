package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Cell2G extends Cell {
    private int BCCH;
    private Object BSIC;
    private Map<String,Double> rxLevperBCCH;
    private String bestScanBCCH;
    private int bestCellID;
    private boolean rightAzimuth;
    private String bcchBsic;
    private List<String>BCCHBsicinband;
    private int countOfPoints;

    public Cell2G(CellInfo cellInfo) {
        super(cellInfo);
        this.BCCH=cellInfo.getCh();
        this.BSIC=cellInfo.getBsic();
        this.bcchBsic=BCCH+" "+BSIC;
    }

    public void putBCCHinband(){
        BCCHBsicinband=new ArrayList<>();
        for (Cell c : super.getCellsInBand()){
            Cell2G p=(Cell2G)c;
            BCCHBsicinband.add(p.getBcchBsic());
        }
    }


    public double findAverRxLevPerBCCHBSIC(String bcchBsic){
        Map<String, Double> map;
        Double tempRxLev=null;
        Double common=0.0;
        int count=0;
        if (super.getBand()==900){
            for (Point p : super.getPointsInCell()){
                map=p.getRxLevel900();
                tempRxLev=map.get(bcchBsic);
                if (tempRxLev!=null){
                    common=common+tempRxLev;
                    count++;
                }
            }
        }
        else
        if (super.getBand()==1800){
            for (Point p : super.getPointsInCell()){
                map=p.getRxLevel1800();
                tempRxLev=map.get(bcchBsic);
                if (tempRxLev!=null){
                    common=common+tempRxLev;
                    count++;
                }
            }
        }
        countOfPoints=count;
        return common/count;
    }

    public int getCountOfPoints() {
        return countOfPoints;
    }

    public void setCountOfPoints(int countOfPoints) {
        this.countOfPoints = countOfPoints;
    }

    public List<String> getBCCHBsicinband() {
        return BCCHBsicinband;
    }

    public void setBCCHBsicinband(List<String> BCCHBsicinband) {
        this.BCCHBsicinband = BCCHBsicinband;
    }

    public Map<String, Double> getRxLevperBCCH() {
        return rxLevperBCCH;
    }

    public void setRxLevperBCCH(Map<String, Double> rxLevperBCCH) {
        this.rxLevperBCCH = rxLevperBCCH;
    }

    public String getBcchBsic() {
        return bcchBsic;
    }

    public void setBcchBsic(String bcchBsic) {
        this.bcchBsic = bcchBsic;
    }

    public String getBestScanBCCH() {
        return bestScanBCCH;
    }

    public void setBestScanBCCH(String bestScanBCCH) {
        this.bestScanBCCH = bestScanBCCH;
    }

    public boolean isRightAzimuth() {
        return rightAzimuth;
    }

    public void setRightAzimuth(boolean rightAzimuth) {
        this.rightAzimuth = rightAzimuth;
    }

    public int getBCCH() {
        return BCCH;
    }

    public void setBCCH(int BCCH) {
        this.BCCH = BCCH;
    }

    public Object getBSIC() {
        return BSIC;
    }

    public void setBSIC(Object BSIC) {
        this.BSIC = BSIC;
    }
}
