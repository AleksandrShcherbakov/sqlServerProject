package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;

import java.util.Map;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Cell2G extends Cell {
    private int BCCH;
    private Object BSIC;
    private Map<String,Double> rxLevperBCCH;
    private int bestScanBCCH;
    private int bestCellID;
    private boolean rightAzimuth;

    public Cell2G(CellInfo cellInfo) {
        super(cellInfo);
        this.BCCH=cellInfo.getCh();
        this.BSIC=cellInfo.getBsic();

    }



    public int getBestScanBCCH() {
        return bestScanBCCH;
    }

    public void setBestScanBCCH(int bestScanBCCH) {
        this.bestScanBCCH = bestScanBCCH;
    }

    public boolean isRightAzimuth() {
        return rightAzimuth;
    }

    public void setRightAzimuth(boolean rightAzimuth) {
        this.rightAzimuth = rightAzimuth;
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
