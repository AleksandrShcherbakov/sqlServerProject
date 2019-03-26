package com.work.sqlServerProject.form;

import com.work.sqlServerProject.model.CellForSZ;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.shcherbakov on 14.03.2019.
 */
public class FormCellForSZ {
    private List<CellForSZ> list;


    public FormCellForSZ(List<CellForSZ> list) {
        this.list = list;
    }

    public List<CellForSZ> getList() {
        return list;
    }


    public void setList(List<CellForSZ> list) {
        this.list = list;
    }

}
