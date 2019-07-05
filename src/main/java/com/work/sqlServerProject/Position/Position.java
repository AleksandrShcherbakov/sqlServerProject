package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Position {
    private int numberOfPosition;
    private List<Cell>cells;
    private StringBuilder stringBuilder=new StringBuilder();
    private List<Point>allPointsInPosition;
    private int distance;

    public void findBestScan(){
        Set<Short> techAndDiapazon = cells.stream() //имеющиеся диапаоны и технологии
                .map(p->p.getNumID())
                .distinct()
                .collect(Collectors.toSet());

        for (Cell c : cells){
            String classOfCell = c.getClass().getSimpleName();
            if (classOfCell.equals("Cell2G")){
                Cell2G p  = (Cell2G) c;
                p.putAllrxLevinband();
                p.checkCell();
                stringBuilder.append(p.toString());
                stringBuilder.append("<br>");
            }
            else
            if (classOfCell.equals("Cell3G")){
                Cell3G p  = (Cell3G) c;
                p.putAllRSCPinband();
                p.checkCell();
                stringBuilder.append(p.toString());
                stringBuilder.append("<br>");
            }
            else
            if (classOfCell.equals("Cell4G")){
                Cell4G p  = (Cell4G) c;
                p.putAllRSRPinband();
                p.checkCell();
                stringBuilder.append(p.toString());
                stringBuilder.append("<br>");
            }
        }
        for (Short i : techAndDiapazon){
            List<Cell>cellsOfOneBand=cells.stream().filter(p->p.getNumID()==i).collect(Collectors.toList());
            List<Cell>cellsOfFalseWithExistBest=cellsOfOneBand.stream().filter(p->p.isOk()==false && p.getBestCellID()!=0).collect(Collectors.toList());
            long countOfDifBests=cellsOfFalseWithExistBest.stream().map(p->p.getBestCellID()).distinct().count();
            long countOfTrue=cellsOfOneBand.stream().filter(p->p.isOk()==true).count();
            long countOfExistBest=cellsOfOneBand.stream().filter(p->p.getBestCellID()!=0).count();
            long countOfNoBest=cellsOfOneBand.size()-countOfExistBest;
            System.out.println(i+" "+countOfExistBest);
            String res=null;
            res=cellsOfOneBand.get(0).getAbout();
            if (countOfExistBest==0){
                stringBuilder.append(res+" - <span style='color:orange'>скорее всего сектора не в эфире, либо Ch, SCR, PCI на сети не соответствует General, либо чтото еще.</span>");
                stringBuilder.append("<br>");
            }
            else
            if (countOfTrue>=cellsOfOneBand.size()-1){
                stringBuilder.append(res+" - <span style='color:green'>сектора сориентированы правильно.</span>");
                stringBuilder.append("<br>");
            }
            else
            if (countOfNoBest>=2 || (cellsOfFalseWithExistBest.size()>=2 &&countOfDifBests<cellsOfFalseWithExistBest.size())){
                stringBuilder.append(res+" - <span style='color:blue'>данных для определения правильности ориентации секторов недостаточно.</span>");
                stringBuilder.append("<br>");
            }
            else
            {
                stringBuilder.append(res+" - <span style='color:red'>сектора перепутаны.</span>");
                stringBuilder.append("<br>");
            }
        }
    }

    public Position(List<CellInfo>cellInfo) {
        cells=new ArrayList<>();
        this.numberOfPosition=cellInfo.get(0).getSyte();
        this.distance=1200;      // 1200м
        for (CellInfo cell : cellInfo) {
            if (cell.toString().startsWith("UMTS")) {
                cells.add(new Cell3G(cell, distance));
            }

            if (cell.toString().startsWith("GSM")){
                cells.add(new Cell2G(cell, distance));
            }

            if (cell.toString().startsWith("LTE")){
                cells.add(new Cell4G(cell, distance));
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
        allPointsInPosition=new ArrayList<>();
        double lonPos=cells.get(0).getLongitude();
        double latPos=cells.get(0).getLalitude();
        for (Point p : allPoints){
            double dist = HelperCell.toDist(latPos,lonPos,p.getLatitude(),p.getLongitude());
            if (dist<distance){
                allPointsInPosition.add(p);
            }
        }
    }



    public void setAllPointsToCells(){
        for (Cell c : cells){
            c.setPointsInCell(allPointsInPosition, c.getPointsInCell(), c.getLeftBorderAzimuth(),c.getRightBorderAzimuth());
        }
    }

    public List<Point> getAllPointsInPosition() {
        return allPointsInPosition;
    }

    public void setAllPointsInPosition(List<Point> allPointsInPosition) {
        this.allPointsInPosition = allPointsInPosition;
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
