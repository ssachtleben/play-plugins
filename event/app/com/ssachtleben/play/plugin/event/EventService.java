package com.ssachtleben.play.plugin.event;

import java.lang.reflect.Type;

/**
 * An EventService provides services for publishing events, register new and
 * unregister existing subscribers. It provides two types of publications and
 * subcriptions: object-based and topic-based.
 * <p/>
 * In the class-based process an event will be delivered to any handler where
 * the event is <em>assignable</em>.
 * <p/>
 * In the topic-based process only subscribers who registered to the topic where
 * the event object is published will be notified.
 * 
 * @author Sebastian Sachtleben
 */
public interface EventService {

  /**
   * Publishes an {@code event} synchronously and all registered subscribers
   * will be notified if they subscribed to the {@code event}, one of its
   * subclasses, or to one of the interfaces it implements. The call is blocked
   * until every listener has processed the {@code event}.
   * 
   * @param event
   *          The event object to publish.
   */
  void publish(Object event);

  /**
   * Publishes an {@code event} synchronously on a {@code topic} and all
   * registered subscribers to that name will be notified. The call is blocked
   * until every listener has processed the {@code event}.
   * 
   * @param topic
   *          The topic to publish.
   * @param event
   *          The event object to publish.
   */
  void publish(String topic, Object event);

  /**
   * Publishes an {@code event} synchronously to all registered subscribers 
   * of the {@code genericType}. The call is blocked until every listener has 
   * processed the {@code event}.
   * 
   * @param genericType
   * @param event
   */
  void publish(Type genericType, Object event);
  
  /**
   * Publishes an {@code event} asynchronously and all registered subscribers
   * will be notified if they subscribed to the {@code event}, one of its
   * subclasses, or to one of the interfaces it implements.
   * 
   * @param event
   *          The event object to publish.
   */
  void publishAsync(Object event);

  /**
   * Publishes an {@code event} asynchronously on a {@code topic} and all
   * registered subscribers to that name will be notified.
   * 
   * @param topic
   *          The topic to publish.
   * @param event
   *          The event object to publish.
   */
  void publishAsync(String topic, Object event);

  /**
   * Publishes an {@code event} asynchronously to all registered subscribers 
   * of the {@code genericType}.
   * 
   * @param genericType
   * @param event
   */
  void publishAsync(Type genericType, Object event);
  
  /**
   * Register new subscriber to the EventService. The given {@code object} could
   * be a class, so we check for annotated methods, or it could be directly a
   * method which will be registered without checking other methods from the
   * same class.
   * 
   * @param object
   *          The subscriber object.
   */
  void register(Object object);

  /**
   * Register new subscriber to the EventService for publication of a specific
   * {@code topic}. The given {@code object} could be a class, so we check for
   * annotated methods, or it could be directly a method which will be
   * registered without checking other methods from the same class.
   * 
   * @param topic
   *          The topic name to subscribe.
   * @param object
   *          The subscriber object.
   */
  void register(String topic, Object object);

  /**
   * Unregister subscriber from EventService. The given {@code object} could be
   * a class, so we check for annotated methods, or it could be directly a
   * method which will be registered without checking other methods from the
   * same class.
   * 
   * @param object
   *          he subscribed object.
   */
  void unregister(Object object);

  /**
   * Unregister subscriber for a specific {@code topic} from EventService. The
   * given {@code object} could be a class, so we check for annotated methods,
   * or it could be directly a method which will be registered without checking
   * other methods from the same class.
   * 
   * @param topic
   *          The topic name to subscribe.
   * @param object
   *          The subscribed object.
   */
  void unregister(String topic, Object object);

}
