package com.ssachtleben.play.plugin.auth.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ssachtleben.play.plugin.auth.models.Identity;

/**
 * Authenticates specific {@link Identity} model.
 * 
 * @author Sebastian Sachtleben
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authenticates {

	/**
	 * The Identity model which will be authenticated.
	 * 
	 * @return The {@link Identity} model.
	 */
	Class<? extends Identity> type() default Identity.class;

	/**
	 * The active boolean decides if the authentication method check will be used.
	 * 
	 * @return The active boolean.
	 */
	boolean active() default true;

}
