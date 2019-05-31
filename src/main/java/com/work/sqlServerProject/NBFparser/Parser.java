package com.work.sqlServerProject.NBFparser;

import com.work.sqlServerProject.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.shcherbakov on 30.05.2019.
 */
public class Parser {
   public static List<Point> getPointsFromScan(List<String> pointsInString){
       String separator = System.lineSeparator();
       List<Point> points = new ArrayList<>();
       for(String s : pointsInString){
           String [] k = s.split(s);
           for (String m : k) {
               if (m.startsWith("GPS")) {
                   Point point = new Point();
                   points.add(point);
                   point.setGPS(getCoorginates(s));
               }
               if (m.startsWith("FREQSCAN")){

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


}
