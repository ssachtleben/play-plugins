package com.ssachtleben.play.plugin.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
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
	protected ConcurrentHashMap<String, List<EventBinding>> subscribers = new ConcurrentHashMap<String, List<EventBinding>>();

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
	public EventResult publish(final Object payload) {
		log.info(String.format("Publish %s", payload));
		return subscribers.containsKey(EMPTY_TOPIC) ? publish(subscribers.get(EMPTY_TOPIC), false, payload) : new EventResult();
	}

	@Override
	public EventResult publish(final String topic, final Object... payload) {
		log.info(String.format("Publish to '%s': %s", topic, Arrays.toString(payload)));
		return subscribers.containsKey(topic) ? publish(subscribers.get(topic), false, payload) : new EventResult();
	}

	@Override
	public EventResult publish(final Type genericType, final Object... payload) {
		log.warn("publish(Type, Object) is not implemented yet");
		return new EventResult();
	}

	@Override
	public void publishAsync(final Object payload) {
		log.info(String.format("Publish async %s", payload));
		if (subscribers.containsKey(EMPTY_TOPIC)) {
			publish(subscribers.get(EMPTY_TOPIC), true, payload);
		}
	}

	@Override
	public void publishAsync(final String topic, final Object... payload) {
		log.info(String.format("Publish async to '%s': %s", topic, Arrays.toString(payload)));
		if (subscribers.containsKey(topic)) {
			publish(subscribers.get(topic), true, payload);
		}
	}

	@Override
	public void publishAsync(final Type genericType, final Object... payload) {
		log.warn("publishAsync(Type, Object) is not implemented yet");
	}

	@Override
	public EventBinding register(final Object object) {
		log.info(String.format("Register %s", object));
		return add(EMPTY_TOPIC, object);
	}

	@Override
	public EventBinding register(final String topic, final Object object) {
		log.info(String.format("Register %s %s", topic, object));
		return add(topic, object);
	}

	@Override
	public boolean unregister(final Object object) {
		log.info(String.format("Unregister %s", object));
		if (subscribers.containsKey(EMPTY_TOPIC)) {
			Iterator<EventBinding> iter = subscribers.get(EMPTY_TOPIC).iterator();
			while (iter.hasNext()) {
				EventBinding binding = iter.next();
				if (binding.method().equals(object)) {
					return unregister(binding);
				}
			}
		}
		return false;
	}

	@Override
	public boolean unregister(final EventBinding binding) {
		if (subscribers.get(EMPTY_TOPIC).contains(binding)) {
			synchronized (subscribers) {
				subscribers.get(EMPTY_TOPIC).remove(binding);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean unregister(final String topic, final Object object) {
		log.info(String.format("Unregister %s %s", topic, object));
		if (subscribers.containsKey(topic)) {
			Iterator<EventBinding> iter = subscribers.get(topic).iterator();
			while (iter.hasNext()) {
				EventBinding binding = iter.next();
				if (binding.method().equals(object)) {
					return unregister(topic, binding);
				}
			}
		}
		return false;
	}

	@Override
	public boolean unregister(final String topic, final EventBinding binding) {
		if (subscribers.get(topic).contains(binding)) {
			synchronized (subscribers) {
				subscribers.get(topic).remove(binding);
			}
			return true;
		}
		return false;
	}

	@Override
	public void unregisterAll() {
		log.info("Unregisters all subscribers");
		synchronized (subscribers) {
			subscribers.clear();
		}
	}

	/**
	 * Adds new subscriber to the EventBus.
	 * 
	 * @param topic
	 *          The topic name.
	 * @param object
	 *          The subcriber object.
	 */
	private EventBinding add(final String topic, final Object object) {
		if (!(object instanceof Method)) {
			log.warn("Currently only methods can be registered as subscribers");
			return null;
		}
		Method method = (Method) object;
		EventBinding binding = null;
		synchronized (subscribers) {
			if (!subscribers.containsKey(topic)) {
				subscribers.put(topic, new ArrayList<EventBinding>());
			}
			binding = new EventBinding(method);
			subscribers.get(topic).add(binding);
		}
		return binding;
	}

	/**
	 * Publishes synchronously a event to a list of recievers. It means the methods will be invoked with the given event.
	 * 
	 * @param receivers
	 *          A list of method.
	 * @param event
	 *          The event to publish.
	 */
	private EventResult publish(final List<EventBinding> receivers, final boolean async, final Object payload) {
		return publish(receivers, async, new Object[] { payload });
	}

	/**
	 * Publishes synchronously a event to a list of recievers. It means the methods will be invoked with the given event.
	 * 
	 * @param receivers
	 *          A list of method.
	 * @param event
	 *          The event to publish.
	 */
	private EventResult publish(final List<EventBinding> receivers, final boolean async, final Object... payload) {
		EventResult result = new EventResult();
		Iterator<EventBinding> iter = receivers.iterator();
		boolean published = false;
		while (iter.hasNext()) {
			EventBinding binding = iter.next();
			log.info(String.format("Found subscriber: %s", binding.method()));
			if (!binding.matches(payload)) {
				log.info(String.format("Ignore %s", binding));
				continue;
			}
			log.info(String.format("Publish to %s", binding));
			if (async) {
				log.info(String.format("Send async"));
				try {
					ActorRef eventActor = system.actorOf(new Props(EventActor.class));
					EventDeliveryRequest request = new EventDeliveryRequest(binding.method(), payload);
					system.scheduler().scheduleOnce(Duration.create(50, TimeUnit.MILLISECONDS), eventActor, request, system.dispatcher());
					if (!published) {
						published = true;
					}
				} catch (IllegalArgumentException e) {
					log.error("Failed to schedule async event delivery request for " + binding.method(), e);
				}
			} else {
				log.info(String.format("Send sync"));
				try {
					binding.method().invoke(null, payload);
					result.getReceivers().add(binding);
					if (!published) {
						published = true;
						result.setPublished(true);
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					log.error("Failed to invoke " + binding.method(), e);
				}
			}
		}
		if (!published && !(payload[0] instanceof DeadEvent)) {
			return publish(new DeadEvent(this, payload[0]));
		}
		return result;
	}

	/**
	 * Provides the subscribers map.
	 * 
	 * @return the subscribers
	 */
	public ConcurrentHashMap<String, List<EventBinding>> getSubscribers() {
		return subscribers;
	}

	/**
	 * The EventDeliveryRequest is used als a data transfer object between the EventBus and the EventActor.
	 * 
	 * @author Sebastian Sachtleben
	 */
	public static class EventDeliveryRequest {

		private Method method;
		private Object[] event;

		public EventDeliveryRequest(Method method, Object event) {
			this(method, new Object[] { event });
		}

		public EventDeliveryRequest(Method method, Object... event) {
			this.method = method;
			this.event = event;
		}

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}

		public Object[] getEvent() {
			return event;
		}

		public void setEvent(Object... event) {
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
