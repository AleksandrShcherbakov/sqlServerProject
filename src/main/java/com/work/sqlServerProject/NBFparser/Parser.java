package com.work.sqlServerProject.NBFparser;

import com.work.sqlServerProject.controller.CheckingController;
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
                if (m.startsWith("FREQSCAN")){
                    String bandGSM=null;
                    if (m.contains("10900")){
                        bandGSM="GSM 900";
                        point.setGSM(getChBsicRxL(m), bandGSM);
                    }
                    else
                    if (m.contains("11800")){
                        bandGSM="GSM 1800";
                        point.setGSM(getChBsicRxL(m), bandGSM);
                    }

                }

                if (m.startsWith("PILOTSCAN")) {
                    point.setUMTS(getScrRscp(m));
                }
                if (m.startsWith("OFDMSCAN")){
                    for (String chBamd : CheckingController.LTE){
                        if (m.contains(chBamd.split("_")[0]+",1,")){
                            point.setLTE(createElementsStr(m, chBamd));
                        }
                    }
                }
            }
        }
        return points;
    }

    public static List<String> createElementsStr(String s,String chband){
        List<String> res = new ArrayList<>();
        String m =null;
        String band = chband.split("_")[0];
        String bandStr= (70000+Integer.parseInt(chband.split("_")[1]))+"";
        if (s.contains(",5,"+band+",1,")){
            try{
                m = s.split(bandStr+",")[1];
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Ошибка при парсинге "+band+":\n"+s);
            }
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

    public static String[] getChBsicRxL(String s){
        String channelString=null;
        if (s.contains("10900,")) {
            channelString = s.split("10900,")[1];
        }
        else
        if (s.contains("11800")){
            channelString = s.split("11800,")[1];
        }
        String[] channeisElements= channelString.split(",");
        List<String> bcchs=new ArrayList<>();
        try {
            for (int i = 2; i < channeisElements.length; i += 5) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(channeisElements[i] + "," + channeisElements[i + 1] + "," + channeisElements[i + 2]);
                bcchs.add(stringBuilder.toString());
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        String[]channels= new String[bcchs.size()];
        bcchs.toArray(channels);

        //String [] channels = channelString.split(",,,");
        //String[]temp = channels[0].split(",");
        //channels[0]=temp[2]+","+temp[3]+","+temp[4];
        return channels;

    }

    public static String[] getScrRscp(String s) {
        String channelString =null;
        if (s.contains("50001,")) {
            channelString = s.split("50001,")[1];
        }
        else
        if (s.contains("50008,")){
            channelString=s.split("50008,")[1];
        }
        if (channelString.split(",").length <= 2) {
            return null;
        }
        String[] channels = channelString.split(",,,,");
        String[] temp = channels[0].split(",");
        String ch=null;
        for (String freq : CheckingController.UMTS){
            if (s.contains(freq+",1,")) {
                ch=freq;
            }
        }
        try{
            channels[0] = ch+" "+ temp[2] + "," + temp[3] + "," + temp[4];
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("ошибка нет уровня"+s);
        }
        return channels;
    }
}
