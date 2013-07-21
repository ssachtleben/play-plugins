package com.ssachtleben.play.plugin.cron.usage.models.jpa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Example jpa model to show database access in scheduled jobs.
 * 
 * @author Sebastian Sachtleben
 */
@Entity
public class ModelJPA {

  @Id
  @GeneratedValue
  private Long id;

  private Date date = new Date();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @Override
  public String toString() {
    return "ModelJPA [id=" + id + ", date=" + date + "]";
  }
}
