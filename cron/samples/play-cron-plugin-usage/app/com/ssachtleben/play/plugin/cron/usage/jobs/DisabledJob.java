package com.ssachtleben.play.plugin.cron.usage.jobs;

import play.Logger;

import com.ssachtleben.play.plugin.cron.annotations.CronJob;
import com.ssachtleben.play.plugin.cron.jobs.Job;

/**
 * The DisabledJob will not run because the active boolean in the @Cronjob annotation is set to false.
 * 
 * @author Sebastian Sachtleben
 */
@CronJob(active = false)
public class DisabledJob implements Job {
	private static final Logger.ALogger log = Logger.of(DisabledJob.class);

	@Override
	public void run() {
		log.info("Job executed");
	}
}
