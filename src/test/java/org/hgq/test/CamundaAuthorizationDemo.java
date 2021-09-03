package org.hgq.test;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.authorization.*;
import org.hgq.App;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 授权操作
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class CamundaAuthorizationDemo {
    @Autowired
    AuthorizationService authorizationService;

    @Before
    public void init() {

        System.out.println("authorizationService:" + authorizationService);
    }

    @Test
    public void getIdentityService() {
        init();
    }

    /**
     * select distinct RES.* from ACT_RU_AUTHORIZATION RES WHERE RES.USER_ID_ in ( ? ) order by RES.ID_ asc LIMIT ? OFFSET ?
     * 也可以向操作用户那样添加一些查询条件
     */
    @Test
    public void createAuthorizationQuery() {
        List<Authorization> list = authorizationService.createAuthorizationQuery().userIdIn("zcc2").list();
        for (int i = 0; i < list.size(); i++) {
            Authorization authorization = list.get(i);
            System.out.println(authorization.getId());
            System.out.println(authorization.getAuthorizationType());
            System.out.println(authorization.getResourceId());
            System.out.println(authorization.getGroupId());
        }


    }


    public void createAuthorization(String userId, String groupId, Resource resource, String resourceId, Permission[] permissions) {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId(userId);
        authorization.setGroupId(groupId);
        authorization.setResource(resource);
        authorization.setResourceId(resourceId);
        authorization.setPermissions(permissions);
        authorizationService.saveAuthorization(authorization);
    }

    public static class TestResource implements Resource {
        String reaourceName;
        int resrouceType;

        public TestResource(String reaourceName, int resrouceType) {
            this.reaourceName = reaourceName;
            this.resrouceType = resrouceType;

        }

        public String getReaourceName() {
            return reaourceName;
        }

        public void setReaourceName(String reaourceName) {
            this.reaourceName = reaourceName;
        }

        public int getResrouceType() {
            return resrouceType;
        }

        public void setResrouceType(int resrouceType) {
            this.resrouceType = resrouceType;
        }

        @Override
        public String resourceName() {
            return null;
        }

        @Override
        public int resourceType() {
            return 0;
        }
    }

    /**
     * Preparing: insert into ACT_RU_AUTHORIZATION ( ID_, TYPE_, GROUP_ID_, USER_ID_, RESOURCE_TYPE_, RESOURCE_ID_, PERMS_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, 1 )
     * Parameters: 601(String), 1(Integer), null, zcc2(String), 0(Integer), 60(String), 2147483647(Integer)
     */
    @Test
    public void addAuthorization() {
        Resource resource1 = new TestResource("resource1", 100);
        Resource resource2 = new TestResource("resource2", 200);
        createAuthorization("zcc2", null, resource1, "60", new Permission[]{Permissions.ALL});
    }


    @Test
    public void addAuthorization2() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("zcc3");
        authorization.setResource(Resources.APPLICATION);
        authorization.setResourceType(10);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[]{Permissions.ALL});
        authorizationService.saveAuthorization(authorization);

    }

    /**
     * 授权访问用户
     */
    @Test
    public void addAuthorization3() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("zcc3");
        authorization.setResource(Resources.USER);
        authorization.setResource(Resources.APPLICATION);
        authorization.setResourceType(1);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[]{Permissions.CREATE, Permissions.UPDATE});
        authorizationService.saveAuthorization(authorization);

    }

    /**
     * 授权访问组
     */
    @Test
    public void addAuthorization4() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("zcc3");
        authorization.setResource(Resources.GROUP);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[]{Permissions.READ, Permissions.CREATE, Permissions.UPDATE});
        authorizationService.saveAuthorization(authorization);

    }

    /**
     * 授权访问租户
     */
    @Test
    public void addAuthorization5() {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("zcc3");
        authorization.setResource(Resources.TENANT);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[]{Permissions.READ, Permissions.CREATE, Permissions.UPDATE});
        authorizationService.saveAuthorization(authorization);

    }

}