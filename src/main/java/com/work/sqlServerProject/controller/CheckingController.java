package com.work.sqlServerProject.controller;

import com.work.sqlServerProject.Helper.ColorHelper;
import com.work.sqlServerProject.Helper.FileScanHelper;
import com.work.sqlServerProject.Helper.NoCarrierException;
import com.work.sqlServerProject.NBFparser.Parser;
import com.work.sqlServerProject.NBFparser.ParserHalper;
import com.work.sqlServerProject.Position.*;
import com.work.sqlServerProject.dao.CellNameDAO;
import com.work.sqlServerProject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by a.shcherbakov on 24.06.2019.
 */
@Controller
public class CheckingController {




    public static String[]LTE;
    public static String[]UMTS;

    @Autowired
    private CellNameDAO cellNameDAO;
    private Map<Integer, Position> positions = null;
    List<Point> points = null;
    @Value("${list.filesNMF}")
    String[] listPath;
    List<String> listWithNmf = null;
    List<String> listFilesBts = null;
    @Value("${filesBTS}")
    String pathToNbf;

    boolean useBTSFile = false;
    String pathToBts = null;
    List<String> btsLines = null;
    List<String> alredyLoadedFiles=new ArrayList<>();


    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String reset(Model model){
        alredyLoadedFiles.clear();
        points.clear();
        return "redirect:/inputScan";
    }



    @RequestMapping(value = "/inputScan", method = RequestMethod.GET)
    public String showSelectScanFilePage(Model model){
        List<Path>list=new ArrayList<>();
        LTE=MainController.LTE;
        UMTS=MainController.UMTS;

        for (String s : listPath) {
            List<Path> nmfs = FileScanHelper.getFiles(s);
            list.addAll(nmfs);
        }
        list.sort(FileScanHelper.comparator);
        List<String>listStr=list.stream().map(p->p.toString()).peek(p-> System.out.println(p)).collect(Collectors.toList());
        this.listWithNmf=listStr;
        PathScanFile pathScanFile = new PathScanFile();
        List<String>filesBts = getBtsPaths(pathToNbf).stream().map(p->p.toString()).peek(System.out::println).collect(Collectors.toList());
        this.listFilesBts=filesBts;
        model.addAttribute("btss",filesBts);
        model.addAttribute("listFiles",listStr);
        model.addAttribute("pathScanFile", pathScanFile);
        model.addAttribute("loaded", alredyLoadedFiles);
        if (points!=null){
            model.addAttribute("countOfPoints", points.size());
        }
        return "checking/checkPathFileScan";
    }


    @RequestMapping(value = "/inputScan", method = RequestMethod.POST)
    public String loadScanAndSelectPos(Model model, @ModelAttribute ("pathScanFile") PathScanFile pathScanFile,
                                       @RequestParam (required = false,name = "file") String files,
                                       @RequestParam (required = false, name= "isBTS") String isBts,//галочка
                                       @RequestParam (required = false, name = "bts") String btsPath){



        if (isBts==null){
            this.useBTSFile=false;
            this.pathToBts=null;
            this.btsLines=null;
        }
        else
        if (isBts!=null){
            this.useBTSFile=true;
            if (!pathScanFile.getToBts().equals("")){
                this.pathToBts=pathScanFile.getToBts();
                try {
                    btsLines=new ArrayList<>();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pathToBts)));
                    String s = reader.readLine();
                    while (s!=null){
                        btsLines.add(s);
                        s=reader.readLine();
                    }
                    reader.close();
                } catch (IOException e) {
                    System.out.println(pathToBts+ " файл с BTS не прочитан");
                    model.addAttribute("nobtsread", "БТС файл указан не верно "+pathToBts);
                }
            }
            else
            if (btsPath!=null){
                this.pathToBts=btsPath;
                try {
                    btsLines=new ArrayList<>();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pathToBts)));
                    String s = reader.readLine();
                    while (s!=null){
                        btsLines.add(s);
                        s=reader.readLine();
                    }
                    reader.close();
                } catch (IOException e) {
                    System.out.println(pathToBts+ " файл с BTS не прочитан");
                    model.addAttribute("nobtsread", "БТС файл указан не верно "+pathToBts);
                }
            }
            else {
                model.addAttribute("nobts", "Указано желание использовать BTS файл, но сам файл не указан.");
                model.addAttribute("btss",this.listFilesBts);
                model.addAttribute("listFiles",this.listWithNmf);
                return "checking/checkPathFileScan";
            }
        }
        StringBuilder stringBuilder=new StringBuilder();
        if (!pathScanFile.getUrl().equals("")) {
            if (alredyLoadedFiles.contains(pathScanFile.getUrl())) {
                System.out.println(pathScanFile.getUrl() + " уже загружен");
            } else {
                stringBuilder.append(readFiles(pathScanFile.getUrl()));
                alredyLoadedFiles.add(pathScanFile.getUrl());
            }
        }
        if (files!=null) {
            String[] file = files.split(",");
            for (String s : file) {
                if (alredyLoadedFiles.contains(s)) {
                    System.out.println(s + " уже загружен");
                } else {
                    stringBuilder.append(readFiles(s));
                    alredyLoadedFiles.add(s);
                }
            }
        }
        if (pathScanFile.getUrl().equals("") && files==null && alredyLoadedFiles.size()==0){
            model.addAttribute("nofiles", "Не указан и не загружен ни один файл сканера.");
            model.addAttribute("btss",this.listFilesBts);
            model.addAttribute("listFiles",this.listWithNmf);
            return "checking/checkPathFileScan";
        }
        if (points.size()==0){
            model.addAttribute("nopoints", "Ни один файл сканера прочитать не удалось. Возможно неправильно указан путь к файлу сканера.");
            model.addAttribute("btss",this.listFilesBts);
            model.addAttribute("listFiles",this.listWithNmf);
            return "checking/checkPathFileScan";
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

    public List<Path> getBtsPaths(String pathDir) {
        File dir = new File(pathDir); //path указывает на директорию
        File[] arrFiles = dir.listFiles();
        if (arrFiles != null) {
            List<Path> files = Arrays.stream(arrFiles).map(p -> p.toPath()).filter(p -> p.toString().endsWith(".csv") || p.toString().endsWith(".nbf")).peek(System.out::println)
                    .sorted(FileScanHelper.comparator).collect(Collectors.toList());
            return files;
        }
        else return new ArrayList<>();
    }

    public String readFiles(String path){
        List<String> parsered;
        try {
            parsered = ParserHalper.createinSrtings(path);
        }
        catch (IOException e){
            return "Путь к файлу сканера указан не верно.";
        }
        int size1=0;
        if (points!=null){
            size1=points.size();
        }
        if (points==null) {
            points = Parser.getPointsFromScan(parsered);
        }
        else points.addAll(Parser.getPointsFromScan(parsered));
        int size2=points.size();
        if (points.size()!=0 && size1!=size2) {
            return "файл " + path + " прочитан";
        }
        else return "файл "+ path + " не прочитан";
    }



    @RequestMapping(value = "/checkPos", method = RequestMethod.POST)
    //@ResponseBody
    public String selectPN(Model model, @ModelAttribute ("pos") PosForCheck posForCheck) throws IOException {
        if (posForCheck.getPosnames().equals("")){
            return "не введена позиция";
        }
        positions=new HashMap<>();

        StringBuilder res=new StringBuilder();
        StringBuilder wrangs = new StringBuilder();
        String[]poss=posForCheck.getPosnames().split(",");
        for (String s : poss){
            s=s.trim();
            try {
                int pos=Integer.parseInt(s);
                int i=0;
                try{
                    i=setPosition(pos);
                }
                catch (NoCarrierException e ){
                    e.printStackTrace();
                    int ch = e.getCh();
                    String wrang = "частота "+ch+" не указана в application.properties. Проверьте.<br>";
                    wrangs.append(wrang);
                    res.append(wrang);
                }

                if (i==-1) {
                    if (useBTSFile) {
                        String wrang = "Позиции " + pos + " в БТС файле не найдено<br>";
                        wrangs.append(wrang);
                        res.append(wrang);
                    } else {
                        String wrang ="Позиции " + pos + " в базе не найдено<br>";
                        wrangs.append(wrang);
                        res.append(wrang);
                    }
                }
            }
            catch (SQLException e){
                String wrang = "Нет доступа к базе general, а БТС файл не выбран.<br>" +
                        "<a href='/inputScan'>Вернуться на страницу выбора</a>";
                wrangs.append(wrang);
                res.append(wrang);
                model.addAttribute("wrangs", wrangs.toString());
                return "checking/result";
            }
            catch (Exception e){
                e.printStackTrace();
                String wrang = "В указани" +
                        "и списка позиций ошибка: "+s+"<br>";
                wrangs.append(wrang);
                res.append(wrang);
            }
        }
        List<String>toTemplate=new ArrayList<>();
        for (Map.Entry p : positions.entrySet()){
            toTemplate.add(positions.get(p.getKey()).getInfoForTemplate());
            res.append("<b>"+p.getKey()+"</b><br><br>"+
                    p.getValue().toString()+
                    positions.get(p.getKey()).printDetailInfo()+"<br>");
            res.append("=================================================================<br><br>");
        }
        model.addAttribute("wrangs", wrangs.toString());
        model.addAttribute("allInfo", toTemplate);
        //return  res.toString();
        return "checking/result";

    }

    public int setPosition(Integer posname) throws SQLException, NullPointerException {
        Position position=null;
        List<CellInfo> list=null;
        if (posname!=null) {
            if (useBTSFile){
                String param = btsLines.get(0);
                String[]parameters=param.split(";");
                int i=0;
                for (int s=0;s<parameters.length;s++){
                    if (parameters[s].equals("SITE")) {
                        i = s;
                        break;
                    }
                }
                int finalI = i;
                list = btsLines.stream().filter(p->p.split(";")[finalI].equals(posname+"")).map(p->new CellInfo(param, p)).collect(Collectors.toList());
                if (list.size() == 0) {
                    return -1;
                }
            }
            else {
                try {
                    list = cellNameDAO.getInfoForBS(posname);
                }
                catch (Exception e){
                    throw new SQLException();
                }
                if (list.size() == 0) {
                    return -1;
                }
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
                         @RequestParam(required = false, name = "posName") String number,
                         @RequestParam(required = false, name = "cell") String cell){
        Integer pos = Integer.parseInt(number);
        List<Cell>cellList= positions.get(pos).getCells().stream().filter(p->p.getAbout().equals(about)).collect(Collectors.toList());
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
            if (about.startsWith("GSM")) {
                Cell2G g = (Cell2G)c;
                Set<Point> set = positions.get(pos).getAllPointsInPosition().stream().filter(p -> p.getMainMap().get(about)!=null && p.getMainMap().get(about).get(g.getBcchBsic())!=null).collect(Collectors.toSet());
                pointsTomap.addAll(set);
            }
            else
            if (about.startsWith("UMTS")) {
                Cell3G g = (Cell3G)c;
                Set<Point> set = positions.get(pos).getAllPointsInPosition().stream().filter(p -> p.getMainMap().get(about)!=null && p.getMainMap().get(about).get(g.getScr()+"")!=null).collect(Collectors.toSet());
                pointsTomap.addAll(set);
            }
            else
            if (about.startsWith("LTE")) {
                Cell4G g = (Cell4G)c;
                Set<Point> set = positions.get(pos).getAllPointsInPosition().stream().filter(p -> p.getMainMap().get(about)!=null && p.getMainMap().get(about).get(g.getPCI()+"")!=null).collect(Collectors.toSet());
                pointsTomap.addAll(set);
            }
        }
        if (cell!=null){
            String param="";
            for (Cell c : cellList){
                if (c.getCi()==Integer.parseInt(cell)){
                    param=c.getParam();
                    model.addAttribute("azimuthOfsector",c.getAzimuth());
                }
            }
            String finalParam = param;
            List<PointToMap> listWithLevels = pointsTomap.stream().map(m->new PointToMap(m, about, finalParam)).
                    peek(p-> System.out.println(p.getLongitude()+" "+p.getLatitude()+" "+p.getColor()+" "+p.getParam())).collect(Collectors.toList());
            model.addAttribute("listWithLevels",listWithLevels);
        }
        List<PointToMap> list = pointsTomap.stream().map(p->new PointToMap(p, about, paramColor)).collect(Collectors.toList());

        double maxDist=500;
        boolean nopoints=false;
        try {
            maxDist=pointsTomap.stream().mapToDouble(p->p.getDistToPos().get(pos)).max().getAsDouble();
        }
        catch (NoSuchElementException e){
            e.printStackTrace();
            nopoints=true;
        }
        if (nopoints){
            model.addAttribute("nopoints", "Для позиции "+pos+" измерений нет.");
        }
        model.addAttribute("pos",pos);
        model.addAttribute("about", about);

        model.addAttribute("cells", cellToMapList);
        model.addAttribute("pointss", list);
        model.addAttribute("lon", positions.get(pos).getCells().get(0).getLongitude());
        model.addAttribute("lat", positions.get(pos).getCells().get(0).getLalitude());
        model.addAttribute("radius",maxDist);
        return "map";
    }
}
