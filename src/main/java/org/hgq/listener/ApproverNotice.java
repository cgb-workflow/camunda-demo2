package org.hgq.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

public class ApproverNotice implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("给"+ delegateTask.getAssignee() +"发一个消息！");
    }
}
