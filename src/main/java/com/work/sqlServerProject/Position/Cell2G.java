package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Cell2G extends Cell {
    private int Band;
    private int BCCH;
    private Object BSIC;

    public Cell2G(CellInfo cellInfo) {
        super(cellInfo);
        this.BCCH=cellInfo.getCh();
        this.BSIC=cellInfo.getBsic();
        this.Band=cellInfo.getBand();
    }

    public int getBand() {
        return Band;
    }

    public void setBand(int band) {
        Band = band;
    }

    public int getBCCH() {
        return BCCH;
    }

    public void setBCCH(int BCCH) {
        this.BCCH = BCCH;
    }

    public Object getBSIC() {
        return BSIC;
    }

    public void setBSIC(Object BSIC) {
        this.BSIC = BSIC;
    }
}
