package org.hgq.test;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.*;
import org.camunda.bpm.engine.impl.identity.Account;
import org.camunda.bpm.engine.impl.persistence.entity.GroupEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TenantEntity;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import org.camunda.commons.utils.IoUtil;
import org.hgq.App;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @description: 操作用户
 * @author: huangguoqiang
 * @create: 2021-08-18 15:58
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class CamundaUserDemo {

    @Autowired
    IdentityService identityService;


    @Before
    public void init() {
        /*ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("camunda.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        identityService = processEngine.getIdentityService();*/
        System.out.println("identityService:" + identityService);
    }

    @Test
    public void getIdentityService() {
        init();
    }

    /**
     * insert into ACT_ID_USER (ID_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, REV_) values ( ?, ?, ?, ?, ?, ?, 1 )  Update counts: [1]
     * Parameters: zcc1(String), zcc(String), zcc(String), zcc@qq.com(String), 1(String), 1(String)
     */
    @Test
    public void saveUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("zcc1");
        userEntity.setEmail("zcc@qq.com");
        userEntity.setFirstName("zcc");
        userEntity.setLastName("zcc");
        userEntity.setDbPassword("1");
        userEntity.setSalt("1");
        identityService.saveUser(userEntity);
    }

    /**
     * 密码加密的方式
     */
    @Test
    public void saveUser2() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("zcc2");
        userEntity.setEmail("zcc2@qq.com");
        userEntity.setFirstName("zcc");
        userEntity.setLastName("zcc");
        userEntity.setPassword("1");
        identityService.saveUser(userEntity);
    }

    /**
     * 密码加密的方式
     */
    @Test
    public void saveUser3() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("zcc3");
        userEntity.setPassword("1");
        identityService.saveUser(userEntity);
    }

    @Test
    public void test22() {
        List<String> employees = Arrays.asList("wangwu", "lisi", "zhangsan");
        Queue<String> queue = new LinkedBlockingQueue<>(employees);

        for (String s : queue) {
            System.out.println(s);
        }
        System.out.println("================");

        String s1 = queue.poll();
        System.out.println(s1);
        queue.offer(s1);

        String s2 = queue.poll();
        System.out.println(s2);
        queue.offer(s2);

/*        String s3 = queue.poll();
        System.out.println(s3);
        queue.offer(s3);*/

        System.out.println("================");
        for (String s : queue) {
            System.out.println(s);
        }

    }

    /**
     * select distinct RES.* from ACT_ID_USER RES order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createUserQuery() {
        UserQuery userQuery = identityService.createUserQuery();
        List<User> list = userQuery.list();
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            System.out.println(user.getFirstName());
            System.out.println(user.getLastName());
            System.out.println(user.getId());
            System.out.println(user.getPassword());
            System.out.println(user.getEmail());
        }

    }

    /**
     * select distinct RES.* from ACT_ID_USER RES WHERE RES.ID_ in ( ? , ? ) order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createUserQuery2() {
        UserQuery userQuery = identityService.createUserQuery();
        List<User> list = userQuery.userIdIn("zcc1", "zcc2").
                list();
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            System.out.println(user.getFirstName());
            System.out.println(user.getLastName());
            System.out.println(user.getId());
            System.out.println(user.getPassword());
            System.out.println(user.getEmail());
        }

    }

    /**
     * select distinct RES.* from ACT_ID_USER RES order by RES.ID_ asc LIMIT ? OFFSET ?
     * Parameters: 3(Integer), 0(Integer)
     */
    @Test
    public void listPage() {
        int maxResult = 3;
        int fistResult = maxResult * (1 - 1);
        UserQuery userQuery = identityService.createUserQuery();

        List<User> list = userQuery.listPage(fistResult, maxResult);
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            System.out.println(user.getFirstName());
            System.out.println(user.getLastName());
            System.out.println(user.getId());
            System.out.println(user.getPassword());
            System.out.println(user.getEmail());
        }


    }

    /**
     * delete from ACT_ID_USER where ID_ = ? and REV_ = ?
     * delete from ACT_ID_MEMBERSHIP where USER_ID_ = ?    Update counts: [0]
     * Result 1:   delete from ACT_ID_TENANT_MEMBER where USER_ID_ = ?    Update counts: [0]
     * Result 2:   delete from ACT_ID_USER where ID_ = ? and REV_ = ? Update counts: [1]
     */
    @Test
    public void deleteUser() {
        String userId = "zcc1";
        identityService.deleteUser(userId);


    }

    /**
     * insert into ACT_ID_GROUP (ID_, NAME_, TYPE_, REV_) values ( ?, ?, ?, 1 )
     * Parameters: group1(String), 项目1(String), 工作流(String)
     */
    @Test
    public void saveGroup() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId("group1");
        groupEntity.setName("项目1");
        groupEntity.setType("工作流");
        identityService.saveGroup(groupEntity);
    }

    /**
     * select distinct RES.* from ACT_ID_GROUP RES order by RES.ID_ asc LIMIT ? OFFSET ?
     * 组查询可以和用户查询一样添加查询条件
     */
    @Test
    public void createGroupQuery() {

        GroupQuery groupQuery = identityService.createGroupQuery();
        List<Group> list = groupQuery.list();
        for (int i = 0; i < list.size(); i++) {
            Group group = list.get(i);
            System.out.println(group.getId());
            System.out.println(group.getName());
            System.out.println(group.getType());
            System.out.println("==================================");
        }
    }

    /**
     * 创建组和用户的联系
     * insert into ACT_ID_MEMBERSHIP (USER_ID_, GROUP_ID_) values ( ?, ? )
     */
    @Test
    public void createMembership() {
        String userId = "zcc2";
        String groupId = "group1";
        identityService.createMembership(userId, groupId);
    }

    @Test
    public void createUserQueryMemberOfGroup() {
        UserQuery userQuery = identityService.createUserQuery();
        List<User> users = userQuery.memberOfGroup("group1").list();
        for (User user : users) {
            System.out.println(user.getId());
        }

    }

    /**
     * delete from ACT_ID_GROUP where ID_ = ? and REV_ = ?
     * delete from ACT_ID_MEMBERSHIP where GROUP_ID_ = ?   Update counts: [1]
     * delete from ACT_ID_TENANT_MEMBER where GROUP_ID_ = ?    Update counts: [0]
     * delete from ACT_ID_GROUP where ID_ = ? and REV_ = ?  Update counts: [1]
     */
    @Test
    public void deleteGroup() {
        String groupId = "group1";
        identityService.deleteGroup(groupId);
    }

    /**
     * 租户存在的目的就是区分不同的系统，A系统和B系统的用户是不一样的，所以要区分
     * insert into ACT_ID_TENANT (ID_, NAME_, REV_) values ( ?, ?, 1 )
     * Parameters: A(String), A系统(String)
     */
    @Test
    public void saveTenant() {
        TenantEntity tenantEntity = new TenantEntity();
        tenantEntity.setId("A");
        tenantEntity.setName("A系统");
        identityService.saveTenant(tenantEntity);
    }

    /**
     * 租户和用户
     * insert into ACT_ID_TENANT_MEMBER (ID_, TENANT_ID_, USER_ID_, GROUP_ID_) values ( ?, ?, ?, ? )
     * Parameters: 101(String), A(String), zcc2(String), null
     */
    @Test
    public void createTenantUserMembership() {
        String tenantId = "A";
        String userId = "zcc2";
        identityService.createTenantUserMembership(tenantId, userId);
    }

    /**
     * 租户和组
     * insert into ACT_ID_TENANT_MEMBER (ID_, TENANT_ID_, USER_ID_, GROUP_ID_) values ( ?, ?, ?, ? )
     * 201(String), A(String), null, group1(String)
     */
    @Test
    public void createTenantGroupMembership() {
        String tenantId = "A";
        String groupId = "group1";
        identityService.createTenantGroupMembership(tenantId, groupId);
    }

    /**
     * 查询租户下面有哪些用户
     * select distinct RES.* from ACT_ID_USER RES inner join ACT_ID_TENANT_MEMBER TM on RES.ID_ = TM.USER_ID_ WHERE TM.TENANT_ID_ = ? order by RES.ID_ asc LIMIT ? OFFSET ?
     * Parameters: A(String), 2147483647(Integer), 0(Integer)
     */
    @Test
    public void createUserQueryMemberOfTenant() {
        String tenantId = "A";
        UserQuery userQuery = identityService.createUserQuery();
        // userQuery.memberOfGroup()
        List<User> list = userQuery.memberOfTenant(tenantId).list();
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            System.out.println(user.getFirstName());
            System.out.println(user.getLastName());
            System.out.println(user.getId());
            System.out.println(user.getPassword());
            System.out.println(user.getEmail());
        }
    }

    /**
     * 查询租户下面有哪些组
     * select distinct RES.* from ACT_ID_GROUP RES inner join ACT_ID_TENANT_MEMBER TM on RES.ID_ = TM.GROUP_ID_ WHERE TM.TENANT_ID_ = ? order by RES.ID_ asc LIMIT ? OFFSET ?
     * Parameters: A(String), 2147483647(Integer), 0(Integer)
     */
    @Test
    public void createGroupQueryMemberOfTenant() {
        String tenantId = "A";
        GroupQuery groupQuery = identityService.createGroupQuery();
        List<Group> list = groupQuery.memberOfTenant(tenantId).list();
        for (int i = 0; i < list.size(); i++) {
            Group group = list.get(i);
            System.out.println(group.getId());
            System.out.println(group.getName());
            System.out.println(group.getType());
        }
    }

    /**
     * 添加用户相关信息
     * insert into ACT_ID_INFO (ID_, USER_ID_, TYPE_, KEY_, VALUE_, PASSWORD_, PARENT_ID_, REV_) values ( ?, ?, ?, ?, ?, ?, ?, 1 )
     * Parameters: 301(String), zcc2(String), account(String), zccAccountName(String), zccAccountUsername(String), java.io.ByteArrayInputStream@5ef0d29e(ByteArrayInputStream), null
     */
    @Test
    public void setUserAccount() {
        String userId = "zcc2";
        String userPassword = "1";
        String accountName = "zccAccountName";
        String accountUsername = "zccAccountUsername";
        String accountPassword = "zccAccountPassword";
        Map<String, String> accountDetails = new HashMap<>();
        accountDetails.put("a", "a");
        identityService.setUserAccount(userId, userPassword, accountName, accountUsername, accountPassword, accountDetails);

    }

    /**
     * 查询用户相关信息
     * select * from ACT_ID_INFO where USER_ID_ = ? and KEY_ = ? and PARENT_ID_ is null
     * Parameters: zcc2(String), zccAccountName(String)
     */
    @Test
    public void getUserAccount() {
        String userId = "zcc2";
        String userPassword = "1";
        String accountName = "zccAccountName";
        Account userAccount = identityService.getUserAccount(userId, userPassword, accountName);
        System.out.println("@@@@@");
        System.out.println(userAccount);
    }

    @Test
    public void setUserInfo() {
        String userId = "zcc2";
        String key = "b";
        String value = "b";
        // void setUserInfo(String userId, String key, String value);
        identityService.setUserInfo(userId, key, value);
//        identityService.getUserInfo()
    }

    /**
     * 添加用户图像信息
     * insert into ACT_ID_INFO (ID_, USER_ID_, TYPE_, KEY_, VALUE_, PASSWORD_, PARENT_ID_, REV_) values ( ?, ?, ?, ?, ?, ?, ?, 1 )
     * Parameters: 501(String), zcc2(String), null, picture(String), 502(String), null, null
     * insert into ACT_GE_BYTEARRAY(ID_, NAME_, BYTES_, DEPLOYMENT_ID_, TENANT_ID_, TYPE_, CREATE_TIME_, ROOT_PROC_INST_ID_, REMOVAL_TIME_, REV_) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )
     * Parameters: 502(String), jpg(String), java.io.ByteArrayInputStream@58bf8650(ByteArrayInputStream), null, null, 1(Integer), 2019-10-07 01:56:06.029(Timestamp), null, null
     */
    @Test
    public void setUserPicture() {
        String userId = "zcc2";
        /**
         * public Picture(byte[] bytes, String mimeType) {
         *     this.bytes = bytes;
         *     this.mimeType = mimeType;
         *   }
         */
        byte[] bytes = IoUtil.fileAsByteArray(new File("src/main/resources/2.jpg"));
        Picture picture = new Picture(bytes, "jpg");
        //void setUserPicture(String userId, Picture picture);
        identityService.setUserPicture(userId, picture);
    }

}
