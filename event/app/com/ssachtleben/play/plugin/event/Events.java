package com.ssachtleben.play.plugin.event;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Provides via {@link #instance()} the current used {@link EventService}
 * implementation which allows to publishing events, register new and unregister
 * existing subscribers.
 * <p/>
 * Its planed to allow custom implementations of {@link EventService} and serve
 * them here, but its not possible right now. The implemented instance is always
 * {@link EventBus}.
 * 
 * @author Sebastian Sachtleben
 * @see EventService
 */
public class Events {

  /**
   * Static instance of the current EventService implementation.
   */
  private static EventService instance;

  /**
   * Returns current {@link EventService} implementation instance which is
   * {@link EventBus}.
   * 
   * @return The EventService instance.
   */
  public static EventService instance() {
    if (instance == null) {
      instance = createInstance();
    }
    return instance;
  }

  /**
   * Create a new instance of {@link EventBus}. The constructor is private to
   * avoid additional instances except this. A reflection hack will be used to
   * change the constructor accessibility.
   * 
   * @return New instance of {@link EventBus}.
   */
  private static EventBus createInstance() {
    Class<EventBus> clazz = EventBus.class;
    try {
      Constructor<EventBus> c = clazz.getDeclaredConstructor((Class[]) null);
      c.setAccessible(true); // hack
      return c.newInstance((Object[]) null);
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
      e.printStackTrace();
    }
    return null;
  }
}
