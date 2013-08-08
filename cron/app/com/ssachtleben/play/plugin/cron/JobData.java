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
	private String pattern;
	private Class<?>[] dependsOn;

	/**
	 * Creates new job data instance.
	 * 
	 * @param job
	 *          The {@link Job} to execute.
	 * @param async
	 *          Boolean if execution should be async.
	 * @param pattern
	 *          The cron pattern.
	 * @param dependsOn
	 *          Array of {@link Class} which needs to be done.
	 */
	public JobData(Job job, boolean async, String pattern, Class<?>[] dependsOn) {
		this.job = job;
		this.async = async;
		this.pattern = pattern;
		this.dependsOn = dependsOn;
	}

	/**
	 * @return The {@link Job} to execute.
	 */
	public Job job() {
		return job;
	}

	/**
	 * @return Boolean if the execution should be async or not.
	 */
	public boolean async() {
		return async;
	}

	/**
	 * @return The cron pattern when this job will be executed.
	 */
	public String pattern() {
		return pattern;
	}

	/**
	 * @return Array of {@link Class} dependencies which needs to be done before this job will be executed.
	 */
	public Class<?>[] dependsOn() {
		return dependsOn;
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
		result = prime * result + (async ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(dependsOn);
		result = prime * result + ((job == null) ? 0 : job.hashCode());
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
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
		JobData other = (JobData) obj;
		if (async != other.async)
			return false;
		if (!Arrays.equals(dependsOn, other.dependsOn))
			return false;
		if (job == null) {
			if (other.job != null)
				return false;
		} else if (!job.equals(other.job))
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
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
		return "JobData [job=" + job + ", async=" + async + ", pattern=" + pattern + ", dependsOn=" + Arrays.toString(dependsOn) + "]";
	}
}
