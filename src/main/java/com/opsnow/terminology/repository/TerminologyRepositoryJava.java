package com.opsnow.terminology.repository;

import com.opsnow.terminology.model.Terminology;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;

@Component
public class TerminologyRepositoryJava {
    private Connection conn = null;
//    private static String URL = "jdbc:mariadb://seoul-dev-lightsaver-alarm-db01.czmesa8znr6d.ap-northeast-2.rds.amazonaws.com:3306/db";
//    private static String USERNAME = "admin";
//    private static String PASSWORD = "qptmvlswltjd!";

    @Value("${spring.datasource.driverClassName}")
    private String DRIVERCLASSNAME;
    @Value("${spring.datasource.url}")
    private String URL;
    @Value("${spring.datasource.username}")
    private String USERNAME;
    @Value("${spring.datasource.password}")
    private String PASSWORD;

//    @Inject
//    private DBConfig dbConfig;

//    @Autowired
//    private DBConfig dbConfig;
//
//    private String DRIVERCLASSNAME = dbConfig.getDriverClassName();
//    private String URL = dbConfig.getUrl();
//    private String USERNAME = dbConfig.getUsername();
//    private String PASSWORD = dbConfig.getPassword();

    public Connection getConn() {
        try {
            System.out.println("db드라이버?????======" + DRIVERCLASSNAME);
            Class.forName(DRIVERCLASSNAME);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    public void deleteAll(String table){
        PreparedStatement pstmt = null;
        String sql = "TRUNCATE TABLE 000_temp_dictionary";
        System.out.println("table명 : " + table);
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int insertAll(List<Terminology> list){
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO 000_temp_dictionary(MSG_ID, CONTS_TYPE_CD, LANG_MSG_ID_NO, MSG_SBST_ENG, MSG_SBST_KOR, MSG_SBST_CHN, MSG_SBST_JPN, ANG_CODE, FM_CODE) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";

        try{
            for(Terminology vo : list){
                pstmt = conn.prepareStatement(sql);

                pstmt.setString(1,vo.getMSG_ID());
                pstmt.setString(2,vo.getCONTS_TYPE_CD());
                pstmt.setInt(3,vo.getLANG_MSG_ID_NO());
                pstmt.setString(4,vo.getMSG_SBST_ENG());
                pstmt.setString(5,vo.getMSG_SBST_KOR());
                pstmt.setString(6,vo.getMSG_SBST_CHN());
                pstmt.setString(7,vo.getMSG_SBST_JPN());
                pstmt.setString(8,vo.getANG_CODE());
                pstmt.setString(9,vo.getFM_CODE());

                pstmt.executeUpdate();
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            return 0;
        }
    }
}
