package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.controller.CheckingController;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by a.shcherbakov on 05.07.2019.
 */
public class HelperCell {
    @Value("${LTEcarriers}")
    static String[] LTEcarrierBand;
    @Value("${UMTScarriers}")
    static String[] UMTScarriers;
    final static double pi=3.1415926535898;
    final static double EarthRadius= 6372795.0;
    static Map<String,Integer>numIds;

    final static Map<String, String> colerSet2G= new HashMap<>();
    static {
        colerSet2G.put("0_-65","#008000");
        colerSet2G.put("-65_-75","#00FF00");
        colerSet2G.put("-75_-85","#FFFF00");
        colerSet2G.put("-85_-95","#FFA500");
        colerSet2G.put("-95_-104","#FF0000");
    }

    final static Map<String,String> colorSet3G= new HashMap<>();
    static {
        colorSet3G.put("0_-80","#008000");
        colorSet3G.put("-80_-90","#FFFF00");
        colorSet3G.put("-90_-100","#FFA500");
        colorSet3G.put("-100_-200","#FF0000");
    }

    final static Map<String,String> colorSet4G= new HashMap<>();
    static {
        colorSet4G.put("0_-65","#008000");
        colorSet4G.put("-65_-80","#00FF00");
        colorSet4G.put("-80_-90","#FFFF00");
        colorSet4G.put("-90_-105","#FFA500");
        colorSet4G.put("-105_-115", "#FF0000");
        colorSet4G.put("-115_-200", "#800000");
    }

    static {
        numIds=new HashMap<>();
        Integer id=1;
        numIds.put("GSM 900",id);
        id++;
        numIds.put("GSM 1800",id);
        id++;
        for (String s : CheckingController.UMTS){
            numIds.put("UMTS "+s,id);
            id++;
        }
        for (String s : CheckingController.LTE){
            numIds.put("LTE "+s.split("_")[0],id);
            id++;
        }
    }

    public static String getLevelColor(String system, Double level){
        String res = "#808080";
        Map<String,String>map=null;
        if (system.equals("GSM")){
            map=colerSet2G;
        }
        else
        if (system.equals("UMTS")){
            map=colorSet3G;
        }
        else
        if (system.equals("LTE")){
            map=colorSet4G;
        }
        String key="";
        for (String s : map.keySet()){
            System.out.println(s);
        }
        for (String s : map.keySet()){
            String[] borders = s.split("_");
            double leftBorder=Double.parseDouble(borders[0]);
            double rightBorder =Double.parseDouble(borders[1]);
            if (level<leftBorder && level>=rightBorder){
                key=s;
                break;
            }
        }
        return map.get(key);
    }

    public static double toDist(double latA, double lonA, double latB, double lonB){
        double latArad=latA*pi/180;
        double latBrad=latB*pi/180;
        double lonArad=lonA*pi/180;
        double lonBrad=lonB*pi/180;

        double cl1= Math.cos(latArad);
        double cl2= Math.cos(latBrad);
        double sl1=Math.sin(latArad);
        double sl2=Math.sin(latBrad);
        double delta = lonBrad-lonArad;
        double cdelta = Math.cos(delta);
        double sdelta = Math.sin(delta);

        double y = Math.sqrt(Math.pow(cl2 * sdelta, 2) + Math.pow(cl1 * sl2 - sl1 * cl2 * cdelta, 2));
        double x = sl1 * sl2 + cl1 * cl2 * cdelta;

        double ad = Math.atan2(y,x);
        double dist=ad*EarthRadius;
        return dist;
    }


}
