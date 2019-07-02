package com.sailing.es.test.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.util.Date;

/**
 * @Auther: Administrator
 * @Date: 2019/7/2 23:16
 * @Description:
 */
@Data
@ToString
@NoArgsConstructor
public class EsModel {

    private String id;
    private String name;
    private int age;
    private Date date;
}
