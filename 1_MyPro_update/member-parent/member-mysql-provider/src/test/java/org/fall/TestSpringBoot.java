package org.fall;


import org.fall.service.api.MemberService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zhaoy
 * @date 2022/8/11 - 19:26
 */

@SpringBootTest
public class TestSpringBoot {
    @Autowired
    DataSource dataSource;
    @Autowired
    MemberService memberPOService;
    private Logger logger = LoggerFactory.getLogger(TestSpringBoot.class);

    @Test
    public void test_Connection() throws SQLException {
        Connection connection = dataSource.getConnection();
    }
}
