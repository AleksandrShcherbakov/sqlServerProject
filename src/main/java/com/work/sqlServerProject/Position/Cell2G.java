package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.Point;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Cell2G extends Cell {
    private int BCCH;
    private Object BSIC;
    private Map<String,Double> rxLevperBCCH;
    private int bestCellID;
    private boolean rightAzimuth;
    private String bcchBsic;
    private Map<Integer, Double> allRxLev;
    private int countOfPoints;
    private boolean ok;


    public Cell2G(CellInfo cellInfo) {
        super(cellInfo);
        this.BCCH=cellInfo.getCh();
        this.BSIC=cellInfo.getBsic();
        this.bcchBsic=BCCH+" "+BSIC;
    }



    public void putAllrxLevinband(){
        allRxLev= new HashMap<>();
        for (Cell c : super.getCellsInBand()){
            Cell2G p=(Cell2G)c;
            allRxLev.put(c.getCi(), this.findAverRxLevPerBCCHBSIC(p.getBcchBsic()));
        }
    }

    public int findBestCI(){
        int bestCI=0;
        double maxRxLev=-200;
        double temp=0;
        for (Integer i : allRxLev.keySet()){
            temp=allRxLev.get(i);
            if (temp==0)
                continue;
            if (temp>maxRxLev){
                maxRxLev=temp;
                bestCI=i;
            }
        }
        bestCellID=bestCI;
        if (bestCellID==super.getCi()){
            ok=true;
        }
        return bestCI;
    }


    public double findAverRxLevPerBCCHBSIC(String bcchBsic){
        Map<String, Double> map=null;
        Double tempRxLev=null;
        Double common=0.0;
        int count=0;
        for (Point p : super.getPointsInCell()) {
            if (super.getBand() == 900) {
                map = p.getRxLevel900();
            }
            else
            if (super.getBand() == 1800) {
                map = p.getRxLevel1800();
            }
            tempRxLev = map.get(bcchBsic);
            if (tempRxLev != null) {
                common = common + tempRxLev;
                count++;
            }
        }
        if (count==0){
            return 0.0;
        }
        countOfPoints=count;
        return common/count;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    @Override
    public String toString() {
        String r =null;
        if (countOfPoints==0){
            r=" измерений в зоне этого сектора нет";
        }
        else
            r=" ok: "+ok;

        return "system: "+super.getSystem()+" "+super.getBand()+
                " selfCI: "+super.getCi()+
                " bestScanCI: "+bestCellID+
                " азимут: "+super.getAzimuth()+r;

    }

    public Map<Integer, Double> getAllRxLev() {
        return allRxLev;
    }

    public void setAllRxLev(Map<Integer, Double> allRxLev) {
        this.allRxLev = allRxLev;
    }

    public int getBestCellID() {
        return bestCellID;
    }

    public void setBestCellID(int bestCellID) {
        this.bestCellID = bestCellID;
    }

    public int getCountOfPoints() {
        return countOfPoints;
    }

    public void setCountOfPoints(int countOfPoints) {
        this.countOfPoints = countOfPoints;
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
