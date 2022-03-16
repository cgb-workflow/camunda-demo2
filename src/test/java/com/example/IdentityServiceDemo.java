package com.example;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.*;
import org.camunda.bpm.engine.impl.persistence.entity.GroupEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TenantEntity;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import org.camunda.commons.utils.IoUtil;
import org.hgq.CamundaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 人员组织架构系列
 * IdentityService 主要是操作用户、组、用户与组、用户信息等。
 * 操作租户，以及给租户下面添加人或者组。
 * 设置系统当前的操作人
 * @author: huangguoqiang
 * @create: 2022-03-15 15:45
 **/
@SpringBootTest(classes = CamundaApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class IdentityServiceDemo {

    @Autowired
    private IdentityService identityService;

    //一、用户创建的两种方式及密码加密

    /**
     * insert into ACT_ID_USER (ID_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, REV_) values ( ?, ?, ?, ?, ?, ?, 1 )
     * <p>
     * peng(String), peng(String), hao(String), 123@qq.com(String), 1(String), 1(String)
     */
    @Test
    public void saveUser() {
        UserEntity user = new UserEntity();
        user.setId("peng");
        user.setEmail("123@qq.com");
        user.setFirstName("peng");
        user.setLastName("hao");
        //该方式用户密码没有加密以及没有加盐
        user.setDbPassword("1");
        user.setSalt("1");
        identityService.saveUser(user);
    }

    /**
     * insert into ACT_ID_USER (ID_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, REV_) values ( ?, ?, ?, ?, ?, ?, 1 )
     * peng2(String), 3(String), 1(String), 123@qq.com(String), {SHA-512}3+H52awaeQkouH/J94W6314xfD9wHCERyUfVRBovtemLsIAco29UXXKg902ISWMs7EgLov4WB0oRc8vwk5REQA==(String), iIKLPiLUIcIN1rfz9gEC3A==(String)
     */
    @Test
    public void saveUser2() {
        UserEntity user = new UserEntity();
        user.setId("peng2");
        user.setEmail("123@qq.com");
        user.setFirstName("2");
        user.setLastName("3");
        user.setPassword("1");
        user.setSalt("1");
        identityService.saveUser(user);
    }


    //二、用户列表查询及过滤


    /**
     * select distinct RES.* from ACT_ID_USER RES order by RES.ID_ asc LIMIT ? OFFSET ?
     * 2147483647(Integer), 0(Integer)
     */
    @Test
    public void createUserQuery() {
        UserQuery userQuery = identityService.createUserQuery();

        List<User> userList = userQuery
//                .userIdIn("peng1", "peng2")
                .userEmailLike("%qq%")
                .list();
        for (User user : userList) {
            System.out.println("##################");
            System.out.println(user.toString());
            System.out.println("##################");
        }

    }


    /**
     * select distinct RES.* from ACT_ID_USER RES order by RES.ID_ asc LIMIT ? OFFSET ?
     * <p>
     * 0(Integer), 3(Integer)
     */
    @Test
    public void createUserQueryPage() {
        UserQuery userQuery = identityService.createUserQuery();
        int max = 2;
        int firstResult = max * (2 - 1);
        List<User> userList = userQuery
                .listPage(3, firstResult);
        for (User user : userList) {
            System.out.println("##################");
            System.out.println(user.toString());
            System.out.println("##################");
        }
    }


    //三、用户删除

    /**
     * delete from ACT_ID_MEMBERSHIP where USER_ID_ = ?
     * peng33(String)
     * delete from ACT_ID_TENANT_MEMBER where USER_ID_ = ?
     * peng33(String)
     * delete from ACT_ID_USER where ID_ = ? and REV_ = ?
     * peng33(String), 1(Integer)
     */
    @Test
    public void deleteUser() {
        String userId = "peng33";
        identityService.deleteUser(userId);
    }


    //四、组的CRUD及与用户建立关系

    /**
     * insert into ACT_ID_GROUP (ID_, NAME_, TYPE_, REV_) values ( ?, ?, ?, 1 )
     * <p>
     * dep(String), 项目经理(String), workflow(String)
     */
    @Test
    public void saveGroup() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId("dep");
        groupEntity.setName("项目经理");
        groupEntity.setType("workflow");
        identityService.saveGroup(groupEntity);
    }


    /**
     * select distinct RES.* from ACT_ID_GROUP RES order by RES.ID_ asc LIMIT ? OFFSET ?
     * 2147483647(Integer), 0(Integer)
     */
    @Test
    public void createGroupQuery() {

        GroupQuery groupQuery = identityService.createGroupQuery();
        List<Group> groupList = groupQuery.list();

        for (Group group : groupList) {
            System.out.println("###############");
            System.out.println(group.toString());
            System.out.println("###############");
        }
    }


    /**
     * insert into ACT_ID_MEMBERSHIP (USER_ID_, GROUP_ID_) values ( ?, ? )
     * peng(String), dep(String)
     */
    @Test
    public void createMemberShip() {
        String userId = "peng2";
        String groupId = "dep";
        identityService.createMembership(userId, groupId);
    }


    /**
     * delete from ACT_ID_MEMBERSHIP where GROUP_ID_ = ?
     * dep(String)
     * delete from ACT_ID_TENANT_MEMBER where GROUP_ID_ = ?
     * dep(String)
     * delete from ACT_ID_GROUP where ID_ = ? and REV_ = ?
     * dep(String), 1(Integer)
     */
    @Test
    public void deleteGroup() {
        String groupId = "dep";
        identityService.deleteGroup(groupId);
    }


    //五、租户添加用户、组及查询
    //创建租户目的：租户存在的目的是区分不同的系统，比如A系统和B系统的用户不一样，所以要区分

    /**
     * insert into ACT_ID_TENANT (ID_, NAME_, REV_) values ( ?, ?, 1 )
     * crm(String), 客户关系管理系统(String)
     */
    @Test
    public void saveTenant() {
        TenantEntity tenantEntity = new TenantEntity();
        tenantEntity.setId("crm");
        tenantEntity.setName("客户关系管理系统");
        identityService.saveTenant(tenantEntity);
    }


    //租户下面添加用户、组、租户下人查询

    /***
     *
     *  insert into ACT_ID_TENANT_MEMBER (ID_, TENANT_ID_, USER_ID_, GROUP_ID_) values ( ?, ?, ?, ? )
     *  8401(String), crm(String), peng(String), null
     *
     */
    @Test
    public void createTenantUserMemberShip() {
        String tenantId = "crm";
        String userId = "peng";
        identityService.createTenantUserMembership(tenantId, userId);
    }

    /***
     *
     *   insert into ACT_ID_TENANT_MEMBER (ID_, TENANT_ID_, USER_ID_, GROUP_ID_) values ( ?, ?, ?, ? )
     *   8501(String), crm(String), null, dep(String)
     *
     */
    @Test
    public void createTenantGroupMemberShip() {
        String tenantId = "crm";
        String groupId = "dep";
        identityService.createTenantGroupMembership(tenantId, groupId);
    }


    /**
     * select distinct RES.* from ACT_ID_USER RES inner join ACT_ID_TENANT_MEMBER TM on RES.ID_ = TM.USER_ID_ WHERE TM.TENANT_ID_ = ? order by RES.ID_ asc LIMIT ? OFFSET ?
     * crm(String), 2147483647(Integer), 0(Integer)
     */
    @Test
    public void createUserQueryMemberOfTenant() {
        String tenantId = "crm";
        UserQuery userQuery = identityService.createUserQuery();
        List<User> userList = userQuery.memberOfTenant(tenantId).list();
        for (User user : userList) {
            System.out.println("##################");
            System.out.println(user.toString());
            System.out.println("##################");
        }
    }

    /**
     * select distinct RES.* from ACT_ID_GROUP RES inner join ACT_ID_TENANT_MEMBER TM on RES.ID_ = TM.GROUP_ID_ WHERE TM.TENANT_ID_ = ? order by RES.ID_ asc LIMIT ? OFFSET ?
     * crm(String), 2147483647(Integer), 0(Integer)
     */
    @Test
    public void createGroupQueryMemberOfTenant() {
        String tenantId = "crm";
        GroupQuery groupQuery = identityService.createGroupQuery();
        List<Group> groupList = groupQuery.memberOfTenant(tenantId).list();
        for (Group group : groupList) {
            System.out.println("##################");
            System.out.println(group.toString());
            System.out.println("##################");
        }
    }


    //六、用户信息CRUD

    //accountDetails 信息是以 key value 形式保存

    /**
     * insert into ACT_ID_INFO (ID_, USER_ID_, TYPE_, KEY_, VALUE_, PASSWORD_, PARENT_ID_, REV_) values ( ?, ?, ?, ?, ?, ?, ?, 1 )
     * 8601(String), peng2(String), account(String), pengaccountName(String), pengaccountUserName(String), java.io.ByteArrayInputStream@5f2f577(ByteArrayInputStream), null
     * 8602(String), null, null, a(String), a(String), null, 8601(String)
     * 8603(String), null, null, b(String), b(String), null, 8601(String)
     */
    @Test
    public void setUserAccount() {
        String userId = "peng2";
        String userPassword = "1";
        String accountName = "pengaccountName";
        String accountUserName = "pengaccountUserName";
        String accountPassword = "pengaccountPassword";
        Map<String, String> accountDetails = new HashMap<String, String>();
        accountDetails.put("a", "a");
        accountDetails.put("b", "b");
        identityService.setUserAccount(userId, userPassword, accountName, accountUserName, accountPassword, accountDetails);
    }


    /**
     * select KEY_ from ACT_ID_INFO where USER_ID_ = ? and TYPE_ = ? and PARENT_ID_ is null
     * peng2(String), account(String)
     */
    @Test
    public void getUserAccountNames() {
        String userId = "peng2";
        List<String> userAccountNames = identityService.getUserAccountNames(userId);
        System.out.println(userAccountNames);
    }


    /**
     * insert into ACT_ID_INFO (ID_, USER_ID_, TYPE_, KEY_, VALUE_, PASSWORD_, PARENT_ID_, REV_) values ( ?, ?, ?, ?, ?, ?, ?, 1 )
     * 8701(String), peng2(String), userinfo(String), c(String), c(String), null, null
     */
    @Test
    public void setUserInfo() {
        String userId = "peng2";
        identityService.setUserInfo(userId, "c", "c");
    }


    /**
     * delete from ACT_ID_INFO where ID_ = ? and REV_ = ?
     * 8701(String), 1(Integer)
     */
    @Test
    public void deleteUserInfo() {
        String userId = "peng2";
        identityService.deleteUserInfo(userId, "c");
    }


    //用户添加头像
    public void saveUserPicture() {

        String userId = "peng";
        byte[] bytes = IoUtil.fileAsByteArray(new File("d:/test/1.jpg"));
        Picture picture = new Picture(bytes, "jpg");
        identityService.setUserPicture(userId, picture);
    }

    //七、人员组织架构相关六张表说明
/**
 * SELECT * from act_id_user;存放用户
 * SELECT * from act_id_group;存放组
 * SELECT * from act_id_info;存放用户个人信息
 * SELECT * from act_id_membership;存放用户与组的关联信息
 * SELECT * from act_id_tenant;存放租户信息
 * SELECT * from act_id_tenant_member;存放租户与人或者组的关联信息
 */


}
