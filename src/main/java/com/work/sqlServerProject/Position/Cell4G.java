package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.Point;

import java.util.HashMap;
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
    private Map<Integer, Double> allRSRP;
    private int bestCellID;
    private boolean ok;

    public Cell4G(CellInfo cellInfo) {
        super(cellInfo);
        this.PCI= cellInfo.getPci();
        this.ch=cellInfo.getCh();
    }

    public void putAllRSRPinband(){
        allRSRP = new HashMap<>();
        for (Cell c : super.getCellsInBand()){
            Cell4G p=(Cell4G)c;
            try {
                allRSRP.put(c.getCi(), this.findAverRSRPerPCI(Integer.parseInt(p.getPCI()+"")));
            }
            catch (NumberFormatException e){
                allRSRP.put(c.getCi(), this.findAverRSRPerPCI(0));
            }
            if (super.getCi()==7702963){
                System.out.println(super.getCi()+" "+c.getCi()+" "+this.findAverRSRPerPCI(Integer.parseInt(p.getPCI()+"")));
            }
        }
    }

    public int findBestCI(){
        int bestCI=0;
        double maxRSRP=-200;
        double temp=0;
        for (Integer i : allRSRP.keySet()){
            temp= allRSRP.get(i);
            if (temp==0)
                continue;
            if (temp>maxRSRP){
                maxRSRP=temp;
                bestCI=i;
            }
        }
        bestCellID=bestCI;
        if (bestCellID==super.getCi()){
            ok=true;
        }
        return bestCI;
    }

    public double findAverRSRPerPCI(Integer pci){
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

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Map<Integer, Double> getAllRSRP() {
        return allRSRP;
    }

    public void setAllRSRP(Map<Integer, Double> allRSRP) {
        this.allRSRP = allRSRP;
    }

    public int getBestCellID() {
        return bestCellID;
    }

    public void setBestCellID(int bestCellID) {
        this.bestCellID = bestCellID;
    }

    public List<Integer> getPciInBand() {
        return pciInBand;
    }

    public void setPciInBand(List<Integer> pciInBand) {
        this.pciInBand = pciInBand;
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
}
