package com.ssachtleben.play.plugin.auth.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ssachtleben.play.plugin.auth.models.AuthUser;

/**
 * Authenticates specific {@link AuthUser} model.
 * 
 * @author Sebastian Sachtleben
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authenticates {

	/**
	 * The AuthUser model which will be authenticated.
	 * 
	 * @return The {@link AuthUser} model.
	 */
	Class<? extends AuthUser> type() default AuthUser.class;

	/**
	 * The active boolean decides if the authentication method check will be used.
	 * 
	 * @return The active boolean.
	 */
	boolean active() default true;

}
