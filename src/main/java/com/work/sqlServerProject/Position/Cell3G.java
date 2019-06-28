package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Cell3G extends Cell {
    private Object Scr;
    private int carrierFrequency;
    private int countOfPoints;
    private List<Integer> scrInband;
    private Map<Integer, Double> allRSCP;
    private int bestCellID;
    private boolean ok;

    public Cell3G(CellInfo cellInfo) {
        super(cellInfo);
        this.Scr=  cellInfo.getScr();
        this.carrierFrequency=cellInfo.getCh();
    }

    public void putAllRSCPinband(){
        allRSCP = new HashMap<>();
        for (Cell c : super.getCellsInBand()){
            Cell3G p=(Cell3G)c;
            allRSCP.put(c.getCi(), this.findAverRSCPPerSCr(Integer.parseInt(p.getScr()+"")));
        }
    }

    public int findBestCI(){
        int bestCI=0;
        double maxRSCP=-200;
        double temp=0;
        for (Integer i : allRSCP.keySet()){
            temp= allRSCP.get(i);
            if (temp==0)
                continue;
            if (temp>maxRSCP){
                maxRSCP=temp;
                bestCI=i;
            }
        }
        bestCellID=bestCI;
        if (bestCellID==super.getCi()){
            ok=true;
        }
        return bestCI;
    }

    public double findAverRSCPPerSCr(Integer scr){
        Map<Integer, Double> map=null;
        Double tempRSCP=null;
        Double common=0.0;
        int count=0;
        for (Point p : super.getPointsInCell()){
            if (super.getBand()==2100){
                if (this.carrierFrequency==10813) {
                    map = p.getRSCP10813();
                }
                else
                if (this.carrierFrequency==10788) {
                    map = p.getRSCP10788();
                }
                else
                if (this.carrierFrequency==10836) {
                    map = p.getRSCP10836();
                }
            }
            else
            if (super.getBand()==900){
                if (this.carrierFrequency==3036) {
                    map = p.getRSCP3036();
                }
                else
                if (this.carrierFrequency==3012) {
                    map = p.getRSCP3012();
                }
            }
            tempRSCP = map.get(scr);
            if (tempRSCP!=null){
                common=common+tempRSCP;
                count++;
            }
        }
        if (count==0 || count<15){
            return 0.0;
        }
        countOfPoints=count;
        return common/count;
    }

    @Override
    public String toString() {
        String r =null;
        if (bestCellID==0){
            r=" измерений в зоне этого сектора нет";
        }
        else
            r=" ok: "+ok;

        return "system: "+super.getSystem()+" "+super.getBand()+
                " selfCI: "+super.getCi()+
                " bestScanCI: "+bestCellID+
                " азимут: "+super.getAzimuth()+r;

    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Map<Integer, Double> getAllRSCP() {
        return allRSCP;
    }

    public void setAllRSCP(Map<Integer, Double> allRSCP) {
        this.allRSCP = allRSCP;
    }

    public int getBestCellID() {
        return bestCellID;
    }

    public void setBestCellID(int bestCellID) {
        this.bestCellID = bestCellID;
    }

    public List<Integer> getScrInband() {
        return scrInband;
    }

    public void setScrInband(List<Integer> scrInband) {
        this.scrInband = scrInband;
    }

    public int getCountOfPoints() {
        return countOfPoints;
    }

    public void setCountOfPoints(int countOfPoints) {
        this.countOfPoints = countOfPoints;
    }

    public Object getScr() {
        return Scr;
    }

    public void setScr(Object scr) {
        Scr = scr;
    }

    public int getCarrierFrequency() {
        return carrierFrequency;
    }

    public void setCarrierFrequency(int carrierFrequency) {
        this.carrierFrequency = carrierFrequency;
    }
}
