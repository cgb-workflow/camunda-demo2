package org.hgq.test;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.hgq.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2021-08-17 19:10
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class CamundaDemo2 {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;

    /**
     * 流程定义部署
     */
    @Test
   public void deploy() {
        Deployment deploy = repositoryService.createDeployment()

               // .addClasspathResource("bpmn/SendTaskTest.bpmn")
                .addClasspathResource("bpmn/ReceiveTaskTest.bpmn")
                .name("审核流程")
                .deploy();
        //部署Id
        System.out.println(deploy.getName());//84c0a64c-cc1c-11ea-85a7-98fa9b4e8fcb
    }

    /**
     * 开启一条流程
     */
    @Test
    public void startProcessInstanceWithAssignee(){

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_034c7gx");
        //流程实例Id
        System.out.println(processInstance.getProcessInstanceId());//c3156a0d-cc28-11ea-adb8-98fa9b4e8fcb
    }


}
