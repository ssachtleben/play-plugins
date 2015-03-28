package com.ssachtleben.play.plugin.cron;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import play.Logger;

import com.ssachtleben.play.plugin.cron.annotations.CronJob;
import com.ssachtleben.play.plugin.cron.annotations.DependsOn;
import com.ssachtleben.play.plugin.cron.annotations.StartJob;
import com.ssachtleben.play.plugin.cron.jobs.Job;

/**
 * The CronUtils provides methods to find cronjob annotated classes in the
 * classpath via reflection.
 * 
 * @author Sebastian Sachtleben
 */
public class CronUtils {
  private static final Logger.ALogger log = Logger.of(CronUtils.class);

  /**
   * Suppresses default constructor, ensuring non-instantiability.
   */
  private CronUtils() {

  }

  public static final Set<Class<?>> findAnnotatedClasses(Class<? extends Annotation> annotation) {
    long nanos = System.nanoTime();
    log.debug("Start searching for classes with annotation @" + annotation.getSimpleName());
    URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();
    Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(new TypeAnnotationsScanner()));
    Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);
    long elapsed = Math.round((double) (System.nanoTime() - nanos) / 1000000.0);
    Set<Class<?>> validClasses = new HashSet<Class<?>>();
    for (Class<?> current : classes) {
      if (!Modifier.isAbstract(current.getModifiers())) {
        validClasses.add(current);
      }
    }
    log.debug("Found " + validClasses.size() + " classes in " + elapsed + " ms : " + Arrays.toString(validClasses.toArray(new Class<?>[0])));
    return validClasses;
  }

  public static final Set<JobData> findCronJobs() {
    Set<Class<?>> cjClasses = findAnnotatedClasses(CronJob.class);
    if (cjClasses.size() == 0) {
      log.info(String.format("No %s annotated classes found", CronJob.class));
      return new HashSet<JobData>();
    }
    Set<JobData> jobs = new HashSet<JobData>();
    Iterator<Class<?>> iter = cjClasses.iterator();
    while (iter.hasNext()) {
      Class<?> cjClass = iter.next();
      if (Job.class.isAssignableFrom(cjClass)) {
        CronJob cj = cjClass.getAnnotation(CronJob.class);
        if (cj.active()) {
          try {
            JobData data = new JobData(cjClass.asSubclass(Job.class).newInstance(), true, cj.pattern(), findDependsOn(cjClass));
            log.info(String.format("Found: %s", data));
            jobs.add(data);
          } catch (InstantiationException | IllegalAccessException e) {
            log.error(String.format("Failed to create instance of %s", cjClass), e);
          }
        }
      }
    }
    return jobs;
  }

  public static final Set<JobData> findStartJobs() {
    Set<Class<?>> sjClasses = findAnnotatedClasses(StartJob.class);
    if (sjClasses.size() == 0) {
      log.info(String.format("No %s annotated classes found", StartJob.class));
      return new HashSet<JobData>();
    }
    Set<JobData> jobs = new HashSet<JobData>();
    Iterator<Class<?>> iter = sjClasses.iterator();
    while (iter.hasNext()) {
      Class<?> sjClass = iter.next();
      if (Job.class.isAssignableFrom(sjClass)) {
        StartJob sj = sjClass.getAnnotation(StartJob.class);
        if (sj.active()) {
          try {
            JobData data = new JobData(sjClass.asSubclass(Job.class).newInstance(), sj.async(), null, findDependsOn(sjClass));
            log.info(String.format("Found: %s", data));
            jobs.add(data);
          } catch (InstantiationException | IllegalAccessException e) {
            log.error(String.format("Failed to create instance of %s", sjClass), e);
          }
        }
      }
    }
    return jobs;
  }

  private static Class<?>[] findDependsOn(Class<?> clazz) {
    if (!clazz.isAnnotationPresent(DependsOn.class)) {
      return new Class<?>[] {};
    }
    return clazz.getAnnotation(DependsOn.class).values();
  }
}
