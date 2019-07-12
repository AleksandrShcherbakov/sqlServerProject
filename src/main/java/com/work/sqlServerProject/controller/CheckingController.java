package com.work.sqlServerProject.controller;

import com.work.sqlServerProject.Helper.ColorHelper;
import com.work.sqlServerProject.Helper.FileScanHelper;
import com.work.sqlServerProject.NBFparser.Parser;
import com.work.sqlServerProject.NBFparser.ParserHalper;
import com.work.sqlServerProject.Position.*;
import com.work.sqlServerProject.dao.CellNameDAO;
import com.work.sqlServerProject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by a.shcherbakov on 24.06.2019.
 */
@Controller
public class CheckingController {

    @Autowired
    private CellNameDAO cellNameDAO;
    private Map<Integer, Position> positions=null;
    List<Point> points=null;


    @RequestMapping(value = "/inputScan", method = RequestMethod.GET)
    public String showSelectScanFilePage(Model model){
        List<String> nmfs = FileScanHelper.getFiles("");

        PathScanFile pathScanFile = new PathScanFile();
        model.addAttribute("listFiles",nmfs);
        model.addAttribute("pathScanFile", pathScanFile);
        return "checking/checkPathFileScan";
    }

    @RequestMapping(value = "/inputScan", method = RequestMethod.POST)
    public String loadScanAndSelectPos(Model model, @ModelAttribute ("pathScanFile") PathScanFile pathScanFile,
                                       @RequestParam (required = false,name = "file") String files){
        StringBuilder stringBuilder=new StringBuilder();
        if (!pathScanFile.getUrl().equals("")) {
            stringBuilder.append(readFiles(pathScanFile.getUrl()));
        }
        if (files!=null) {
            String[] file = files.split(",");
            for (String s : file) {
                stringBuilder.append(readFiles(s));
            }
        }
        System.out.println(stringBuilder.toString());
        return "redirect:/checkPos";
    }

    @RequestMapping(value = "/checkPos", method = RequestMethod.GET)
    public String showSelectPos(Model model){
        PosForCheck posForCheck = new PosForCheck();
        model.addAttribute("pos", posForCheck);
        return "checking/checkPos";
    }

    public String readFiles(String path){
        List<String> parsered;
        try {
            parsered = ParserHalper.createinSrtings(path);
        }
        catch (IOException e){
            return "Путь к файлу сканера указан не верно.";
        }
        points = Parser.getPointsFromScan(parsered);
        return "файл "+path+" прочитан";
    }



    @RequestMapping(value = "/checkPos", method = RequestMethod.POST)
    @ResponseBody
    public String selectPN(Model model, @ModelAttribute ("pos") PosForCheck posForCheck) throws IOException {
        if (posForCheck.getPosnames().equals("")){
            return "не введена позиция";
        }
        positions=new HashMap<>();
        StringBuilder res=new StringBuilder();
        String[]poss=posForCheck.getPosnames().split("[, ]");
        for (String s : poss){
            s=s.trim();
            try {
                int pos=Integer.parseInt(s);
                int i=setPosition(pos);
                if (i==-1){
                    res.append("Позиции "+pos+" в базе не найдено<br>");
                }
            }
            catch (Exception e){
                res.append("В указании списка позиций ошибка: "+s+"<br>");
            }
        }

        for (Map.Entry p : positions.entrySet()){
            res.append(p.getKey()+"<br>"+
                    p.getValue().toString()+"<br>");
            res.append("=================================================================<br><br>");
        }
        return res.toString();

    }

    public int setPosition(Integer posname){
        Position position=null;
        if (posname!=null) {
            List<CellInfo> list = cellNameDAO.getInfoForBS(posname);
            if (list.size()==0){
                return -1;
            }
            position = new Position(list);
        }
        position.setPointsInPosition(points);
        position.setAllPointsToCells();
        position.findBestScan();
        this.positions.put(posname,position);
        return 1;
    }




    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public String getMap(Model model,
                         @RequestParam(required = false, name = "about") String about,
                         @RequestParam(required = false, name = "posName") String number){
        Integer pos = Integer.parseInt(number);
        System.out.println(about);
        List<Cell>cellList= positions.get(pos).getCells().stream().filter(p->p.getAbout().equals(about)).peek(System.out::println).collect(Collectors.toList());
        Map<String, String> paramColor=new HashMap<>();
        for (int i=0; i<cellList.size();i++){
            if (cellList.get(i).getAbout().startsWith("GSM")){
                Cell2G p = (Cell2G) cellList.get(i);
                paramColor.put(p.getBcchBsic(), ColorHelper.mColors[i]);
            }
            else
            if (cellList.get(i).getAbout().startsWith("UMTS")) {
                Cell3G p = (Cell3G) cellList.get(i);
                paramColor.put(p.getScr() + "", ColorHelper.mColors[i]);
            }
            else
            if (cellList.get(i).getAbout().startsWith("LTE")){
                Cell4G p = (Cell4G) cellList.get(i);
                paramColor.put(p.getPCI()+"", ColorHelper.mColors[i]);
            }
        }
        List<CellToMap> cellToMapList=null;
        if (about.startsWith("GSM")) {
            cellToMapList = cellList.stream().map(p -> (Cell2G) p).map(p -> new CellToMap(p.getCi(), p.getAzimuth(), paramColor.get(p.getBcchBsic()))).collect(Collectors.toList());
        }
        else
        if (about.startsWith("UMTS")) {
            cellToMapList = cellList.stream().map(p -> (Cell3G) p).map(p -> new CellToMap(p.getCi(), p.getAzimuth(), paramColor.get(p.getScr()+""))).collect(Collectors.toList());
        }
        else
        if (about.startsWith("LTE")) {
            cellToMapList = cellList.stream().map(p -> (Cell4G) p).map(p -> new CellToMap(p.getCi(), p.getAzimuth(), paramColor.get(p.getPCI()+""))).collect(Collectors.toList());
        }
        Set<Point> pointsTomap=new HashSet<>();
        for (Cell c : cellList){
            if (about.equals("GSM 1800")) {
                Cell2G g = (Cell2G)c;
                Set<Point> set = positions.get(pos).getAllPointsInPosition().stream().filter(p -> p.getRxLevel1800()!=null && p.getRxLevel1800().get(g.getBcchBsic())!=null).collect(Collectors.toSet());
                pointsTomap.addAll(set);
            }
            else
            if (about.equals("GSM 900")) {
                Cell2G g = (Cell2G)c;
                Set<Point> set = positions.get(pos).getAllPointsInPosition().stream().filter(p->p.getRxLevel900()!=null && p.getRxLevel900().get(g.getBcchBsic())!=null).collect(Collectors.toSet());
                pointsTomap.addAll(set);
            }
            else
            if (about.equals("UMTS 2100 10813")) {
                Cell3G g = (Cell3G)c;
                Set<Point> set = positions.get(pos).getAllPointsInPosition().stream().filter(p -> p.getRSCP10813()!=null && p.getRSCP10813().get(Integer.parseInt(g.getScr()+""))!=null).collect(Collectors.toSet());
                pointsTomap.addAll(set);
            }
            else
            if (about.equals("UMTS 2100 10788")) {
                Cell3G g = (Cell3G)c;
                Set<Point> set = positions.get(pos).getAllPointsInPosition().stream().filter(p -> p.getRSCP10788()!=null && p.getRSCP10788().get(Integer.parseInt(g.getScr()+""))!=null).collect(Collectors.toSet());
                pointsTomap.addAll(set);
            }
            else
            if (about.equals("UMTS 2100 10836")) {
                Cell3G g = (Cell3G)c;
                Set<Point> set = positions.get(pos).getAllPointsInPosition().stream().filter(p -> p.getRSCP10836()!=null && p.getRSCP10836().get(Integer.parseInt(g.getScr()+""))!=null).collect(Collectors.toSet());
                pointsTomap.addAll(set);
            }
            else
            if (about.equals("UMTS 900 3036")) {
                Cell3G g = (Cell3G)c;
                Set<Point> set = positions.get(pos).getAllPointsInPosition().stream().filter(p -> p.getRSCP3036()!=null && p.getRSCP3036().get(Integer.parseInt(g.getScr()+""))!=null).collect(Collectors.toSet());
                pointsTomap.addAll(set);
            }
            else
            if (about.equals("UMTS 900 3012")) {
                Cell3G g = (Cell3G)c;
                Set<Point> set = positions.get(pos).getAllPointsInPosition().stream().filter(p -> p.getRSCP3012()!=null && p.getRSCP3012().get(Integer.parseInt(g.getScr()+""))!=null).collect(Collectors.toSet());
                pointsTomap.addAll(set);
            }
            else
            if (about.equals("LTE 2600")) {
                Cell4G g = (Cell4G)c;
                Set<Point> set = positions.get(pos).getAllPointsInPosition().stream().filter(p -> p.getRSRP3300()!=null && p.getRSRP3300().get(Integer.parseInt(g.getPCI()+""))!=null).collect(Collectors.toSet());
                pointsTomap.addAll(set);
            }
            else
            if (about.equals("LTE 2600")) {
                Cell4G g = (Cell4G)c;
                Set<Point> set = positions.get(pos).getAllPointsInPosition().stream().filter(p -> p.getRSRP3300()!=null && p.getRSRP3300().get(Integer.parseInt(g.getPCI()+""))!=null).collect(Collectors.toSet());
                pointsTomap.addAll(set);
            }
            else
            if (about.equals("LTE 1800")) {
                Cell4G g = (Cell4G)c;
                Set<Point> set = positions.get(pos).getAllPointsInPosition().stream().filter(p -> p.getRSRP1351()!=null && p.getRSRP1351().get(Integer.parseInt(g.getPCI()+""))!=null).collect(Collectors.toSet());
                pointsTomap.addAll(set);
            }
            else
            if (about.equals("LTE 800")) {
                Cell4G g = (Cell4G)c;
                Set<Point> set = positions.get(pos).getAllPointsInPosition().stream().filter(p -> p.getRSRP6413()!=null && p.getRSRP6413().get(Integer.parseInt(g.getPCI()+""))!=null).collect(Collectors.toSet());
                pointsTomap.addAll(set);
            }
        }
        List<PointToMap> list = pointsTomap.stream().map(p->new PointToMap(p, about, paramColor)).peek(p-> System.out.println(p.getColor())).collect(Collectors.toList());
        double maxDist=pointsTomap.stream().mapToDouble(p->p.getDistToPos().get(pos)).max().getAsDouble();

        model.addAttribute("cells", cellToMapList);
        model.addAttribute("pointss", list);
        model.addAttribute("lon", positions.get(pos).getCells().get(0).getLongitude());
        model.addAttribute("lat", positions.get(pos).getCells().get(0).getLalitude());
        model.addAttribute("radius",maxDist);
        return "map";
    }
}
