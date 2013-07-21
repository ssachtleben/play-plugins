package com.ssachtleben.play.plugin.cron;

import play.Logger;
import akka.actor.UntypedActor;

/**
 * The CronCaller class will be called once a cronjob needs to be executed.
 * 
 * @author Sebastian Sachtleben
 */
public class CronActor extends UntypedActor {
  private static final Logger.ALogger log = Logger.of(CronActor.class);

  @Override
  public void onReceive(Object cronjob) throws Exception {
    if (!(cronjob instanceof Runnable)) {
      log.error("Failed to run " + cronjob.toString() + " because runnable interface is not implemented ...");
      return;
    }
    log.info("Execute '" + cronjob.toString() + "'");
    ((Runnable) cronjob).run();
  }
}
