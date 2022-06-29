package com.flashmoney.ccc.core.manager.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-06-27 21:28
 **/
@Service
public class UserTaskDueDateListener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
        String id = delegateTask.getId();
        String processDefinitionId = delegateTask.getProcessDefinitionId();

        String processInstanceId = delegateTask.getProcessInstanceId();
        System.out.println("id: " + id);
        System.out.println("processDefinitionId: " + processDefinitionId + " Thread: " + Thread.currentThread());
        System.out.println("processInstanceId: " + processInstanceId);

        if (TaskListener.EVENTNAME_CREATE.equals(delegateTask.getEventName())) {
            Calendar instance = Calendar.getInstance();
            instance.setTime(new Date());
            instance.add(Calendar.MINUTE,3);
            Date time = instance.getTime();
            delegateTask.setDueDate(time);


        }


    }
}
