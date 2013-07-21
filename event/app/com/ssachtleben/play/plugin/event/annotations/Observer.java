package com.ssachtleben.play.plugin.event.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Observer annotation declares a method as listener to events and will be
 * registered during application start.
 * 
 * @author Sebastian Sachtleben
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Observer {

  /**
   * The topic where the observer method will be registered.
   * 
   * @return The topic.
   */
  String topic() default "";

}
