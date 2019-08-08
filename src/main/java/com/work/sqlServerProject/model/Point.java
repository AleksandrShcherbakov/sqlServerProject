package com.work.sqlServerProject.model;

import java.util.*;

/**
 * Created by a.shcherbakov on 30.05.2019.
 */
public class Point {
    private String input;
    private double longitude;
    private double latitude;
    Map<String, Double> RxLevel900;
    Map<String, Double> RxLevel1800;
    Map<Integer, Double> RSCP10813;
    Map<Integer, Double> RSCP10788;
    Map<Integer, Double> RSCP10836;
    Map<Integer, Double> RSCP3036;
    Map<Integer, Double> RSCP3012;
    Map<Integer, Double> RSRP3300;
    Map<Integer, Double> RSRP6413;
    Map<Integer, Double> RSRP1301;
    Map<Integer, Double> distToPos;

    public Point() {
        this.distToPos=new HashMap<>();
    }
    public void setGPS(String gpsinstring){
        String[] coord = gpsinstring.split(" ");
        double lon = Double.parseDouble(coord[1]);
        double lat = Double.parseDouble(coord[0]);
        this.setLongitude(lon);
        this.setLatitude(lat);
    }

    public void setLTE(List<String>PciRsrp){
        Map<Integer, Double> map=null;
        if (PciRsrp!=null && PciRsrp.size()>0){
            if (PciRsrp.get(0).startsWith("3300")){
                RSRP3300=new HashMap<>();
                map=RSRP3300;
            }
            if (PciRsrp.get(0).startsWith("6413")){
                RSRP6413=new HashMap<>();
                map=RSRP6413;
            }
            if (PciRsrp.get(0).startsWith("1301")){
                RSRP1301=new HashMap<>();
                map=RSRP1301;
            }
            for (String s : PciRsrp){
                try {
                    String[] temp = s.split(",");
                    Integer pci = Integer.parseInt(temp[3]);
                    Double rsrp = Double.parseDouble(temp[6]);
                    map.put(pci, rsrp);
                }
                catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("ошибка нет уровня в "+s);
                }
            }
        }
    }

    public void setGSM900(String[] chBsicRxL){
        RxLevel900=new HashMap<>();
        for (String s : chBsicRxL){
            String [] temp = s.split(",");
            if (temp[1].equals("")){
                continue;
            }
            Double rxLev = Double.parseDouble(temp[2]);
            String ChBsic = temp[0]+" "+temp[1];
            RxLevel900.put(ChBsic,rxLev);
        }
    }

    public void setGSM1800(String[] chBsicRxL){
        RxLevel1800=new HashMap<>();
        for (String s : chBsicRxL){
            String [] temp = s.split(",");
            if (temp[1].equals("")){
                continue;
            }
            Double rxLev = Double.parseDouble(temp[2]);
            String ChBsic = temp[0]+" "+temp[1];
            RxLevel1800.put(ChBsic,rxLev);
        }
    }

    public void setUMTS(String[] scrEcNoRscp){
        if (scrEcNoRscp!=null) {
            String freq = scrEcNoRscp[0].split(" ")[0];
            try {
                scrEcNoRscp[0] = scrEcNoRscp[0].split(" ")[1];
            }
            catch (ArrayIndexOutOfBoundsException e){
                for (String k : scrEcNoRscp){
                    System.out.println(k);
                }

            }
            if (freq.equals("10813")) {
                RSCP10813 = new HashMap<>();
                for (String s : scrEcNoRscp) {
                    String[] temp = s.split(",");
                    Integer scr = Integer.parseInt(temp[0]);
                    Double rscp = Double.parseDouble(temp[2]);
                    RSCP10813.put(scr, rscp);
                }
            }
            if (freq.equals("10788")) {
                RSCP10788 = new HashMap<>();
                for (String s : scrEcNoRscp) {
                    String[] temp = s.split(",");
                    Integer scr = Integer.parseInt(temp[0]);
                    Double rscp = Double.parseDouble(temp[2]);
                    RSCP10788.put(scr, rscp);
                }
            }
            if (freq.equals("10836")) {
                RSCP10836 = new HashMap<>();
                for (String s : scrEcNoRscp) {
                    String[] temp = s.split(",");
                    Integer scr = Integer.parseInt(temp[0]);
                    Double rscp = Double.parseDouble(temp[2]);
                    RSCP10836.put(scr, rscp);
                }
            }
            if (freq.equals("3036")) {
                RSCP3036 = new HashMap<>();
                for (String s : scrEcNoRscp) {
                    String[] temp = s.split(",");
                    Integer scr = Integer.parseInt(temp[0]);
                    Double rscp = Double.parseDouble(temp[2]);
                    RSCP3036.put(scr, rscp);
                }
            }
            if (freq.equals("3012")) {
                RSCP3012 = new HashMap<>();
                for (String s : scrEcNoRscp) {
                    String[] temp = s.split(",");
                    Integer scr = Integer.parseInt(temp[0]);
                    Double rscp = Double.parseDouble(temp[2]);
                    RSCP3012.put(scr, rscp);
                }
            }
        }
        distToPos=new HashMap<>();
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

    public Map<String, Double> getRxLevel900() {
        return RxLevel900;
    }

    public void setRxLevel900(Map<String, Double> rxLevel900) {
        RxLevel900 = rxLevel900;
    }

    public Map<String, Double> getRxLevel1800() {
        return RxLevel1800;
    }

    public void setRxLevel1800(Map<String, Double> rxLevel1800) {
        RxLevel1800 = rxLevel1800;
    }

    public Map<Integer, Double> getRSCP10813() {
        return RSCP10813;
    }

    public void setRSCP10813(Map<Integer, Double> RSCP10813) {
        this.RSCP10813 = RSCP10813;
    }

    public Map<Integer, Double> getRSCP10788() {
        return RSCP10788;
    }

    public void setRSCP10788(Map<Integer, Double> RSCP10788) {
        this.RSCP10788 = RSCP10788;
    }

    public Map<Integer, Double> getRSCP10836() {
        return RSCP10836;
    }

    public void setRSCP10836(Map<Integer, Double> RSCP10836) {
        this.RSCP10836 = RSCP10836;
    }

    public Map<Integer, Double> getRSCP3036() {
        return RSCP3036;
    }

    public void setRSCP3036(Map<Integer, Double> RSCP3036) {
        this.RSCP3036 = RSCP3036;
    }

    public Map<Integer, Double> getRSCP3012() {
        return RSCP3012;
    }

    public void setRSCP3012(Map<Integer, Double> RSCP3012) {
        this.RSCP3012 = RSCP3012;
    }

    public Map<Integer, Double> getRSRP3300() {
        return RSRP3300;
    }

    public void setRSRP3300(Map<Integer, Double> RSRP3300) {
        this.RSRP3300 = RSRP3300;
    }

    public Map<Integer, Double> getRSRP6413() {
        return RSRP6413;
    }

    public void setRSRP6413(Map<Integer, Double> RSRP6413) {
        this.RSRP6413 = RSRP6413;
    }

    public Map<Integer, Double> getRSRP1301() {
        return RSRP1301;
    }

    public void setRSRP1301(Map<Integer, Double> RSRP1301) {
        this.RSRP1301 = RSRP1301;
    }

    @Override
    public String toString() {
        return longitude+" "+latitude+"<br>"+RxLevel900+"<br>"+RxLevel1800+"<br>"+RSCP10813+
                "<br>"+RSCP10788+"<br>"+RSCP10836+"<br>"+
                RSCP3036+"<br>"+RSCP3012+"<br>"+
                RSRP3300+"<br>"+
                RSRP6413+"<br>"+
                RSRP1301;
    }
}
