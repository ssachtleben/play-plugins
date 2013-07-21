package com.ssachtleben.play.plugin.cron.usage.jobs;

import play.Logger;

import com.ssachtleben.play.plugin.cron.annotations.Cronjob;
import com.ssachtleben.play.plugin.cron.jobs.Job;

/**
 * The LoggingJob will be run every minute and write a log message.
 * 
 * @author Sebastian Sachtleben
 */
@Cronjob(pattern = "0 * * * * ?")
public class LoggingJob implements Job {
  private static final Logger.ALogger log = Logger.of(LoggingJob.class);

  @Override
  public void run() {
    log.info(this.getClass().getSimpleName() + " executed");
  }
}
