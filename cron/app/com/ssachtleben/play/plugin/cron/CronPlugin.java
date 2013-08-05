package com.ssachtleben.play.plugin.cron;

import java.lang.annotation.Annotation;
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
import com.ssachtleben.play.plugin.cron.annotations.StartJob;
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
	private Set<Job> jobs = Collections.synchronizedSet(new HashSet<Job>());

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
		jobs.addAll(findJobsByAnnotation(CronJob.class));
		for (Job job : jobs) {
			scheduleJob(quartzActor, cronActor, job);
		}
		// Handle start jobs
		Set<Job> startJobs = findJobsByAnnotation(StartJob.class);
		for (Job job : startJobs) {
			StartJob annotation = job.getClass().getAnnotation(StartJob.class);
			if (annotation != null && annotation.active()) {
				executeJob(job, annotation.async());
			}
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
	 * @param job
	 *          The job to execute.
	 * @param async
	 *          Boolean if execute sync or async.
	 */
	private void executeJob(final Job job, final boolean async) {
		if (async) {
			Akka.future(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					job.run();
					return null;
				}
			});
		} else {
			job.run();
		}
	}

	/**
	 * Check the classpath for jobs. Depending on the config property "cron.annotation" the classes will be searched by @Cronjob annotation or
	 * by Job interace.
	 * 
	 * @return Set of classes which extends the job interface
	 */
	private Set<Job> findJobsByAnnotation(Class<? extends Annotation> annotation) {
		return CronUtils.findAnnotatedJobs(annotation);
	}
}
