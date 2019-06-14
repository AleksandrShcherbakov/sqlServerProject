package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Position {
    private int numberOfPosition;
    private List<Cell>cells;

    public Position(List<CellInfo>cellInfo) {
        cells=new ArrayList<>();
        this.numberOfPosition=cellInfo.get(0).getSyte();
        for (CellInfo cell : cellInfo) {
            if (cell.toString().startsWith("UMTS")) {
                cells.add(new Cell3G(cell));
            }

            if (cell.toString().startsWith("GSM")){
                cells.add(new Cell2G(cell));
            }

            if (cell.toString().startsWith("LTE")){
                cells.add(new Cell4G(cell));
            }
        }
        for (Cell cell : cells){
            cell.setAllCellsInBand(cells);
            cell.setPreviousAndNextCells();
        }
    }

    public void setPointsInPosition(List<Point> allPoints){
        for (Cell c : cells){
            c.setPointsInCellFromNBF(allPoints);
        }
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }



    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Cell c: cells){
            stringBuilder.append(c.toString()+"<br>");

        }
        return stringBuilder.toString();
    }
}
