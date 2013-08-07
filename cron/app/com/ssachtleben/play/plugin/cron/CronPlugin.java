package com.ssachtleben.play.plugin.cron;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import play.Application;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import us.theatr.akka.quartz.AddCronSchedule;
import us.theatr.akka.quartz.QuartzActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.ssachtleben.play.plugin.base.ExtendedPlugin;
import com.ssachtleben.play.plugin.cron.annotations.CronJob;
import com.ssachtleben.play.plugin.cron.jobs.Job;

/**
 * The CronPlugin handles the start and stop process for all cronjobs. During the application start all classes annotated with @Cronjob will
 * be started if the given active boolean is true and the given pattern is valid. During application stop the scheduler will be shutdown and
 * remove all running cronjobs.
 * 
 * @author Sebastian Sachtleben
 */
public class CronPlugin extends ExtendedPlugin {

	/**
	 * Create a ActorSystem for Akka to schedule jobs.
	 */
	private ActorSystem system = ActorSystem.create("sys");

	/**
	 * A set with all registered jobs.
	 */
	private Set<JobData> jobs = Collections.synchronizedSet(new HashSet<JobData>());

	/**
	 * Default constructor.
	 * 
	 * @param app
	 *          The app to set
	 */
	public CronPlugin(final Application app) {
		super(app);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.base.ExtendedPlugin#name()
	 */
	@Override
	public String name() {
		return "cron";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.base.ExtendedPlugin#start()
	 */
	@Override
	public void start() {
		ActorRef quartzActor = system.actorOf(new Props(QuartzActor.class));
		ActorRef cronActor = system.actorOf(new Props(CronActor.class));
		// Handle cron jobs
		jobs.addAll(CronUtils.findCronJobs());
		for (JobData data : jobs) {
			scheduleJob(quartzActor, cronActor, data.job());
		}
		// Handle start jobs
		Set<JobData> startJobs = CronUtils.findStartJobs();
		for (JobData data : startJobs) {
			executeJob(data);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.base.ExtendedPlugin#stop()
	 */
	@Override
	public void stop() {
		system.shutdown();
	}

	/**
	 * The scheduleJob method register the cronjob with the given pattern with a delay of 50ms.
	 * 
	 * @param quartzActor
	 *          The quartActor to set
	 * @param runActor
	 *          The runActor to set
	 * @param jobClass
	 *          The jobClass to set
	 */
	private void scheduleJob(ActorRef quartzActor, ActorRef runActor, Job job) {
		CronJob cronjob = job.getClass().getAnnotation(CronJob.class);
		if (cronjob.active()) {
			log.debug("Register job '" + job.toString() + "' with cron pattern '" + cronjob.pattern() + "'");
			system.scheduler().scheduleOnce(Duration.create(50, TimeUnit.MILLISECONDS), quartzActor,
					new AddCronSchedule(runActor, cronjob.pattern(), job, true), system.dispatcher());
		}
	}

	/**
	 * Executes a job once.
	 * 
	 * @param jobData
	 *          The jobData to execute.
	 */
	private void executeJob(final JobData jobData) {
		if (jobData.async()) {
			Akka.future(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					log.info(String.format("Run %s", jobData));
					jobData.job().run();
					return null;
				}
			});
		} else {
			log.info(String.format("Run %s", jobData));
			jobData.job().run();
		}
	}
}
