package org.hgq.test;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.hgq.App;
import org.hgq.domain.base.CollectionCase;
import org.hgq.mapper.base.CollectionCaseMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2021-08-19 11:41
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class CollectionCaseDemo2 {


    @Autowired
    RuntimeService runtimeService;
    @Autowired
    RepositoryService repositoryService;

    @Autowired
    CollectionCaseMapper caseMapper;
    @Autowired
    TaskService taskService;

    //入催数据
    public List<CollectionCase> getCollectionCaseList() {
        List<CollectionCase> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            CollectionCase collectionCase = new CollectionCase();
            collectionCase.setId((long) i);
            collectionCase.setUserNo(i + "");
            collectionCase.setUserName("user" + i);
            collectionCase.setIdCard(i + "");
            collectionCase.setMobile(i + "");
            collectionCase.setCaseSource((byte) 1);
            collectionCase.setProductCode(1);
            collectionCase.setOverdueDays(-2);
            collectionCase.setDebtAmount(new BigDecimal(i));

//            collectionCase.setLifecycle((byte)1);
//            collectionCase.setAllocationTime(Instant.now());
//            collectionCase.setGroupNo("" +1);
//            collectionCase.setGroupName("一组");
//            collectionCase.setEmpNo("" +i);
//            collectionCase.setEmpName("催收员" +i);

            collectionCase.setCreateTime(Instant.now());
            collectionCase.setUpdateTime(Instant.now());
            list.add(collectionCase);
        }

        return list;
    }

    //候选用户组
    public Queue<String> getCandidateEmps() {
        Queue<String> candidateUsers = new LinkedBlockingDeque();
        for (int i = 1; i <= 5; i++) {
            //入队
            candidateUsers.offer("催收员" + i);
        }
        return candidateUsers;
    }


    //自动分配
    @Test
    public void allocation() {
        List<CollectionCase> collectionCaseList = getCollectionCaseList();
        Queue<String> candidateEmps = getCandidateEmps();

        for (CollectionCase aCase : collectionCaseList) {
            Long id = aCase.getId();
            String businessKey = String.valueOf(id);
            //队头出队
            String emp = candidateEmps.poll();

            //从队尾入队
            candidateEmps.offer(emp);


            Map<String, Object> variables = new HashMap<>();
            variables.put("candidateUsers", emp);
            String processInstanceId = null;// startProcessInstanceBykey(businessKey, variables);


            aCase.setProcessInstanceId(processInstanceId);

            aCase.setLifecycle((byte) 1);

            aCase.setAllocationTime(Instant.now());
            aCase.setEmpNo(emp);
            saveCase(aCase);

        }

    }

    private void saveCase(CollectionCase aCase) {
        caseMapper.insertSelective(aCase);
    }


    @Test
    public void createDeployment() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deploy = deploymentBuilder
                .name("案件分配流程2")
                .addClasspathResource("bpmn/case_allocation2.bpmn").deploy();

        System.out.println("deploymentBuilder" + deploymentBuilder);
        System.out.println("deploy" + deploy);

    }

    @Test
    public void queryProcessDefinitionId() {

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> list = processDefinitionQuery.deploymentId("3fafa3ee-00c7-11ec-bc23-ace2d36d35ee").list();
        System.out.println("ProcessDefinition.id:" + list.get(0).getId());
        System.out.println("ProcessDefinition.key:" + list.get(0).getKey());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstanceBykey() {
        String businessKey = "ggg";
        String processDefinitionKey = "case_allocation2";

        Map<String, Object> variables = new HashMap<>();
        variables.put("case", new CollectionCase());
        variables.put("candidateEmps", "emp1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);

        System.out.println("业务key==" + processInstance.getBusinessKey());
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
    }


    /**
     * 查询组任务
     */
    @Test
    public void findGroupTaskList() {
        // 流程定义key
        String processDefinitionKey = "case_allocation";
        // 任务候选人
        String candidateUser = "催收员1";

        //查询组任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskCandidateUser(candidateUser)//根据候选人查询
                .list();
        for (Task task : list) {
            System.out.println("----------------------------");
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    /**
     * 拾取组任务
     * <p>
     * 候选人员拾取组任务后该任务变为自己的个人任务。
     * 说明：即使该用户不是候选人也能拾取，建议拾取时校验是否有资格
     * <p>
     * 组任务拾取后，该任务已有负责人，通过候选人将查询不到该任务
     */
    @Test
    public void claimTask() {

        //要拾取的任务id
        String taskId = "6302";
        //任务候选人id
        String userId = "lisi";
        //拾取任务
        //即使该用户不是候选人也能拾取(建议拾取时校验是否有资格)
        //校验该用户有没有拾取任务的资格
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateUser(userId)//根据候选人查询
                .singleResult();
        if (task != null) {
            //拾取任务
            taskService.claim(taskId, userId);
            System.out.println("任务拾取成功");
        }
    }


    /**
     * 催收经理能回收订单，就是归还给组里
     * <p>
     * 归还组任务，由个人任务变为组任务，还可以进行任务交接
     * <p>
     * 如果个人不想办理该组任务，可以归还组任务，归还后该用户不再是该任务的负责人
     * 说明：建议归还任务前校验该用户是否是该任务的负责人
     * <p>
     * 也可以通过setAssignee方法将任务委托给其它用户负责，注意被委托的用户可以不是候选人（建议不要这样使用）
     */

    @Test
    public void setAssigneeToGroupTask() {
        //  获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 查询任务使用TaskService
        TaskService taskService = processEngine.getTaskService();
        // 当前待办任务
        String taskId = "6004";
        // 任务负责人
        String userId = "zhangsan2";
        // 校验userId是否是taskId的负责人，如果是负责人才可以归还组任务
        Task task = taskService
                .createTaskQuery()
                .taskId(taskId)
                .taskAssignee(userId)
                .singleResult();
        if (task != null) {
            // 如果设置为null，归还组任务,该 任务没有负责人
            taskService.setAssignee(taskId, null);
        }
    }

    /**
     * 任务交接,任务负责人将任务交给其它候选人办理该任务
     */
    @Test
    public void setAssigneeToCandidateUser() {

        // 当前待办任务
        String taskId = "6004";
        // 任务负责人
        String userId = "zhangsan2";
// 将此任务交给其它候选人办理该 任务
        String candidateuser = "zhangsan";
        // 校验userId是否是taskId的负责人，如果是负责人才可以归还组任务
        Task task = taskService
                .createTaskQuery()
                .taskId(taskId)
                .taskAssignee(userId)
                .singleResult();
        if (task != null) {
            taskService.setAssignee(taskId, candidateuser);
        }
    }

}
