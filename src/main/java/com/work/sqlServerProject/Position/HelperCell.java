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
