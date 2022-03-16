package org.hgq.test;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.hgq.CamundaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2021-08-17 19:10
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CamundaApplication.class)
public class CamundaDemo {

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
                .addClasspathResource("bpmn/holiday.bpmn")
                .name("测试请假流程2")
                .deploy();
        //部署Id
        System.out.println(deploy.getName());//84c0a64c-cc1c-11ea-85a7-98fa9b4e8fcb
    }

    /**
     * 开启一条流程，并给用户任务的 assignee 赋值
     */
    @Test
    public void startProcessInstanceWithAssignee(){
        Map<String,Object> map = new HashMap<>();
        map.put("employee","zhangsan");
        map.put("deptment","lisi");
        map.put("personal","wangwu");
        ProcessInstance holiday = runtimeService.startProcessInstanceByKey("holiday", map);
        //流程实例Id
        System.out.println(holiday.getProcessInstanceId());//c3156a0d-cc28-11ea-adb8-98fa9b4e8fcb
    }

    @Test
    public void runProcinst(){
        Map<String,Object> params = new HashMap<>();
        params.put("employee","zhangsan");
        //params.put("leave",new Leave("NO00001","休假",new Date()));
        params.put("days",2);
        ProcessInstance holiday = runtimeService.startProcessInstanceByKey("holiday",params);
        System.out.println(holiday.getProcessDefinitionId());
        System.out.println(holiday.getId());
        System.out.println(holiday.getProcessInstanceId());
    }

    @Test
    public void taskComplete(){
        //目前zhangsan只有一个任务，业务中根据场景选择其他合适的方式
        Task task = taskService.createTaskQuery()
                .taskAssignee("zhangsan")
                .singleResult();
        Map<String,Object> params = new HashMap<>();
        params.put("deptment","lisi");
        params.put("days",5);
        //zhangsan完成任务，交由部门经理lisi审批
        taskService.complete(task.getId(),params);
    }

    @Test
    public void taskComplete2(){
        //目前lisi只有一个任务，业务中根据场景选择其他合适的方式
        Task task = taskService.createTaskQuery()
                .taskAssignee("lisi")
                .singleResult();
        Map<String,Object> params = new HashMap<>();
        params.put("personal","wangwu");
        taskService.complete(task.getId(),params);
    }
    @Test
    public void taskComplete3(){
        //目前lisi只有一个任务，业务中根据场景选择其他合适的方式
        Task task = taskService.createTaskQuery()
                .taskAssignee("wangwu")
                .singleResult();

        taskService.complete(task.getId());
    }
}
