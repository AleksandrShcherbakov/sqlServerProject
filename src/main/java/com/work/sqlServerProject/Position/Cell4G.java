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
    private Map<Integer, Double> allRSRPWeight;

    public Cell4G(CellInfo cellInfo, int distance) {
        super(cellInfo, distance);
        this.PCI= cellInfo.getPci();
        this.ch=cellInfo.getCh();
    }

    public void putAllRSRPinband(){
        allRSRP = new HashMap<>();
        allRSRPWeight = new HashMap<>();
        for (Cell c : super.getCellsInBand()){
            Cell4G p=(Cell4G)c;
            String[] temp=null;
            try {
                temp=this.findAverRSRPerPCI(Integer.parseInt(p.getPCI()+"")).split(" ");
            }
            catch (NumberFormatException e){
                temp=this.findAverRSRPerPCI(0).split(" ");
            }
            double tempRSRP = Double.parseDouble(temp[0]);
            double tempRSRPWEight = Double.parseDouble(temp[1]);
            allRSRP.put(c.getCi(), tempRSRP);
            allRSRPWeight.put(c.getCi(), tempRSRPWEight);
        }
    }

    public String findBestCI(Map<Integer, Double> map){
        int bestCI=0;
        double maxRSRP=-200;
        double temp=0;
        for (Integer i : map.keySet()){
            temp= map.get(i);
            if (temp==0)
                continue;
            if (temp>maxRSRP){
                maxRSRP=temp;
                bestCI=i;
            }
        }
        System.out.println(super.getCi()+" "+bestCI+" "+super.getAzimuth());
        return bestCI+" "+(bestCI==super.getCi()? "true":"false");
    }

    public void checkCell(){
        String[] checkWithAverRxLev = findBestCI(allRSRP).split(" ");
        String[] checkWithWeight = findBestCI(allRSRPWeight).split(" ");
        int best1=Integer.parseInt(checkWithAverRxLev[0]);
        super.setBest1(best1);
        int best2=Integer.parseInt(checkWithWeight[0]);
        super.setBest2(best2);
        boolean ok1 = Boolean.parseBoolean(checkWithAverRxLev[1]);
        boolean ok2= Boolean.parseBoolean(checkWithWeight[1]);

        if (best1==best2 && ok1==ok2){
            super.setBestCellID(best1);
            super.setOk(ok1);
        }
        else
        if (ok1 || ok2){
           super.setOk(true);
            if (ok1) {
                super.setBestCellID(best1);
            }
            else
                super.setBestCellID(best2);
        }
        else
        if (best1!=0 && best2!=0){
            super.setBestCellID(best2);
        }
        else
        if (best1==0 && best2!=0){
            super.setBestCellID(best2);
            super.setOk(ok2);
        }
        else super.setOk(false);
    }

    public String findAverRSRPerPCI(Integer pci){
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

        if (count==0){
            return 0+" "+0;
        }


        if (count<10){
            return 0+" "+(common/count)/count;
        }
        return common/count+" "+(common/count)/count;
    }

    @Override
    public String toString() {
        String r =null;
        if (super.getBestCellID()==0){
            r=" измерений в зоне этого сектора нет";
        }
        else
            r=" ok: "+super.isOk();

        return "system: "+super.getSystem()+" "+super.getBand()+
                " selfCI: "+super.getCi()+
                " bestScanCI: "+super.getBestCellID()+
                " азимут: "+super.getAzimuth()+r;

    }

    public Map<Integer, Double> getAllRSRPWeight() {
        return allRSRPWeight;
    }

    public void setAllRSRPWeight(Map<Integer, Double> allRSRPWeight) {
        this.allRSRPWeight = allRSRPWeight;
    }


    public Map<Integer, Double> getAllRSRP() {
        return allRSRP;
    }

    public void setAllRSRP(Map<Integer, Double> allRSRP) {
        this.allRSRP = allRSRP;
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
