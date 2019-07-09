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
        if (about.equals("GSM 900")){
            double tempRxLev=-200;
            for (String s : paramColor.keySet()){
                if (point.getRxLevel900().get(s)!=null) {
                    if (point.getRxLevel900().get(s) >= tempRxLev) {
                        tempRxLev = point.getRxLevel900().get(s);
                        this.param = s;
                        this.color = paramColor.get(s);
                    }
                }
            }
        }
        else
        if (about.equals("GSM 1800")){
            double tempRxLev=-200;
            for (String s : paramColor.keySet()){
                if (point.getRxLevel1800().get(s)!=null) {
                    if (point.getRxLevel1800().get(s) >= tempRxLev) {
                        tempRxLev = point.getRxLevel1800().get(s);
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
