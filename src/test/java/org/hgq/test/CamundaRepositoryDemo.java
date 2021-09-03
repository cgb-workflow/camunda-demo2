package org.hgq.test;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.commons.utils.IoUtil;
import org.hgq.App;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * 流程部署
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class CamundaRepositoryDemo {
    RepositoryService repositoryService;

    @Before
    public void init() {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("camunda.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        repositoryService = processEngine.getRepositoryService();
        System.out.println("repositoryService:" + repositoryService);
    }

    /**
     * classpath方式部署
     * insert into ACT_RE_DEPLOYMENT(ID_, NAME_, DEPLOY_TIME_, SOURCE_, TENANT_ID_) values(?, ?, ?, ?, ?)
     * Parameters: 1801(String), 请假流程(String), 2019-10-09 21:47:34.728(Timestamp), 本地测试(String), a(String)
     * insert into ACT_GE_BYTEARRAY( ID_, NAME_, BYTES_, DEPLOYMENT_ID_, GENERATED_, TENANT_ID_, TYPE_, CREATE_TIME_, REV_) values ( ?, ?, ?, ?, ?, ?, ?, ?, 1)
     * Parameters: 1802(String), leave.bpmn(String), java.io.ByteArrayInputStream@73c60324(ByteArrayInputStream), 1801(String), false(Boolean), null, 1(Integer), 2019-10-09 21:47:34.892(Timestamp)
     * insert into ACT_RE_PROCDEF(ID_, CATEGORY_, NAME_, KEY_, VERSION_, DEPLOYMENT_ID_, RESOURCE_NAME_, DGRM_RESOURCE_NAME_, HAS_START_FORM_KEY_, SUSPENSION_STATE_, TENANT_ID_, VERSION_TAG_, HISTORY_TTL_, STARTABLE_, REV_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )
     * Parameters: Process_1jgi0yv:1:2603(String), http://bpmn.io/schema/bpmn(String), null, Process_1jgi0yv(String), 1(Integer), 2601(String), leave.bpmn(String), null, false(Boolean), 1(Integer), a(String), null, null, true(Boolean)
     * update ACT_GE_PROPERTY SET REV_ = ?, VALUE_ = ? where NAME_ = ? and REV_ = ?    Update counts: [1]
     */
    @Test
    public void createDeployment() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deploy = deploymentBuilder.name("请假流程").source("本地测试").tenantId("a").addClasspathResource("leave.bpmn").deploy();
        System.out.println("deploymentBuilder" + deploymentBuilder);
        System.out.println("deploy" + deploy);

    }

    /**
     * 文本方式部署
     */
    @Test
    public void createDeployment2() {
        //DeploymentBuilder addString(String resourceName, String text)
        String resourceName = "new.bpmn";//资源的名称必须是以bpmn或者bpmn20.xml结尾
        String text = IoUtil.fileAsString("leave.bpmn");
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deploy = deploymentBuilder.name("请假流程").source("本地测试").tenantId("a")
                .addString(resourceName, text).deploy();
        System.out.println("deploymentBuilder" + deploymentBuilder);
        System.out.println("deploy" + deploy);

    }

    /**
     * 流方式部署
     */
    @Test
    public void createDeployment3() {
        //DeploymentBuilder addInputStream(String resourceName, InputStream inputStream);
        String resourceName = "new2.bpmn";//资源的名称必须是以bpmn或者bpmn20.xml结尾
        InputStream inputStream = CamundaRepositoryDemo.class.getClassLoader().getResourceAsStream("leave.bpmn");
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deploy = deploymentBuilder.name("请假流程").source("本地测试").tenantId("a")
                .addInputStream(resourceName, inputStream).deploy();
        System.out.println("deploymentBuilder" + deploymentBuilder);
        System.out.println("deploy" + deploy);

    }

    /**
     * 压缩流方式部署,一次可以部署多个流程
     */
    @Test
    public void createDeployment4() {
        //  DeploymentBuilder addZipInputStream(ZipInputStream zipInputStream);
        InputStream inputStream = CamundaRepositoryDemo.class.getClassLoader().getResourceAsStream("mybpmn.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deploy = deploymentBuilder.name("请假流程").source("本地测试").tenantId("a")
                .addZipInputStream(zipInputStream).deploy();
        System.out.println("deploymentBuilder" + deploymentBuilder);
        System.out.println("deploy" + deploy);

    }
}