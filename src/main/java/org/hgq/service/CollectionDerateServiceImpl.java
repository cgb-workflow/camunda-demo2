package org.hgq.service;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.hgq.domain.base.CollectionDerate;
import org.hgq.domain.base.CollectionDerateExample;
import org.hgq.mapper.base.CollectionDerateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-04-15 17:10
 **/
@Service
public class CollectionDerateServiceImpl implements CollectionDerateService {
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;


    @Autowired
    RepositoryService repositoryService;


    @Autowired
    CollectionDerateMapper collectionDerateMapper;
    //首先主管审核

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void startProcess(Long id) {
        //启动审核流程，并且设置业务key是 collection_derate_id（减免申请id）
        String processDefinitionKey = "collection_derate_2";


        //减免申请id
        //Long id = 798845465L;
        CollectionDerate derate = new CollectionDerate();

        derate.setId(id);
        derate.setUserId(564L);
        derate.setUserNo("564");
        derate.setDerateType(1);
        derate.setStatus(1);
        derate.setProductCode(1004);
        derate.setCreateTime(Instant.now());
        derate.setUpdateTime(Instant.now());
        //业务逻辑代码完成

        //Map<String, Object> variables = new HashMap<>();
        //variables.put("candidateGroups", "credit_audit");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, id.toString());
        System.out.println("processInstance: " + processInstance.getId());
        //把流程实例id 与 信审审核记录 相关联 ，credit_audit_record 表新增个字段 process_instance_id
        derate.setProcessInstanceId(processInstance.getId());

        collectionDerateMapper.insertSelective(derate);

    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void audit(Long id, Long userId, Map<String, Object> variables) {

        CollectionDerate dbCollectionDerate = collectionDerateMapper.selectByPrimaryKey(id);

        String processInstanceId = dbCollectionDerate.getProcessInstanceId();
        //根据 process_instance_id 查询到 taskId
        Task currentTask = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

        //认领任务
        taskService.claim(currentTask.getId(), userId.toString());

        taskService.setVariablesLocal(currentTask.getId(), variables);

        taskService.complete(currentTask.getId(), variables);

        //相应的业务逻辑处理
        //更新业务表
        CollectionDerate updateDerate = new CollectionDerate();
        //1-初审待审核 2-初审代扣失败待审核 3-复审待审核 4-复审代扣失败待审核 5-审核通过 6-审核拒绝 99-代扣中 :status
        updateDerate.setStatus(99);
        updateDerate.setOperatorId(Long.valueOf(variables.get("operator_id").toString()));
        Instant now = Instant.now();
        updateDerate.setAuditTime(now);
        updateDerate.setUpdateTime(now);

        CollectionDerateExample example = new CollectionDerateExample();
        example.createCriteria().andIdEqualTo(dbCollectionDerate.getId()).andStatusEqualTo(dbCollectionDerate.getStatus());
        collectionDerateMapper.updateByExampleSelective(updateDerate, example);



    }
}
