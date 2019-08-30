package com.work.sqlServerProject.Helper;

import com.work.sqlServerProject.controller.MainController;
import com.work.sqlServerProject.controller.SZController;
import com.work.sqlServerProject.model.CellForSZ;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by a.shcherbakov on 25.03.2019.
 */
public class CreateWord {
    public static String createZagolovok(Integer numOfSZ){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String date = sdf.format(new Date());
        return "��������� ������� �"+numOfSZ+" �� "+date;
    }

    public static String createSZname(Integer numOfSz){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
        String date = sdf.format(new Date());
        if (numOfSz<10){
            return "Mos_"+date+"_"+0+numOfSz+".docx";
        }
        return "Mos_"+date+"_"+numOfSz+".docx";
    }

    public static String createDateIspolneniya(){
        int noOfDays = 14; //i.e two weeks
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(date);
    }

    public static  String createVendorDiapazons(List<CellForSZ> list){
        Set<String> set = new TreeSet<>();
        for (CellForSZ c : list){
            set.add(String.valueOf(c.getRan()));
        }
        StringBuilder s = new StringBuilder();
        for (String c : set){
            s.append(c+"/");
        }
        s.replace(s.length()-1,s.length(),";");

        String vendor;
        if (list.get(0).getVendor().equals("E")){
            vendor="Ericsson ";
        }
        else vendor="Huawei ";
        return vendor+s.toString();
    }

    public static String createVvodniyText(List<CellForSZ>list) {
        Set<String> set = new TreeSet<>();
        for (CellForSZ c : list) {
            set.add(c.getRan() + "-" + c.getDiapazon());
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String c : set) {
            stringBuilder.append(c + ", ");
        }
        stringBuilder.replace(stringBuilder.length()-2,stringBuilder.length(),"");
        String diapazonah="���������";
        if (set.size()>1){
            diapazonah="����������";
        }
        String res = "� ���������� ������� ������� �� �� "+list.get(0).getPosname() +
                " ("+list.get(0).getNameAddress()+") �������� ������������ ���������� �������� " +
                "� "+diapazonah+" "+stringBuilder.toString();
        return res;
    }


    public static void createWordFile(List<CellForSZ> list, Integer numOfSZ, String filePath) throws IOException {
        XWPFDocument document = new XWPFDocument();

        //������� ������� ������ �������
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFRun run = paragraph.createRun();
        run.setFontFamily("Times new roman");
        run.setBold(true);
        run.setFontSize(14);
        run.setText("������������");
        run.addCarriageReturn();
        run.setText("������ ������������");
        run.addCarriageReturn();
        run.setText("������� �������");
        run.addCarriageReturn();
        run.setText(MainController.headerExp);
        run.addCarriageReturn();


        //������� ��������� � ������� ��
        XWPFParagraph zagolovok = document.createParagraph();
        zagolovok.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run1 = zagolovok.createRun();
        run1.setFontFamily("Times new roman");
        run1.setBold(true);
        run1.setFontSize(18);
        run1.setText(createZagolovok(numOfSZ));//����������� ����� ��

        //������� ������� � ���������������� ��
        XWPFParagraph characteristiky = document.createParagraph();
        characteristiky.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run2 = characteristiky.createRun();
        run2.setFontFamily("Times new roman");
        run2.setFontSize(14);
        run2.setText("��� �����: �������� ����������� RU;");
        run2.addCarriageReturn();
        run2.setText("���� ����������: "+createDateIspolneniya()+";");
        run2.addCarriageReturn();
        run2.setText("����: "+ createVendorDiapazons(list));
        run2.addCarriageReturn();
        run2.setText("�����������: ���������� ������������� ����������� RU.");
        run2.addCarriageReturn();

        //������� ����� ������� ����� ��� ��
        XWPFParagraph vvodnaya = document.createParagraph();
        vvodnaya.setIndentationFirstLine(1000);
        XWPFRun run3 = vvodnaya.createRun();
        run3.setFontFamily("Times new roman");
        run3.setFontSize(14);
        run3.setText(createVvodniyText(list)+":");
        run3.addCarriageReturn();


        //������� �������
        XWPFTable table = document.createTable();
        table.setRowBandSize(14);
        table.setStyleID("tymes new roman");
        XWPFTableRow tableRowZag = table.getRow(0);
        tableRowZag.setHeight(10);
        tableRowZag.getCell(0).setText("������, ����.");
        tableRowZag.createCell().setText("��������");
        tableRowZag.createCell().setText("������� �������");
        tableRowZag.createCell().setText("������������");
        tableRowZag.createCell().setText("������ �� �������");
        tableRowZag.createCell().setText("������ �� ����");
        for (int i = 0; i < list.size(); i++) {
            XWPFTableRow tableRow = table.createRow();
            tableRow.setHeight(10);
            tableRow.getCell(0).setText(String.valueOf(list.get(i).getAzimuth()));
            tableRow.getCell(1).setText(String.valueOf(list.get(i).getDiapazon()));
            tableRow.getCell(2).setText(list.get(i).getCarryingFrequency()==0 ? "-" : String.valueOf(list.get(i).getCarryingFrequency()));
            tableRow.getCell(3).setText(list.get(i).getName());
            tableRow.getCell(4).setText(String.valueOf(list.get(i).getCIinGeneral()));
            tableRow.getCell(5).setText(String.valueOf(list.get(i).getCIinNetwork()));
        }

        XWPFParagraph obshayaFrazaVKonce = document.createParagraph();
        obshayaFrazaVKonce.setIndentationFirstLine(1000);
        XWPFRun run4 =obshayaFrazaVKonce.createRun();
        run4.setFontFamily("Times new roman");
        run4.setFontSize(14);
        run4.addCarriageReturn();
        run4.setText("����� ��� ���� �������� ��������� �� ������� ��������� �����������, ��������� ������ � ������������ ����������� ����������� � ��������.");


        //�������
        XWPFParagraph podpis = document.createParagraph();
        XWPFRun run5 =podpis.createRun();
        run5.setFontFamily("Times new roman");
        run5.setFontSize(14);
        run5.addCarriageReturn();
        run5.setText("������������ ������ �����������");
        run5.setText("                 "+MainController.headerSOMS);
        run5.addCarriageReturn();
        run5.setText("��������� ����:");
        run5.addCarriageReturn();
        run5.setText("�����������:                                                       ");
        run5.setText(SZController.getExecutor());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath + "/" + createSZname(numOfSZ)));
            document.write(fileOutputStream);
            fileOutputStream.close();
        }
        catch (IOException e){

        }
    }
}
