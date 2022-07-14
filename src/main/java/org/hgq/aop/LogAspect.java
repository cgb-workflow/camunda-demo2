package org.hgq.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
 
@Aspect // @Aspect注解就是告诉 Spring 这是一个aop类，AOP切面
@Component
@Slf4j
public class LogAspect {
 
    @Autowired
    private Environment environment;
 
    /**
     * 用@Pointcut来注解一个切入方法
     * @Pointcut注解 声明这是一个需要拦截的切面，也就是说，当调用任何一个controller方法的时候，都会激活这个aop
     */
    @Pointcut("@annotation(org.hgq.annotation.MyTestAop)")
    public void Pointcut() {
    }
 
    //@Before("Pointcut()")
    public void beforeMethod(JoinPoint joinPoint) {
        String system = environment.getProperty("spring.application.system");
        String name = environment.getProperty("spring.application.name");
        if (system == null || "".equals(system)) {
            system = name;
        }
        MDC.put("system", system);
        MDC.put("app", name);

    }
 
    //@After("Pointcut()")
    public void afterMethod(JoinPoint joinPoint) {
        //MDC.clear();
    }
 
    /**
     * @Around注解 环绕执行，就是在调用目标方法之前和调用之后，都会执行一定的逻辑
     */
    @Around("Pointcut()")
    public Object Around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Around start ");
        long start = System.currentTimeMillis();
        // 调用执行目标方法(result为目标方法执行结果)，必须有此行代码才会执行目标调用的方法（等价于@befor+@after），否则只会执行一次之前的（等价于@before）
        Object result = "null";
        long end = System.currentTimeMillis();
        log.info(pjp.getTarget().getClass().getSimpleName() + "->" + pjp.getSignature().getName() + " 耗费时间:" + (end - start) + "毫秒");
        log.info("Around end ");
        return result;
    }
}