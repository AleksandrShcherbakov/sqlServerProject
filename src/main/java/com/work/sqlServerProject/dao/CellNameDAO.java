package com.work.sqlServerProject.dao;

import com.work.sqlServerProject.mapper.CellForSZMapper;
import com.work.sqlServerProject.mapper.CellMapper;
import com.work.sqlServerProject.mapper.CellNameMapper;
import com.work.sqlServerProject.model.CellForSZ;
import com.work.sqlServerProject.model.CellInfo;
import com.work.sqlServerProject.model.CellNameInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlVlShcherbakov on 14.02.2019.
 */

@Repository
public class CellNameDAO extends JdbcDaoSupport {

    @Autowired
    public CellNameDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public List<CellInfo> getAll2GCells(){
        String sql = CellMapper.All_2G_SQL;

        Object[]params=new Object[]{};
        CellMapper mapper = new CellMapper();
        List<CellInfo>list=this.getJdbcTemplate().query(sql,params,mapper);
        return list;
    }
    public List<CellInfo> getAll3GECells(){
        String sql = CellMapper.SQL_UMTS_Huawei;

        Object[]params=new Object[]{};
        CellMapper mapper = new CellMapper();
        List<CellInfo>list=this.getJdbcTemplate().query(sql,params,mapper);
        return list;
    }
    public List<CellInfo> getAll3GHCells(){
        String sql = CellMapper.SQL_UMTS_Huawei;

        Object[]params=new Object[]{};
        CellMapper mapper = new CellMapper();
        List<CellInfo>list=this.getJdbcTemplate().query(sql,params,mapper);
        return list;
    }

    public List<CellInfo> getAllLteEricssonCells(){
        String sql = CellMapper.SQL_LTE_Ericsson;
        Object[]params=new Object[]{};
        CellMapper mapper = new CellMapper();
        List<CellInfo>list=this.getJdbcTemplate().query(sql,params,mapper);
        return list;
    }

    public List<CellInfo> getAllLteHuaweiCells(){
        String sql = CellMapper.SQL_LTE_Huawei;
        Object[]params=new Object[]{};
        CellMapper mapper = new CellMapper();
        List<CellInfo>list=this.getJdbcTemplate().query(sql,params,mapper);
        return list;
    }

    public List<CellForSZ> getBSforSZ (int posname){
        String[] partsOfQuerry=CellForSZMapper.SQL_SZ.split("union",2);
        String sql = partsOfQuerry[0]+" and posname= "+posname+" union "+partsOfQuerry[1]+" and a.posname= "+posname+" order by ran, frequency, carriernum, uarfcndl, azimuth;";
        Object[]params=new Object[]{};
        CellForSZMapper mapper = new CellForSZMapper();
        List<CellForSZ>list=this.getJdbcTemplate().query(sql,params,mapper);
        return list;
    }

    public  List<CellInfo> getInfoForBS (int posname){
        String sql2G = CellMapper.All_2G_SQL+" and a.posname="+posname;
        String sql3GE= CellMapper.SQL_UMTS_Erisccon+" and a.posname="+posname;
        String sql3GH= CellMapper.SQL_UMTS_Huawei+" and a.posname="+posname;
        String sql4GE= CellMapper.SQL_LTE_Ericsson+" and a.posname="+posname;
        String sql4GH= CellMapper.SQL_LTE_Huawei+" and a.posname="+posname;
        CellMapper mapper = new CellMapper();
        Object[]params= new Object[]{};
        List<CellInfo>result = new ArrayList<>();
        result.addAll(this.getJdbcTemplate().query(sql2G,params,mapper));
        result.addAll(this.getJdbcTemplate().query(sql3GE,params,mapper));
        result.addAll(this.getJdbcTemplate().query(sql3GH,params,mapper));
        result.addAll(this.getJdbcTemplate().query(sql4GE,params,mapper));
        result.addAll(this.getJdbcTemplate().query(sql4GH,params,mapper));
        return result;
    }

    public CellNameInfo findCellNumber(String cellname){
        String sql = CellNameMapper.BASE_SQL+" where cellname=?";

        Object[] params = new Object[]{cellname};
        CellNameMapper mapper = new CellNameMapper();
        try {
            CellNameInfo cellName = this.getJdbcTemplate().queryForObject(sql,params,mapper);
            System.out.println(cellName.getCellName()+" "+cellName.getCI());
            return cellName;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }
}
