package com.work.sqlServerProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Александр on 11.07.2019.
 */
@Controller
public class TastController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String date(){
        String dateInString = "82May13";
        String dateInString2 = "80May13";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMMdd",Locale.ENGLISH);
        LocalDate dateTime = LocalDate.parse(dateInString, formatter);
        LocalDate dateTime2 = LocalDate.parse(dateInString2, formatter);
        System.out.println(dateTime);
        return  dateTime2.compareTo(dateTime)+"";

    }


}
