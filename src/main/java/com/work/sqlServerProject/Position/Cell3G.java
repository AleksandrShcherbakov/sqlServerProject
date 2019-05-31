package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Cell3G extends Cell {
    private int Band;
    private Object Scr;
    private int carrierFrequency;

    public Cell3G(CellInfo cellInfo) {
        super(cellInfo);
        this.Band=cellInfo.getBand();
        this.Scr=  cellInfo.getScr();
        this.carrierFrequency=cellInfo.getCh();
    }

    public int getBand() {
        return Band;
    }

    public void setBand(int band) {
        Band = band;
    }

    public Object getScr() {
        return Scr;
    }

    public void setScr(Object scr) {
        Scr = scr;
    }

    public int getCarrierFrequency() {
        return carrierFrequency;
    }

    public void setCarrierFrequency(int carrierFrequency) {
        this.carrierFrequency = carrierFrequency;
    }
}
