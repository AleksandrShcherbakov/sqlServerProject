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
               if (m.startsWith("PILOTSCAN")) {
                   if (m.contains("50001")) {
                       point.setUMTS(getScrRscp2100(m));
                   }
                   if (m.contains("50008")) {
                       point.setUMTS(getScrRscp900(m));
                   }
               }
               if (m.startsWith("OFDMSCAN")) {
                   if (m.contains("1351,1,")) {
                       point.setLTE(createElementsStr(m));
                   }
                   if (m.contains("3300,1,")) {
                       point.setLTE(createElementsStr(m));
                   }
                   if (m.contains("6413,1,")) {
                       point.setLTE(createElementsStr(m));
                   }
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


   public static String[] createElementsStr(String s){

       String band="";
       try {
           if (s.contains("3300")) {
               s = s.split("70007,")[1];
               band = "3300,";
           }
           if (s.contains("6413")) {
               s = s.split("70020,")[1];
               band = "6413,";
           }
           if (s.contains("1351")) {
               s = s.split("70003,")[1];
               band = "1351,";
           }
       }
       catch (ArrayIndexOutOfBoundsException e){
           return null;
       }
       String[] temp=s.split(",");
       StringBuilder stringBuilder = new StringBuilder(band+",");
       for (String k : temp){
           if (k.length()>=5 && !k.startsWith("-")){
               stringBuilder.append(" ");
               continue;
           }
           stringBuilder.append(k);
           stringBuilder.append(",");
       }
       String[] channels = stringBuilder.toString().split(" ");
       return channels;
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

    public static String[] getScrRscp2100(String s) {
        String channelString = s.split("50001,")[1];
        if (channelString.split(",").length <= 2) {
            return null;
        }
        String[] channels = channelString.split(",,,,");
        String[] temp = channels[0].split(",");
        if (s.contains("10788")) {
            channels[0] = "10788 " + temp[2] + "," + temp[3] + "," + temp[4];
        }
        if (s.contains("10813")) {
            channels[0] = "10813 " + temp[2] + "," + temp[3] + "," + temp[4];
        }
        if (s.contains("10836")) {
            channels[0] = "10836 " + temp[2] + "," + temp[3] + "," + temp[4];
        }
        return channels;
    }

    public static String[] getScrRscp900(String s) {
        String channelString = s.split("50008,")[1];
        if (channelString.split(",").length <= 2) {
            return null;
        }
        String[] channels = channelString.split(",,,,");
        String[] temp = channels[0].split(",");
        if (s.contains("3036")) {
            channels[0] = "3036 " + temp[2] + "," + temp[3] + "," + temp[4];
        }
        if (s.contains("3012")) {
            channels[0] = "3012 " + temp[2] + "," + temp[3] + "," + temp[4];
        }
        return channels;
    }




}
