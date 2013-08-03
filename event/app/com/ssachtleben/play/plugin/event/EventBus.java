package com.ssachtleben.play.plugin.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
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
import akka.actor.Scheduler;
import akka.actor.UntypedActor;

/**
 * The Eventbus implements the core interface {@link EventService} and is accessible via {@link Events#instance()}.
 * <p/>
 * Synchronous events will be delivered immediately after publishing and blocks until all handler finished their work. The {@link Scheduler}
 * from <a href="http://akka.io/">akka</a> is used to publish asynchronous events with a delay of 50ms.
 * <p/>
 * Using different implementations for {@link EventService} is currently not possible, but planed for future releases.
 * <p/>
 * Do not try to create this class, use the provides instance via {@link Events#instance()}.
 * 
 * @author Sebstian Sachtleben
 * @see Events
 */
public class EventBus implements EventService {
	private static final Logger.ALogger log = Logger.of(EventBus.class);

	public static String EMPTY_TOPIC = "";

	/**
	 * The map contains of several method lists grouped by topic.
	 */
	protected ConcurrentHashMap<String, List<Method>> subscribers = new ConcurrentHashMap<String, List<Method>>();

	/**
	 * Create a akka ActorSystem for asyncronous events.
	 */
	protected ActorSystem system = ActorSystem.create("events");

	/**
	 * The name of the EventBus instance.
	 */
	protected String name;

	/**
	 * Creates a EventBus instance named "default". Get the instance via {@link Events#instance()}.
	 */
	private EventBus() {
		this("default");
	}

	/**
	 * Creates a EventBus instance with the given {@code name}. Get the instance via {@link Events#instance()}.
	 * 
	 * @param name
	 *          The EventBus name should be a valid java identifier.
	 */
	private EventBus(String name) {
		this.name = name;
	}

	@Override
	public void publish(Object event) {
		log.info(String.format("Publish %s", event));
		if (subscribers.containsKey(EMPTY_TOPIC)) {
			publish(subscribers.get(EMPTY_TOPIC), event);
		}
	}

	@Override
	public void publish(String topic, Object event) {
		log.info(String.format("Publish to %s %s", topic, event));
		if (subscribers.containsKey(topic)) {
			publish(subscribers.get(topic), event);
		}
	}

	@Override
	public void publish(Type genericType, Object event) {
		log.warn("publish(Type, Object) is not implemented yet");
	}

	@Override
	public void publishAsync(Object event) {
		log.info(String.format("Publish async %s", event));
		if (subscribers.containsKey(EMPTY_TOPIC)) {
			publishAsync(subscribers.get(EMPTY_TOPIC), event);
		}
	}

	@Override
	public void publishAsync(String topic, Object event) {
		log.info(String.format("Publish async to %s %s", topic, event));
		if (subscribers.containsKey(topic)) {
			publishAsync(subscribers.get(topic), event);
		}
	}

	@Override
	public void publishAsync(Type genericType, Object event) {
		log.warn("publishAsync(Type, Object) is not implemented yet");
	}

	@Override
	public void register(Object object) {
		log.info(String.format("Register %s", object));
		add(EMPTY_TOPIC, object);
	}

	@Override
	public void register(String topic, Object object) {
		log.info(String.format("Register %s %s", topic, object));
		add(topic, object);
	}

	@Override
	public void unregister(Object object) {
		log.info(String.format("Unregister %s", object));
		if (subscribers.containsKey(EMPTY_TOPIC)) {
			subscribers.get(EMPTY_TOPIC).remove(object);
		}
	}

	@Override
	public void unregister(String topic, Object object) {
		log.info(String.format("Unregister %s %s", topic, object));
		if (subscribers.containsKey(topic)) {
			subscribers.get(topic).remove(object);
		}
	}

	@Override
	public void unregisterAll() {
		log.info("Unregisters all subscribers");
		subscribers.clear();
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
		if (!subscribers.containsKey(topic)) {
			subscribers.put(topic, new ArrayList<Method>());
		}
		subscribers.get(topic).add(method);
	}

	/**
	 * Publishes synchronously a event to a list of recievers. It means the methods will be invoked with the given event.
	 * 
	 * @param receivers
	 *          A list of method.
	 * @param event
	 *          The event to publish.
	 */
	private void publish(List<Method> receivers, Object event) {
		Iterator<Method> iter = receivers.iterator();
		boolean published = false;
		while (iter.hasNext()) {
			Method method = iter.next();
			try {
				method.invoke(null, event);
				if (!published) {
					published = true;
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				log.error("Failed to invoke " + method, e);
			}
		}
		if (!published && !(event instanceof DeadEvent)) {
			publish(new DeadEvent(this, event));
		}
	}

	/**
	 * Publishes asynchronously a event to a list of recievers. It means the methods will be invoked with the given event.
	 * 
	 * @param receivers
	 *          A list of method.
	 * @param event
	 *          The event to publish.
	 */
	private void publishAsync(List<Method> receivers, Object event) {
		Iterator<Method> iter = receivers.iterator();
		boolean published = false;
		while (iter.hasNext()) {
			Method method = iter.next();
			try {
				ActorRef eventActor = system.actorOf(new Props(EventActor.class));
				EventDeliveryRequest request = new EventDeliveryRequest(method, event);
				system.scheduler().scheduleOnce(Duration.create(50, TimeUnit.MILLISECONDS), eventActor, request, system.dispatcher());
				if (!published) {
					published = true;
				}
			} catch (IllegalArgumentException e) {
				log.error("Failed to schedule async event delivery request for " + method, e);
			}
		}
		if (!published && !(event instanceof DeadEvent)) {
			publishAsync(new DeadEvent(this, event));
		}
	}

	/**
	 * Provides the subscribers map.
	 * 
	 * @return the subscribers
	 */
	public ConcurrentHashMap<String, List<Method>> getSubscribers() {
		return subscribers;
	}

	/**
	 * The EventDeliveryRequest is used als a data transfer object between the EventBus and the EventActor.
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
				log.error("Failed to publish async event '" + obj.getClass().getName() + "' because it is not an instance of '"
						+ EventDeliveryRequest.class.getName() + "'");
				return;
			}
			EventDeliveryRequest request = (EventDeliveryRequest) obj;
			request.getMethod().invoke(null, request.getEvent());
		}
	}
}
