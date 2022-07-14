package org.hgq.test;

import org.hgq.CamundaApplication;
import org.hgq.service.TestAopServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-07-02 14:39
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CamundaApplication.class)
public class AopTest {

    @Autowired
    TestAopServiceImpl testAopService;
    @Test
    public void t1(){



        testAopService.ttttt("zhangsna");
    }
}
