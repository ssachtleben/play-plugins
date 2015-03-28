package com.ssachtleben.play.plugin.auth;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import play.Application;
import play.Logger;

import com.ssachtleben.play.plugin.auth.annotations.Authenticates;
import com.ssachtleben.play.plugin.auth.annotations.Provider;
import com.ssachtleben.play.plugin.auth.models.Identity;
import com.ssachtleben.play.plugin.auth.providers.BaseProvider;

public class AuthUtils {

  /**
   * The logger for {@link AuthUtils} class.
   */
  private static final Logger.ALogger log = Logger.of(AuthUtils.class);

  /**
   * Find all {@link Provider} annotated provider classes.
   * 
   * @return Set of {@link BaseProvider} instances.
   */
  @SuppressWarnings("unchecked")
  public static Set<BaseProvider<Identity>> findProviders(Application app) {
    long nanos = System.nanoTime();
    log.info(String.format("Start searching for provider classes with annotation @%s", Provider.class.getSimpleName()));
    URL[] urls = ((URLClassLoader) (AuthUtils.class.getClassLoader())).getURLs();
    Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(new TypeAnnotationsScanner()));
    Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Provider.class);
    long elapsed = Math.round((double) (System.nanoTime() - nanos) / 1000000.0);
    Set<BaseProvider<Identity>> foundClasses = new HashSet<BaseProvider<Identity>>();
    for (Class<?> current : classes) {
      if (!Modifier.isAbstract(current.getModifiers()) && BaseProvider.class.isAssignableFrom(current)) {
        try {
          foundClasses.add(current.asSubclass(BaseProvider.class).newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
          log.error("Failed to create job class instance of " + current.getName());
        }
      }
    }
    log.info("Found " + foundClasses.size() + " providers in " + elapsed + " ms : " + Arrays.toString(foundClasses.toArray(new BaseProvider[0])));
    return foundClasses;
  }

  /**
   * Find all {@link Authenticates} annotated methods.
   * 
   * @return Set of methods.
   */
  public static Set<Method> findAuthMethods() {
    long nanos = System.nanoTime();
    log.info("Start searching for methods with annotation @" + Authenticates.class.getSimpleName());
    URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();
    Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(new MethodAnnotationsScanner()));
    Set<Method> results = reflections.getMethodsAnnotatedWith(Authenticates.class);
    long elapsed = Math.round((double) (System.nanoTime() - nanos) / 1000000.0);
    log.info("Found " + results.size() + " methods in " + elapsed + " ms : " + Arrays.toString(results.toArray(new Object[0])));
    return results;
  }

}
