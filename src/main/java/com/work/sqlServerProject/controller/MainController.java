package com.work.sqlServerProject.controller;

import com.work.sqlServerProject.Helper.Helper;
import com.work.sqlServerProject.dao.CellNameDAO;
import com.work.sqlServerProject.form.BTSForm;
import com.work.sqlServerProject.form.CellNameForm;
import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.CellNameInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by AlVlShcherbakov on 14.02.2019.
 */
@Controller
public class MainController {
    @Value("${ltecarriers}")
    String [] LTEcarrierBand;
    @Value("${umtscarriers}")
    String [] UMTScarriers;

    public static String[]LTE;
    public static String[]UMTS;

    @Autowired
    private CellNameDAO cellNameDAO;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String openStartPage(){
        LTE=this.LTEcarrierBand;
        UMTS=this.UMTScarriers;
        for (String s : LTEcarrierBand){
            System.out.println(s);
        }
        for (String s : UMTScarriers){
            System.out.println(s);
        }
        return "start";
    }

    @RequestMapping(value = "/createBts", method = RequestMethod.GET)
    public String find(Model model){
        BTSForm btsForm = new BTSForm();
        model.addAttribute("btsForm", btsForm);
        return "createBts";
    }


    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public String select(Model model, CellNameForm cellNameForm){
        CellNameInfo cellNameInfo=cellNameDAO.findCellNumber(cellNameForm.getFormCellName());
        model.addAttribute("cellInfos",cellNameInfo);
        Helper.writeToCSV("C://",cellNameInfo.getCellName()+";"+cellNameInfo.getCI());
        return "show result";
    }


    @RequestMapping(value = "/bts", method = RequestMethod.POST)
    @ResponseBody
    public String createBTS(@ModelAttribute("btsForm")BTSForm btsForm, Model model) throws IOException {
        String path = btsForm.getPath();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");

        String fileName= simpleDateFormat.format(new Date())+"_ALL_BS.csv";
        File file = new File(path+"\\"+fileName);
        if (file.exists()){
            return "BTS ���� �� ������� ��� ������.<br><p><a href='/'>�� ��������� ��������</a></p>";
        }
        List<CellInfo>list=cellNameDAO.getAllCells();
        List<CellInfo>listLteEricsson = cellNameDAO.getAllLteEricssonCells();
        List<CellInfo>listLteHuawei = cellNameDAO.getAllLteHuaweiCells();
        StringBuilder log = new StringBuilder();

        System.out.println(path);

        if (!path.isEmpty()) {
            file = new File(path);

            if (file.isFile()){
                return "�� ������� ���� � �� ����������, �����������!<br><p><a href='/'>������ ���������� ��� ���</a></p>";
            }
            else
            if (!file.exists()){
                Files.createDirectory(Paths.get(path));
            }}
        else
        if (path.equals("")){
            path="C://";
        }
        log.append(Helper.writeToCSV(path,list));
        log.append(Helper.writeToCSV(path,listLteEricsson));
        log.append(Helper.writeToCSV(path,listLteHuawei));
        /*StringBuilder stringBuilder = new StringBuilder();
        for (CellInfo a : listLteHuawei){
            //stringBuilder.append(a);
            //System.out.println(a);
        }*/
        return "BTS ���� ������<br><p><a href='/'>�� ��������� ��������</a></p>";
    }
    @RequestMapping(value = "/exit", method = RequestMethod.GET)
    public String exit(){
        System.exit(0);
        return null;
    }

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    @ResponseBody
    public String  createFile() throws IOException {
        File file1 = new File("C://123.txt");
        String string = "\n ������ ghbdtn";
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1, true)));
            bufferedWriter.write(string);
            bufferedWriter.close();
        }
        catch (Exception e){
            return "������ �� ����������";
        }
        return file1.toString();
    }

    @RequestMapping(value = "/read", method = RequestMethod.GET)
    @ResponseBody
    public  String readFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:\\123.txt");
        InputStreamReader reader = new InputStreamReader(fileInputStream,"UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        String res = bufferedReader.readLine();
        return res;
    }


}
