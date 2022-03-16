package org.hgq.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

/**
 * org.camunda.bpm.engine.delegate.TaskListener#EVENTNAME_CREATE 创建事件：任务创建时候设置当前任务的办理人
 * org.camunda.bpm.engine.delegate.TaskListener#EVENTNAME_ASSIGNMENT 分配事件：任设置当前任务的办理人
 * 如果一个用户任务配置的任务监听器中，监听了2个事件，EVENTNAME_CREATE 和 EVENTNAME_ASSIGNMENT ，那么先执行 EVENTNAME_CREATE，然后在执行 EVENTNAME_ASSIGNMENT，
 * 如果都设置了当前任务的办理人，那么 EVENTNAME_ASSIGNMENT 事件设置的办理人会覆盖前者。
 */
public class MyTaskListener implements TaskListener {
    public void notify(DelegateTask delegateTask) {
        System.out.println("#########");
        String eventName = delegateTask.getEventName();
        System.out.println(eventName);//create delete等
        int priority = delegateTask.getPriority();
        System.out.println(priority);
        //注意这里，如果不判断类型，则可能陷入死循环（如果配置了assignment类型的listener）
        if (eventName.equals("create")) {
            System.out.println("create 事件");
            //这里设置的办理人 是 当前任务的办理人
            delegateTask.setAssignee("张太公");
        }else if (eventName.equals("assignment")) {
            System.out.println("assignment 事件");
            //这里设置的办理人是当前任务的办理人，会覆盖create事件设置的当前任务的办理人
            delegateTask.setAssignee("哈喽");
        }
        System.out.println("#########");
    }


}
