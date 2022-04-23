package com.test.terminology.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OauthParameter {

    // oauth 에 필요
    private String client_id;
    private String client_secret;
    private String refresh_token;
    private String grant_type;

}
