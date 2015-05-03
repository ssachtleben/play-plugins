package com.ssachtleben.play.plugin.auth.filters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface BasicAuth {

  String realm() default "login";

  String username() default "security.baseauth.username";

  String password() default "security.baseauth.password";

}
