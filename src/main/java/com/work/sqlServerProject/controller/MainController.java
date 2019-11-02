package com.work.sqlServerProject.controller;

import com.work.sqlServerProject.Helper.Helper;
import com.work.sqlServerProject.dao.CellNameDAO;
import com.work.sqlServerProject.form.BTSForm;
import com.work.sqlServerProject.form.CellNameForm;
import com.work.sqlServerProject.mapper.CellMapper;
import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.CellNameInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.net.URISyntaxException;
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
    @Value("${headerOfSOMS}")
    String headerOfSOMS;
    @Value("${headerOfExpluatation}")
    String heagerOfExpl;

    public static String[]LTE;
    public static String[]UMTS;
    public static String headerSOMS;
    public static String headerExp;

    @Autowired
    private CellNameDAO cellNameDAO;

    @EventListener(ApplicationReadyEvent.class)
    public void setStaticVariables() throws UnsupportedEncodingException {
        headerSOMS=this.headerOfSOMS;
        System.out.println(headerSOMS);
        headerSOMS=new String (headerSOMS.getBytes("ISO-8859-1"), "Cp1251");
        System.out.println(headerSOMS);
        headerExp=this.heagerOfExpl;
        headerExp=new String (headerExp.getBytes("ISO-8859-1"), "Cp1251");
        LTE=this.LTEcarrierBand;
        UMTS=this.UMTScarriers;
        System.out.println("статические переменный установлены");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String openStartPage(){
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
            return "BTS файл за сегодня уже создан.<br><p><a href='/'>На стартовую страницу</a></p>";
        }
        List<CellInfo>list2G=cellNameDAO.getAll2GCells();
        List<CellInfo>listUmtsEricsson=cellNameDAO.getAll3GECells();
        List<CellInfo>listUmtsHuawei=cellNameDAO.getAll3GHCells();

        List<CellInfo>listLteEricsson = cellNameDAO.getAllLteEricssonCells();
        List<CellInfo>listLteHuawei = cellNameDAO.getAllLteHuaweiCells();
        StringBuilder log = new StringBuilder();

        System.out.println(path);

        if (!path.isEmpty()) {
            file = new File(path);

            if (file.isFile()){
                return "Вы указали файл а не директорию, ипаравьтесь!<br><p><a href='/'>Ввести директорию еще раз</a></p>";
            }
            else
            if (!file.exists()){
                Files.createDirectory(Paths.get(path));
            }}
        else
        if (path.equals("")){
            path="C://";
        }
        log.append(Helper.writeToCSV(path,list2G));
        log.append(Helper.writeToCSV(path,listUmtsEricsson));
        log.append(Helper.writeToCSV(path,listUmtsHuawei));
        log.append(Helper.writeToCSV(path,listLteEricsson));
        log.append(Helper.writeToCSV(path,listLteHuawei));
        /*StringBuilder stringBuilder = new StringBuilder();
        for (CellInfo a : listLteHuawei){
            //stringBuilder.append(a);
            //System.out.println(a);
        }*/
        return "BTS файл создан<br><p><a href='/'>На стартовую страницу</a></p>";
    }
    @RequestMapping(value = "/exit", method = RequestMethod.GET)
    public String exit(){
        try {
            Files.delete(Paths.get("C://points.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
        return null;
    }


    @RequestMapping(value = "/file", method = RequestMethod.GET)
    @ResponseBody
    public String  createFile() throws URISyntaxException {

        CellMapper mapper = new CellMapper();
        String res=mapper.readSqlStr("/sqlQuerries/2G.sql");
        String res2 = mapper.readSqlStr("/sqlQuerries/2G.sql");

        return res+" "+res2;
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
