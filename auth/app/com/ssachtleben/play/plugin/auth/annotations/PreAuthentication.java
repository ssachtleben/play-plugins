package com.ssachtleben.play.plugin.auth.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ssachtleben.play.plugin.auth.providers.BaseProvider;

/**
 * Invoked before authentication process.
 * 
 * @author Sebastian Sachtleben
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PreAuthentication {

	/**
	 * The Provider to handle, leave this blank to invoke on all authentications.
	 * 
	 * @return The {@link BaseProvider} key.
	 */
	String provider();

	/**
	 * The active boolean decides if method will be invoked or not.
	 * 
	 * @return The active boolean.
	 */
	boolean active() default true;

}
