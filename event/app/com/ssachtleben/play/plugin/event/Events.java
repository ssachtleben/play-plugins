package com.ssachtleben.play.plugin.event;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import play.Play;

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
      instance = createService();
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
  private static EventService createService() {
    String eventService = Play.application().configuration().getString("eventService", EventBus.class.getName());
    Class<?> clazz = null;
    try {

      clazz = Class.forName(eventService, true, Thread.currentThread().getContextClassLoader());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(String.format("Could not find event service class: %s", eventService));
    }
    Object service;
    try {
      Constructor<?> c = clazz.getDeclaredConstructor((Class[]) null);
      c.setAccessible(true); // hack
      service = c.newInstance((Object[]) null);
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
      throw new RuntimeException(String.format("Failed to invoke constructor of %s", eventService));
    }
    try {
      return (EventService) service;
    } catch (ClassCastException e) {
      throw new RuntimeException(String.format("ClassCastException during casting %s to %s", eventService, EventService.class));
    }
  }
}
