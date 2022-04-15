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
import org.camunda.bpm.engine.task.Task;
import org.hgq.CamundaApplication;
import org.hgq.service.CollectionDerateService;
import org.hgq.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-03-16 20:49
 **/
@SpringBootTest(classes = CamundaApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CollectionDerateDemo2 {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;


    @Autowired
    RepositoryService repositoryService;
    @Autowired
    CollectionDerateService collectionDerateService;

    @Test
    public void tttt() {
    }

    @Test
    public void deploy() {
        Deployment deployment = repositoryService
                .createDeployment()
                .name("催收减免申请2")
                .addClasspathResource("collection_derate_2.bpmn")
                .deploy();
        System.out.println("deployment: " + deployment);//1cf006b9-a528-11ec-be05-88b11139d73a

    }

    /**
     *
     */
    @Test
    public void startProcess() {

        Long id = 798845465L;
        collectionDerateService.startProcess(id);


    }

    //真正审核，完成审核任务
    @Test
    public void audit1() {

        Long id = 798845465L;

        Long userId = 888L;
        Map<String, Object> variables = new HashMap<>();
        //audit_result==5 通过，audit_result==6拒绝
        variables.put("audit_result", 5);
        //减免比例大于50%，经理审批
        variables.put("derate_num", 35);

        variables.put("operator_id", userId);

        collectionDerateService.audit(id, userId, variables);

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //真正审核，完成审核任务
    @Test
    public void audit2() {

        //根据creditAuditId 查询 credit_audit_record表，得到result=3(审核中)状态的 唯一一条记录，然后得到 process_instance_id

        String userId = "999经理";

        String process_instance_id = "e2b27614-b653-11ec-89ea-88b11139d73a";
        //根据 process_instance_id 查询到 taskId
        Task currentTask = taskService.createTaskQuery().processInstanceId(process_instance_id).singleResult();

        if (userId.equals(currentTask.getAssignee())) {

            //完成 真正审核 任务
            Map<String, Object> variables = new HashMap<>();
            //audit_result==5 通过，audit_result==6拒绝
            variables.put("audit_result", 5);
            //减免比例大于50%，经理审批
            variables.put("derate_num", 0.9);
            taskService.complete(currentTask.getId(), variables);

            //根据 process_instance_id 查询到 taskId  发起扣款任务
            Task nextTask = taskService.createTaskQuery().processInstanceId(process_instance_id).singleResult();

            //认领任务,设置变量 记录任务开始时间
            taskService.setAssignee(nextTask.getId(), userId);
        } else {
            System.out.println("不是办理人");
        }
        //相应的业务逻辑处理
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    OrderService orderService;

    //发起扣款
    @Test
    public void withholdingTest() {
        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                String order = orderService.getOrder();
                System.out.println("org.hgq.delegate.Withhold.execute: " + order + "LocalDate.now(): " + LocalTime.now() + " Thread: " + Thread.currentThread());

                if (orderService.increment() == 10) {
                    System.out.println("关闭任务");
                    this.cancel();
                }

            }
        };
        timer.schedule(timerTask, 2000l, 2000l);


        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //发起扣款
    @Test
    public void withholding() {

        //根据creditAuditId 查询 credit_audit_record表，得到result=3(审核中)状态的 唯一一条记录，然后得到 process_instance_id

        String userId = "999经理";

        String process_instance_id = "dce01d0b-b64b-11ec-bba1-88b11139d73a";
        //根据 process_instance_id 查询到 taskId
        Task currentTask = taskService.createTaskQuery().processInstanceId(process_instance_id).singleResult();
        taskService.claim(currentTask.getId(), userId);

        //发起扣款  withhold==1 扣款成功  withhold==0 扣款失败
        Map<String, Object> variables = new HashMap<>();
        //withhold==1 扣款成功  withhold==0 扣款失败
        variables.put("withhold", 1);
        if (userId.equals(currentTask.getAssignee())) {
            taskService.complete(currentTask.getId(), variables);
        } else {
            System.out.println("不是办理人");
        }
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
        System.out.println("统计整个流程的执行时长：" + durationInMillis);
        System.out.println("统计整个流程的开始时间：" + dateFormat(historicProcessInstance.getStartTime()));
        System.out.println("统计整个流程的结束时间：" + dateFormat(historicProcessInstance.getEndTime()));


        //统计 整个流程中 的 一级审核节点 的执行时长 , 历史 任务实例
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        List<HistoricTaskInstance> list = historicTaskInstanceQuery.processInstanceId(process_instance_id).list();
        for (HistoricTaskInstance historicTaskInstance : list) {

            System.out.println("historicTaskInstance.getId(): " + historicTaskInstance.getId());
            System.out.println("historicTaskInstance.getName(): " + historicTaskInstance.getName());
            System.out.println("historicTaskInstance.getTaskDefinitionKey(): " + historicTaskInstance.getTaskDefinitionKey());
            System.out.println("historicTaskInstance.getActivityInstanceId(): " + historicTaskInstance.getActivityInstanceId());
            System.out.println("historicTaskInstance.getDurationInMillis(): " + historicTaskInstance.getDurationInMillis());
            System.out.println("historicTaskInstance.getStartTime(): " + dateFormat(historicTaskInstance.getStartTime()));
            System.out.println("historicTaskInstance.getEndTime(): " + dateFormat(historicTaskInstance.getEndTime()));


            System.out.println("==============================");
        }

    }

    public static String dateFormat(Date date) {

        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        return sdf.format(date);
    }

    public static Date parse(String dateStr) {

        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

}
