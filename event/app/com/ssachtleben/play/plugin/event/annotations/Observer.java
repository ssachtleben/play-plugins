package com.ssachtleben.play.plugin.event.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ssachtleben.play.plugin.event.EventPlugin;
import com.ssachtleben.play.plugin.event.EventService;
import com.ssachtleben.play.plugin.event.ReferenceStrength;

/**
 * Marks the annotated method as potential subscriber for the class-based or
 * topic-based, if the topic name is changed, event publication and subscription
 * process.
 * <p/>
 * The method will be registered to the implemented instance of
 * {@link EventService} during the application start by the {@link EventPlugin}.
 * <p/>
 * <b>Important: Currently only static methods can be registered as
 * subscribers.</b>
 * 
 * @author Sebastian Sachtleben
 * @see EventService
 * @see EventPlugin
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

  /**
   * @return The ReferenceStrength.
   */
  ReferenceStrength referenceStrength() default ReferenceStrength.WEAK;

}
