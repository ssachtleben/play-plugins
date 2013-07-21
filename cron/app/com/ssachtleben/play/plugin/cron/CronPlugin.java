package com.ssachtleben.play.plugin.cron;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import play.Application;
import play.Logger;
import play.Plugin;
import scala.concurrent.duration.Duration;
import us.theatr.akka.quartz.AddCronSchedule;
import us.theatr.akka.quartz.QuartzActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.ssachtleben.play.plugin.cron.annotations.Cronjob;
import com.ssachtleben.play.plugin.cron.jobs.Job;

/**
 * The CronPlugin handles the start and stop process for all cronjobs. During
 * the application start all classes annotated with @Cronjob will be started if
 * the given active boolean is true and the given pattern is valid. During
 * application stop the scheduler will be shutdown and remove all running
 * cronjobs.
 * 
 * @author Sebastian Sachtleben
 */
public class CronPlugin extends Plugin {
  private static final Logger.ALogger log = Logger.of(CronPlugin.class);

  /**
   * Current running play application.
   */
  protected Application app;

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
  public CronPlugin(Application app) {
    log.debug("Plugin created");
    this.app = app;
  }

  /**
   * The onStart method will be invoked during application start and scans the
   * classpath for classes annotated with @Cronjob and call scheduleJob method.
   */
  @Override
  public void onStart() {
    log.debug("Plugin started");
    ActorRef quartzActor = system.actorOf(new Props(QuartzActor.class));
    ActorRef cronActor = system.actorOf(new Props(CronActor.class));
    jobs.addAll(findJobs());
    for (Job job : jobs) {
      scheduleJob(quartzActor, cronActor, job);
    }
    super.onStart();
  }

  /**
   * The onStop method shutdown and remove all scheduled jobs.
   */
  @Override
  public void onStop() {
    log.debug("Plugin stopped");
    system.shutdown();
    super.onStop();
  }

  /**
   * The scheduleJob method register the cronjob with the given pattern with a
   * delay of 50ms.
   * 
   * @param quartzActor
   *          The quartActor to set
   * @param runActor
   *          The runActor to set
   * @param jobClass
   *          The jobClass to set
   */
  private void scheduleJob(ActorRef quartzActor, ActorRef runActor, Job job) {
    Cronjob cronjob = job.getClass().getAnnotation(Cronjob.class);
    if (cronjob.active()) {
      log.debug("Register job '" + job.toString() + "' with cron pattern '" + cronjob.pattern() + "'");
      system.scheduler().scheduleOnce(Duration.create(50, TimeUnit.MILLISECONDS), quartzActor, new AddCronSchedule(runActor, cronjob.pattern(), job, true),
          system.dispatcher());
    }
  }

  /**
   * Check the classpath for jobs. Depending on the config property
   * "cron.useAnnotation" the classes will be searched by @Cronjob annotation or
   * by Job interace.
   * 
   * @return Set of classes which extends the job interface
   */
  private Set<Job> findJobs() {
    return app.configuration().getBoolean("cron.scanner.annotation", Boolean.TRUE) ? CronUtils.findAnnotatedJobs(Cronjob.class) : CronUtils
        .findImplementedJobs();
  }

}
