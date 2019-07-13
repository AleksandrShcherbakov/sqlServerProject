package com.work.sqlServerProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Александр on 11.07.2019.
 */
@Controller
public class TastController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String getBtsPaths(/*String pathDir*/){
        File dir = new File("C:\\giants"); //path указывает на директорию
        File[] arrFiles = dir.listFiles();
        List<File> lst = Arrays.asList(arrFiles);
        for (File f : lst){
            System.out.println(f.getAbsolutePath());
        }
        return "ok";
    }


}
