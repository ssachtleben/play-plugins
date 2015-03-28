package com.ssachtleben.play.plugin.event;

import java.lang.reflect.Method;
import java.util.Set;

import play.Application;

import com.ssachtleben.play.plugin.base.ExtendedPlugin;
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
public class EventPlugin extends ExtendedPlugin {

  /**
   * Creates a new EventPlugin.
   * 
   * @param app
   *          The app to set
   */
  public EventPlugin(final Application app) {
    super(app);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ssachtleben.play.plugin.base.ExtendedPlugin#name()
   */
  @Override
  public String name() {
    return "event";
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ssachtleben.play.plugin.base.ExtendedPlugin#start()
   */
  @Override
  public void start() {
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

  /*
   * (non-Javadoc)
   * 
   * @see com.ssachtleben.play.plugin.base.ExtendedPlugin#stop()
   */
  @Override
  public void stop() {
    Events.instance().unregisterAll();
  }
}
