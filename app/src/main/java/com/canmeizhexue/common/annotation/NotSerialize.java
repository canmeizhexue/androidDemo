package com.canmeizhexue.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>用于Gson命名策略的注解-指定不序列化的字段</p>
 *
 * @author canmeizhexue
 * @version 1.0 (2015/11/17)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotSerialize {

}
