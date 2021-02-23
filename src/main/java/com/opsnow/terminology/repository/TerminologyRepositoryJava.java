package com.opsnow.terminology.repository;

import com.opsnow.terminology.model.Terminology;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
@PropertySource("classpath:application.properties") // 프로퍼티 파일 경로 지정
public class TerminologyRepositoryJava {
    private Connection conn = null;

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

//    private String DRIVERCLASSNAME = dbConfig.getDriverClassName();
//    private String URL = dbConfig.getUrl();
//    private String USERNAME = dbConfig.getUsername();
//    private String PASSWORD = dbConfig.getPassword();

    // 커넥션 얻기
    public Connection getConn() {
        try {
            Class.forName(DRIVERCLASSNAME);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    // truncate
    public void truncate(String table){
        PreparedStatement pstmt = null;
        String sql = "TRUNCATE TABLE 000_temp_dictionary";
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
