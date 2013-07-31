package com.ssachtleben.play.plugin.auth.models;

import java.io.Serializable;

import com.ssachtleben.play.plugin.auth.providers.BaseProvider;
import com.ssachtleben.play.plugin.auth.service.AuthService;

/**
 * The {@link AuthUser} provides {@link #hashCode()}, {@link #equals(Object)} and {@link #toString()} method for all authenticated user
 * exchange models. It will be used as exchange model for {@link AuthService} plugin to find the real user instance.
 * 
 * @author Sebastian Sachtleben
 * @see Identity
 * @see BaseProvider
 */
@SuppressWarnings("serial")
public abstract class AuthUser implements Identity, Serializable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id() == null) ? 0 : id().hashCode());
		result = prime * result + ((provider() == null) ? 0 : provider().hashCode());
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
		final Identity other = (Identity) obj;
		if (id() == null) {
			if (other.id() != null)
				return false;
		} else if (!id().equals(other.id()))
			return false;
		if (provider() == null) {
			if (other.provider() != null)
				return false;
		} else if (!provider().equals(other.provider()))
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
		return id() + "@" + provider();
	}
}
