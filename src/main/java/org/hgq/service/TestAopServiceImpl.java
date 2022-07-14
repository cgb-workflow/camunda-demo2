package org.hgq.service;

import org.hgq.annotation.MyTestAop;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-07-02 14:40
 **/
@Service
public class TestAopServiceImpl implements TestAopService {


    @MyTestAop
    @Override
    public String ttttt(String name){

        System.out.println("name: " +name);

        return name;
    }




}
