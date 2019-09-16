package com.work.sqlServerProject.model;

import com.work.sqlServerProject.controller.CheckingController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by a.shcherbakov on 30.05.2019.
 */
public class Point {
    private String input;
    private double longitude;
    private double latitude;
    Map<Integer, Double> distToPos;
    Map<String, Map<String,Double>> mainMap;

    public Point() {
        System.out.println(CheckingController.LTE);
        System.out.println(CheckingController.UMTS);
        this.distToPos=new HashMap<>();
        this.mainMap = new HashMap<>();
        mainMap.put("GSM 900", null);
        mainMap.put("GSM 1800", null);


        for (String umts:CheckingController.UMTS){
            String key="UMTS "+umts;
            mainMap.put(key,null);
        }
        for (String lte:CheckingController.LTE){
            String key="LTE "+lte.split("_")[0];
            mainMap.put(key, null);
        }
    }
    public void setGPS(String gpsinstring){
        String[] coord = gpsinstring.split(" ");
        double lon = Double.parseDouble(coord[1]);
        double lat = Double.parseDouble(coord[0]);
        this.setLongitude(lon);
        this.setLatitude(lat);
    }

    public void setGSM(String[] chBsicRxL, String band){
        Map<String,Double>tempmap=null;
        if (mainMap.get(band)!=null){
            tempmap=mainMap.get(band);
        }
        else {
            mainMap.put(band,new HashMap<>());
            tempmap=mainMap.get(band);
        }
        for (String s : chBsicRxL) {
            String[] temp = s.split(",");
            if (temp[1].equals("")) {
                continue;
            }
            Double rxLev = Double.parseDouble(temp[2]);
            String ChBsic = temp[0] + " " + temp[1];
            tempmap.put(ChBsic, rxLev);
        }

    }

    public void setUMTS(String[] scrEcNoRscp) {
        if (scrEcNoRscp != null) {
            String freq = scrEcNoRscp[0].split(" ")[0];
            try {
                scrEcNoRscp[0] = scrEcNoRscp[0].split(" ")[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                for (String k : scrEcNoRscp) {
                    System.out.println(k);
                }

            }
            Map<String, Double> map = null;
            if (mainMap.get("UMTS " + freq)!=null){
                map=mainMap.get("UMTS " + freq);
            }
            else {
                mainMap.put("UMTS " + freq,new HashMap<>());
                map=mainMap.get("UMTS " + freq);
            }
            for (String s : scrEcNoRscp) {
                String[] temp = s.split(",");
                String scr = temp[0];
                Double rscp = Double.parseDouble(temp[2]);
                map.put(scr, rscp);
            }
        }
    }

    public void setLTE(List<String>PciRsrp){
        Map<String, Double> map=null;
        if (PciRsrp!=null && PciRsrp.size()>0){
            String carrierLTE = PciRsrp.get(0).split(",")[0];
            if (carrierLTE.equals("1351")){
                carrierLTE="1301";
            }
            if (mainMap.get("LTE " + carrierLTE)!=null){
                map=mainMap.get("LTE " + carrierLTE);
            }
            else {
                mainMap.put("LTE " + carrierLTE,new HashMap<>());
                map=mainMap.get("LTE " + carrierLTE);
            }
        }
        if (map!=null) {
            for (String s : PciRsrp) {
                try {
                    String[] temp = s.split(",");
                    String pci = temp[3];
                    Double rsrp = Double.parseDouble(temp[6]);
                    map.put(pci, rsrp);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("ошибка нет уровня в " + s);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : mainMap.keySet()){
            stringBuilder.append(mainMap.get(key)+"<br>");
        }
        return longitude+" "+latitude+"<br>"+stringBuilder.toString();
    }


    public Map<String, Map<String, Double>> getMainMap() {
        return mainMap;
    }

    public void setMainMap(Map<String, Map<String, Double>> mainMap) {
        this.mainMap = mainMap;
    }

    public Map<Integer, Double> getDistToPos() {
        return distToPos;
    }

    public void setDistToPos(Map<Integer, Double> distToPos) {
        this.distToPos = distToPos;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}
