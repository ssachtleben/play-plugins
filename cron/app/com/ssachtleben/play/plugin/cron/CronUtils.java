package com.ssachtleben.play.plugin.cron;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import play.Logger;

import com.ssachtleben.play.plugin.cron.jobs.Job;

/**
 * The CronUtils provides methods to find cronjob annotated classes in the classpath via reflection.
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

	/**
	 * The findAnnotatedJobs method searches for all classes in the current context annotated by the given annotation and returns them as a
	 * set.
	 * 
	 * @param annotation
	 *          The annotation to set
	 * @return Set of jobs
	 */
	public static final Set<Job> findAnnotatedJobs(Class<? extends Annotation> annotation) {
		long nanos = System.nanoTime();
		log.debug("Start searching for job classes with annotation @" + annotation.getSimpleName());
		URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();
		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(new TypeAnnotationsScanner()));
		Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);
		long elapsed = Math.round((double) (System.nanoTime() - nanos) / 1000000.0);
		Set<Job> jobs = new HashSet<Job>();
		for (Class<?> current : classes) {
			if (!Modifier.isAbstract(current.getModifiers()) && Job.class.isAssignableFrom(current)) {
				try {
					jobs.add(current.asSubclass(Job.class).newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					log.error("Failed to create job class instance of " + current.getName());
				}
			}
		}
		log.debug("Found " + jobs.size() + " jobs in " + elapsed + " ms : " + Arrays.toString(jobs.toArray(new Job[0])));
		return jobs;
	}

	/**
	 * The findByInterface method searches for all classes in the current context which implementing the given interface and returns them as a
	 * set.
	 * 
	 * @param type
	 *          The type to set
	 * @return Set of jobs
	 */
	public static final Set<Job> findImplementedJobs() {
		long nanos = System.nanoTime();
		log.debug("Start searching for job classes with interface " + Job.class.getSimpleName());
		URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();
		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(new SubTypesScanner(),
				new TypeAnnotationsScanner().filterResultsBy(new FilterBuilder().exclude(Job.class.getName()))));
		Set<Class<? extends Job>> classes = reflections.getSubTypesOf(Job.class);
		long elapsed = Math.round((double) (System.nanoTime() - nanos) / 1000000.0);
		Set<Job> jobs = new HashSet<Job>();
		for (Class<? extends Job> current : classes) {
			if (!Modifier.isAbstract(current.getModifiers())) {
				try {
					jobs.add(current.asSubclass(Job.class).newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					log.error("Failed to create job class instance of " + current.getName());
				}
			}
		}
		log.debug("Found " + jobs.size() + " jobs in " + elapsed + " ms : " + Arrays.toString(jobs.toArray(new Job[0])));
		return jobs;
	}
}
