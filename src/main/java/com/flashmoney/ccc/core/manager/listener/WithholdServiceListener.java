package com.flashmoney.ccc.core.manager.listener;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.hgq.domain.base.CollectionDerate;
import org.hgq.domain.base.CollectionDerateExample;
import org.hgq.mapper.base.CollectionDerateMapper;
import org.hgq.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-04-17 15:28
 **/

@Service
public class WithholdServiceListener implements TaskListener {

    @Autowired
    private OrderService orderService;


    @Autowired
    private TaskService taskService;
    @Autowired
    HistoryService historyService;

    @Autowired
    CollectionDerateMapper collectionDerateMapper;

    @Override
    public void notify(DelegateTask delegateTask) {
        String id = delegateTask.getId();
        String processDefinitionId = delegateTask.getProcessDefinitionId();

        String processInstanceId = delegateTask.getProcessInstanceId();
        System.out.println("id: " + id);
        System.out.println("processDefinitionId: " + processDefinitionId + " Thread: " + Thread.currentThread());
        System.out.println("processInstanceId: " + processInstanceId);

        if (TaskListener.EVENTNAME_CREATE.equals(delegateTask.getEventName())) {

            //首先认领任务
            Object operator_id = delegateTask.getVariable("operator_id");
            delegateTask.setAssignee(operator_id.toString());

            Timer timer = new Timer();

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {

                    //模拟发起代扣，根据代扣id 查询代扣结果

                    String order = orderService.getOrder();
                    System.out.println("org.hgq.delegate.Withhold.execute: " + order + "LocalDate.now(): " + LocalTime.now() + " Thread: " + Thread.currentThread());

                    if (orderService.increment() == 20) {
                        //${withhold==1}
                        //发起扣款  withhold==1 扣款成功  withhold==0 扣款失败
                        Map<String, Object> variables = new HashMap<>();
                        //withhold==1 扣款成功  withhold==0 扣款失败
                        variables.put("withhold", 0);
                        taskService.complete(id, variables);


                        //更新业务表
                        //更新业务表
                        CollectionDerate updateDerate = new CollectionDerate();
                        //1-初审待审核 2-初审代扣失败待审核 3-复审待审核 4-复审代扣失败待审核 5-审核通过 6-审核拒绝 99-代扣中 :status
                        updateDerate.setStatus(2);
                        Instant now = Instant.now();
                        updateDerate.setAuditTime(now);
                        updateDerate.setUpdateTime(now);

                        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
                        HistoricProcessInstance historicProcessInstance = historicProcessInstanceQuery.processInstanceId(processInstanceId).singleResult();
                        String businessKey = historicProcessInstance.getBusinessKey();
                        CollectionDerateExample example = new CollectionDerateExample();
                        example.createCriteria().andIdEqualTo(Long.valueOf(businessKey));
                        collectionDerateMapper.updateByExampleSelective(updateDerate, example);


                        System.out.println("关闭任务");
                        this.cancel();
                    }

                }
            };
            timer.schedule(timerTask, 2000l, 2000l);
        }


    }
}
