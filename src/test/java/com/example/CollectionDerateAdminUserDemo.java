package com.example;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.*;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.IdentityLinkType;
import org.camunda.bpm.engine.task.Task;
import org.hgq.CamundaApplication;
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
 * @create: 2022-06-27 21:00
 **/
@SpringBootTest(classes = CamundaApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CollectionDerateAdminUserDemo {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;


    @Autowired
    RepositoryService repositoryService;

    @Test
    public void tttt() {
    }

    @Test
    public void deploy() {
        Deployment deployment = repositoryService
                .createDeployment()
                .name("催收减免催收员申请1")
                .addClasspathResource("collection_derate_admin_user_v1.bpmn")
                .deploy();
        System.out.println("deployment: " + deployment);

    }


    /**
     *
     */
    @Test
    public void startProcess() {


        //减免申请id
        Integer collection_derate_id = 798845465;

        //业务逻辑代码完成


        //启动审核流程，并且设置业务key是 collection_derate_id（减免申请id）
        String processDefinitionKey = "collection_derate_admin_user_v1";

        //${excludeInterest==false || derateAmount>500}
        Map<String, Object> variables = new HashMap<>();

        variables.put("excludeInterest", false);
        variables.put("derateAmount", 499.15);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        System.out.println("processInstance: " + processInstance.getId());
        //把流程实例id 与 信审审核记录 相关联 ，credit_audit_record 表新增个字段 process_instance_id


    }

    //开始审核 ，就是认领审批任务
    @Test
    public void claim() {

            String userId = "888第二个主管";
        //String userId = "999经理";

        String process_instance_id = "cf406814-f758-11ec-bc95-88b11139d73a";
        //根据 process_instance_id 查询到 taskId
        Task currentTask = taskService.createTaskQuery().processInstanceId(process_instance_id).singleResult();

        //认领任务,设置变量 记录任务开始时间

        //String taskId="42f4c5bb-f68a-11ec-936f-88b11139d73a";

        Date dueDate = currentTask.getDueDate();
        System.out.println("dueDate: " + dueDate);

        taskService.claim(currentTask.getId(), userId);



        //相应的业务逻辑处理


        System.out.println("=============");


    }

    //开始审核 ，就是认领审批任务
   /* @Test
    public void setAssignee() {

        //根据 creditAuditId 查询 credit_audit_record 表，得到 result=99(初始化)状态的 唯一一条记录，然后得到 process_instance_id

        String userId = "999";

        String process_instance_id = "f3437f65-b097-11ec-bdcb-88b11139d73a";
        //根据 process_instance_id 查询到 taskId
        Task currentTask = taskService.createTaskQuery().processInstanceId(process_instance_id).singleResult();

        //认领任务,设置变量 记录任务开始时间

        taskService.setAssignee(currentTask.getId(), userId);


    }*/


    @Test
    public void query() {

        String businessKey = "456";

        List<HistoricTaskInstance> list1 = historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(businessKey).list();
        for (HistoricTaskInstance historicTaskInstance : list1) {


            HistoricIdentityLinkLogQuery historicIdentityLinkLogQuery = historyService.createHistoricIdentityLinkLogQuery();

            List<HistoricIdentityLinkLog> list = historicIdentityLinkLogQuery
                    .taskId(historicTaskInstance.getId())
                    .type(IdentityLinkType.ASSIGNEE)
                    .operationType("add")
                    .list();
            for (HistoricIdentityLinkLog historicIdentityLinkLog : list) {

                System.out.println(historicIdentityLinkLog.getUserId());

            }
        }

    }


    //真正审核，完成审核任务
    @Test
    public void audit1() {

        //根据creditAuditId 查询 credit_audit_record表，得到result=3(审核中)状态的 唯一一条记录，然后得到 process_instance_id

        String userId = "888主管";

        String process_instance_id = "e2b27614-b653-11ec-89ea-88b11139d73a";
        //根据 process_instance_id 查询到 taskId
        Task currentTask = taskService.createTaskQuery().processInstanceId(process_instance_id).singleResult();


        if (userId.equals(currentTask.getAssignee())) {
            //完成 真正审核 任务
            Map<String, Object> variables = new HashMap<>();
            //audit_result==5 通过，audit_result==6拒绝
            variables.put("audit_result", 5);
            //减免比例大于50%，经理审批
            variables.put("derate_num", 0.65);
            taskService.setVariablesLocal(currentTask.getId(), variables);

            taskService.complete(currentTask.getId(), variables);
        } else {
            System.out.println("不是办理人");
        }
        //相应的业务逻辑处理

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
