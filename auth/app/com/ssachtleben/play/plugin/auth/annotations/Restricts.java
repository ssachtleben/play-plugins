package com.ssachtleben.play.plugin.auth.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import play.mvc.Controller;

/**
 * Restricts {@link Controller} methods and checks if the logged in user has
 * privilege to execute the {@link Method}.
 * <p>
 * <b>Important:</b> This annotation works only for methods which will be
 * directly invoked by routes.
 * </p>
 * 
 * @author Sebastian Sachtleben
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Restricts {

  /**
   * The current logged in user must match this role otherwise the method will
   * not invoked.
   * 
   * @return The cron pattern
   */
  String role() default "";

  /**
   * The active boolean decides if the restriction check will be executed or
   * not.
   * 
   * @return The active boolean
   */
  boolean active() default true;

}
