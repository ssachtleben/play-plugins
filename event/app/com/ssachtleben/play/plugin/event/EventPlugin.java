package com.ssachtleben.play.plugin.event;

import java.lang.reflect.Method;
import java.util.Set;

import play.Application;
import play.Logger;
import play.Plugin;

import com.ssachtleben.play.plugin.event.annotations.Observer;

/**
 * During application start the EventPlugin checks for all {@link Observer}
 * annotated methods register them as subscribers at the implemented instance of
 * {@link EventService} which could be received from {@link Events}.
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
  public EventPlugin(Application app) {
    log.debug("Plugin created");
    this.app = app;
  }

  /**
   * Scans classpath during application start and register all annotated handler
   * methods.
   */
  @Override
  public void onStart() {
    log.debug("Plugin started");
    Set<Method> annotatedMethods = EventUtils.findAnnotatedMethods(Observer.class);
    for (Method method : annotatedMethods) {
      Observer observer = method.getAnnotation(Observer.class);
      if (observer.topic() == null || "".equals(observer.topic())) {
        Events.instance().register(method);
      } else {
        Events.instance().register(observer.topic(), method);
      }
    }
    super.onStart();
  }

}
