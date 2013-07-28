package com.ssachtleben.play.plugin.cron.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Cronjob annotation declares a class as cronjob and will be started by the
 * CronService if the active boolean is true. The annotated class should also
 * implement Runnable for the run method.
 * 
 * @author Sebastian Sachtleben
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Cronjob {

  /**
   * The pattern where the cronjob will be executed. By default the cronjob will
   * be triggered every second.
   * 
   * @return The cron pattern
   */
  String pattern() default "* * * * * ?";

  /**
   * The active boolean decides if the cronjob will be executed or not.
   * 
   * @return The active boolean
   */
  boolean active() default true;

}
