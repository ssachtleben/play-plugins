package com.ssachtleben.play.plugin.cron.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Cronjob annotation declares a class as job and will be executed by the
 * {@link CronService} if the {@code active} boolean is true. The annotated
 * class should also implement Runnable for the run method.
 * 
 * @author Sebastian Sachtleben
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CronJob {

  /**
   * The pattern when the job will be executed. By default it will be triggered
   * every second.
   * 
   * @return The cron pattern
   */
  String pattern() default "* * * * * ?";

  /**
   * The active boolean decides if the job will be executed or not.
   * 
   * @return The active boolean
   */
  boolean active() default true;

}
