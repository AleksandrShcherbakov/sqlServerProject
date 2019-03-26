package com.work.sqlServerProject.controller;

import com.work.sqlServerProject.Helper.CreateWord;
import com.work.sqlServerProject.Helper.Helper;
import com.work.sqlServerProject.dao.CellNameDAO;
import com.work.sqlServerProject.form.FormCellForSZ;
import com.work.sqlServerProject.form.SZFormPos;
import com.work.sqlServerProject.model.CellForSZ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.shcherbakov on 14.03.2019.
 */
@Controller
public class SZController {

    @Autowired
    CellNameDAO cellNameDAO;

    List<CellForSZ>list;


    @RequestMapping(value = "/szPos", method = RequestMethod.GET)
    public String shoeInfoForSZ(Model model){
        SZFormPos szFormPos = new SZFormPos();
        model.addAttribute("szFormPos", szFormPos);
        return "checkPos";
    }

    @RequestMapping(value = "/szPos", method = RequestMethod.POST)
    public String getInfoForSZ(Model model, @ModelAttribute("szFormPos") SZFormPos szFormPos,
                               @RequestParam (required=false, name = "system") String system){
        if (szFormPos.getPosName()==0){
            model.addAttribute("nopos", "Номер позиции не может быть \"0\"");
            return "checkPos";
        }
        if (system==null) {
            model.addAttribute("nothing", "Вы не указали ни одного диапазона.");
            return "checkPos";
        }
        list=cellNameDAO.getBSforSZ(szFormPos.getPosName());
        if (list.size()==0){
            model.addAttribute("noposonnetwork", "БС с указанным номером не существует.");
            return "checkPos";
        }
        String[] bands = system.split(",");
        List<CellForSZ>checkedList = new ArrayList<>();
        for (CellForSZ c : list){
            c.checking(bands);
            c.createSetCells(list);
            checkedList.add(c);
        }
        List<CellForSZ>resList = new ArrayList<>();
        for (CellForSZ c : checkedList){
            if (c.isCheck()){
                resList.add(c);
            }
        }
        list=resList;
        FormCellForSZ formCellForSZ = new FormCellForSZ(resList);
        model.addAttribute("formForSZ", formCellForSZ);
        return "szTable";
    }

    @RequestMapping(value = "/setCI", method = RequestMethod.POST)
    public String showResultTable(Model model, @ModelAttribute("formForSZ") FormCellForSZ formCellForSZ) throws IOException {
        Helper.setCIfromform(list, formCellForSZ.getList());
        model.addAttribute("listSZ",list);
        CreateWord.createWordFile(list,15,"zxczx");
        return "szTableRes";
    }
}
