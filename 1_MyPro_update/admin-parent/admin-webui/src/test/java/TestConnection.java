//import crowd.entity.Admin;
//import org.fall.mapper.AdminMapper;
//
//import org.fall.util.CrowdUtil;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//
///**
// * @author Zhaoy
// * @creat 2022-03-25-20:30
// */
//
///**
// * RunWith与ContextConfiguration指定xml的作用与
// * ApplicationContext context =
// *          new ClassPathXmlApplicationContext("spring-persist-mybatis.xml");
// * 类似
// * 前者通过让测试在Spring容器环境下执行，使得DataSource可以被自动注入，后者导入Spring配置文件
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml"})
//public class TestConnection {
//    @Autowired
//    DataSource dataSource;
//    @Test
//    public void test01() throws SQLException {
//        //ApplicationContext context = new ClassPathXmlApplicationContext("spring-persist-mybatis.xml");
//        //DataSource dataSource = context.getBean(DataSource.class);
//        Connection connection = dataSource.getConnection();
//        System.out.println(connection);
//    }
//
//    @Autowired
//    private AdminMapper adminMapper;
//    @Test
//    public void test02(){
//        Admin admin = new Admin(null, "zhaoy", "123456", "Jhon", "Jhon@qq.com", null);
//        int count = adminMapper.insert(admin);
////        System.out.println(count);
////        System.out.println(admin);
////        Admin admin = adminMapper.selectByPrimaryKey(5);
////        System.out.println(admin);
//    }
//
//    @Test
//    public void test03(){
//        //获取Logger对象，这里传入的Class就是当前打印日志的类
//        Logger logger = LoggerFactory.getLogger(TestConnection.class);
//        //等级 DEBUG < INFO < WARN < ERROR
//        logger.debug("I am DEBUG!!!");
//
//        logger.info("I am INFO!!!");
//
//        logger.warn("I am WARN!!!");
//
//        logger.error("I am ERROR!!!");
//
//    }
//
//    @Test
//    public void test04(){
//        Admin admin = new Admin(3, "zy0710", CrowdUtil.md5("123456"), "zhaoyang", "hdu@edu.com", null);
//        int count = adminMapper.insert(admin);
//    }
//
//}
