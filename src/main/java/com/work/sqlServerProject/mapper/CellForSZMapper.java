package com.work.sqlServerProject.mapper;

import com.work.sqlServerProject.model.CellForSZ;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by a.shcherbakov on 07.03.2019.
 */
public class CellForSZMapper implements RowMapper<CellForSZ> {
    public static final String SQL_SZ =
            "select \n" +
            "posname,\n" +
            "ran,\n" +
            "vendor,\n" +
            "azimuth,\n" +
            "frequency,\n" +
            "carriernum,\n" +
            "      name,\n" +
            "                  address,\n" +
            "                ci,\n" +
            "                     geo_zone,\n" +
            "                  uarfcndl= null\n" +
            "                   from pmc.bs_general_table_cr where ran='2G'\n" +

            "                   union\n" +
            "\n" +
            "\n" +
            "select posname,\n" +
            "a.ran,\n" +
            "a.vendor,\n" +
            "azimuth,\n" +
            "frequency,\n" +
            "carriernum,\n" +
            "      name,\n" +
            "                  address,\n" +
            "                a.ci,\n" +
            "                     geo_zone,\n" +
            "                  case when a.ran = '2G' then null else b.uarfcnDL end as uarfcndl\n" +
            "                   from pmc.bs_general_table_cr as a left join (\n" +
            "                    \n" +
            "            select cellid, uarfcndownlink as uarfcndl, 'H'as vendor\n" +
            "            from pmc.EXPORT_HU_UCELL_BSC6900GU\n" +
            "            union\n" +
            "            select cellid, uarfcndownlink, 'H'as vendor\n" +
            "            from pmc.EXPORT_HU_UCELL_BSC6910UMTS\n" +
            "            union\n" +
            "            select utrancellid, utrandlarfcn,'H'as vendor\n" +
            "            from pmc.EXPORT_HU_eNodeBUtranExternalCell_eNodeB\n" +
            "            union\n" +
            "            select cellid, utrandlarfcn,'H'as vendor\n" +
            "            from pmc.EXPORT_HU_UTRANEXTERNALCELL_BTS5900\n" +
            "            union\n" +
            "            select cellid, utrandlarfcn,'H'as vendor\n" +
            "            from pmc.EXPORT_HU_UTRANEXTERNALCELL_BTS3900\n" +
            "            union\n" +
            "            select cellid, utrandlarfcn,'H'as vendor\n" +
            "            from pmc.EXPORT_HU_UTRANEXTERNALCELL_MICROBTS3900\n" +
            "            union\n" +
            "            select\n" +
            "            case when isnumeric(cellname)=1 then cellname else 0 end as cellname, dlearfcn,'H'as vendor\n" +
            "            from pmc.EXPORT_HU_CELL_BTS3900\n" +
            "            union\n" +
            "            select\n" +
            "            case when isnumeric(cellname)=1 then cellname else 0 end as cellname,dlearfcn,'H'as vendor \n" +
            "            from pmc.EXPORT_HU_CELL_BTS5900\n" +
            "            union\n" +
            "            select\n" +
            "            case when isnumeric(cellname)=1 then cellname else 0 end as cellname, dlearfcn,'H'as vendor \n" +
            "            from pmc.EXPORT_HU_eNodeBCell_eNodeB\n" +
            "            union\n" +
            "            select eutrancellfddid, earfcndl,'E'as vendor\n" +
            "            from pmc.LTE_data_Ericsson\n" +
            "            where isnumeric(azimuth)=1\n" +
            "            union\n" +
            "            select cid, uarfcndl,'E'as vendor \n" +
            "            from pmc.umts_data_Ericsson\n" +
            "            \n" +
            "            ) as b on a.ci=b.cellid and a.vendor =b.vendor\n" +
            "                        where a.ant_location = 'outdoor'           \n" +
            "                        and (a.duplexmode is null or a.duplexmode!='nb-iot')\n" +
            "                        and (a.ran='3G' or a.ran='4G')\n" +
            "                         ";



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

