package com.work.sqlServerProject.NBFparser;

import com.work.sqlServerProject.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.shcherbakov on 30.05.2019.
 */
public class Parser {
    static Point point;
    public static List<Point> getPointsFromScan(List<String> pointsInString){

       String separator = System.lineSeparator();
       List<Point> points = new ArrayList<>();
       for(String s : pointsInString){
           String [] k = s.split(separator);
           for (String m : k) {
               if (m.startsWith("GPS")) {
                   point = new Point();
                   points.add(point);
                   point.setGPS(getCoorginates(m));
               }
               if (m.startsWith("FREQSCAN") && m.contains("10900")){
                   point.setGSM900(getChBsicRxL(m));
                   System.out.println(point.getLongitude());
                   System.out.println(point.getLatitude());
                   System.out.println(point.getRxLevel900());
                   System.out.println("");
               }
           }

       }
       return null;
   }

   public static String getCoorginates(String s){
       String [] compons = s.split(",");
       String res=compons[4]+" "+compons[3];
       return res;
   }

   public static String[] getChBsicRxL(String s){
       String channelString = s.split("10900,")[1];
       String [] channels = channelString.split(",,,");
       String[]temp = channels[0].split(",");
       channels[0]=temp[2]+","+temp[3]+","+temp[4];
       //channels[channels.length-1]=channels[channels.length-1].substring(0,channels[channels.length-1].length()-2);
       return channels;

   }


}
