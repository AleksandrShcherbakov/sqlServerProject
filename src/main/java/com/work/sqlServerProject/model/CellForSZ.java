package com.work.sqlServerProject.model;

import java.util.*;

/**
 * Created by a.shcherbakov on 06.03.2019.
 */
public class CellForSZ {
    private int posname; //posname
    private String ran; //RAN
    private String vendor; //vendor
    private int azimuth; //azimuth
    private int diapazon; //frequency
    private int carryingFrequency; //указываем в конструкторе
    private int carrierNumber; //CerrierNum
    private String name; //name
    private String nameAddress;
    private String address; //address
    private int CIinGeneral; //CI
    private int CIinNetwork; //в конструкторе указываем default
    private String geozone; //geo_Zone
    private String checkingStr;
    private boolean check;
    private int dlCh; //uarfcnDL

    public List<Integer> getCells() {
        return cells;
    }

    public void setCells(List<Integer> cells) {
        this.cells = cells;
    }

    private List <Integer> cells;

    public CellForSZ() {
    }

    public CellForSZ(int posname, String ran, String vendor, int azimuth, int diapazon, int carrierNumber, String name, String address, int CIinGeneral, String geozone, int dlCh) {
        this.posname = posname;
        this.ran = ran;
        this.vendor = vendor;
        this.azimuth = azimuth;
        this.diapazon = diapazon;
        this.carrierNumber = carrierNumber;
        this.name = name.substring(name.lastIndexOf("_")+1,name.length());
        this.nameAddress= name.substring(0, name.indexOf("_"));
        this.address = address;
        this.CIinGeneral = CIinGeneral;
        this.geozone = geozone;

        if (!ran.equals("2G")) {
            this.carryingFrequency = dlCh;
        }
        else
        if (ran.equals("2G")){
            this.carryingFrequency = 0;
        }
        this.CIinNetwork=0;
        this.checkingStr=ran+" "+diapazon+" "+carryingFrequency;
        this.checkingStr.intern();
        this.check=false;
        this.cells=new ArrayList<>();
    }

    public void checking(String[] spisok){
        for (String s : spisok){
            if (checkingStr.equals(s)){
                check=true;
                break;
            }
        }
    }

    public void createSetCells(List<CellForSZ> list){
        List<Integer> set = new ArrayList<>();
        for (CellForSZ s : list){
            if (this.checkingStr.equals(s.checkingStr)){
                if (this.CIinGeneral==s.CIinGeneral){
                    set.add(0,s.CIinGeneral);
                }
                if (set.contains(s.CIinGeneral))
                    continue;
                set.add(s.CIinGeneral);
            }
        }
        this.cells=set;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getPosname() {
        return posname;
    }

    public void setPosname(int posname) {
        this.posname = posname;
    }

    public String getRan() {
        return ran;
    }

    public void setRan(String ran) {
        this.ran = ran;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public int getCarryingFrequency() {
        return carryingFrequency;
    }

    public void setCarryingFrequency(int carryingFrequency) {
        this.carryingFrequency = carryingFrequency;
    }

    public int getCarrierNumber() {
        return carrierNumber;
    }

    public void setCarrierNumber(int carrierNumber) {
        this.carrierNumber = carrierNumber;
    }

    public String getGeozone() {
        return geozone;
    }

    public void setGeozone(String geozone) {
        this.geozone = geozone;
    }

    public int getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(int azimuth) {
        this.azimuth = azimuth;
    }

    public int getDiapazon() {
        return diapazon;
    }

    public void setDiapazon(int diapazon) {
        this.diapazon = diapazon;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCIinGeneral() {
        return CIinGeneral;
    }

    public void setCIinGeneral(int CIinGeneral) {
        this.CIinGeneral = CIinGeneral;
    }

    public int getCIinNetwork() {
        return CIinNetwork;
    }

    public void setCIinNetwork(int CIinNetwork) {
        this.CIinNetwork = CIinNetwork;
    }



    public void setCells(ArrayList<Integer> cells) {
        this.cells = cells;
    }

    public String getNameAddress() {
        return nameAddress;
    }

    public void setNameAddress(String nameAddress) {
        this.nameAddress = nameAddress;
    }

    public String getCheckingStr() {
        return checkingStr;
    }

    public void setCheckingStr(String checkingStr) {
        this.checkingStr = checkingStr;
    }

    public int getDlCh() {
        return dlCh;
    }

    public void setDlCh(int dlCh) {
        this.dlCh = dlCh;
    }

    @Override
    public String toString() {
        return azimuth+" "+diapazon+" "+carryingFrequency+" "+ name+" "+CIinGeneral+" "+CIinNetwork;
    }
}
