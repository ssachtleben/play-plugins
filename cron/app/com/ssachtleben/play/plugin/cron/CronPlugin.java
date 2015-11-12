package com.ssachtleben.play.plugin.cron;

import static play.libs.F.Promise.promise;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import play.Application;
import play.libs.F.Function;
import play.libs.F.Function0;
import scala.concurrent.duration.Duration;
import us.theatr.akka.quartz.AddCronSchedule;
import us.theatr.akka.quartz.QuartzActor;
import us.theatr.akka.quartz.Spigot;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.ssachtleben.play.plugin.base.ExtendedPlugin;

/**
 * The CronPlugin handles the start and stop process for all cronjobs. During
 * the application start all classes annotated with @Cronjob will be started if
 * the given active boolean is true and the given pattern is valid. During
 * application stop the scheduler will be shutdown and remove all running
 * cronjobs.
 * 
 * @author Sebastian Sachtleben
 */
public class CronPlugin extends ExtendedPlugin {

  /**
   * Create a ActorSystem for Akka to schedule jobs.
   */
  private ActorSystem system = ActorSystem.create("sys");

  private Set<Class<?>> jobsDone = Collections.synchronizedSet(new HashSet<Class<?>>());

  private Set<JobData> jobsPending = Collections.synchronizedSet(new HashSet<JobData>());

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
    ActorRef quartzActor = system.actorOf(Props.create(QuartzActor.class));
    ActorRef cronActor = system.actorOf(Props.create(CronActor.class));
    // Handle cron jobs
    log.info("Register annotated cron jobs");
    Set<JobData> jobs = CronUtils.findCronJobs();
    for (JobData data : jobs) {
      Jobs.add(data);
      scheduleJob(quartzActor, cronActor, data);
    }
    // Handle start jobs
    log.info("Running annotated start jobs");
    Set<JobData> startJobs = CronUtils.findStartJobs();
    for (JobData data : startJobs) {
      Jobs.add(data);
      if (data.dependsOn() != null && data.dependsOn().length > 0) {
        jobsPending.add(data);
      } else {
        executeJob(data);
      }
    }
    log.info(String.format("Found %d pending jobs", jobsPending.size()));
    validatePendingJobs();
    checkPendingJobs();
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
   * The scheduleJob method register the cronjob with the given pattern with a
   * delay of 50ms.
   * 
   * @param quartzActor
   *          The quartActor to set
   * @param runActor
   *          The runActor to set
   * @param jobData
   *          The jobData to set
   */
  private void scheduleJob(ActorRef quartzActor, ActorRef runActor, JobData jobData) {
    log.debug(String.format("Register %s", jobData));
    final Spigot spigot = new Spigot() {
      @Override
      public boolean open() {
        return true;
      }
    };
    system.scheduler().scheduleOnce(Duration.create(50, TimeUnit.MILLISECONDS), quartzActor,
        new AddCronSchedule(runActor, jobData.pattern(), jobData.job(), true, spigot), system.dispatcher(), null);
  }

  /**
   * Executes a job once.
   * 
   * @param jobData
   *          The jobData to execute.
   */
  private void executeJob(final JobData jobData) {
    if (jobData.async()) {
      promise(new Function0<JobData>() {
        @Override
        public JobData apply() throws Exception {
        	log.info(String.format("Run %s", jobData));
        	jobData.job().run();
        	return jobData;
        }
      }).map(new Function<JobData, Void>() {
        @Override
        public Void apply(JobData jobData) throws Exception {
          finishedJob(jobData);
          checkPendingJobs();
          return null;
        }
      });
    } else {
      try {
    	  log.info(String.format("Run %s", jobData));
    	  jobData.job().run();
      } catch (Exception e) {
        log.error("Exception during jop occured", e);
      }
      finishedJob(jobData);
    }
  }

  /**
   * The current running job is done and checks for pending jobs to start the
   * next one.
   * 
   * @param jobData
   *          The jobData of the finished job.
   */
  private void finishedJob(final JobData jobData) {
    synchronized (jobsDone) {
      log.info(String.format("Job %s done", jobData.job()));
      jobsDone.add(jobData.job().getClass());
      log.info(String.format("Jobs done: %s", Arrays.toString(jobsDone.toArray(new Class<?>[0]))));
    }
  }

  /**
   * Validate pending jobs and check for their dependencies. If a wrong
   * dependency occur the job will be removed from the list. TODO: This part
   * should be improved alot.
   */
  private void validatePendingJobs() {
    log.debug("Validate pending jobs");
    synchronized (jobsPending) {
      Iterator<JobData> iter = jobsPending.iterator();
      while (iter.hasNext()) {
        JobData data = iter.next();
        log.info(String.format("Found pending %s", data));
        boolean valid = true;
        Class<?>[] dependsOn = data.dependsOn();
        for (int i = 0; i < dependsOn.length; i++) {
          if (Jobs.contains(dependsOn[i])) {
            log.debug(String.format("Dependency %s exists", dependsOn[i]));
          } else {
            log.debug(String.format("Dependency %s not exists", dependsOn[i]));
            valid = false;
            break;
          }
        }
        if (!valid) {
          log.error(String.format("Removing invalid %s due missing depedencies", data));
          iter.remove();
        }
      }
    }
  }

  /**
   * Check for next pending job with finished dependencies to execute. If still
   * pending jobs exists but still all have at least one missing dependencies
   * all jobs will be removed and an error log will be written. TODO: This part
   * should be improved alot.
   */
  private void checkPendingJobs() {
    synchronized (jobsPending) {
      if (jobsPending == null || jobsPending.size() == 0) {
        return;
      }
      log.debug("Check for pending jobs");
      int size = jobsPending.size();
      Iterator<JobData> iter = jobsPending.iterator();
      while (iter.hasNext()) {
        JobData data = iter.next();
        log.debug(String.format("Found pending %s", data));
        boolean valid = true;
        Class<?>[] dependsOn = data.dependsOn();
        for (int i = 0; i < dependsOn.length; i++) {
          if (jobsDone.contains(dependsOn[i])) {
            log.debug(String.format("Dependency %s is done", dependsOn[i]));
          } else {
            log.debug(String.format("Dependency %s not done", dependsOn[i]));
            valid = false;
            break;
          }
        }
        if (valid) {
          log.debug(String.format("All dependencies finished for %s", data));
          executeJob(data);
          iter.remove();
          return;
        }
      }
      if (size > 0 && size == jobsPending.size()) {
        // TODO: Check for running jobs here ...
        log.debug(String.format("Check for pending jobs, but no job executed (maybe fail configuration?!?) - %s", jobsPending));
        // log.error(String.format("Found invalid jobs due missing dependencies: %s",
        // Arrays.toString(jobsPending.toArray(new
        // JobData[0]))));
        // jobsPending.clear();
      }
    }
  }
}
