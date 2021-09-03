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
    }
}
