package play.plugin.cron.usage.jobs;

import play.Logger;

import com.play.module.cron.Cronjob;

/**
 * The LoggingJob will be run every second (because in @Cronjob annotation is no
 * cron pattern set) and write a message into the log file.
 * 
 * @author Sebastian Sachtleben
 */
@Cronjob
public class LoggingJob implements Runnable {
  private static final Logger.ALogger log = Logger.of(LoggingJob.class);

  @Override
  public void run() {
    log.info("Job executed");
  }
}
