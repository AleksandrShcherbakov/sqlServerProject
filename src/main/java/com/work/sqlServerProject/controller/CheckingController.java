package com.work.sqlServerProject.controller;

import com.work.sqlServerProject.NBFparser.Parser;
import com.work.sqlServerProject.NBFparser.ParserHalper;
import com.work.sqlServerProject.Position.Cell;
import com.work.sqlServerProject.Position.Position;
import com.work.sqlServerProject.dao.CellNameDAO;
import com.work.sqlServerProject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by a.shcherbakov on 24.06.2019.
 */
@Controller
public class CheckingController {

    @Autowired
    private CellNameDAO cellNameDAO;
    private Position pos=null;
    private List<Point>allPoints=null;
    List<Point> points=null;


    @RequestMapping(value = "/inputScan", method = RequestMethod.GET)
    public String showSelectScanFilePage(Model model){
        PathScanFile pathScanFile = new PathScanFile();
        model.addAttribute("pathScanFile", pathScanFile);
        return "checking/checkPathFileScan";
    }

    @RequestMapping(value = "/inputScan", method = RequestMethod.POST)
    public String loadScanAndSelectPos(Model model, @ModelAttribute ("pathScanFile") PathScanFile pathScanFile){
        List<String> parsered;
        try {
            parsered = ParserHalper.createinSrtings(pathScanFile.getUrl());
        }
        catch (IOException e){
            return "Путь к файлу сканера указан не верно.";
        }
        points = Parser.getPointsFromScan(parsered);
        return "redirect:/checkPos";
    }

    @RequestMapping(value = "/checkPos", method = RequestMethod.GET)
    public String showSelectPos(Model model){
        PosForCheck posForCheck = new PosForCheck();
        model.addAttribute("pos", posForCheck);
        return "checking/checkPos";
    }



    @RequestMapping(value = "/checkPos", method = RequestMethod.POST)
    @ResponseBody
    public String selectPN(Model model, @ModelAttribute ("pos") PosForCheck posForCheck) throws IOException {
        if (posForCheck.getPosnames().equals("")){
            return "не введена позиция";
        }
        Position position=null;
        String pos= posForCheck.getPosnames();
        Integer posname=Integer.parseInt(pos);
        if (posname!=null) {
            List<CellInfo> list = cellNameDAO.getInfoForBS(posname);
            if (list.size()==0){
                return "БС с таким номером нет.";
            }
            position = new Position(list);
        }
        position.setPointsInPosition(points);
        position.findBestScan();
        this.pos=position;
        this.allPoints=points;
        StringBuilder ssylka= new StringBuilder();
        for (Cell p : position.getCells()) {
            ssylka.append("<a href='/map?cell=" + p.getCi() + "'>показать " + p.getCi() + "</a><br>");
        }
        ssylka.append("<a href='/map'>показать весь маршрут</a>");
        return posname+"<br>"+
                position.toString()+"<br>"+
                ssylka.toString();
    }




    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public String getMap(Model model, @RequestParam (required = false, name = "cell") String cell){
        if (cell==null){

            List<PointsToMap>allpoints = allPoints.stream()
                    .map(p->new PointsToMap(p.getLongitude(),p.getLatitude()))
                    .collect(Collectors.toList());

            model.addAttribute("points", allpoints);
            return "map";
        }
        Integer i = Integer.parseInt(cell);

        List<PointsToMap>points = pos.getCells().stream()
                .filter(p->p.getCi()==i)
                .flatMap(p->p.getPointsInCell().stream())
                .map(p->new PointsToMap(p.getLongitude(),p.getLatitude()))
                .collect(Collectors.toList());

        CellToMap cel=pos.getCells().stream()
                .filter(p->p.getCi()==i)
                .map(p->new CellToMap(p.getLongitude(),p.getLalitude(),p.getCi()))
                .findFirst()
                .get();

        model.addAttribute("cell", cel);
        model.addAttribute("points", points);
        return "map";
    }
}
