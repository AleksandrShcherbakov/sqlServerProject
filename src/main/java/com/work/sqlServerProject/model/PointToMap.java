package com.work.sqlServerProject.model;

import java.util.Map;

/**
 * Created by a.shcherbakov on 28.06.2019.
 */
public class PointToMap {
    private double longitude;
    private double latitude;
    private String param;
    private String color;

    public PointToMap(Point point, String about, Map<String, String>paramColor) {
        this.longitude = point.getLongitude();
        this.latitude = point.getLatitude();
        if (about.startsWith("GSM")){
            Map<String, Double> map=null;
            if (about.equals("GSM 900")) {
                map = point.getRxLevel900();
            }
            else
            if (about.equals("GSM 1800")) {
                map = point.getRxLevel1800();
            }
            double tempRxLev=-200;
            for (String s : paramColor.keySet()){
                if (map.get(s)!=null) {
                    if (map.get(s) >= tempRxLev) {
                        tempRxLev = map.get(s);
                        this.param = s;
                        this.color = paramColor.get(s);
                    }
                }
            }
        }
        else
        if (about.startsWith("UMTS")){
            Map<Integer, Double> map=null;
            switch (about){
                case "UMTS 2100 10813":
                    map=point.getRSCP10813();
                    break;
                case "UMTS 2100 10788":
                    map=point.getRSCP10788();
                    break;
                case "UMTS 2100 10836":
                    map=point.getRSCP10836();
                    break;
                case "UMTS 900 3036":
                    map=point.getRSCP3036();
                    break;
                case "UMTS 900 3012":
                    map=point.getRSCP3012();
                    break;
            }
            double RSCP=-200;
            for (String s : paramColor.keySet()){
                int m=Integer.parseInt(s);
                if (map.get(m)!=null) {
                    if (map.get(m) >= RSCP) {
                        RSCP = map.get(m);
                        this.param = s;
                        this.color = paramColor.get(s);
                    }
                }
            }
        }
        else
        if (about.startsWith("LTE")){
            Map<Integer, Double> map=null;
            switch (about){
                case "LTE 2600":
                    map=point.getRSRP3300();
                    break;
                case "LTE 1800":
                    map=point.getRSRP1351();
                    break;
                case "UMTS 800":
                    map=point.getRSRP6413();
                    break;
            }
            double RSRP=-200;
            for (String s : paramColor.keySet()){
                Integer m = Integer.parseInt(s);
                if (map.get(m)!=null) {
                    if (map.get(m) >= RSRP) {
                        RSRP = map.get(m);
                        this.param = s;
                        this.color = paramColor.get(s);
                    }
                }
            }
        }
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    @Override
    public String toString() {
        return "PointToMap{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
