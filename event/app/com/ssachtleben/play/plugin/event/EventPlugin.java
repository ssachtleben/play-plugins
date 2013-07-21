package com.ssachtleben.play.plugin.event;

import java.lang.reflect.Method;
import java.util.Set;

import play.Application;
import play.Logger;
import play.Plugin;

import com.ssachtleben.play.plugin.event.annotations.Observer;

/**
 * The EventPlugin register all subscriber methods to the eventbus during
 * application start.
 * 
 * @author Sebastian Sachtleben
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
        Events.instance().addListener(method);
      } else {
        Events.instance().addListener(observer.topic(), method);
      }
    }
    super.onStart();
  }

}
