package com.work.sqlServerProject.Helper;

import com.work.sqlServerProject.Position.Cell;

/**
 * Created by a.shcherbakov on 22.08.2019.
 */
public class NoCarrierException extends RuntimeException {
    private int ch;
    private int pos;
    public NoCarrierException(Cell cell) {
        this.ch=cell.getChannel();
        this.pos=cell.getPosname();
    }

    public int getCh() {
        return ch;
    }

    public void setCh(int ch) {
        this.ch = ch;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
