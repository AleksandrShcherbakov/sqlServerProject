package com.work.sqlServerProject.mapper;

import com.work.sqlServerProject.model.CellForSZ;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by a.shcherbakov on 07.03.2019.
 */
public class CellForSZMapper implements RowMapper<CellForSZ> {
    public static final String SQL_SZ = "select posname,\n" +
            "a.ran,\n" +
            "            a.vendor,\n" +
            "            azimuth,\n" +
            "            frequency,\n" +
            "            carriernum,\n" +
            "            name,\n" +
            "            address,\n" +
            "            a.ci,\n" +
            "            geo_zone,\n" +
            "            b.uarfcnDL \n" +
            "            from pmc.bs_general_table_cr as a left join (select a.cellname, uarfcndl from pmc.cellstatus as a  join (\n" +
            "            select max(date) as date, cellname from pmc.cellstatus group by cellname\n" +
            "            ) as b on a.cellname=b.cellname and a.date=b.date \n" +
            "            \n" +
            "union\n" +
            "select\n" +
            "case when isnumeric(cellname)=1 then concat(1,cellname) else 0 end as cellname, dlearfcn from pmc.EXPORT_HU_CELL_BTS3900\n" +
            "union\n" +
            "select\n" +
            "case when isnumeric(cellname)=1 then concat(1,cellname) else 0 end as cellname,dlearfcn from pmc.EXPORT_HU_CELL_BTS5900\n" +
            "union\n" +
            "select\n" +
            "case when isnumeric(cellname)=1 then concat(1,cellname) else 0 end as cellname, dlearfcn from pmc.EXPORT_HU_eNodeBCell_eNodeB\n" +
            "            )as b on a.cellname=b.cellname\n" +
            "            where a.ant_location = 'outdoor'\n" +
            "            and (a.duplexmode is null or a.duplexmode!='nb-iot') ";



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

