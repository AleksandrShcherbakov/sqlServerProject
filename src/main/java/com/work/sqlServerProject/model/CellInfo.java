package com.work.sqlServerProject.model;

/**
 * Created by a.shcherbakov on 19.02.2019.
 */
public class CellInfo {
    private String system;
    private int syte;
    private int ci;
    private float lat;
    private float lon;
    private String cell;
    private int lac;
    private Object tac;
    private int band;
    private Object bsc;
    private int ch;
    private Object bsic;
    private Object scr;
    private Object pci;
    private int dir;
    private int height;
    private int tilt;
    private Object rncid;
    private String region;

    public CellInfo() {
    }

    public CellInfo(String system, int syte, int ci, float lat, float lon, String cell, int lac, Object tac, int band, Object bsc, int ch, Object bsic, Object scr, Object pci, int dir, int height, int tilt, Object rncid, String region) {
        this.system = system;
        this.syte = syte;
        this.ci = ci;
        this.lat = lat;
        this.lon = lon;
        this.cell = cell;
        this.lac = lac;
        this.tac = tac;
        this.band = band;
        this.bsc = bsc;
        this.ch = ch;
        if (bsic!=null){
            Integer i=Integer.parseInt(bsic+"",8);
            this.bsic=i.toString();
        }
        else this.bsic=null;
        this.scr = scr;
        this.pci = pci;
        this.dir = dir;
        this.height = height;
        this.tilt = tilt;
        this.rncid = rncid;
        this.region = region;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public int getSyte() {
        return syte;
    }

    public void setSyte(int syte) {
        this.syte = syte;
    }

    public int getCi() {
        return ci;
    }

    public void setCi(int ci) {
        this.ci = ci;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public Object getTac() {
        return tac;
    }

    public void setTac(Object tac) {
        this.tac = tac;
    }

    public int getBand() {
        return band;
    }

    public void setBand(int band) {
        this.band = band;
    }

    public Object getBsc() {
        return bsc;
    }

    public void setBsc(Object bsc) {
        this.bsc = bsc;
    }

    public int getCh() {
        return ch;
    }

    public void setCh(int ch) {
        this.ch = ch;
    }

    public Object getBsic() {
        return bsic;
    }

    public void setBsic(Object bsic) {
        this.bsic = bsic;
    }

    public Object getScr() {
        return scr;
    }

    public void setScr(Object scr) {
        this.scr = scr;
    }

    public Object getPci() {
        return pci;
    }

    public void setPci(Object pci) {
        this.pci = pci;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTilt() {
        return tilt;
    }

    public void setTilt(int tilt) {
        this.tilt = tilt;
    }

    public Object getRncid() {
        return rncid;
    }

    public void setRncid(Object rncid) {
        this.rncid = rncid;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {

        String resultString = system + ';' +
                syte +';'+
                ci +';'+
                lat +';' +
                lon +';' +
                cell + '\'' +';' +
                lac +';' +
                tac +';' +
                band +';' +
                bsc +';' +
                ch +';' +
                bsic +';' +
                scr +';' +
                pci +';' +
                dir +';' +
                height +';' +
                tilt +';' +
                rncid +';' +
                region;
        resultString = resultString.replaceAll("null","");
        return resultString;
    }
}
