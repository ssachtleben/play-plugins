package com.ssachtleben.play.plugin.event;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import play.Logger;

import com.ssachtleben.play.plugin.event.annotations.Observer;

/**
 * Provides event bindings for subscriptions. TODO: improve javadoc here...
 * 
 * @author Sebastian Sachtleben
 */
public class EventBinding {
	private static final Logger.ALogger log = Logger.of(EventBinding.class);

	private Object target = null;

	private Method method = null;

	private ReferenceStrength strengthReference = ReferenceStrength.WEAK;

	private boolean proxy;

	/**
	 * Default constructor takes {@link Object} target and {@link Method} parameter.
	 * 
	 * @param target
	 *          The bound {@link Object}.
	 * @param method
	 *          The bound {@link Method}.
	 */
	public EventBinding(final Object target, final Method method) {
		this(method);
		this.target = target;
	}

	/**
	 * Default constructor takes {@link Method} parameter.
	 * 
	 * @param method
	 *          The bound {@link Method}.
	 */
	public EventBinding(final Method method) {
		this.method = method;
		if (method().isAnnotationPresent(Observer.class)) {
			this.strengthReference = method().getAnnotation(Observer.class).referenceStrength();
		}
		this.proxy = !Modifier.isStatic(method.getModifiers());
	}

	/**
	 * @return the target
	 */
	public Object target() {
		return target;
	}

	/**
	 * @return The bound {@link Method}.
	 */
	public Method method() {
		return method;
	}

	/**
	 * @return the proxy
	 */
	public boolean proxy() {
		return proxy;
	}

	public boolean matches(Object payload) {
		return matches(new Object[] { payload });
	}

	public boolean matches(Object... payload) {
		Class<?>[] parameterTypes = method().getParameterTypes();
		List<Class<?>> payloadTypes = new ArrayList<Class<?>>();
		for (Object param : payload) {
			payloadTypes.add(param.getClass());
		}
		log.debug(String.format("strength = %s", strengthReference));
		log.debug(String.format("method   = %s", Arrays.toString(payloadTypes.toArray(new Class<?>[0]))));
		log.debug(String.format("payload  = %s", Arrays.toString(parameterTypes)));
		if (parameterTypes.length != payload.length) {
			log.debug(String.format("Length not match for strong reference"));
			return false;
		}
		int notAssignable = 0;
		for (int i = 0; i < parameterTypes.length; i++) {
			if (i < payload.length) {
				if (!parameterTypes[i].isAssignableFrom(payload[i].getClass())) {
					log.debug(String.format("Value NOT assignable %s %s", parameterTypes[i], payload[i]));
					if (isWeakReference()) {
						if (parameterTypes[i].isPrimitive()) {
							log.debug("Cannot assign null value to primitive");
							return false;
						}
						log.debug("Set value to null and keep weak reference");
						payload[i] = null;
						notAssignable++;
					} else {
						return false;
					}
				} else {
					log.debug(String.format("Value is assignable %s %s", parameterTypes[i], payload[i]));
				}
			}
		}
		if (notAssignable == parameterTypes.length) {
			log.debug("No values assignable at all");
			return false;
		}
		return true;
	}

	private boolean isWeakReference() {
		return ReferenceStrength.WEAK.equals(strengthReference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventBinding other = (EventBinding) obj;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventBinding [target=" + target + ", method=" + method + ", strengthReference=" + strengthReference + "]";
	}
}
