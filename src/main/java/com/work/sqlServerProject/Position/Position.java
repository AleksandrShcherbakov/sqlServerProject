package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Position {
    private int numberOfPosition;
    private List<Cell>cells;
    private StringBuilder stringBuilder=new StringBuilder();

    public void findBestScan(){
        for (Cell c : cells){
            String classOfCell = c.getClass().getSimpleName();
            if (classOfCell.equals("Cell2G")){
                Cell2G p  = (Cell2G) c;
                p.putAllrxLevinband();
                p.findBestCI();
                stringBuilder.append(p.toString());
                stringBuilder.append("<br>");
            }
            else
            if (classOfCell.equals("Cell3G")){
                Cell3G p  = (Cell3G) c;
                p.putAllRSCPinband();
                p.findBestCI();
                stringBuilder.append(p.toString());
                stringBuilder.append("<br>");
            }
            else
            if (classOfCell.equals("Cell4G")){
                Cell4G p  = (Cell4G) c;
                p.putAllRSRPinband();
                p.findBestCI();
                stringBuilder.append(p.toString());
                stringBuilder.append("<br>");
            }
        }
    }

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
        cells.sort(new Comparator<Cell>() {
            @Override
            public int compare(Cell o1, Cell o2) {
                if (o1.getNumID()>o2.getNumID())
                    return 1;
                else
                if (o1.getNumID()<o2.getNumID()){
                    return -1;
                }
                else
                if (o1.getAzimuth()>o2.getAzimuth()){
                    return 1;
                }
                else
                if (o1.getAzimuth()<o2.getAzimuth()){
                    return -1;
                }
                else
                    return 0;
            }
        });
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

    public int getNumberOfPosition() {
        return numberOfPosition;
    }

    public void setNumberOfPosition(int numberOfPosition) {
        this.numberOfPosition = numberOfPosition;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }


    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
