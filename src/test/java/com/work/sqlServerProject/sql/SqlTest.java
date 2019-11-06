package com.work.sqlServerProject.sql;

import com.work.sqlServerProject.dao.CellNameDAO;
import com.work.sqlServerProject.mapper.CellMapper;
import com.work.sqlServerProject.model.CellForSZ;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Александр on 02.11.2019.
 */
@SpringBootTest
public class SqlTest {
    @Autowired
    CellNameDAO cellNameDAO;

    @Test
    public void testSql() throws URISyntaxException {
        String res = CellMapper.readSqlStr("/sqlQuerries/2G.sql");
        System.out.println(res);
    }

    @Test
    public void testCellInit(){
        System.out.println(CellMapper.All_2G_SQL);
        System.out.println(CellMapper.SQL_LTE_Ericsson);
    }

    @Test
    public void getBsForSZ(){
        List<CellForSZ> list=cellNameDAO.getBSforSZ(16530);
        System.out.println(Arrays.asList(list));

    }
}
