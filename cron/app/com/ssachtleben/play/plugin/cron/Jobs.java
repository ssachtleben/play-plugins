package com.ssachtleben.play.plugin.cron;

import java.util.Arrays;
import java.util.Collection;

import play.Logger;

import com.ssachtleben.play.plugin.base.MapCache;

public class Jobs extends MapCache<Class<?>, JobData> {
	private static final Logger.ALogger log = Logger.of(Jobs.class);

	private static Jobs instance = new Jobs();

	private static Jobs instance() {
		return instance;
	}

	private Jobs() {
		// empty
	}

	public static JobData get(final Class<?> clazz) {
		return instance().cache().get(clazz);
	}

	public static Collection<JobData> all() {
		return instance().cache().values();
	}

	public static boolean contains(final Class<?> clazz) {
		log.info(String.format("Contains %s - %s", clazz, Arrays.toString(instance().cache().keySet().toArray(new Class<?>[0]))));
		return instance().cache().containsKey(clazz);
	}

	public static JobData add(final JobData data) {
		return add(data.job().getClass(), data);
	}

	public static JobData add(final Class<?> clazz, final JobData data) {
		log.info(String.format("Adding %s - %s", clazz, data));
		return instance().cache().put(clazz, data);
	}
}
