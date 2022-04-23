package com.work.terminology.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Parameter {
    private OauthParameter oauth_parameter;
    private SheetParameter sheet_parameter;
}
