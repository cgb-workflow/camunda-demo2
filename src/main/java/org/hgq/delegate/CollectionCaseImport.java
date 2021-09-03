package org.hgq.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.hgq.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2021-08-19 16:16
 **/
@Service
public class CollectionCaseImport implements JavaDelegate {

    @Autowired
    private OrderService orderService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("入催自动分单----------开始");
        String processBusinessKey = execution.getProcessBusinessKey();
        String processDefinitionId = execution.getProcessDefinitionId();
        String businessKey = execution.getBusinessKey();
        String processInstanceId = execution.getProcessInstanceId();
        System.out.println("processBusinessKey: " + processBusinessKey);
        System.out.println("processDefinitionId: " + processDefinitionId);
        System.out.println("businessKey: " + businessKey);
        System.out.println("processInstanceId: " + processInstanceId);

        Object aCase = execution.getVariable("case");
        Object emp = execution.getVariable("emp");
        String order = orderService.getOrder();

        System.out.println("入催自动分单----------结束");

    }
}
