package org.hgq.test;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.Deployment;
import org.hgq.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2021-08-24 11:38
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class LeaveDemo {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;

    /**
     * 流程定义部署
     */
    @Test
    public void deploy() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("bpmn/leave01.bpmn")
                .name("请假流程")
                .deploy();
        //部署Id
        System.out.println(deploy.getName());//84c0a64c-cc1c-11ea-85a7-98fa9b4e8fcb
    }

    @Test
    public void test() {


        List<Person> peoples = Arrays.asList(
                new Person(1, "zhangsan", 22),
                new Person(1, "lisi", 23),
                new Person(1, "zhaoliu", null),
                new Person(2, "wangwu", null));
        // Map<String, List<Person>> collect = peoples.stream().collect(Collectors.groupingBy(person -> person.groupId + "," + person.age));

        List<Person> collect = peoples.stream().filter(new Predicate<Person>() {
            @Override
            public boolean test(Person person) {
                if (person.getAge() == null) {
                    return false;
                }
                return true;
            }
        }).collect(Collectors.toList());

        System.out.println(collect);
    }

    @Test
    public void test22() {
        Person zhangsan = new Person(1, "zhangsan", 22);

        Ha h = new Ha();
        h.setP(zhangsan);

        Person p = h.getP();
        p.setAge(36);
        p.setName("zs");

        System.out.println("https://flm-all.obs.ap-southeast-2.myhuaweicloud.com:443/fslidcard/9edbe9c2-b3fd-4300-903b-36296be27213.jpg?AccessKeyId=WZ35JXPLW0U3HHDDMWKC&Expires=1634947796&Signature=RnZMIP8SY3r%2Bf%2Bud%2FoNew24U9%2B8%3D".length());

    }

    /*  public static void main(String[] args) {
          //String ur ="https:\\\\/\\\\/flm-all.obs.ap-southeast-2.myhuaweicloud.com:443\\\\/fslidcard\\\\/9edbe9c2-b3fd-4300-903b-36296be27213.jpg?AccessKeyId=WZ35JXPLW0U3HHDDMWKC&Expires=1634947796&Signature=RnZMIP8SY3r%2Bf%2Bud%2FoNew24U9%2B8%3D\\";
          String ur = "http://static-asset-internal.flashexpress.com/backyardUpload/1617724272-4c744c7e03cf4fad951c81e5c6398bb4.jpg";


          String regUrl = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$";
          Pattern p = Pattern.compile(regUrl);
          Matcher m = p.matcher(ur);
          if (m.matches()) {
              System.out.println(111);
          }

        /*  String ur = "httppp://static-asset-internal.flashexpress.com/backyardUpload/1617724272-4c744c7e03cf4fad951c81e5c6398bb4.jpg";

          if (ur.startsWith("http://") || ur.startsWith("https://")) {

          }


      }*/

    @Test
    public void ttest() {
        checkNotNull("123", "hh",null);
    }

    private void checkNotNull(String code, Object... objs) {
        if (objs == null) {
            throw new RuntimeException(code);
        }

        if (objs.length > 1) {
            for (Object obj : objs) {
                if (obj == null) {
                    throw new RuntimeException(code);
                }
            }
        }
    }

    static class Ha {

        Person p;

        public Person getP() {
            return p;
        }

        public void setP(Person p) {
            this.p = p;
        }

        @Override
        public String toString() {
            return "Ha{" +
                    "p=" + p +
                    '}';
        }
    }

    static class Person {
        private Integer groupId;
        private String name;
        private Integer age;

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Integer getGroupId() {
            return groupId;
        }

        public void setGroupId(Integer groupId) {
            this.groupId = groupId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Person(Integer groupId, String name, Integer age) {
            this.groupId = groupId;
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "groupId=" + groupId +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
