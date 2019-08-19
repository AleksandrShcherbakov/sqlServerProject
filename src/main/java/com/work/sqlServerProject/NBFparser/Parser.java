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
                   String coord = getCoorginates(m);
                   if (!coord.equals(" ")) {
                       points.add(point);
                       point.setGPS(coord);
                   }
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
                   else
                   if (m.contains("50008")) {
                       point.setUMTS(getScrRscp900(m));
                   }
               }
               if (m.startsWith("OFDMSCAN") && (m.contains("1301,1,") || m.contains("3300,1,") || m.contains("6413,1,"))) {
                   point.setLTE(createElementsStr(m));
               }
           }
       }
       return points;
   }

    public static List<String> createElementsStr(String s){
        List<String> res = new ArrayList<>();
        String m =null;
        String band = null;
        if (s.contains(",5,3300,1,")){
            try{
                m = s.split("70007,")[1];
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Ошибка при парсинге 3300:\n"+s);
            }
            band="3300";
        }
        else
        if (s.contains(",5,6413,1,")){
            try {
                m = s.split("70020,")[1];
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Ошибка при парсинге 6413:\n"+s);
            }
            band="6413";
        }
        else
        if (s.contains(",5,1301,1,")){
            try {
                m = s.split("70003,")[1];
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Ошибка при парсинге 1301:\n"+s);
            }
            band = "1301";
        }
        else return null;
        String[]u=null;
        try {
            u = m.split(",");
        }
        catch (NullPointerException e){
            //System.out.println(m);
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(band+",");
        int count=1;
        for (String n : u){
            if (n.length()>=4 && !n.startsWith("-") && !n.contains(".")){
                res.add(stringBuilder.toString());
                stringBuilder=new StringBuilder();
                count=0;
                continue;
            }
            count++;
            if (count>7)
                continue;
            stringBuilder.append(n+",");
        }
        return res;
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
        else
        if (s.contains("10813")) {
            channels[0] = "10813 " + temp[2] + "," + temp[3] + "," + temp[4];
        }
        else
        if (s.contains("10836")) {
            channels[0] = "10836 " + temp[2] + "," + temp[3] + "," + temp[4];
        }
        else return null;
        return channels;
    }

    public static String[] getScrRscp900(String s){
        String channelString = s.split("50008,")[1];
        if (channelString.split(",").length <= 2) {
            return null;
        }
        String[] channels = channelString.split(",,,,");
        String[] temp = channels[0].split(",");
        if (s.contains("3036")) {
            try {
                channels[0] = "3036 " + temp[2] + "," + temp[3] + "," + temp[4];
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("ошибка нет уровня"+s);
            }

        }
        else
        if (s.contains("3012")) {
            try {
                channels[0] = "3012 " + temp[2] + "," + temp[3] + "," + temp[4];
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("ошибка нет уровня"+s);
            }
        }
        else return null;
        return channels;
    }




}
