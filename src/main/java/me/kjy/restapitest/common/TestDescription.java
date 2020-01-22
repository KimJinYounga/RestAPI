package me.kjy.restapitest.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
*@Retention : 이 애노테이션을 얼마나 오래 가져갈 것인가?
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface TestDescription {
    // 기본 값을 주지 않고 항상 입력하도록 함.
    String value();
}
