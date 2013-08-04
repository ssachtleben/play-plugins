package com.ssachtleben.play.plugin.event;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.Callable;

import play.Application;
import play.Logger;
import play.Plugin;
import play.libs.Akka;

import com.ssachtleben.play.plugin.event.annotations.Observer;

/**
 * During application start the EventPlugin checks for all {@link Observer} annotated methods register them as subscribers at the
 * implemented instance of {@link EventService} which could be received from {@link Events}.
 * 
 * @author Sebastian Sachtleben
 * @see Observer
 * @see EventService
 * @see Events
 */
public class EventPlugin extends Plugin {
	private static final Logger.ALogger log = Logger.of(EventPlugin.class);

	/**
	 * Current play application instance.
	 */
	protected Application app;

	/**
	 * Creates a new EventPlugin.
	 * 
	 * @param app
	 *          The app to set
	 */
	public EventPlugin(final Application app) {
		log.debug("Plugin created");
		this.app = app;
	}

	/**
	 * Scans classpath during application start and register all annotated handler methods.
	 */
	@Override
	public void onStart() {
		log.debug("Start Plugin");
		if (!app.configuration().getBoolean("event.scanner.active", Boolean.TRUE)) {
			return;
		}
		if (app.configuration().getBoolean("event.scanner.async", Boolean.TRUE)) {
			Akka.future(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					scan();
					return null;
				}
			});
		} else {
			scan();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see play.Plugin#onStop()
	 */
	@Override
	public void onStop() {
		log.debug("Stop Plugin");
		Events.instance().unregisterAll();
	}

	private void scan() {
		Set<Method> annotatedMethods = EventUtils.findAnnotatedMethods(Observer.class);
		for (Method method : annotatedMethods) {
			Observer observer = method.getAnnotation(Observer.class);
			if (observer.topic() == null || "".equals(observer.topic())) {
				log.debug(String.format("Register: %s", method));
				Events.instance().register(method);
			} else {
				log.debug(String.format("Register on topic '%s': %s", observer.topic(), method));
				Events.instance().register(observer.topic(), (Object) method);
			}
		}
	}

}
