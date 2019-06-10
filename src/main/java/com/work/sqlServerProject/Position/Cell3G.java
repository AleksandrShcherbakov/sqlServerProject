package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Cell3G extends Cell {

    private Object Scr;
    private int carrierFrequency;

    public Cell3G(CellInfo cellInfo) {
        super(cellInfo);

        this.Scr=  cellInfo.getScr();
        this.carrierFrequency=cellInfo.getCh();
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
