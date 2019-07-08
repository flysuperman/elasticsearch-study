package com.sailing.es.elasticsearchrestclientdemo.model;


import lombok.Data;

import java.util.Date;

/**
 * @Auther: Administrator
 * @Date: 2019/7/6 16:14
 * @Description:
 */
@Data
public class UserInfo {

    private String id;

    private String userName;

    private String passWord;

    private Date birth;
}
