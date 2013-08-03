package com.ssachtleben.play.plugin.event.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ssachtleben.play.plugin.event.ReferenceStrength;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RuntimeObserver {

	String topicMethodName() default "";

	ReferenceStrength referenceStrength() default ReferenceStrength.WEAK;

}
