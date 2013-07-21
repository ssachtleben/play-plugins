package com.ssachtleben.play.plugin.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import play.Logger;

/**
 * The default EventService implementation.
 * 
 * @author Sebstian Sachtleben
 */
public class EventBus implements EventService {
  private static final Logger.ALogger log = Logger.of(EventBus.class);

  /**
   * The map contains of several method lists grouped by topic.
   */
  private ConcurrentHashMap<String, List<Method>> listeners = new ConcurrentHashMap<String, List<Method>>();

  @Override
  public void publish(Object event) {
    log.info("Publish " + event);
    publish(listeners.get(""), event);
  }

  @Override
  public void publish(String topic, Object event) {
    log.info("Publish to " + topic + " " + event);
    publish(listeners.get(topic), event);
  }

  @Override
  public void addListener(Method method) {
    log.info("Add listener " + method);
    add("", method);
  }

  @Override
  public void addListener(String topic, Method method) {
    log.info("Add listener to " + topic + " " + method);
    add(topic, method);
  }

  /**
   * Add a new method to our listeners cache.
   * 
   * @param topic
   *          The topic receiver.
   * @param method
   *          The method to invoke.
   */
  private void add(String topic, Method method) {
    if (!listeners.contains(topic)) {
      listeners.put(topic, new ArrayList<Method>());
    }
    listeners.get(topic).add(method);
  }

  /**
   * Publishes a event to a list of recievers. It means the methods will be
   * invoked with the given event.
   * 
   * @param receivers
   *          A list of method.
   * @param event
   *          The event to publish.
   */
  private void publish(List<Method> receivers, Object event) {
    Iterator<Method> iter = receivers.iterator();
    while (iter.hasNext()) {
      Method method = iter.next();
      try {
        method.invoke(null, event);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        log.error("Failed to invoke " + method, e);
      }
    }
  }
}
