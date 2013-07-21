package com.ssachtleben.play.plugin.cron.usage;

import play.Logger;

import com.ssachtleben.play.plugin.cron.Cronjob;

/**
 * The DisabledJob will not run because the active boolean in the @Cronjob
 * annotation is set to false.
 * 
 * @author Sebastian Sachtleben
 */
@Cronjob(active = false)
public class DisabledJob implements Runnable {
  private static final Logger.ALogger log = Logger.of(DisabledJob.class);

  @Override
  public void run() {
    log.info("Job executed");
  }
}
