package com.ssachtleben.play.plugin.cron.usage.jobs;

import java.util.Date;
import java.util.List;

import play.Logger;
import play.db.jpa.JPA;

import com.ssachtleben.play.plugin.cron.annotations.Cronjob;
import com.ssachtleben.play.plugin.cron.jobs.SimpleJob;
import com.ssachtleben.play.plugin.cron.usage.models.jpa.ModelJPA;

/**
 * The DatabaseJpaJob runs every 10 seconds and creates a new ModelJPA instance
 * and save to database. Once a model exists the job will update the date
 * property on every execution.
 * 
 * @author Sebastian Sachtleben
 */
@Cronjob(pattern = "0/10 * * * * ?")
public class DatabaseJpaJob extends SimpleJob {
  private static final Logger.ALogger log = Logger.of(DatabaseJpaJob.class);

  @SuppressWarnings("unchecked")
  @Override
  public void run() {
    try {
      JPA.withTransaction("default2", false, new play.libs.F.Function0<Void>() {
        @Override
        public Void apply() throws Throwable {
          List<ModelJPA> models = JPA.em("default2").createQuery("FROM " + ModelJPA.class.getName()).getResultList();
          if (models.size() > 0) {
            ModelJPA model = models.get(0);
            log.info(model + " loaded");
            model.setDate(new Date());
            log.info(model + " saved");
          } else {
            ModelJPA model = new ModelJPA();
            JPA.em("default2").persist(model);
            log.info(model + " persisted");
          }
          return null;
        }
      });
    } catch (Throwable e) {
      log.info("Couldnt execute jpa transaction: ", e);
    }
  }

}
