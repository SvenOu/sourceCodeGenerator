package com.sven.security.config.anotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@RequestMapping("${API_security_PREFIX}")
public @interface APIController {
    @AliasFor(annotation = Component.class)
    String value() default "";
}