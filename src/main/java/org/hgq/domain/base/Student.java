package org.hgq.domain.base;

import java.io.Serializable;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-04-16 12:50
 **/

public class Student implements Serializable {

    private Integer age;
    private String name;


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
