package com.example;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.hgq.CamundaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @description: 流程实例系列
 * RuntimeService : 该类核心功能是启动实例，查询与实例相关的一些信息。
 * 流程定义类似我们提前制定好了规则，让规则开始运转的过程就是实例运转的过程。
 * 类似我们Java中实例化一个类的 对象，然后操作这个实例对象。
 * <p>
 * 是camunda的流程 执行服务 类，可以从这个服务类中获取很多关于流程执行相关的信息。
 * 实现类是 RuntimeServiceImpl，提供了一系列根据自身业务场景启动流程实例的api。
 * 可以根据不同的api启动实例，并返回ProcessInstance对象。
 * 可以异步删除所有实例，并返回Batch对象。
 * 可以获取变量信息，并返回VariableMap对象
 * 可以获取变量信息，并返回Map<String,Object>对象。
 * 可以获取单个变量信息，并返回TypedValue对象。
 * 可以查询执行实例，返回ExecutionQuery对象。
 * 可以自定义SQL查询执行实例，返回NativeExecutionQuery对象。
 * 可以查询流程实例信息，返回ProcessInstanceQuery对象。
 * 可以自定义SQL查询流程实例信息，返回NativeProcessInstanceQuery对象。
 * 可以查询Incident相关信息，返回Incident对象。
 * @author: huangguoqiang
 * @create: 2022-03-16 10:36
 **/
@SpringBootTest(classes = CamundaApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RuntimeServiceDemo {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    /**
     * insert into ACT_HI_TASKINST ( ID_, PROC_DEF_KEY_, PROC_DEF_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, EXECUTION_ID_, CASE_DEF_KEY_, CASE_DEF_ID_, CASE_INST_ID_, CASE_EXECUTION_ID_, ACT_INST_ID_, NAME_, PARENT_TASK_ID_, DESCRIPTION_, OWNER_, ASSIGNEE_, START_TIME_, END_TIME_, DURATION_, DELETE_REASON_, TASK_DEF_KEY_, PRIORITY_, DUE_DATE_, FOLLOW_UP_DATE_, TENANT_ID_, REMOVAL_TIME_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
     * insert into ACT_HI_PROCINST ( ID_, PROC_INST_ID_, BUSINESS_KEY_, PROC_DEF_KEY_, PROC_DEF_ID_, START_TIME_, END_TIME_, REMOVAL_TIME_, DURATION_, START_USER_ID_, START_ACT_ID_, END_ACT_ID_, SUPER_PROCESS_INSTANCE_ID_, ROOT_PROC_INST_ID_, SUPER_CASE_INSTANCE_ID_, CASE_INST_ID_, DELETE_REASON_, TENANT_ID_, STATE_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
     * insert into ACT_HI_IDENTITYLINK ( ID_, TIMESTAMP_, TYPE_, USER_ID_, GROUP_ID_, TASK_ID_, ROOT_PROC_INST_ID_, PROC_DEF_ID_, OPERATION_TYPE_, ASSIGNER_ID_, PROC_DEF_KEY_, TENANT_ID_, REMOVAL_TIME_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
     * insert into ACT_HI_ACTINST ( ID_, PARENT_ACT_INST_ID_, PROC_DEF_KEY_, PROC_DEF_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, EXECUTION_ID_, ACT_ID_, TASK_ID_, CALL_PROC_INST_ID_, CALL_CASE_INST_ID_, ACT_NAME_, ACT_TYPE_, ASSIGNEE_, START_TIME_, END_TIME_, DURATION_, ACT_INST_STATE_, SEQUENCE_COUNTER_, TENANT_ID_, REMOVAL_TIME_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
     * insert into ACT_RU_EXECUTION ( ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, BUSINESS_KEY_, PROC_DEF_ID_, ACT_ID_, ACT_INST_ID_, IS_ACTIVE_, IS_CONCURRENT_, IS_SCOPE_, IS_EVENT_SCOPE_, PARENT_ID_, SUPER_EXEC_, SUPER_CASE_EXEC_, CASE_INST_ID_, SUSPENSION_STATE_, CACHED_ENT_STATE_, SEQUENCE_COUNTER_, TENANT_ID_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )
     * insert into ACT_RU_TASK ( ID_, NAME_, PARENT_TASK_ID_, DESCRIPTION_, PRIORITY_, CREATE_TIME_, OWNER_, ASSIGNEE_, DELEGATION_, EXECUTION_ID_, PROC_INST_ID_, PROC_DEF_ID_, CASE_EXECUTION_ID_, CASE_INST_ID_, CASE_DEF_ID_, TASK_DEF_KEY_, DUE_DATE_, FOLLOW_UP_DATE_, SUSPENSION_STATE_, TENANT_ID_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )
     */
    @Test
    public void startProcessInstanceByKey() {
        String processDefinitionKey = "leave_1";

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println(processInstance.getId() + "," + processInstance.getBusinessKey() + "," + processInstance.getProcessDefinitionId());
    }




    //ACT_RU_TASK : 正在执行的任务信息
    /**
     * select distinct RES.REV_, RES.ID_, RES.NAME_, RES.PARENT_TASK_ID_,
     * RES.DESCRIPTION_, RES.PRIORITY_, RES.CREATE_TIME_,
     * RES.OWNER_, RES.ASSIGNEE_, RES.DELEGATION_, RES.EXECUTION_ID_,
     * RES.PROC_INST_ID_, RES.PROC_DEF_ID_, RES.CASE_EXECUTION_ID_,
     * RES.CASE_INST_ID_, RES.CASE_DEF_ID_, RES.TASK_DEF_KEY_, RES.DUE_DATE_,
     * RES.FOLLOW_UP_DATE_, RES.SUSPENSION_STATE_,
     * RES.TENANT_ID_ from ACT_RU_TASK RES WHERE ( 1 = 1 and RES.ASSIGNEE_ = ? ) order by RES.ID_ asc LIMIT ? OFFSET ?
     * 张三(String), 2147483647(Integer), 0(Integer)
     */
    @Test
    public void createTaskQuery() {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskAssignee("lisi");
        List<Task> list = taskQuery.list();
        for (Task task : list) {
            System.out.println(task.getId());
        }
    }


    //四、历史数据5张表说明及实例跟踪
    //查询流程状态：正在执行，还是结束.
    // 在流程执行的过程中，创建的流程实例id在整个过程中都不会变，当流程结束后，流程实将会在正在执行的执行对象表（act_ru_execution）中删除


    //ACT_RU_EXECUTION ：正在执行的信息
    /**
     * select distinct RES.* from ACT_RU_EXECUTION RES inner join
     * ACT_RE_PROCDEF P on RES.PROC_DEF_ID_ = P.ID_ WHERE
     * <p>
     * RES.PARENT_ID_ is null and RES.PROC_INST_ID_ = ? order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createProcessInstanceQuery() {
        String processInstanceId = "9390f496-a4e1-11ec-a216-88b11139d73a";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                //结果唯一的，可以用single，如果大于1，则会报错
                .singleResult();
        if (processInstance == null) {
            System.out.println("当前实例已经结束了");
        } else {
            System.out.println("当前实例正在运转");
        }
    }


    //ACT_HI_PROCINST表：历史流程实例与运行流程实例是一对一的关系，存放已经执行完的历史流程实例信息

    /**
     * select distinct RES.* from
     * ( SELECT SELF.*, DEF.NAME_, DEF.VERSION_ FROM ACT_HI_PROCINST
     * SELF LEFT JOIN ACT_RE_PROCDEF DEF ON SELF.PROC_DEF_ID_ = DEF.ID_
     * <p>
     * WHERE SELF.PROC_INST_ID_ = ? and STATE_ = ? ) RES order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createHistoricProcessInstanceQuery() {
        String processInstanceId = "9390f496-a4e1-11ec-a216-88b11139d73a";
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
//                .completed()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (historicProcessInstance.getEndTime() != null) {
            System.out.println("当前实例结束了");
        } else {
            System.out.println("当前实例正在运转");
        }


    }

    //历史活动表是：ACT_HI_ACTINST，活动节点数据都会存在这个表中，存放历史所有完成的活动

    /**
     * select RES.* FROM ACT_HI_ACTINST RES WHERE RES.PROC_INST_ID_ = ? order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createHistoricActivityInstanceQuery() {
        String processInstanceId = "9390f496-a4e1-11ec-a216-88b11139d73a";
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        for (HistoricActivityInstance historicActivityInstance : list) {
            System.out.println(historicActivityInstance.toString());
        }
    }

//历史任务表：ACT_HI_TASKINST： 已经执行完的历史任务信息
    /**
     * select distinct RES.* from ACT_HI_TASKINST RES WHERE RES.PROC_INST_ID_ = ? order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createHistoricTaskInstanceQuery() {
        String processInstanceId = "9390f496-a4e1-11ec-a216-88b11139d73a";
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println(historicTaskInstance);
        }
    }


}
