package com.work.sqlServerProject.controller;

import com.work.sqlServerProject.dao.CellNameDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by AlVlShcherbakov on 14.02.2019.
 */
@Controller
public class MainController {

    @Autowired
    private CellNameDAO cellNameDAO;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String find(){
        int id=13010;
        return cellNameDAO.findCellNumber().toString();
    }
}
