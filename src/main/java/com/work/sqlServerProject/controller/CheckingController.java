package com.work.sqlServerProject.controller;

import com.work.sqlServerProject.NBFparser.Parser;
import com.work.sqlServerProject.NBFparser.ParserHalper;
import com.work.sqlServerProject.Position.Position;
import com.work.sqlServerProject.dao.CellNameDAO;
import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.Point;
import com.work.sqlServerProject.model.PosAndNMF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by a.shcherbakov on 24.06.2019.
 */
@Controller
public class CheckingController {

    @Autowired
    private CellNameDAO cellNameDAO;
    List<Point> pointss;

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String showSelectPosAndNBF(Model model){
        PosAndNMF posAndNMF = new PosAndNMF();
        model.addAttribute("posAndNMF", posAndNMF);
        return "checking/checkPosAndNMF";
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public String selectPN(Model model, @ModelAttribute ("posAndNMF") PosAndNMF posAndNMF) throws IOException {
        if (posAndNMF.getPosnames().equals("")){
            return "не введена позиция";
        }
        if (posAndNMF.getUrl().equals("")){
            return "файл сканера не указан";
        }
        Position position=null;
        String pos=posAndNMF.getPosnames();
        Integer posname=Integer.parseInt(pos);
        if (posname!=null) {
            List<CellInfo> list = cellNameDAO.getInfoForBS(posname);
            if (list.size()==0){
                return "БС с таким номером нет.";
            }
            position = new Position(list);
        }
        List<String> parsered;
        try {
            parsered = ParserHalper.createinSrtings(posAndNMF.getUrl());
        }
        catch (IOException e){
            return "Путь к файлу сканера указан не верно.";
        }
        List<Point> points = Parser.getPointsFromScan(parsered);
        position.setPointsInPosition(points);
        position.findBestScan();
        return posname+"<br>"+
                position.toString();
    }


    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public String getMap(Model model){
        model.addAttribute("points",pointss);
        return "map";
    }
}
