package com.example;

import org.apache.commons.io.FileUtils;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.*;
import org.camunda.commons.utils.IoUtil;
import org.hgq.CamundaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

/**
 * @description: 流程定义系列
 * RepositoryService 是仓库服务类，所谓的仓库是指流程定义文档的两个文件：bpmn文件和流程图片。
 * RepositoryService 定义了一系列跟流程定义操作相关的api，比如流程的部署、查询、删除、挂起、激活、
 * 流程定义的启动人或组、流程定义元素的坐标信息、流程部署对象等API。
 * @author: huangguoqiang
 * @create: 2022-03-15 17:16
 **/
@SpringBootTest(classes = CamundaApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RepositoryServiceDemo {

    @Autowired
    RepositoryService repositoryService;

    /*
        DeploymentBuilder:用来定义流程部署相关参数
        DeploymentQuery:用来构造查询流程部署对象相关参数
        ProcessApplicationDeploymentBuilder:用来定义流程部署的相关参数

        ProcessDefinitionQuery:用来构造查询流程定义相关参数


        部署资源步骤
        首先构造 DeploymentBuilder ，
        然后设置部署资源相关的信息，如资源的名称、租户、source（来源），
        最后调用部署相关的方法。
        我们可以一次险部署多个资源，换言之，就是一次部署操作，可以批量进行文档的部署，这些分批次部署到文档同属一个部署对象。
        租户：主要是为了区分该部署操作是属于哪个系统的，比如a系统有请假流程，b系统也有请假流程，那么他们就要进行区分。
        部署对象与流程资源是一对多的关系。

        SELECT * FROM act_re_deployment;
        act_re_deployment：部署对象表，存放流程定义的显示名和部署时间，每部署一次增加一条记录


        SELECT * FROM act_re_procdef;
        act_re_procdef：流程定义表，存放流程定义的属性信息，部署每个新的流程定义都会在这个表增加一条记录，
        注意：当流程定义的key相同时，使用的是版本升级。

        SELECT * FROM act_ge_bytearray;
        act_ge_bytearray：资源文件表，存放流程定义相关的部署信息，即流程定义文档的存放地。
        每部署一次，就增加一条记录，记录是关于bpmn规则文件的，文件不是很大，是以二进制形式存储在数据库中。


     */

    //一、通过classpath部署流程

    @Test
    public void deployByClasspath() {

        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

        Deployment deployment = deploymentBuilder
                .addClasspathResource("leave_6.bpmn")
                .name("简单请假流程6")
                .deploy();

        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
        System.out.println(deploymentBuilder);
        System.out.println(deployment);


    }


    //二、文本方式部署
    //资源的名称必须是bpmn或者bpmn20.xml为后缀，否则act_re_procdef表无数据，无法启动流程实例。
    @Test
    public void addString() {
        String resoueceName = "leave_1.bpmn";
        String text = IoUtil.fileAsString("D:\\dev\\work\\test\\camunda-demo\\src\\test\\resources\\leave_1.bpmn");
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
                .name("简单请假流程")
                .source("本地测试")
                .tenantId("a")
                .addString(resoueceName, text)
                .deploy();

        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    //三、流与压缩流部署
    @Test
    public void addInputStream() {
        String resoueceName = "3.bpmn";

        InputStream inputStream = RepositoryServiceDemo.class.getClassLoader().getResourceAsStream("com.demo/ch8/diagram_1.bpmn");

        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
                .name("请求流程")
                .source("本地测试")
                .tenantId("a")
                .addInputStream(resoueceName, inputStream)
                .deploy();

        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }


    @Test
    public void addZipInputStream() {

        InputStream inputStream = RepositoryServiceDemo.class.getClassLoader().getResourceAsStream("com.demo/ch8/diagram_1.bpmn.zip");

        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
                .name("请求流程")
                .source("本地测试")
                .tenantId("a")
                .addZipInputStream(zipInputStream)
                .deploy();

        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }


    //四、已部署资源再部署(仅camunda才有)

    @Test
    public void addDeploymentResourceById() {
        //DeploymentEntity[id=0dd6f51e-a451-11ec-a34c-88b11139d73a, name=简单请假流程, resources={leave_1.bpmn=ResourceEntity[id=0dd71c2f-a451-11ec-a34c-88b11139d73a, name=leave_1.bpmn, deploymentId=0dd6f51e-a451-11ec-a34c-88b11139d73a, generated=false, tenantId=null, type=1, createTime=Tue Mar 15 19:14:17 CST 2022]}, deploymentTime=Tue Mar 15 19:14:17 CST 2022, validatingSchema=true, isNew=true, source=null, tenantId=null]
        String deploymentId = "0dd6f51e-a451-11ec-a34c-88b11139d73a";//act_re_deployment 的id
        String resourceId = "0dd71c2f-a451-11ec-a34c-88b11139d73a"; //act_ge_bytearray 的id

        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
                .name("请假假流程")
                .source("本地测试")
                .tenantId("a")
                .addDeploymentResourceById(deploymentId, resourceId)
                .deploy();

        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }


    //五、流程定义及资源批删除
    //因为删除的是流程定义，而流程定义是属于仓库的，要使用repositoryService。如果该流程定义下没有正在运行的流程，则可以普通删除。
    // 如果有关联的信息，要级联删除。删除一般开放给超级管理员。

    /**
     * delete from ACT_RU_IDENTITYLINK where PROC_DEF_ID_ = ?
     * delete from ACT_RE_PROCDEF where ID_ = ?
     * DELETE FROM ACT_RU_JOBDEF where PROC_DEF_ID_ = ?
     */
    @Test
    public void deleteProcessDefinition() {
        String processDefinationId = "leave:7:4003";
        repositoryService.deleteProcessDefinition(processDefinationId);
    }


    /**
     * delete from ACT_RU_IDENTITYLINK where PROC_DEF_ID_ = ?
     * delete B from ACT_GE_BYTEARRAY B inner join ACT_HI_JOB_LOG J on B.ID_ = J.JOB_EXCEPTION_STACK_ID_ and J.JOB_EXCEPTION_STACK_ID_ is not null and J.PROCESS_DEF_ID_ = ?
     * delete from ACT_RE_PROCDEF where ID_ = ?
     * delete from ACT_HI_INCIDENT where PROC_DEF_ID_ = ? and PROC_INST_ID_ is null
     * delete from ACT_HI_JOB_LOG where PROCESS_DEF_ID_ = ?
     * DELETE FROM ACT_RU_JOBDEF where PROC_DEF_ID_ = ?
     */
    @Test
    public void deleteProcessDefinitionCascade() {
        String processDefinationId = "leave:6:3903";
        boolean cascade = true;
        repositoryService.deleteProcessDefinition(processDefinationId, cascade);
    }


    @Test
    public void deleteProcessDefinitions() {
        String processDefinationId = "leave:6:3903";
        DeleteProcessDefinitionsSelectBuilder deleteProcessDefinitionsSelectBuilder = repositoryService.deleteProcessDefinitions();
        deleteProcessDefinitionsSelectBuilder.byIds(processDefinationId, processDefinationId).cascade().delete();
    }

    //删除部署：

    @Test
    public void deleteDeployment() {
        String deploymentId = "";
        repositoryService.deleteDeployment(deploymentId);
    }

    //六、获取流程定义文档资源
    //camunda没有根据流程文档生成图片的方法，所以部署的时候需要提前将流程图生成好，并部署的时候一起部署

    //七、流程定义文档、图片、坐标获取

    /**
     * select * from ACT_RE_PROCDEF where ID_ = ?   leave:5:3803(String)
     * select * from ACT_RE_DEPLOYMENT where ID_ = ?  3801(String)
     * select * from ACT_GE_BYTEARRAY where DEPLOYMENT_ID_ = ? and NAME_ = ?   3801(String), diagram_1.bpmn
     * select * from ACT_RE_PROCDEF where DEPLOYMENT_ID_ = ? and KEY_ = ?  3801(String), leave(String)
     * select * from ACT_RE_PROCDEF RES where KEY_ = ? and TENANT_ID_ = ? and VERSION_ = ( select max(VERSION_) from ACT_RE_PROCDEF where KEY_ = ? and TENANT_ID_ = ?)
     * leave(String), a(String), leave(String), a(String)
     */
    @Test
    public void getProcessModel() throws IOException {
        String processDefinitionId = "leave:5:3803";
        InputStream inputStream = repositoryService.getProcessModel(processDefinitionId);
        FileUtils.copyInputStreamToFile(inputStream, new File("/Users/zhoupeng/IdeaProjects/activiti-demo/camunda/camunda-simple/src/main/resources/com.demo/ch8/tmp.txt"));
    }


    @Test
    public void getProcessDiagram() throws IOException {
        String processDefinitionId = "leave_1:1:b5eb92bc-a456-11ec-b323-88b11139d73a";
        InputStream inputStream = repositoryService.getProcessDiagram(processDefinitionId);
        FileUtils.copyInputStreamToFile(inputStream, new File("D:\\dev\\work\\test\\camunda-demo\\src\\test\\resources\\leave_1.png"));
    }


    /**
     * select distinct RES.* from ACT_RE_DEPLOYMENT RES order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createDeploymentQuery() throws IOException {
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        List<Deployment> list = deploymentQuery.list();
        for (Deployment deployment : list) {
            System.out.println(deployment.toString());
            repositoryService.deleteDeployment(deployment.getId());
        }
    }

    @Test
    public void createProcessDefinitionQuery() throws IOException {

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> list = processDefinitionQuery.list();
        for (ProcessDefinition definition : list) {
            System.out.println("===================");
            System.out.println("definition.getId():" + definition.getId());
            System.out.println("definition.getKey():" + definition.getKey());
            System.out.println("definition.getName():" + definition.getName());
            System.out.println("definition.getDeploymentId():" + definition.getDeploymentId());
            System.out.println("definition.getDiagramResourceName():" + definition.getDiagramResourceName());
            System.out.println("definition.getVersionTag():" + definition.getVersionTag());
            System.out.println("definition.getVersion():" + definition.getVersion());
            System.out.println("definition.getResourceName():" + definition.getResourceName());
            System.out.println("definition.getTenantId():" + definition.getTenantId());
            System.out.println("===================");

        }
    }


    @Test
    public void getProcessDiagramLayout() throws IOException {
        String processDefinitionId = "paiche:1:503";

        DiagramLayout diagramLayout = repositoryService.getProcessDiagramLayout(processDefinitionId);

        Map<String, DiagramElement> elements = diagramLayout.getElements();
        Set<Map.Entry<String, DiagramElement>> entries = elements.entrySet();

        for (Map.Entry<String, DiagramElement> entry : entries) {
            String key = entry.getKey();
            DiagramElement value = entry.getValue();

            System.out.println(key + "," + value.getId());
        }

        DeploymentQuery deploymentQuery =
                (DeploymentQuery) diagramLayout;
        List<Deployment> list = deploymentQuery.list();
        for (Deployment deployment : list) {
            System.out.println(deployment.toString());
        }
    }


}
