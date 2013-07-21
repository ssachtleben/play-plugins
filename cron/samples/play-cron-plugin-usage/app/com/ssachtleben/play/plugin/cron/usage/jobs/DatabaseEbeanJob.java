package com.ssachtleben.play.plugin.cron.usage.jobs;

import java.util.Date;
import java.util.List;

import play.Logger;

import com.ssachtleben.play.plugin.cron.annotations.Cronjob;
import com.ssachtleben.play.plugin.cron.jobs.SimpleJob;
import com.ssachtleben.play.plugin.cron.usage.models.ebean.EbeanModel;

/**
 * The ExampleDatabaseJob runs every 10 seconds and creates a new ExampleModel
 * instance and save to database. Once a model exists the job will update the
 * date property on every execution.
 * 
 * @author Sebastian Sachtleben
 */
@Cronjob(pattern = "0/10 * * * * ?")
public class DatabaseEbeanJob extends SimpleJob {
  private static final Logger.ALogger log = Logger.of(DatabaseEbeanJob.class);

  @Override
  public void run() throws Exception {
    List<EbeanModel> models = EbeanModel.find.all();
    if (models.size() > 0) {
      EbeanModel model = models.get(0);
      log.info(model + " loaded");
      model.date = new Date();
      model.save();
      log.info(model + " saved");
    } else {
      EbeanModel model = new EbeanModel();
      model.save();
      log.info(model + " persisted");
    }
  }

}
