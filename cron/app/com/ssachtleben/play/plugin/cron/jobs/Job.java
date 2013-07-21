package com.ssachtleben.play.plugin.cron.jobs;

/**
 * The Job interface provides the run method and every cronjob needs to
 * implement this interface.
 * 
 * @author Sebastian Sachtleben
 */
public interface Job {

  /**
   * The run method will be invoked during the job execution.
   */
  void run() throws Exception;

}
