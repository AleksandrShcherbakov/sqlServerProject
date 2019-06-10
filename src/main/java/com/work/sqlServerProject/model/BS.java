package com.work.sqlServerProject.model;

import com.work.sqlServerProject.Position.Cell;
import com.work.sqlServerProject.dao.CellNameDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class BS {
    @Autowired
    CellNameDAO cellNameDAO;

    List<Cell>allCells;
    private int posName;

    public BS(int posName) {
        List<CellInfo>list = cellNameDAO.getInfoForBS(posName);

    }
}
