package com.ssachtleben.play.plugin.cron;

import java.util.Arrays;

import com.ssachtleben.play.plugin.cron.jobs.Job;

/**
 * Contains all important informations about a {@link Job}.
 * 
 * @author Sebastian Sachtleben
 */
public class JobData {

	private Job job;
	private boolean async;
	private Class<?>[] dependsOn;

	public JobData(Job job, boolean async, Class<?>[] dependsOn) {
		this.job = job;
		this.async = async;
		this.dependsOn = dependsOn;
	}

	public Job job() {
		return job;
	}

	public boolean async() {
		return async;
	}

	public Class<?>[] dependsOn() {
		return dependsOn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JobData [job=" + job + ", async=" + async + ", dependsOn=" + Arrays.toString(dependsOn) + "]";
	}
}
