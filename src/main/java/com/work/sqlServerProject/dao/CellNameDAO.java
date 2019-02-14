package com.work.sqlServerProject.dao;

import com.work.sqlServerProject.mapper.CellNameMapper;
import com.work.sqlServerProject.model.CellNameInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by AlVlShcherbakov on 14.02.2019.
 */

@Repository
public class CellNameDAO extends JdbcDaoSupport {

    @Autowired
    public CellNameDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public CellNameInfo findCellNumber(){
        String sql = CellNameMapper.BASE_SQL+" where cellname=3313000";

        CellNameMapper mapper = new CellNameMapper();
        try {
            CellNameInfo cellName = this.getJdbcTemplate().queryForObject(sql,mapper);
            System.out.println(cellName.getCellName()+" "+cellName.getCI());
            return cellName;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }
}
