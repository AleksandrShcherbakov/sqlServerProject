package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Cell4G extends Cell {
    private int band;
    private Object PCI;
    private int ch;

    public Cell4G(CellInfo cellInfo) {
        super(cellInfo);
        this.band=cellInfo.getBand();
        this.PCI= cellInfo.getPci();
        this.ch=cellInfo.getCh();
    }

    public int getBand() {
        return band;
    }

    public void setBand(int band) {
        this.band = band;
    }

    public Object getPCI() {
        return PCI;
    }

    public void setPCI(Object PCI) {
        this.PCI = PCI;
    }

    public int getCh() {
        return ch;
    }

    public void setCh(int ch) {
        this.ch = ch;
    }

    @Override
    public String toString() {
        return super.toString()+"Cell4G{" +
                "band=" + band +
                ", PCI=" + PCI +
                ", ch=" + ch +
                '}';
    }
}
