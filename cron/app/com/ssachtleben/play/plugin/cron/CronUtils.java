package com.ssachtleben.play.plugin.cron;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import play.Logger;

/**
 * The CronUtils provides methods to find cronjob annotated classes in the
 * classpath via reflection.
 * 
 * @author Sebastian Sachtleben
 */
public class CronUtils {
  private static final Logger.ALogger log = Logger.of(CronUtils.class);

  /**
   * The findAnnotatedClasses method searches for all classes in the current
   * context annotated by the given annotation and returns them as a set.
   * 
   * @param annotation
   *          The annotation to set
   * @return Set of classes
   */
  public static Set<Class<?>> findAnnotatedClasses(Class<? extends Annotation> annotation) {
    log.debug("Search for classes with annotation " + annotation.getName());
    URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();
    Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(new TypeAnnotationsScanner()));
    Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);
    log.debug("Found " + classes.size() + " classes with annotation " + annotation.getName());
    return classes;
  }
}
