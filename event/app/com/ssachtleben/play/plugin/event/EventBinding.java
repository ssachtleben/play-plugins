package com.ssachtleben.play.plugin.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import play.Logger;

/**
 * Provides event bindings for subscriptions. TODO: improve javadoc here...
 * 
 * @author Sebastian Sachtleben
 */
public class EventBinding {
	private static final Logger.ALogger log = Logger.of(EventBinding.class);

	/**
	 * Contains the bound {@link Method}.
	 */
	private Method method;

	/**
	 * Default constructor takes {@link Method} parameter.
	 * 
	 * @param method
	 *          The bound {@link Method}.
	 */
	public EventBinding(final Method method) {
		this.method = method;
	}

	/**
	 * @return The bound {@link Method}.
	 */
	public Method method() {
		return method;
	}

	public boolean matches(Object param) {
		return matches(new Object[] { param });
	}

	public boolean matches(Object... params) {
		Class<?>[] parameterTypes = method().getParameterTypes();
		List<Class<?>> paramClasses = new ArrayList<Class<?>>();
		for (Object param : params) {
			paramClasses.add(param.getClass());
		}
		log.info(String.format("method=%s", Arrays.toString(paramClasses.toArray(new Class<?>[0]))));
		log.info(String.format("params=%s", Arrays.toString(parameterTypes)));
		if (parameterTypes.length != params.length) {
			log.info(String.format("length not match"));
			return false;
		}
		for (int i = 0; i < parameterTypes.length; i++) {
			if (!parameterTypes[i].isAssignableFrom(params[i].getClass())) {
				log.info(String.format("NOT assignable %s %s", parameterTypes[i], params[i]));
				return false;
			} else {
				log.info(String.format("is assignable %s %s", parameterTypes[i], params[i]));
			}
		}
		return true;
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
		return "EventBinding [method=" + method + "]";
	}
}
