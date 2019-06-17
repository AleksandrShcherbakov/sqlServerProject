package com.work.sqlServerProject.Position;

import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by a.shcherbakov on 29.05.2019.
 */
public class Cell2G extends Cell {
    private int BCCH;
    private Object BSIC;
    private Map<String,Double> rxLevperBCCH;

    public Cell2G(CellInfo cellInfo) {
        super(cellInfo);
        this.BCCH=cellInfo.getCh();
        this.BSIC=cellInfo.getBsic();

    }

    public void findBestBCCH(){

    }

    private Double getAverRxLev(String bcchBsic, int band){//BCCH+" "+Bsic
        Double temp=null;
        double common=0;
        int count=0;
        Map<String, Double> map=null;

        for (Point p : pointsInCell){
            if (band==900){
                map=p.getRxLevel900();
            }
            else
            if (band==1800){
                map=p.getRxLevel1800();
            }
            temp=map.get(bcchBsic);
            if (temp!=null){
                common=common+temp;
                count++;
            }
        }
        double res = common/count;
        return res;
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
