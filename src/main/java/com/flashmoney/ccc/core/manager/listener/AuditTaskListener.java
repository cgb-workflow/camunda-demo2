package com.flashmoney.ccc.core.manager.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.task.IdentityLink;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-06-27 21:28
 **/
@Service
public class AuditTaskListener implements TaskListener {
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

