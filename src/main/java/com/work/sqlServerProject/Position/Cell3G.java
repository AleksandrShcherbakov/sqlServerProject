package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.Point;

import java.util.ArrayList;
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

    public Cell3G(CellInfo cellInfo) {
        super(cellInfo);
        this.Scr=  cellInfo.getScr();
        this.carrierFrequency=cellInfo.getCh();
    }

    public void putScrinband(){
        scrInband=new ArrayList<>();
        for (Cell c : super.getCellsInBand()){
            Cell3G p=(Cell3G)c;
            scrInband.add(Integer.parseInt(p.getScr()+""));
        }
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
        countOfPoints=count;
        return common/count;
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
