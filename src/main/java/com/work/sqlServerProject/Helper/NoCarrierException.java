package com.work.sqlServerProject.Helper;

import com.work.sqlServerProject.Position.Cell;

/**
 * Created by a.shcherbakov on 22.08.2019.
 */
public class NoCarrierException extends RuntimeException {
    private int ch;
    public NoCarrierException(Cell cell) {
        this.ch=cell.getChannel();
    }

    public int getCh() {
        return ch;
    }

    public void setCh(int ch) {
        this.ch = ch;
    }
}
