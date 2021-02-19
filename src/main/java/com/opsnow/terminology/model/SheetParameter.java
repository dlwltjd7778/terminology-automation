package com.opsnow.terminology.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SheetParameter {

    // sheet 에 필요
    private String sheet_id;
    private String sheet_name;
    private String access_token;
}
