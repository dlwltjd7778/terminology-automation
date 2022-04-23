package com.test.terminology.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@Entity(name="000_temp_dictionary")
public class Terminology implements Persistable<String> {

    @Id
    @NonNull
    private String MSG_ID;
    @NonNull
    private String CONTS_TYPE_CD;
    @NonNull
    private int LANG_MSG_ID_NO;
    @NonNull
    private String MSG_SBST_ENG;
    @NonNull
    private String MSG_SBST_KOR;
    @NonNull
    private String MSG_SBST_CHN;
    @NonNull
    private String MSG_SBST_JPN;

    private String ANG_CODE;
    private String FM_CODE;

    @Override
    public String getId() {
        return MSG_ID;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
