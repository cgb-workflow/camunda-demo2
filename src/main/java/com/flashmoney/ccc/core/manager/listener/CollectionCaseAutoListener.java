package com.flashmoney.ccc.core.manager.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.task.IdentityLink;

import java.util.Set;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2021-09-04 14:44
 **/
public class CollectionCaseAutoListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {

        if (TaskListener.EVENTNAME_CREATE.equals(delegateTask.getEventName())) {

            Set<IdentityLink> candidates = delegateTask.getCandidates();
            String userId = null;
            for (IdentityLink candidate : candidates) {
                System.out.println(candidate.getUserId());
                userId = candidate.getUserId();
            }
            delegateTask.setAssignee(userId);
        }

    }
}
