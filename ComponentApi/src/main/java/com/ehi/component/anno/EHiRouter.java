package com.ehi.component.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这是一个路由的 Api
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface EHiRouter {

    /**
     * 定义host
     *
     * @return
     */
    String host() default "";

    /**
     * 路径
     *
     * @return
     */
    String value();

    /**
     * 是否需要登录
     *
     * @return
     */
    boolean needLogin() default false;

    /**
     * 描述信息
     *
     * @return
     */
    String desc() default "";

}
