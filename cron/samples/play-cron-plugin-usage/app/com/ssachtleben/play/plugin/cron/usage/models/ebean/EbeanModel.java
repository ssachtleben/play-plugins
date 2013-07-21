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
public class EbeanModel extends Model {

  @Id
  public Long id;

  public Date date = new Date();

  /**
   * Generic ebean query helper for model ExampleModel.
   */
  public static Finder<Long, EbeanModel> find = new Finder<Long, EbeanModel>(Long.class, EbeanModel.class);

  @Override
  public String toString() {
    return "EbeanModel [id=" + id + ", date=" + date + "]";
  }
}
