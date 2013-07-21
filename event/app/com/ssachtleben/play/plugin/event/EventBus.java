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
  private ConcurrentHashMap<String, List<Method>> listeners = new ConcurrentHashMap<String, List<Method>>();

  /**
   * Create a akka ActorSystem for asyncronous events.
   */
  private ActorSystem system = ActorSystem.create("events");

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
  public void publishAsync(Object event) {
    log.info("Publish async " + event);
    publishAsync(listeners.get(""), event);
  }

  @Override
  public void publishAsync(String topic, Object event) {
    log.info("Publish async to " + topic + " " + event);
    publishAsync(listeners.get(topic), event);
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
