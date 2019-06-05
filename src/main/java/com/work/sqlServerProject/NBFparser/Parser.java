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
                   point.setGSM900(getChBsicRxL900(m));
               }
               if (m.startsWith("FREQSCAN") && m.contains("11800")){
                   point.setGSM1800(getChBsicRxL1800(m));
               }
               if (m.startsWith("PILOTSCAN") && m.contains("50001")){
                   point.setUMTS2100(getScrRscp2100(m));
               }
           }
       }
       return points;
   }

   public static String getCoorginates(String s){
       String [] compons = s.split(",");
       String res=compons[4]+" "+compons[3];
       return res;
   }

   public static String[] getChBsicRxL900(String s){
       String channelString = s.split("10900,")[1];
       String [] channels = channelString.split(",,,");
       String[]temp = channels[0].split(",");
       channels[0]=temp[2]+","+temp[3]+","+temp[4];
       return channels;

   }

    public static String[] getChBsicRxL1800(String s){
        String channelString = s.split("11800,")[1];
        String [] channels = channelString.split(",,,");
        String[]temp = channels[0].split(",");
        channels[0]=temp[2]+","+temp[3]+","+temp[4];
        return channels;

    }

    public static String[] getScrRscp2100(String s){
        String channelString = s.split("50001,")[1];
        if (channelString.split(",").length<=2){
            return null;
        }
        String [] channels = channelString.split(",,,,");
        String[]temp = channels[0].split(",");
        if (s.contains("10788")) {
            channels[0] ="10788 "+ temp[2] + "," + temp[3] + "," + temp[4];
        }
        if (s.contains("10813")) {
            channels[0] ="10813 "+ temp[2] + "," + temp[3] + "," + temp[4];
        }
        if (s.contains("10836")) {
            channels[0] ="10836 "+ temp[2] + "," + temp[3] + "," + temp[4];
        }
        return channels;
    }


}
