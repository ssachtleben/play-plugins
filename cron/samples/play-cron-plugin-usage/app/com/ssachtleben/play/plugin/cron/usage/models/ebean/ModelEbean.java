package com.ssachtleben.play.plugin.cron.usage.models.ebean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

/**
 * Example ebean model to show database access in scheduled jobs.
 * 
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class ModelEbean extends Model {

  @Id
  public Long id;

  public Date date = new Date();

  /**
   * Generic ebean query helper for model ExampleModel.
   */
  public static Finder<Long, ModelEbean> find = new Finder<Long, ModelEbean>(Long.class, ModelEbean.class);

  @Override
  public String toString() {
    return "ModelEbean [id=" + id + ", date=" + date + "]";
  }
}
