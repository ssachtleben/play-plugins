package com.ssachtleben.play.plugin.auth.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ssachtleben.play.plugin.auth.providers.BaseProvider;

/**
 * Authenticates specific {@link BaseProvider} provider.
 * 
 * @author Sebastian Sachtleben
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authenticates {

  /**
   * The Provider which will be authenticated.
   * 
   * @return The {@link BaseProvider} key.
   */
  String provider();

  /**
   * The active boolean decides if the authentication method check will be used.
   * 
   * @return The active boolean.
   */
  boolean active() default true;

}
