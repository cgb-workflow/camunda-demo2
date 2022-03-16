package com.example;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricIdentityLinkLog;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.hgq.CamundaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-03-16 15:45
 **/
@SpringBootTest(classes = CamundaApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TaskServiceDemo {
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;


    @Test
    public void createTaskQuery() {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskAssignee("zhangsan");
        List<Task> list = taskQuery.list();
        for (Task task : list) {
            System.out.println(task.getId());//93944ff9-a4e1-11ec-a216-88b11139d73a

        }
    }


    @Test
    public void completeTask() {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskAssignee("lisi");
        List<Task> list = taskQuery.list();
        for (Task task : list) {
            System.out.println("任务id： "+task.getId());
            taskService.complete(task.getId());
            System.out.println("完成任务id： "+task.getId());
        }
        System.out.println("=======================");
        TaskQuery taskQuery2 = taskService.createTaskQuery()
                .taskAssignee("lisi");
        List<Task> list2 = taskQuery2.list();
        for (Task task : list2) {
            System.out.println("再次查询任务，任务id： "+task.getId());
        }

    }



    //使用变量分配任务，在启动流程时，需要指定变量，变量存在ACT_RU_VARIABLE中
    @Test
    public void startProcessInstanceByKey() {
        String processDefinitionKey = "leave_2";

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("user", "张翠山");
        //TODO 启动流程时候设置的变量是全局变量 ，在整个流程中都可以使用
        runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
    }


    /**
     * select distinct RES.REV_, RES.ID_, RES.NAME_,
     * RES.PARENT_TASK_ID_, RES.DESCRIPTION_, RES.PRIORITY_, RES.CREATE_TIME_, RES.OWNER_,
     * RES.ASSIGNEE_, RES.DELEGATION_, RES.EXECUTION_ID_, RES.PROC_INST_ID_, RES.PROC_DEF_ID_,
     * RES.CASE_EXECUTION_ID_, RES.CASE_INST_ID_, RES.CASE_DEF_ID_, RES.TASK_DEF_KEY_, RES.DUE_DATE_,
     * RES.FOLLOW_UP_DATE_, RES.SUSPENSION_STATE_, RES.TENANT_ID_
     * from ACT_RU_TASK RES WHERE ( 1 = 1 and RES.ASSIGNEE_ = ? ) order by RES.ID_ asc LIMIT ? OFFSET ?
     * <p>
     * 张三(String), 2147483647(Integer), 0(Integer)
     */
    @Test
    public void findMyTask() {
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("lisi").list();
        for (Task task : taskList) {
            System.out.println("#############");
            System.out.println(task.getId());
            System.out.println(task.getCreateTime());
            System.out.println(task.getPriority());
            System.out.println(task.getExecutionId());
            System.out.println(task.getProcessInstanceId());
            System.out.println("#############");
        }
    }

    @Test
    public void complete() {
        String taskId = "dc3fc53a-a50c-11ec-89f9-88b11139d73a";
        taskService.complete(taskId);

    }


    /*
    使用监听器设置任务的办理人
    启动流程 会走到流程图第一个节点，第一个节点设置了监听器
     */

    @Test
    public void startProcessInstance1() {
        String processDefinitionKey = "leave_4";

        runtimeService.startProcessInstanceByKey(processDefinitionKey);
    }


    @Test
    public void setAssignee() {
        String taskId = "5503";
        String assignee = "张无极";
        //setAssignee会触发assignment类型的监听器
        taskService.setAssignee(taskId, assignee);
    }


    /**
     * 设置候选用户办理任务
     */
    @Test
    public void startProcessInstance2() {
        String processDefinitionKey = "leave_5";

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println(processInstance);
    }

    //组任务的ASSIGNEE_字段没有值，而对应的处理人存放在ACT_RU_IDENTITYLINK中，该表存了任务ID的值
    //组任务查询：
    /**
     * select distinct RES.REV_, RES.ID_, RES.NAME_, RES.PARENT_TASK_ID_, RES.DESCRIPTION_,
     * RES.PRIORITY_, RES.CREATE_TIME_, RES.OWNER_, RES.ASSIGNEE_, RES.DELEGATION_, RES.EXECUTION_ID_,
     * RES.PROC_INST_ID_, RES.PROC_DEF_ID_, RES.CASE_EXECUTION_ID_, RES.CASE_INST_ID_, RES.CASE_DEF_ID_,
     * RES.TASK_DEF_KEY_, RES.DUE_DATE_, RES.FOLLOW_UP_DATE_, RES.SUSPENSION_STATE_, RES.TENANT_ID_
     * from ACT_RU_TASK RES
     * inner join ACT_RU_IDENTITYLINK I on I.TASK_ID_ = RES.ID_
     * WHERE ( 1 = 1 and ( RES.ASSIGNEE_ is null and I.TYPE_ = 'candidate' and ( I.USER_ID_ = ? ) ) ) order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void findGroupTask() {
        List<Task> taskList = taskService
                .createTaskQuery()
                .taskCandidateUser("帅哥2")//指定候选人
                .list();
        for (Task task : taskList) {
            System.out.println("#############");
            System.out.println(task.getId());
            System.out.println(task.getCreateTime());
            System.out.println(task.getPriority());
            System.out.println(task.getExecutionId());
            System.out.println("#############");
        }
    }


    //根据任务反向查询处理人：
    @Test
    public void getIdentityLinksForTask() {
        String taskId = "559b76aa-a519-11ec-9148-88b11139d73a";
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
        for (IdentityLink identityLink : identityLinksForTask) {
            System.out.println("########");
            System.out.println(identityLink.getTaskId());
            System.out.println(identityLink.getProcessDefId());
            System.out.println(identityLink.getUserId());
            System.out.println("########");

        }
    }


    //查询历史任务处理人：
    /**
     * select distinct RES.* from ACT_HI_IDENTITYLINK RES order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createHistoricIdentityLinkLogQuery() {
        //查询历史权限表
        List<HistoricIdentityLinkLog> list = historyService.createHistoricIdentityLinkLogQuery()
                .list();
        for (HistoricIdentityLinkLog identityLinkLog : list) {
            System.out.println("###########");
            System.out.println(identityLinkLog.getOperationType());
            System.out.println(identityLinkLog.getUserId());
            System.out.println(identityLinkLog.getGroupId());
            System.out.println(identityLinkLog.getId());
            System.out.println("###########");
        }
    }


    //五、组任务处理人三种设置方式
    //任务签收：
    /**
     * insert into ACT_HI_IDENTITYLINK ( ID_, TIMESTAMP_, TYPE_, USER_ID_, GROUP_ID_, TASK_ID_, ROOT_PROC_INST_ID_, PROC_DEF_ID_, OPERATION_TYPE_, ASSIGNER_ID_, PROC_DEF_KEY_, TENANT_ID_, REMOVAL_TIME_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
     * update ACT_RU_TASK SET REV_ = ?, NAME_ = ?, PARENT_TASK_ID_ = ?, PRIORITY_ = ?, CREATE_TIME_ = ?, OWNER_ = ?, ASSIGNEE_ = ?, DELEGATION_ = ?, EXECUTION_ID_ = ?, PROC_DEF_ID_ = ?, CASE_EXECUTION_ID_ = ?, CASE_INST_ID_ = ?, CASE_DEF_ID_ = ?, TASK_DEF_KEY_ = ?, DESCRIPTION_ = ?, DUE_DATE_ = ?, FOLLOW_UP_DATE_ = ?, SUSPENSION_STATE_ = ?, TENANT_ID_ = ? where ID_= ? and REV_ = ?
     * UPDATE ACT_HI_ACTINST SET EXECUTION_ID_ = ?, PROC_DEF_KEY_ = ?, PROC_DEF_ID_ = ?, ACT_ID_ = ?, ACT_NAME_ = ?, ACT_TYPE_ = ?, PARENT_ACT_INST_ID_ = ? , ASSIGNEE_ = ? , TASK_ID_ = ? WHERE ID_ = ?
     * update ACT_HI_TASKINST set EXECUTION_ID_ = ?, PROC_DEF_KEY_ = ?, PROC_DEF_ID_ = ?, NAME_ = ?, PARENT_TASK_ID_ = ?, DESCRIPTION_ = ?, OWNER_ = ?, ASSIGNEE_ = ?, DELETE_REASON_ = ?, TASK_DEF_KEY_ = ?, PRIORITY_ = ?, DUE_DATE_ = ?, FOLLOW_UP_DATE_ = ?, CASE_INST_ID_ = ? where ID_ = ?
     */
    @Test
    public void claim() {
        String taskId="559b76aa-a519-11ec-9148-88b11139d73a";
        String userId="lisi";
        taskService.claim(taskId,userId);
    }
    //组任务被签收后，任务会指定具体的ASSIGNEE_，表示指定的人已经明确要处理当前任务了，组成员的人已经查询不到这个任务了，且也无法进行签收


    //个人任务归还：
    @Test
    public void claim2() {
        String taskId="559b76aa-a519-11ec-9148-88b11139d73a";
        String userId="lisi";
        taskService.claim(taskId,null);
    }

    //归还后，组内的人又可以查询到组任务了，并且可以签收了，任务的ASSIGNEE_会重新被置空


    //组任务也可以通过任务监听器指定
    /**
     * 设置候选用户办理任务
     */
    @Test
    public void startProcessInstance3() {
        String processDefinitionKey = "leave_6";

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println(processInstance);
    }


}
