package org.hgq.delegate;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.hgq.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-04-07 15:28
 **/

@Service
//public class WithholdService implements JavaDelegate {
public class WithholdService implements TaskListener {

    @Autowired
    private OrderService orderService;


    @Autowired
    private TaskService taskService;


   /* @Override
    public void execute(DelegateExecution execution) throws Exception {

        String processBusinessKey = execution.getProcessBusinessKey();
        String processDefinitionId = execution.getProcessDefinitionId();
        String businessKey = execution.getBusinessKey();
        String processInstanceId = execution.getProcessInstanceId();
        System.out.println("processBusinessKey: " + processBusinessKey);
        System.out.println("processDefinitionId: " + processDefinitionId);
        System.out.println("businessKey: " + businessKey);
        System.out.println("processInstanceId: " + processInstanceId);

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                String order = orderService.getOrder();
                System.out.println("org.hgq.delegate.Withhold.execute: " + order+ "LocalDate.now(): " + LocalTime.now() +" Thread: " +Thread.currentThread());

                if(orderService.increment()== 40){
                    //${withhold==1}
                    //发起扣款  withhold==1 扣款成功  withhold==0 扣款失败
                    Map<String, Object> variables = new HashMap<>();
                    //withhold==1 扣款成功  withhold==0 扣款失败
                    variables.put("withhold", 1);
                    execution.setVariables(variables);


                    System.out.println("关闭任务");
                    this.cancel();
                }

            }
        };
        timer.schedule(timerTask,2000l,2000l);

    }*/

    @Override
    public void notify(DelegateTask delegateTask) {
        String id = delegateTask.getId();
        String processDefinitionId = delegateTask.getProcessDefinitionId();

        String processInstanceId = delegateTask.getProcessInstanceId();
        System.out.println("id: " + id);
        System.out.println("processDefinitionId: " + processDefinitionId + " Thread: " + Thread.currentThread());
        System.out.println("processInstanceId: " + processInstanceId);

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                String order = orderService.getOrder();
                System.out.println("org.hgq.delegate.Withhold.execute: " + order + "LocalDate.now(): " + LocalTime.now() + " Thread: " + Thread.currentThread());

                if (orderService.increment() == 40) {
                    //${withhold==1}
                    //发起扣款  withhold==1 扣款成功  withhold==0 扣款失败
                    Map<String, Object> variables = new HashMap<>();
                    //withhold==1 扣款成功  withhold==0 扣款失败
                    variables.put("withhold", 1);
                    taskService.complete(id, variables);


                    System.out.println("关闭任务");
                    this.cancel();
                }

            }
        };
        timer.schedule(timerTask, 2000l, 2000l);
    }
}
