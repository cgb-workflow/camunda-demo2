package org.hgq.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.task.IdentityLink;

import java.util.Set;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-03-16 17:47
 **/
public class MyTaskListener2 implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        if (TaskListener.EVENTNAME_CREATE.equals(delegateTask.getEventName())) {

            //获取候选人
            Set<IdentityLink> candidates = delegateTask.getCandidates();
            String userId = null;
            for (IdentityLink candidate : candidates) {
                userId = candidate.getUserId();
            }
            //把候选人设置办理人
            delegateTask.setAssignee(userId);
        }
    }
}
