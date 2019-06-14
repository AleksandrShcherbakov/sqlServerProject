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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.shcherbakov on 14.03.2019.
 */
@Controller
public class SZController {

    private String pathDir;

    public String getPathDir() {
        return pathDir;
    }

    public void setPathDir(String pathDir) {
        this.pathDir = pathDir;
    }

    private static String executor;

    public int getNumOfSZ() {
        return numOfSZ;
    }

    public void setNumOfSZ(int numOfSZ) {
        this.numOfSZ = numOfSZ;
    }

    private int numOfSZ;

    private int numOfBS;

    public int getNumOfBS() {
        return numOfBS;
    }

    public void setNumOfBS(int numOfBS) {
        this.numOfBS = numOfBS;
    }

    @Autowired
    CellNameDAO cellNameDAO;

    List<CellForSZ>list;

    public static String getExecutor() {
        return executor;
    }

    public  void setExecutor(String executor) {
        this.executor = executor;
    }

    @RequestMapping(value = "/szPos", method = RequestMethod.GET)
    public String shoeInfoForSZ(Model model){
        SZFormPos szFormPos = new SZFormPos();
        model.addAttribute("szFormPos", szFormPos);
        return "checkPos";
    }

    @RequestMapping(value = "/szPos", method = RequestMethod.POST)
    public String getInfoForSZ(Model model, @ModelAttribute("szFormPos") SZFormPos szFormPos,
                               @RequestParam (required=false, name = "system") String system,
                               @RequestParam (required=false, name="executor") String ex) throws IOException {
        if (ex==null){
            model.addAttribute("noExecutor", "Необходимо указать имя исполнителя.");
            return "checkPos";
        }
        this.setExecutor(ex);

        if (szFormPos.getPosName()==0){
            model.addAttribute("nopos", "Номер позиции не может быть \"0\"");
            return "checkPos";
        }
        numOfBS=szFormPos.getPosName();
        if (system==null) {
            model.addAttribute("nothing", "Вы не указали ни одного диапазона.");
            return "checkPos";
        }
        if (szFormPos.getPathDir().equals("")){
            this.pathDir=Helper.createDefPath();
        }
        else {
            this.pathDir=Helper.createDefPath(szFormPos.getPathDir());
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
        model.addAttribute("numOfBS", numOfBS);
        return "szTable";
    }

    @RequestMapping(value = "/setCI", method = RequestMethod.POST)
    public String showResultTable(Model model, @ModelAttribute("formForSZ") FormCellForSZ formCellForSZ) throws IOException {
        Helper.setCIfromform(list, formCellForSZ.getList());
        model.addAttribute("listSZ",list);
        model.addAttribute("phrase",CreateWord.createVvodniyText(list));
        model.addAttribute("dateOfExecuting", CreateWord.createDateIspolneniya());
        model.addAttribute("szcontr",this);
        model.addAttribute("numOfBS", numOfBS);
        //StringSelection stringSelection = new StringSelection(CreateWord.createVvodniyText(list));
        //SqlServerProjectApplication.clpbrd.setContents(stringSelection, null);
        return "szTableRes";
    }

    /*@RequestMapping(value = "/copy", method = RequestMethod.POST)
    public String copy() throws IOException {
        StringSelection stringSelection = new StringSelection(CreateWord.createVvodniyText(list));
        SqlServerProjectApplication.clpbrd.setContents(stringSelection, null);
        return "redirect:/setCI";
    }*/


    @RequestMapping(value = "/createword", method = RequestMethod.POST)
    @ResponseBody
    public String createword(Model model, @ModelAttribute("szcontr") SZController szController) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        CreateWord.createWordFile(list,szController.getNumOfSZ(),pathDir);
        return "СЗ создана.<br>" +
                "<p><a href='/'>На главную.</a></p>" +
                "<p><a href='/szPos'>Создать еще одну СЗ.</a>";
    }


}
