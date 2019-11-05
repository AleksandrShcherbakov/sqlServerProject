package com.work.sqlServerProject.mapper;

import com.work.sqlServerProject.model.CellInfo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.ui.Model;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by a.shcherbakov on 19.02.2019.
 */
public class CellMapper implements RowMapper<CellInfo> {
    public static final String resourceFolderSql="/sqlQuerries/";

    public static String readSqlStr(String path) {
        String result="000";
        try {
            Class clazz = Model.class;
            InputStream inputStream = clazz.getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder res = new StringBuilder();
            while (reader.ready()){
                res.append(reader.readLine()+" ");
            }
            result=res.toString();
        } catch (FileNotFoundException e) {
            System.out.println("файл sql не найден");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static final String All_2G_SQL;
    public static final String SQL_UMTS_Erisccon;
    public static final String SQL_UMTS_Huawei;
    public static final String SQL_LTE_Ericsson;
    public static final String SQL_LTE_Huawei;

    static {
        All_2G_SQL=readSqlStr(resourceFolderSql+"2G.sql");
        SQL_UMTS_Erisccon=readSqlStr(resourceFolderSql+"3G Ericsson.sql");
        SQL_UMTS_Huawei=readSqlStr(resourceFolderSql+"3G Huawei.sql");
        SQL_LTE_Ericsson=readSqlStr(resourceFolderSql+"4G Ericsson.sql");
        SQL_LTE_Huawei=readSqlStr(resourceFolderSql+"4G Huawei.sql");

    }


    @Override
    public CellInfo mapRow(ResultSet resultSet, int i) throws SQLException {
        String system = resultSet.getString("SYSTEM");
        int syte = resultSet.getInt("SITE");
        int ci = resultSet.getInt("CID");
        float lat = resultSet.getFloat("LAT");
        float lon = resultSet.getFloat("LON");
        String cell = resultSet.getString("CELL");
        int lac = resultSet.getInt("LAC");
        Object tac = resultSet.getObject("TAC");
        int band = resultSet.getInt("BAND");
        Object bsc = resultSet.getObject("BSC");
        int ch = resultSet.getInt("CH");
        Object bsic = resultSet.getObject("BSIC");
        Object scr = resultSet.getObject("SCR");
        Object pci = resultSet.getObject("PCI");
        int dir = resultSet.getInt("DIR");
        int height = resultSet.getInt("HEIGHT");
        int tilt = resultSet.getInt("TILT");
        Object rncid = resultSet.getObject("RNCID");
        String region = resultSet.getString("REGION");
        return new CellInfo(system, syte, ci, lat, lon, cell, lac, tac, band, bsc, ch, bsic, scr, pci, dir, height, tilt, rncid, region);
    }
}
