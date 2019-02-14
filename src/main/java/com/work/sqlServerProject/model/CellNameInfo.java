package com.work.sqlServerProject.model;

/**
 * Created by AlVlShcherbakov on 14.02.2019.
 */
public class CellNameInfo {
    private int cellName;
    private int CI;

    public CellNameInfo(int cellName, int CI) {
        this.cellName = cellName;
        this.CI = CI;
    }

    public int getCellName() {
        return cellName;
    }

    public void setCellName(int cellName) {
        this.cellName = cellName;
    }

    public int getCI() {
        return CI;
    }

    public void setCI(int CI) {
        this.CI = CI;
    }
}
