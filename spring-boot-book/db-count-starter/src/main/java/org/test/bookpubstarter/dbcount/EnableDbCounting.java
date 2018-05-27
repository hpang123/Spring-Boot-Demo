package org.test.bookpubstarter.dbcount;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/*
 * We can use @EnableDbCounting in client for
 * adding auto configuration DbCountAutoConfiguration
 * Then we don't need to define it in spring.factories
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DbCountAutoConfiguration.class)
@Documented
public @interface EnableDbCounting {
}
