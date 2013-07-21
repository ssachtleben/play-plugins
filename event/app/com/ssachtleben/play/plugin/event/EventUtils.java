package com.ssachtleben.play.plugin.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import play.Logger;

/**
 * The EventUtils provides methods to find @Observer annotated classes in the
 * classpath via reflection.
 * 
 * @author Sebastian Sachtleben
 */
public class EventUtils {
  private static final Logger.ALogger log = Logger.of(EventUtils.class);

  /**
   * Suppresses default constructor, ensuring non-instantiability.
   */
  private EventUtils() {

  }

  /**
   * Searches for annotated methods in current classpath and returns them as a
   * set.
   * 
   * @param annotation
   *          The annotation to set
   * @return Set of methods
   */
  public static final Set<Method> findAnnotatedMethods(Class<? extends Annotation> annotation) {
    long nanos = System.nanoTime();
    log.info("Start searching for methods with annotation @" + annotation.getSimpleName());
    URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();
    Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(new MethodAnnotationsScanner()));
    Set<Method> results = reflections.getMethodsAnnotatedWith(annotation);
    long elapsed = Math.round((double) (System.nanoTime() - nanos) / 1000000.0);
    log.info("Found " + results.size() + " methods in " + elapsed + " ms : " + Arrays.toString(results.toArray(new Object[0])));
    return results;
  }
}
