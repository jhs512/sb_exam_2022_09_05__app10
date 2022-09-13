package com.ll.exam.app10.app.member.entity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : context")
public @interface CurrentUser {
}
