package com.ssachtleben.play.plugin.base.constraints;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import play.db.ebean.Model;
/**
 * Validation annotation to check unique constraint/
 *
 * An array of fields can be supplied.
 *
 * Example, check uniqueness on code
 * @Unique(modelClass = User.class, fields = {"code"}, message = "Record has already been taken")
 *
 * Example, check uniqueness on more fields
 * @Unique.List({
 *   @Unique(modelClass = User.class, fields = {"code"}, message = "Code has already been taken"),
     @Unique(modelClass = User.class, fields = {"name"}, message = "Name has already been taken"),
     @Unique(modelClass = User.class, fields = {"name", "published"}, message = "Name has already been taken on published status")
 * })
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
@play.data.Form.Display(name="constraints.unique")
@Documented
public @interface Unique
{
    String message() default "{constraints.unique}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Model> modelClass();
    Class<?> idClass() default Long.class;
    /**
     * @return The fields
     */
    String[] fields();

}