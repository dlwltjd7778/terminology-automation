package com.opsnow.terminology.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opsnow.terminology.config.DBConfig;
import com.opsnow.terminology.repository.TerminologyRepositoryJava;
import com.opsnow.terminology.model.Terminology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TerminologyService {

//    @NonNull
//    private final TerminologyRepository terminologyDAO;

    // 2. 데이터를 파싱하는 부분 ( 컬럼명과 시트데이터 가져오는 부분 )
    public JsonArray parseData(String data) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonData = (JsonObject) jsonParser.parse(data);

        // 전체에서 values 라는 JSONArray 가져옴
        JsonArray jsonArray = jsonData.getAsJsonArray("values");

        return jsonArray;
    }


    // 3. 가져온 데이터를 VO에 매핑하기위한 작업들
    public List<Terminology> mappingData(JsonArray jsonArray) {
        List<Terminology> resultList = new ArrayList<>();

        // 0번 행에 적힌 컬럼명을 통해 열 번호 가져옴
        int num_MSG_ID = 0, num_CONTS_TYPE_CD = 0, num_LANG_MSG_ID_NO = 0, num_MSG_SBST_ENG = 0, num_MSG_SBST_KOR = 0, num_MSG_SBST_CHN = 0, num_MSG_SBST_JPN = 0;
        JsonArray keyArr = (JsonArray) jsonArray.get(0);

        for(int i=0;i<keyArr.size();i++){
            String key = keyArr.get(i).getAsString();
            if("MSG_ID".equals(key)){
                num_MSG_ID=i;
            } else if("CONTS_TYPE_CD".equals(key)){
                num_CONTS_TYPE_CD = i;
            } else if("LANG_MSG_ID_NO".equals(key)){
                num_LANG_MSG_ID_NO = i;
            } else if("MSG_SBST(ENG)\nAS-IS".equals(key)){
                num_MSG_SBST_ENG = i;
            } else if("MSG_SBST(KOR)\nAS-IS".equals(key)) {
                num_MSG_SBST_KOR = i;
            } else if("MSG_SBST(CHN)\nAS-IS".equals(key)){
                num_MSG_SBST_CHN = i;
            } else if("MSG_SBST(JPN)\nAS-IS".equals(key)){
                num_MSG_SBST_JPN = i;
            }
        }

        // vo에 파싱한 데이터 매핑
        for(int i=1;i<jsonArray.size();i++){

            JsonArray temp = (JsonArray) jsonArray.get(i);
            Terminology vo = new Terminology();

            try{
                vo.setMSG_ID(temp.get(num_MSG_ID).getAsString());
                vo.setCONTS_TYPE_CD(temp.get(num_CONTS_TYPE_CD).getAsString());
                vo.setLANG_MSG_ID_NO(temp.get(num_LANG_MSG_ID_NO).getAsInt());
                vo.setMSG_SBST_ENG(temp.get(num_MSG_SBST_ENG).getAsString());
                vo.setMSG_SBST_KOR(temp.get(num_MSG_SBST_KOR).getAsString());
                vo.setMSG_SBST_CHN(temp.get(num_MSG_SBST_CHN).getAsString());
                vo.setMSG_SBST_JPN(temp.get(num_MSG_SBST_JPN).getAsString());
            } catch (Exception e){
                System.err.println(i + "번째 행 에러!!");
                //e.printStackTrace();
                break;
            }
            resultList.add(vo);
        }
        return resultList;
    }

    public void saveData(List<Terminology> list){
//        terminologyDAO.saveAll(list);
        String tableName = "000_temp_dictionary";
        TerminologyRepositoryJava db = new TerminologyRepositoryJava();



        db.getConn();
        db.deleteAll(tableName);
        db.insertAll(list);
        System.out.println("DB data 삽입 완료");
    }
}
