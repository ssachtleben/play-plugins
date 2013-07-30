package com.ssachtleben.play.plugin.auth.models;

import java.io.Serializable;

/**
 * The authenticated user. TODO: rework this javadoc...
 * 
 * @author Sebastian Sachtleben
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
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getProvider() == null) ? 0 : getProvider().hashCode());
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
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		if (getProvider() == null) {
			if (other.getProvider() != null)
				return false;
		} else if (!getProvider().equals(other.getProvider()))
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
		return getId() + "@" + getProvider();
	}

}
