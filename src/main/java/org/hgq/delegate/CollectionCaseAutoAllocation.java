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
 * @create: 2021-09-04 11:22
 **/
@Service
public class CollectionCaseAutoAllocation implements JavaDelegate {


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

        Object emp = execution.getVariable("candidateEmps");
        System.out.println("emp: " + emp);
        //execution.setVariable("candidateUsers",emp);
        System.out.println("入催自动分单----------结束");


    }
}