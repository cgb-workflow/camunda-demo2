package com.example;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.authorization.*;
import org.hgq.CamundaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-03-15 16:26
 **/
@SpringBootTest(classes = CamundaApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ResourcesPermissionsDemo {

    @Autowired
    AuthorizationService authorizationService;
    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;
    @Autowired
    IdentityService identityService;
    @Autowired
    ManagementService managementService;

    //一、资源与权限码说明
    /**
     * 在一个系统中所有用户可以操作的动作我们称之为“资源”，大到页面，小到按钮。
     * 不同的资源，可以配置不同的访问权限，比如可以配置指定的用户访问资源的权限，只读、只写等。
     * camunda 将所有的资源定义在{@link org.camunda.bpm.engine.authorization.Resources}类中，一个有 21 个资源定义。
     * camunda 将所有的权限定义在{@link org.camunda.bpm.engine.authorization.Permissions}类中，一个有 18 个权限码。
     * 资源通常与用户权限一起控制。
     * 同一个用户的权限是可以叠加的，比如用户张三对资源a有写、更新、删除的权限
     *
     *
     */

    //二、查询及新增用户权限

    /**
     * select distinct RES.* from ACT_RU_AUTHORIZATION RES WHERE RES.USER_ID_ in ( ? ) order by RES.ID_ asc LIMIT ? OFFSET ?
     * demo(String), 2147483647(Integer), 0(Integer)
     */
    @Test
    public void queryUserAuthorization() {

        AuthorizationQuery authorizationQuery = authorizationService.createAuthorizationQuery();
        List<Authorization> authorizationList = authorizationQuery
                .userIdIn("demo")
//                .groupIdIn("crm")
                .list();
        for (Authorization authorization : authorizationList) {
            System.out.println("##############");
            System.out.println(authorization.getId());
            System.out.println(authorization.getAuthorizationType());
            System.out.println(authorization.getGroupId());
            System.out.println(authorization.getResourceId());
            System.out.println(authorization.getResourceType());
            System.out.println("##############");
        }

    }

    public void createAuthorization(String userId, String groupId, Resource resource, Permissions[] permissions) {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId(userId);
        authorization.setGroupId(groupId);
        authorization.setResource(resource);
        authorization.setResourceId(resource.resourceType() + "");
        authorization.setPermissions(permissions);
        authorizationService.saveAuthorization(authorization);
    }

    public static class TestResource implements Resource {
        private String resourceName;
        private int resourceType;

        public TestResource(String resourceName, int resourceType) {
            this.resourceName = resourceName;
            this.resourceType = resourceType;
        }

        @Override
        public String resourceName() {
            return this.resourceName;
        }

        @Override
        public int resourceType() {
            return this.resourceType;
        }
    }

    /**
     * insert into ACT_RU_AUTHORIZATION ( ID_, TYPE_, GROUP_ID_, USER_ID_, RESOURCE_TYPE_, RESOURCE_ID_, PERMS_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, 1 )
     * 801(String), 1(Integer), null, peng(String), 100(Integer), 100(String), 2147483647(Integer)
     * <p>
     * insert into ACT_RU_AUTHORIZATION ( ID_, TYPE_, GROUP_ID_, USER_ID_, RESOURCE_TYPE_, RESOURCE_ID_, PERMS_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, 1 )
     * 802(String), 1(Integer), null, peng2(String), 200(Integer), 200(String), 32(Integer)
     */
    @Test
    public void saveUserAuthorization() {
        Resource resource1 = new TestResource("resource1", 100);
        Resource resource2 = new TestResource("resource2", 200);
        createAuthorization("peng", null, resource1, new Permissions[]{Permissions.ALL});
        createAuthorization("peng2", null, resource2, new Permissions[]{Permissions.ACCESS});
    }


    //三、授权用户登录应用

    @Test
    public void addUserAuthorization2() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("peng4");
        authorization.setGroupId(null);
        authorization.setResource(Resources.APPLICATION);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }

    @Test
    public void addUserAuthorization3() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("peng4");
        authorization.setGroupId(null);
        authorization.setResource(Resources.USER);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.READ, Permissions.CREATE});
        authorizationService.saveAuthorization(authorization);


    }

    /**
     * 授权用户有 创建用户和删除用户的权限
     * Permissions[] permissions = {Permissions.READ, Permissions.CREATE, Permissions.UPDATE, Permissions.DELETE};
     * 同一个用户对同一个资源在表里只能用一条记录，因此添加权限的时候，需要一次险添加完成。
     * 读的权限码是2，更新的权限码是4，删除的权限码是16，创建的权限码是8，因此用户最终的权限码是2+4+16+8=30
     *
     */


    //四、授权操作组、租户、授权用户的授权操作

    /**
     * 授权访问资源名称是组
     */
    @Test
    public void addUserAuthorization4() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("peng4");
        authorization.setGroupId(null);
        authorization.setResource(Resources.GROUP);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.READ, Permissions.CREATE, Permissions.UPDATE, Permissions.DELETE});
        authorizationService.saveAuthorization(authorization);
    }


    /**
     * 授权访问资源名称是租户
     */
    @Test
    public void addUserAuthorization5() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("peng4");
        authorization.setGroupId(null);
        authorization.setResource(Resources.TENANT);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.READ, Permissions.CREATE, Permissions.UPDATE, Permissions.DELETE});
        authorizationService.saveAuthorization(authorization);
    }

    /**
     * 授权访问资源名称是用户与组关系
     */
    @Test
    public void addUserAuthorization6() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("peng4");
        authorization.setGroupId(null);
        authorization.setResource(Resources.GROUP_MEMBERSHIP);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.READ, Permissions.CREATE, Permissions.UPDATE, Permissions.DELETE});
        authorizationService.saveAuthorization(authorization);
    }

    /**
     * 授权访问资源名称是租户添加用户、组权限
     */
    @Test
    public void addUserAuthorization7() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("peng4");
        authorization.setGroupId(null);
        authorization.setResource(Resources.TENANT_MEMBERSHIP);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.READ, Permissions.CREATE, Permissions.UPDATE, Permissions.DELETE});
        authorizationService.saveAuthorization(authorization);
    }

    /**
     * 授权用户的授权操作
     */
    @Test
    public void addUserAuthorization8() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("peng4");
        authorization.setGroupId(null);
        authorization.setResource(Resources.AUTHORIZATION);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.READ, Permissions.CREATE, Permissions.UPDATE, Permissions.DELETE});
        authorizationService.saveAuthorization(authorization);
    }


    //五、授权用户的cockpit操作

    /**
     * 授权cockpit模块-流程定义
     */
    @Test
    public void addUserAuthorization9() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("peng4");
        authorization.setGroupId(null);
        authorization.setResource(Resources.PROCESS_DEFINITION);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }

    /**
     * 授权cockpit模块-决策表
     */
    @Test
    public void addUserAuthorization10() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("peng4");
        authorization.setGroupId(null);
        authorization.setResource(Resources.DECISION_DEFINITION);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }

    /**
     * 授权cockpit模块-用户任务
     */
    @Test
    public void addUserAuthorization11() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("peng4");
        authorization.setGroupId(null);
        authorization.setResource(Resources.TASK);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }


    /**
     * 授权cockpit模块-流程部署
     */
    @Test
    public void addUserAuthorization12() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("peng4");
        authorization.setGroupId(null);
        authorization.setResource(Resources.DEPLOYMENT);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }


    @Test
    public void addUserAuthorization13() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("peng4");
        authorization.setGroupId(null);
        authorization.setResource(Resources.BATCH);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }

    /**
     * 授权cockpit模块-授权流程实例操作
     */
    @Test
    public void addUserAuthorization14() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("peng4");
        authorization.setGroupId(null);
        authorization.setResource(Resources.PROCESS_INSTANCE);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }


    //五、授权用户的tasklist操作

    /**
     * 授权tasklist模块-授权流程实例操作
     */
    @Test
    public void addUserAuthorization15() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("peng4");
        authorization.setGroupId(null);
        authorization.setResource(Resources.FILTER);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }


    //六、命令类授权与超级管理员使用

    /**
     * 授权cmd命令类控制
     */
    @Test
    public void setAuthorizationEnabledFalse() {

        //关闭权限校验
        processEngineConfiguration.setAuthorizationEnabled(false);

        //当前用户身份
        identityService.setAuthenticatedUserId("peng");

        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setGroupId(null);
        authorization.setUserId("peng_c");
        authorization.setResource(Resources.USER);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.READ});
        authorizationService.saveAuthorization(authorization);

        Map<String, String> properties = managementService.getProperties();
        System.out.println(properties);
    }


    /**
     * 授权cmd命令类控制
     */
    @Test
    public void setAuthorizationEnabledTrue() {

        //开启权限校验
        processEngineConfiguration.setAuthorizationEnabled(true);

        //当前用户身份
        identityService.setAuthenticatedUserId("peng");

        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setGroupId(null);
        authorization.setUserId("peng_d");
        authorization.setResource(Resources.USER);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permissions[]{Permissions.READ});
        authorizationService.saveAuthorization(authorization);

        Map<String, String> properties = managementService.getProperties();
        System.out.println(properties);
    }


    //设置当前身份
    /*
     以下代码可以强行设置当前用户身份为userId
      identityService.setAuthenticatedUserId(userId);
         try {
              //
         } finally {
             identityService.setAuthenticatedUserId(null);
         }

        有什么作用？
        比如添加了该代码，发起流程后，ACT_HI_PROCINST表的START_USER_ID_字段就会有值，否则START_USER_ID_字段就会为空
     */


   /* 关闭权限校验
    很多时候我们流程引擎服务是内网部署的，且有自己的外部用户，所以不需要使用到引擎的权限校验，那么直接关闭即可：processEngineConfiguration.setTenantCheckEnabled(false);
    @Bean
    public ProcessEngineConfiguration processEngineConfiguration() {
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDatabaseSchemaUpdate(databaseSchemaUpdate);
        processEngineConfiguration.setDataSource(dataSource);
        processEngineConfiguration.setTransactionManager(transactionManager);
        //提前设置数据库类型为mysql，加快启动速度
        processEngineConfiguration.setDatabaseType(databaseType);
        //关闭权限校验
        processEngineConfiguration.setTenantCheckEnabled(false);
        //日志记录级别
        processEngineConfiguration.setHistory(history);
        //关闭监控日志，提升性能
        processEngineConfiguration.setMetricsEnabled(isMetricsEnabled);
        return processEngineConfiguration;
    }*/


}
