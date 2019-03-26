package com.work.sqlServerProject.mapper;

import com.work.sqlServerProject.model.CellForSZ;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by a.shcherbakov on 07.03.2019.
 */
public class CellForSZMapper implements RowMapper<CellForSZ> {
    public static final String SQL_SZ = "select posname," +
            " ran, " +
            "vendor," +
            " azimuth," +
            " frequency," +
            " carriernum," +
            " name," +
            " address," +
            " ci," +
            " geo_zone," +
            " uarfcnDL" +
            " from pmc.bs_general_table_cr as a left outer join" +
            " (SELECT distinct cellname, uarfcnDL from pmc.cellstatus where ran= '3G') as b on a.cellname=b.cellname "+
            " where a.ant_location = 'outdoor'";

    @Override
    public CellForSZ mapRow(ResultSet resultSet, int i) throws SQLException {
        int posname = resultSet.getInt("posname");
        String ran = resultSet.getString("ran");
        String vendor = resultSet.getString("vendor");
        int azimuth = resultSet.getInt("azimuth");
        int diapazon = resultSet.getInt("frequency");
        int carriernum = resultSet.getInt("carriernum");
        String name = resultSet.getString("name");
        String address = resultSet.getString("address");
        int ci = resultSet.getInt("ci");
        String geozone = resultSet.getString("geo_zone");
        int dlCh = resultSet.getInt("uarfcnDL");

        return new CellForSZ(posname, ran, vendor, azimuth, diapazon, carriernum, name, address, ci, geozone, dlCh);
    }
}

