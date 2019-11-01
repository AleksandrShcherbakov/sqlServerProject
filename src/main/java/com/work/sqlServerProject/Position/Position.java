package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.Helper.NoCarrierException;
import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.Point;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Position {
    private int numberOfPosition;
    private List<Cell>cells;
    private StringBuilder stringBuilder=new StringBuilder();
    private StringBuilder detailResult=new StringBuilder();
    String resDetail="";
    private List<Point>allPointsInPosition;
    private int distance;

    public String printDetailInfo(){
        StringBuilder builder=new StringBuilder();
        String ab = cells.get(0).getAbout();
        for (Cell c : cells){
            String classOfCell = c.getClass().getSimpleName();
            if (!ab.equals(c.getAbout())) {
                builder.append("<br>");
            }
            if (classOfCell.equals("Cell2G")) {
                Cell2G p = (Cell2G) c;
                builder.append(p.toString());
                builder.append("<br>");

            } else if (classOfCell.equals("Cell3G")) {
                Cell3G p = (Cell3G) c;
                builder.append(p.toString());
                builder.append("<br>");

            } else if (classOfCell.equals("Cell4G")) {
                Cell4G p = (Cell4G) c;
                builder.append(p.toString());
                builder.append("<br>");

            }
            ab=c.getAbout();
        }
        return builder.toString();
    }

    public void findBestScan(){
        Set<Integer> techAndDiapazon = cells.stream() //????????? ???????? ? ??????????
                .map(p->p.getNumID())
                .distinct()
                .collect(Collectors.toSet());
        String ab = cells.get(0).getAbout();
        for (Cell c : cells){
            String classOfCell = c.getClass().getSimpleName();
            if (!ab.equals(c.getAbout())) {
                detailResult.append("<br>");
            }
            if (classOfCell.equals("Cell2G")) {
                    Cell2G p = (Cell2G) c;
                    p.putAllrxLevinband();
                    p.checkCell();
                    detailResult.append(p.toString());
                    detailResult.append("<br>");

                } else if (classOfCell.equals("Cell3G")) {
                    Cell3G p = (Cell3G) c;
                    p.putAllRSCPinband();
                    p.checkCell();
                    detailResult.append(p.toString());
                    detailResult.append("<br>");

                } else if (classOfCell.equals("Cell4G")) {
                    Cell4G p = (Cell4G) c;
                    p.putAllRSRPinband();
                    p.checkCell();
                    detailResult.append(p.toString());
                    detailResult.append("<br>");

                }
            ab=c.getAbout();
        }

        for (Integer i : techAndDiapazon){
            Map<Integer,Cell>selfAndBestCI= new HashMap<>();
            List<Cell>cellsOfOneBand=cells.stream().filter(p->p.getNumID()==i).peek(p->selfAndBestCI.put(p.getCi(),p)).collect(Collectors.toList());
            List<Cell>cellsOfFalseWithExistBest=cellsOfOneBand.stream().filter(p->p.isOk()==false && p.getBestCellID()!=0).collect(Collectors.toList());

            for (Cell c : cellsOfOneBand){
                if (c.getBestCellID()!=0 && !c.isOk()){
                    if (selfAndBestCI.get(c.getBestCellID()).getBest1()==selfAndBestCI.get(c.getBestCellID()).getBest2() && selfAndBestCI.get(c.getBestCellID()).getBestCellID()==c.getBestCellID()){
                        if (c.getBest1()!=c.getBest2()){
                            if (c.getBestCellID()==c.getBest1()) {
                                c.setBestCellID(c.getBest2());
                                c.setOk(c.isOk1());
                            }
                            else {
                                c.setBestCellID(c.getBest1());
                                c.setOk(c.isOk2());
                            }
                        }
                    }
                }
            }

            long countOfDifBests=cellsOfFalseWithExistBest.stream().map(p->p.getBestCellID()).distinct().count();
            long countOfTrue=cellsOfOneBand.stream().filter(p->p.isOk()==true).count();
            long countOfExistBest=cellsOfOneBand.stream().filter(p->p.getBestCellID()!=0).count();
            long countOfNoBest=cellsOfOneBand.size()-countOfExistBest;

            Set<Integer>unicBest=new HashSet<>();
            long countOfFindedBests=cellsOfOneBand.stream().filter(p->p.getBestCellID()!=0).peek(p->unicBest.add(p.getBestCellID())).count();
            long countOfUniques=unicBest.size();

            System.out.println(i+" "+countOfExistBest);
            String res=null;
            res="<a href='/map?about=" + cellsOfOneBand.get(0).getAbout() + "&posName="+cellsOfOneBand.get(0).getPosname()+"' target=\"_blank\">" + cellsOfOneBand.get(0).getAbout() + "</a>";
            if (cellsOfOneBand.get(0).getAbout().endsWith(" 0")){
                StringBuilder cells = new StringBuilder();
                cells.append(cellsOfOneBand.get(0).getSystem()+" c CI: ");
                for (Cell c : cellsOfOneBand){
                    cells.append(c.getCi()+", ");
                }
                cells.delete(cells.lastIndexOf(", "),cells.length());
                String oneOrMany1, oneOrMany2="";
                if (cellsOfOneBand.size()==1){
                    oneOrMany1="сектора";
                    oneOrMany2="сектор";
                }
                else {
                    oneOrMany1="секторов";
                    oneOrMany2="сектора";
                }
                res="Для "+oneOrMany1+" "+cells+" частота в БД не указана. Вероятнее всего, "+oneOrMany2+" не в эфире.";
                stringBuilder.append("<span style='color:red'>"+res+"</span>");
                stringBuilder.append("<br>");
            }
            else
            if (countOfExistBest==0){
                stringBuilder.append(res+" - <span style='color:orange'>или сектора не в эфире, или Ch, SCR, PCI на сети не соответствуют БД General. Либо станцию не объехали.</span>");
                stringBuilder.append("<br>");
            }
            else
            if (cellsOfOneBand.size()==1){
                stringBuilder.append(res+" - <span style='color:#FF4500'>Один сектор. Проверьте визуально.</span>");
                stringBuilder.append("<br>");
            }
            else
            if (countOfUniques<countOfFindedBests && countOfTrue<cellsOfOneBand.size()-1){
                stringBuilder.append(res+" - <span style='color: #31372b'>имеется неоднозначность.</span>");
                stringBuilder.append("<br>");
            }
            else
            if (cellsOfOneBand.size()==2 && ((countOfTrue==1)||unicBest.size()==1)){
                stringBuilder.append(res+" - <span style='color: #31372b'>имеется неоднозначность.</span>");
                stringBuilder.append("<br>");
            }
            else
            if (countOfTrue>=cellsOfOneBand.size()-1){
                stringBuilder.append(res+" - <span style='color:green'>Ok</span>");
                stringBuilder.append("<br>");
            }
            else
            if ((countOfNoBest>=2 || (cellsOfFalseWithExistBest.size()>=2 &&countOfDifBests<cellsOfFalseWithExistBest.size())) || (cellsOfOneBand.size()==3 && cellsOfOneBand.size()-countOfExistBest==1 && countOfTrue==1)){
                stringBuilder.append(res+" - <span style='color:blue'>данных недостаточно.</span>");
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
        switch (cellInfo.get(0).getRegion()){
            case "Mck: Center":
                this.distance=500;
                break;
            case "Mck: M":
                this.distance=700;
                break;
            case "Mck: M+":
                this.distance=1000;
                break;
            case "Mck: M++":
                this.distance=1500;
                break;
            default:
                this.distance=1200;
        }

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
        cells.sort(new Comparator<Cell>()  {
            @Override
            public int compare(Cell o1, Cell o2) throws NoCarrierException {
                    try{
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
                    catch (NullPointerException e){
                        throw new NoCarrierException(o1);

                    }
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
                p.getDistToPos().put(this.numberOfPosition, dist);
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

    public String getInfoForTemplate(){
        return this.numberOfPosition+"___"+ stringBuilder.toString()+"___"+detailResult.toString();
    }


    @Override
    public String toString() {
        return stringBuilder.toString()+"<br>"/*+detailResult.toString()*/;
    }
}
