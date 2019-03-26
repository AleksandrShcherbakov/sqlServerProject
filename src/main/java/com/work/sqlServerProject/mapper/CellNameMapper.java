package com.work.sqlServerProject.mapper;

import com.work.sqlServerProject.model.CellNameInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by AlVlShcherbakov on 14.02.2019.
 */
public class CellNameMapper implements RowMapper<CellNameInfo> {
    public static final String BASE_SQL = "select * from pmc.bs_general_table_cr";


    @Override
    public CellNameInfo mapRow(ResultSet resultSet, int i) throws SQLException {
        int cellName = resultSet.getInt("cellName");
        int CI = resultSet.getInt("CI");

        return new CellNameInfo(cellName,CI);
    }
}
