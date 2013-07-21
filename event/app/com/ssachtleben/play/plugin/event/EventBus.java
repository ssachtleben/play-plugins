package com.ssachtleben.play.plugin.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import play.Logger;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

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
  private ConcurrentHashMap<String, List<Method>> subscribers = new ConcurrentHashMap<String, List<Method>>();

  /**
   * Create a akka ActorSystem for asyncronous events.
   */
  private ActorSystem system = ActorSystem.create("events");

  /**
   * The name of the EventBus instance.
   */
  private String name;

  /**
   * Creates a EventBus instance named "default".
   */
  public EventBus() {
    this("default");
  }

  /**
   * Creates a EventBus instance with the given {@code name}.
   * 
   * @param name
   *          The EventBus name should be a valid java identifier.
   */
  public EventBus(String name) {
    this.name = name;
  }

  @Override
  public void publish(Object event) {
    log.info("Publish " + event);
    publish(subscribers.get(""), event);
  }

  @Override
  public void publish(String topic, Object event) {
    log.info("Publish to " + topic + " " + event);
    publish(subscribers.get(topic), event);
  }

  @Override
  public void publishAsync(Object event) {
    log.info("Publish async " + event);
    publishAsync(subscribers.get(""), event);
  }

  @Override
  public void publishAsync(String topic, Object event) {
    log.info("Publish async to " + topic + " " + event);
    publishAsync(subscribers.get(topic), event);
  }

  @Override
  public void register(Object object) {
    log.info("Register " + object);
    add("", object);
  }

  @Override
  public void register(String topic, Object object) {
    log.info("Register " + topic + " " + object);
    add(topic, object);
  }

  @Override
  public void unregister(Object object) {
    log.warn("Unregister is not implemented yet");
  }

  @Override
  public void unregister(String topic, Object object) {
    log.warn("Unregister is not implemented yet");
  }

  /**
   * Adds new subscriber to the EventBus.
   * 
   * @param topic
   *          The topic name.
   * @param object
   *          The subcriber object.
   */
  private void add(String topic, Object object) {
    if (!(object instanceof Method)) {
      log.warn("Currently only methods can be registered as subscribers");
      return;
    }
    Method method = (Method) object;
    if (!subscribers.contains(topic)) {
      subscribers.put(topic, new ArrayList<Method>());
    }
    subscribers.get(topic).add(method);
  }

  /**
   * Publishes synchronously a event to a list of recievers. It means the
   * methods will be invoked with the given event.
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

  /**
   * Publishes asynchronously a event to a list of recievers. It means the
   * methods will be invoked with the given event.
   * 
   * @param receivers
   *          A list of method.
   * @param event
   *          The event to publish.
   */
  private void publishAsync(List<Method> receivers, Object event) {
    Iterator<Method> iter = receivers.iterator();
    while (iter.hasNext()) {
      Method method = iter.next();
      try {
        ActorRef eventActor = system.actorOf(new Props(EventActor.class));
        EventDeliveryRequest request = new EventDeliveryRequest(method, event);
        system.scheduler().scheduleOnce(Duration.create(50, TimeUnit.MILLISECONDS), eventActor, request, system.dispatcher());
      } catch (IllegalArgumentException e) {
        log.error("Failed to schedule async event delivery request for " + method, e);
      }
    }
  }

  /**
   * The EventDeliveryRequest is used als a data transfer object between the
   * EventBus and the EventActor.
   * 
   * @author Sebastian Sachtleben
   */
  public static class EventDeliveryRequest {

    private Method method;
    private Object event;

    /**
     * The constructor with given method and event.
     * 
     * @param method
     *          The method to invoke
     * @param event
     *          The event to pass
     */
    public EventDeliveryRequest(Method method, Object event) {
      this.method = method;
      this.event = event;
    }

    public Method getMethod() {
      return method;
    }

    public void setMethod(Method method) {
      this.method = method;
    }

    public Object getEvent() {
      return event;
    }

    public void setEvent(Object event) {
      this.event = event;
    }
  }

  /**
   * The EventActor class will be called for every async event.
   * 
   * @author Sebastian Sachtleben
   */
  public static class EventActor extends UntypedActor {
    private static final Logger.ALogger log = Logger.of(EventActor.class);

    @Override
    public void onReceive(Object obj) throws Exception {
      if (!(obj instanceof EventDeliveryRequest)) {
        log.error("Failed to publish async event '" + obj.getClass().getName() + "' because it is not an instance of '" + EventDeliveryRequest.class.getName()
            + "'");
        return;
      }
      EventDeliveryRequest request = (EventDeliveryRequest) obj;
      request.getMethod().invoke(null, request.getEvent());
    }
  }
}
