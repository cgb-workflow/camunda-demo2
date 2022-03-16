package com.example;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.hgq.CamundaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-03-16 20:49
 **/
@SpringBootTest(classes = CamundaApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CreditAuditDemo {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;


    @Autowired
    RepositoryService repositoryService;

    @Test
    public void tttt() {}

    @Test
    public void deploy() {
        Deployment deployment = repositoryService
                .createDeployment()
                .name("信审审核1")
                .addClasspathResource("credit_audit_1.bpmn")
                .deploy();
        System.out.println("deployment: " + deployment);//1cf006b9-a528-11ec-be05-88b11139d73a

    }

    @Test
    public void definitionByDeploymentId() {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition definition = processDefinitionQuery.deploymentId("1cf006b9-a528-11ec-be05-88b11139d73a").singleResult();
        System.out.println("definitionByDeploymentId: " + definition);

    }

    @Test
    public void definitionByProcessDefinitionKey() {

        String processDefinitionKey = "credit_audit_1";
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition definitionByProcessDefinitionKey = processDefinitionQuery.processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
        System.out.println("definitionByProcessDefinitionKey: " + definitionByProcessDefinitionKey);

    }


    /**
     * 用户首次提交，生成信审任务，生成信审审核记录，并且启动审核流程
     * 用户补件提交，更新信审任务，生成信审审核记录，并且启动审核流程
     */
    @Test
    public void startProcess() {

        //信审任务
        Integer creditAuditId = 123;

        //信审审核记录id
        Integer creditAuditRecordId = 456;


        //启动审核流程，并且设置业务key是 creditAuditRecordId（信审审核记录id）
        String processDefinitionKey = "credit_audit_1";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, creditAuditRecordId.toString());
        System.out.println("processInstance: " + processInstance.getId());
        //把流程实例id 与 信审审核记录 相关联 ，credit_audit_record 表新增个字段 process_instance_id

    }

    //开始审核 ，就是认领审批任务
    @Test
    public void claim() {

        //根据creditAuditId 查询 credit_audit_record表，得到result=99(初始化)状态的 唯一一条记录，然后得到 process_instance_id

        String userId = "9999";

        String process_instance_id = "bdb8f0cf-a530-11ec-be35-88b11139d73a";
        //根据 process_instance_id 查询到 taskId
        Task currentTask = taskService.createTaskQuery().processInstanceId(process_instance_id).singleResult();

        //认领任务,并且完成 开始审核 任务
        taskService.claim(currentTask.getId(),userId);

        //完成 开始审核 任务
        taskService.complete(currentTask.getId());


        //然后查询出下一个节点任务，设置办理人为当前用户
        Task nextTask = taskService.createTaskQuery().processInstanceId(process_instance_id).singleResult();

        //认领任务,并且完成 开始审核 任务
        taskService.claim(nextTask.getId(),userId);


        //相应的业务逻辑处理

    }


    //真正审核，完成审核任务
    @Test
    public void audit() {

        //根据creditAuditId 查询 credit_audit_record表，得到result=3(审核中)状态的 唯一一条记录，然后得到 process_instance_id

        String userId = "9999";

        String process_instance_id = "bdb8f0cf-a530-11ec-be35-88b11139d73a";
        //根据 process_instance_id 查询到 taskId
        Task currentTask = taskService.createTaskQuery().processInstanceId(process_instance_id).singleResult();


        //完成 真正审核 任务
        taskService.complete(currentTask.getId());


        //相应的业务逻辑处理

    }


    @Test
    public void queryTime() {

        //根据 credit_audit_record表 的  process_instance_id

        String process_instance_id = "bdb8f0cf-a530-11ec-be35-88b11139d73a";

        //统计整个流程的执行时长，历史流程实例
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        HistoricProcessInstance historicProcessInstance = historicProcessInstanceQuery.processInstanceId(process_instance_id).singleResult();
        Long durationInMillis = historicProcessInstance.getDurationInMillis();
        System.out.println("统计整个流程的执行时长：" +durationInMillis);


        //统计 整个流程中 的 一级审核节点 的执行时长 , 历史 任务实例
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        List<HistoricTaskInstance> list = historicTaskInstanceQuery.processInstanceId(process_instance_id).list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println("historicTaskInstance.getId(): "+historicTaskInstance.getId());
            System.out.println("historicTaskInstance.getTaskDefinitionKey(): "+historicTaskInstance.getTaskDefinitionKey());
            System.out.println("historicTaskInstance.getActivityInstanceId(): "+historicTaskInstance.getActivityInstanceId());
            System.out.println("historicTaskInstance.getDurationInMillis(): "+historicTaskInstance.getDurationInMillis());
            System.out.println("historicTaskInstance.getName(): "+historicTaskInstance.getName());
            System.out.println("historicTaskInstance.getCaseInstanceId(): "+historicTaskInstance.getCaseInstanceId());
            System.out.println("historicTaskInstance.getCaseDefinitionId(): "+historicTaskInstance.getCaseDefinitionId());
            System.out.println("historicTaskInstance.getCaseDefinitionKey(): "+historicTaskInstance.getCaseDefinitionKey());

            System.out.println("==============================");
        }

    }


}
