package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解：用于标识某个方法需要进行公共字段自动填充处理
 */

@Target(ElementType.METHOD) // 这个注解要加在上面位置，这里是这个注解加在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    // 数据库操作类型：UPDATE INSERT
    OperationType value(); //OperationType是一个枚举类型，里面只有两个值，分别是UPDATE和INSERT

}
