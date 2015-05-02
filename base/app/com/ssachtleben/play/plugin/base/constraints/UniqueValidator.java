package com.ssachtleben.play.plugin.base.constraints;

import javax.validation.ConstraintValidator;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import play.data.validation.Constraints.Validator;
import play.db.ebean.Model;
import play.libs.F.Tuple;

import com.avaje.ebean.ExpressionList;

public class UniqueValidator extends Validator<Object> implements ConstraintValidator<Unique, Object> {
  private Class<? extends Model> modelClass;
  private Class<?> idClass;
  private String[] fields;
  private String message;

  @Override
  public void initialize(final Unique constraintAnnotation) {
    this.modelClass = constraintAnnotation.modelClass();
    this.idClass = constraintAnnotation.idClass();
    this.message = constraintAnnotation.message();
    fields = constraintAnnotation.fields();
  }

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public boolean isValid(final Object value) {
    try {
      Model.Finder<?, ? extends Model> find = new Model.Finder(idClass, modelClass);
      Logger.debug("Finder: " + idClass.toString() + " - " + modelClass.toString());
      ExpressionList el = find.where();
      for (String f : fields) {
        Logger.debug("Eq: " + f + " - " + value);
        el.eq(f, value);
      }
      Model record = (Model) el.findUnique();
      boolean recordFound = record != null;
      boolean sameRecord = false;
      boolean recordValid = false;
      Long modelId = null;
      Long currentId = null;
      if (recordFound) {
        Logger.debug("Record: " + record.toString());
        modelId = new Long(BeanUtils.getProperty(record, "id"));
        try {
          currentId = new Long(BeanUtils.getProperty(value, "id"));
        } catch (Exception e) {
          currentId = new Long(0);
        }
        sameRecord = currentId.longValue() == modelId.longValue();
      }
      recordValid = !recordFound || sameRecord;
      // Some debug...
      // Logger.info(">>> searching name = " + BeanUtils.getProperty(record,
      // "name"));
      Logger.debug(">>> record found ? " + recordFound);
      Logger.debug(">>> same record ? " + sameRecord);
      Logger.debug(">>> record valid ? " + recordValid);
      return recordValid;
    } catch (final Exception e) {
      // FIXME: ignored exception, WARNING...
      Logger.error("Exception occured: ", e);
    }
    return true;
  }

  @Override
  public Tuple<String, Object[]> getErrorMessageKey() {
    return new Tuple<String, Object[]>(message, new Object[] {});
  }

  public static Validator<Object> unique() {
    return new UniqueValidator();
  }

}