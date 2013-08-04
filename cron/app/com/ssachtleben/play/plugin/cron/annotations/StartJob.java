package com.ssachtleben.play.plugin.cron.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The StartJob annotation declares a class as job and will be executed by the {@link CronService} during application start.
 * 
 * @author Sebastian Sachtleben
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StartJob {

	/**
	 * @return The async boolean decides if the job will be executed sync or async.
	 */
	boolean async() default false;

	/**
	 * @return The active boolean decides if the job will be executed or not.
	 */
	boolean active() default true;

}
