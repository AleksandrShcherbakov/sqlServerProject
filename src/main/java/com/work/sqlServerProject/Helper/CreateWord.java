package com.work.sqlServerProject.Helper;

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
        return "Служебная записка №"+numOfSZ+" от "+date+".";
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
            set.add(String.valueOf(c.getDiapazon()));
        }
        StringBuilder s = new StringBuilder();
        for (String c : set){
            s.append(c+", ");
        }
        s.append(".");
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
            set.add(c.getDiapazon() + " " + c.getCarryingFrequency());
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String c : set) {
            stringBuilder.append(c + "/");
        }
        stringBuilder.replace(stringBuilder.indexOf("/"), stringBuilder.capacity(), ".");
        String res = "В результате анализа измерений на БС "+list.get(0).getPosname() +
                " ("+list.get(0).getAddress()+") выявлена неправильная ориентация секторов " +
                "в диапазоне "+stringBuilder.toString();
        return res;
    }

   /* public static void fillInTable(XWPFTable table, List<CellForSZ>list) {
        XWPFTableRow tableRowZag = table.createRow();
        tableRowZag.getCell(0).setText("Азимут, град.");
        tableRowZag.createCell().setText("Диапазон");
        tableRowZag.createCell().setText("Несущая частота");
        tableRowZag.createCell().setText("Наименование");
        tableRowZag.createCell().setText("Сектор по проекту");
        tableRowZag.createCell().setText("Сектор на сети");
        for (int i = 0; i < list.size(); i++) {
            XWPFTableRow tableRow = table.createRow();
            tableRow.getCell(0).setText(String.valueOf(list.get(i).getAzimuth()));
            tableRow.getCell(1).setText(String.valueOf(list.get(i).getDiapazon()));
            tableRow.getCell(2).setText(String.valueOf(list.get(i).getCarryingFrequency()));
            tableRow.getCell(3).setText(list.get(i).getName());
            tableRow.getCell(4).setText(String.valueOf(list.get(i).getCIinGeneral()));
            tableRow.getCell(4).setText(String.valueOf(list.get(i).getCIinNetwork()));
        }
    }*/

    public static void createWordFile(List<CellForSZ> list, Integer numOfSZ, String filePath) throws IOException {
        XWPFDocument document = new XWPFDocument();

        //создаем верхнюю правую подпись
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFRun run = paragraph.createRun();
        run.setFontFamily("Times new roman");
        run.setBold(true);
        run.setFontSize(16);
        run.setText("Привет мир");
        run.addCarriageReturn();
        run.setText("hello world");
        run.addCarriageReturn();
        run.setText("hello world");
        run.addCarriageReturn();
        run.setText("hello world");
        run.addCarriageReturn();


        //создаем заголовок с номером СЗ
        XWPFParagraph zagolovok = document.createParagraph();
        zagolovok.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run1 = zagolovok.createRun();
        run1.setFontFamily("Times new roman");
        run1.setBold(true);
        run1.setFontSize(20);
        run1.setText(createZagolovok(numOfSZ));//указывается номер СЗ

        //создаем подпись с характиристиками СЗ
        XWPFParagraph characteristiky = document.createParagraph();
        characteristiky.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run2 = characteristiky.createRun();
        run2.setFontFamily("Times new roman");
        run2.setFontSize(16);
        run2.setText("Дата исполнения: "+createDateIspolneniya());
        run2.addCarriageReturn();
        run2.setText("Вендор: "+ createVendorDiapazons(list));

        //создаем общую вводную фразу для СЗ
        XWPFParagraph vvodnaya = document.createParagraph();
        vvodnaya.setIndentationFirstLine(1000);
        XWPFRun run3 = vvodnaya.createRun();
        run3.setFontFamily("Times new roman");
        run3.setFontSize(16);
        run3.setText(createVvodniyText(list));
        run3.addCarriageReturn();

        //Создаем таблицу
        XWPFTable table = document.createTable();
        //fillInTable(table,list);

        XWPFParagraph obshayaFrazaVKonce = document.createParagraph();
        obshayaFrazaVKonce.setIndentationFirstLine(1000);
        XWPFRun run4 =obshayaFrazaVKonce.createRun();
        run4.setFontFamily("Times new roman");
        run4.setFontSize(16);
        run4.addCarriageReturn();
        run4.setText("О результатах сообщить в отдел");

        //подпись
        XWPFParagraph podpis = document.createParagraph();
        podpis.setAlignment(ParagraphAlignment.DISTRIBUTE);
        XWPFRun run5 =podpis.createRun();
        run5.setFontFamily("Times new roman");
        run5.setFontSize(16);
        run5.addCarriageReturn();
        run5.setText("исполнитель утвердитель");
        run5.addCarriageReturn();
        run5.setText("я он");




        FileOutputStream fileOutputStream = new FileOutputStream(new File("C://123.docx"));

        document.write(fileOutputStream);

        fileOutputStream.close();
    }
}
