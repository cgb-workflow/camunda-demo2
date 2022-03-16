package org.hgq.test;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricActivityInstanceQuery;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.hgq.CamundaApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-03-15 14:43
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CamundaApplication.class)
public class HistoricDetailQueryTest {


    @Autowired
    private HistoryService historyService;


    public void test() {
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();

        List<HistoricActivityInstance> list = historicActivityInstanceQuery
                .processInstanceId("2501") // 执行流程实例id
                .finished()
                .list();

        for (HistoricActivityInstance historicActivityInstance : list) {
            historicActivityInstance.getEndTime();
        }


        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        List<HistoricProcessInstance> list1 = historicProcessInstanceQuery
                .processInstanceId("2501") // 执行流程实例id
                .finished()
                .list();

        for (HistoricProcessInstance historicProcessInstance : list1) {
            historicProcessInstance.getDurationInMillis();


        }

        System.out.println("sss");
    }
}
