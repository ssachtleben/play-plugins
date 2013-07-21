package com.ssachtleben.play.plugin.event;

import java.lang.reflect.Method;

/**
 * The EventBus interface provides event subscription and publication services.
 * Its a simple interface for an EventService.
 * 
 * @author Sebastian Sachtleben
 */
public interface EventService {

  /**
   * Publishes synchronously a event to all registered listeners.
   * 
   * @param event
   *          The event to publish
   */
  void publish(Object event);

  /**
   * Publishes synchronously a event to all registered listeners of a specific
   * topic.
   * 
   * @param topic
   *          The topic receiver
   * @param event
   *          The event to publish
   */
  void publish(String topic, Object event);

  /**
   * Publishes asynchronously a event to all registered listeners.
   * 
   * @param event
   *          The event to publish
   */
  void publishAsync(Object event);

  /**
   * Publishes asynchronously a event to all registered listeners of a specific
   * topic.
   * 
   * @param topic
   *          The topic receiver
   * @param event
   *          The event to publish
   */
  void publishAsync(String topic, Object event);

  /**
   * Add new method listener to current service.
   * 
   * @param method
   *          The method to invoke
   */
  void addListener(Method method);

  /**
   * Add new method listener to current service for a specific topic.
   * 
   * @param topic
   *          The topic receiver
   * @param method
   *          The method to invoke
   */
  void addListener(String topic, Method method);

}
