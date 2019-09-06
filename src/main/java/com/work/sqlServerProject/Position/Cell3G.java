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
    private Map<Integer, Double> allRSCPWeight;


    public Cell3G(CellInfo cellInfo, int distance) {
        super(cellInfo, distance);
        this.Scr=  cellInfo.getScr();
        super.setParam(Scr+"");
        this.carrierFrequency=cellInfo.getCh();
    }

    public void putAllRSCPinband(){
        allRSCP = new HashMap<>();
        allRSCPWeight = new HashMap<>();
        for (Cell c : super.getCellsInBand()){
            Cell3G p=(Cell3G)c;
            String[] temp = this.findAverRSCPPerSCr(p.getScr()+"").split(" ");
            double tempRSCP = Double.parseDouble(temp[0]);
            double tempRSCPWeight = Double.parseDouble(temp[1]);
            allRSCP.put(c.getCi(), tempRSCP);
            allRSCPWeight.put(c.getCi(), tempRSCPWeight);
        }
    }

    public String findBestCI(Map<Integer, Double> map){
        int bestCI=0;
        double maxRSCP=-200;
        double temp=0;
        for (Integer i : map.keySet()){
            temp= map.get(i);
            if (temp==0)
                continue;
            if (temp>maxRSCP){
                maxRSCP=temp;
                bestCI=i;
            }
        }
        return bestCI+" "+(bestCI==super.getCi()? "true":"false");
    }

    public void checkCell(){
        String[] checkWithAverRSCP = findBestCI(allRSCP).split(" ");
        String[] checkWithWeight = findBestCI(allRSCPWeight).split(" ");
        int best1=Integer.parseInt(checkWithAverRSCP[0]);
        super.setBest1(best1);
        int best2=Integer.parseInt(checkWithWeight[0]);
        super.setBest2(best2);
        boolean ok1 = Boolean.parseBoolean(checkWithAverRSCP[1]);
        boolean ok2= Boolean.parseBoolean(checkWithWeight[1]);
        if (super.getCi()==16791 || super.getCi()==19789){
            System.out.println(super.getCi()+" aver ="+best1+" "+ok1);
            System.out.println(super.getCi()+" averWeight ="+best2+" "+ok2);
        }
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
            else super.setBestCellID(best2);
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

    public String findAverRSCPPerSCr(String scr){
        Map<String, Double> map=null;
        Double tempRSCP=null;
        Double common=0.0;
        int count=0;
        for (Point p : super.getPointsInCell()){
            map=p.getMainMap().get("UMTS "+carrierFrequency);
            tempRSCP = map.get(scr);
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

        return "system: "+super.getSystem()+" "+super.getBand()+" "+super.getChannel()+
                " selfCI: "+super.getCi()+
                " bestScanCI: "+super.getBestCellID()+
                " азимут: "+super.getAzimuth()+r;

    }

    public Map<Integer, Double> getAllRSCPWeight() {
        return allRSCPWeight;
    }

    public void setAllRSCPWeight(Map<Integer, Double> allRSCPWeight) {
        this.allRSCPWeight = allRSCPWeight;
    }


    public Map<Integer, Double> getAllRSCP() {
        return allRSCP;
    }

    public void setAllRSCP(Map<Integer, Double> allRSCP) {
        this.allRSCP = allRSCP;
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
