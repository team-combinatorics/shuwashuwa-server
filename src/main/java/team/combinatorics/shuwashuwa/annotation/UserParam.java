package team.combinatorics.shuwashuwa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserParam {
    /**
     * 若请求者无特殊身份，将检查value对应的参数是否是请求者自身id
     */
    String value();
}
