package com.ssachtleben.play.plugin.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import play.Logger;

public class ClassUtils {
	private static final Logger.ALogger log = Logger.of(ClassUtils.class);

	public static final Set<?> findAnnotated(Class<? extends Annotation> annotation) {
		long nanos = System.nanoTime();
		log.debug("Start searching for classes with annotation @" + annotation.getSimpleName());
		URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();
		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(urls).setScanners(new TypeAnnotationsScanner()));
		Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);
		long elapsed = Math.round((double) (System.nanoTime() - nanos) / 1000000.0);
		Set<Class<?>> result = new HashSet<Class<?>>();
		for (Class<?> current : classes) {
			if (!Modifier.isAbstract(current.getModifiers())) {
				result.add(current);
			}
		}
		log.debug("Found " + result.size() + " classes in " + elapsed + " ms : " + Arrays.toString(result.toArray(new Class<?>[0])));
		return result;
	}

}
