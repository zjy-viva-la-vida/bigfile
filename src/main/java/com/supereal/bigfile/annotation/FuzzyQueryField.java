package com.supereal.bigfile.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 模糊查询字段注解
 * @program: manager-op
 * @author: xinchao.pan
 * @create: 2019-04-03
 */

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface FuzzyQueryField {
}
