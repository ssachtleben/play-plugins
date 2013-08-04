package com.ssachtleben.play.plugin.cron;

import play.Logger;
import akka.actor.UntypedActor;

import com.ssachtleben.play.plugin.cron.jobs.Job;

/**
 * The CronCaller class will be called once a cronjob needs to be executed.
 * 
 * @author Sebastian Sachtleben
 */
public class CronActor extends UntypedActor {
	private static final Logger.ALogger log = Logger.of(CronActor.class);

	@Override
	public void onReceive(Object job) throws Exception {
		if (!(job instanceof Job)) {
			log.error("Failed to run job '" + job.getClass().getName() + "' because the interface '" + Job.class.getName()
					+ "' is not implemented ...");
			return;
		}
		log.info("Execute '" + job.getClass().getName() + "' job");
		((Job) job).run();
	}
}
