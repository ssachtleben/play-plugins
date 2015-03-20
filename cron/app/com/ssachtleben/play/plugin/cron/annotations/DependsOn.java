package com.ssachtleben.play.plugin.cron.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The DependsOn annotation declares a list of other jobs that this one depends on and wait until the listed jobs finished before the job
 * runs.
 * 
 * @author Sebastian Sachtleben
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DependsOn {

	/**
	 * The classes which needs to be done before this one can start.
	 * 
	 * @return
	 */
	Class<?>[] values();

	/**
	 * @return boolean if missing dependencies should be ignored or not.
	 */
	boolean ignoreMissing() default true;

	/**
	 * @return boolean if inactive dependencies should be ignored or not.
	 */
	boolean ignoreInactive() default true;

}
