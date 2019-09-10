package com.work.sqlServerProject.model;

import com.work.sqlServerProject.Position.HelperCell;

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
        Map<String, Double> map=point.getMainMap().get(about);
        double tempLev=-200;
        for (String s : paramColor.keySet()){
            if (map.get(s)!=null) {
                if (map.get(s) >= tempLev) {
                    tempLev = map.get(s);
                    this.param = s;
                    this.color = paramColor.get(s);
                }
            }
        }
    }

    public PointToMap(Point point, String about, String param) {
        this.longitude = point.getLongitude();
        this.latitude = point.getLatitude();
        Map<String, Double> map=point.getMainMap().get(about);
        String system=about.split(" ")[0];
        if (map.get(param)!=null) {
            this.param = map.get(param)+"";
            this.color = HelperCell.getLevelColor(system, map.get(param));
        }
        else {
            this.param=null;
            this.color=null;
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
