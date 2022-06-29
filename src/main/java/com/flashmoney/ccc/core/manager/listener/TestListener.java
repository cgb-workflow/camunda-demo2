package com.flashmoney.ccc.core.manager.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-06-27 21:38
 **/
@Service
public class TestListener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {

        if (TaskListener.EVENTNAME_CREATE.equals(delegateTask.getEventName())) {

            System.out.println("TestListener: " );

        }


    }
}
