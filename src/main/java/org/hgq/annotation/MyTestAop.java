package org.hgq.annotation;

import java.lang.annotation.*;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-07-02 14:37
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyTestAop {
}
