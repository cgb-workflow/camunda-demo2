package org.hgq.delegate;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-06-28 19:33
 **/
public class TestDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String processInstanceId = execution.getProcessInstanceId();

        TaskService taskService = execution.getProcessEngineServices()
                .getTaskService();
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        for (Task task : list) {
            System.out.println("task.getId: " + task.getId());
            System.out.println("task.getName: " + task.getName());
            if (task.getDueDate() != null && task.getDueDate().compareTo(new Date()) < 0) {
                System.out.println("任务超时了: " + task.getId());

                Map<String, Object> vars = new HashMap<>();
                vars.put("timeout", true);
                taskService.complete(task.getId(), vars);

            }
        }

    }
}
