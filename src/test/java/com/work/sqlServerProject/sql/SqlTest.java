package com.work.sqlServerProject.sql;

import com.work.sqlServerProject.mapper.CellMapper;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;

/**
 * Created by Александр on 02.11.2019.
 */
@SpringBootTest
public class SqlTest {
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
}
