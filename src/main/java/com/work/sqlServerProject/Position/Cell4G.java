package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Cell4G extends Cell {
    private Object PCI;
    private int ch;
    private int countOfPoints;
    private List<Integer> pciInBand;

    public Cell4G(CellInfo cellInfo) {
        super(cellInfo);
        this.PCI= cellInfo.getPci();
        this.ch=cellInfo.getCh();
    }

    public void putPciinband(){
        pciInBand=new ArrayList<>();
        for (Cell c : super.getCellsInBand()){
            Cell4G p=(Cell4G)c;
            pciInBand.add(Integer.parseInt(p.getPCI()+""));
        }
    }

    public double findAverRSCPPerSCr(Integer pci){
        Map<Integer, Double> map=null;
        Double tempRSCP=null;
        Double common=0.0;
        int count=0;
        for (Point p : super.getPointsInCell()){
            if (super.getBand()==2600) {
                map = p.getRSRP3300();
            }
            else
            if (super.getBand()==800) {
                map = p.getRSRP6413();
            }
            else
            if (super.getBand()==1800) {
                map = p.getRSRP1351();
            }
            tempRSCP = map.get(pci);
            if (tempRSCP!=null){
                common=common+tempRSCP;
                count++;
            }
        }
        countOfPoints = count;
        return common/count;
    }

    public int getCountOfPoints() {
        return countOfPoints;
    }

    public void setCountOfPoints(int countOfPoints) {
        this.countOfPoints = countOfPoints;
    }

    public Object getPCI() {
        return PCI;
    }

    public void setPCI(Object PCI) {
        this.PCI = PCI;
    }

    public int getCh() {
        return ch;
    }

    public void setCh(int ch) {
        this.ch = ch;
    }

    @Override
    public String toString() {
        return super.toString()+"Cell4G{" +
                ", PCI=" + PCI +
                ", ch=" + ch +
                '}';
    }
}
