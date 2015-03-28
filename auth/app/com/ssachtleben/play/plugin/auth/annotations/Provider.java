package com.ssachtleben.play.plugin.auth.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ssachtleben.play.plugin.auth.models.Identity;

/**
 * Provider for specific {@link Identity} model.
 * 
 * @author Sebastian Sachtleben
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Provider {

  /**
   * The {@link Identity} model which will be provided.
   * 
   * @return The {@link Identity} model.
   */
  Class<? extends Identity> type() default Identity.class;
}