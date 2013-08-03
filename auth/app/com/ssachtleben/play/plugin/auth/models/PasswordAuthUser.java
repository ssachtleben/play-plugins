package com.ssachtleben.play.plugin.auth.models;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

import play.Logger;

/**
 * The {@link PasswordAuthUser} provides clear password for {@link PasswordEmailAuthUser} and {@link PasswordUsernameAuthUser} and also the
 * create new passwords and compare hashed and candidates.
 * 
 * @author Sebastian Sachtleben
 * @see PasswordEmailAuthUser
 * @see PasswordUsernameAuthUser
 */
@SuppressWarnings("serial")
public abstract class PasswordAuthUser extends AuthUser {
	private static final Logger.ALogger log = Logger.of(PasswordAuthUser.class);

	/**
	 * Keeps the clear password used during authentication process.
	 */
	private String clearPassword;

	/**
	 * Default constructor for {@link PasswordAuthUser}.
	 * 
	 * @param clearPassword
	 *          The clearPassword to set
	 */
	public PasswordAuthUser(final String clearPassword) {
		this.clearPassword = clearPassword;
	}

	/**
	 * Check if password is valid. TODO: This should be somehow handled by application...
	 * 
	 * @param hashed
	 *          The hased password.
	 * @param candidate
	 *          The clear password candidate.
	 * @return The success boolean.
	 */
	public static boolean checkPassword(final String hashed, final String candidate) {
		log.info("Compare passwords");
		String salt = hashed.substring(0, hashed.indexOf("$"));
		String hashedPW = salt + "$" + DigestUtils.md5Hex(salt + "$" + candidate);
		boolean validated = hashedPW.equals(hashed);
		log.info(String.format("Password 1: %s", hashed));
		log.info(String.format("Password 2: %s", hashedPW));
		log.info(String.format("Password %s", (validated ? "is valid" : "is not valid")));
		return validated;
	}

	/**
	 * Create new password. TODO: This should be somehow handled by application...
	 * 
	 * @param clearPassword
	 *          The clearPassword to set
	 * @return The hashed password.
	 */
	public static String createPassword(final String clearPassword) {
		String salt = RandomStringUtils.randomAlphabetic(4);
		return salt + "$" + DigestUtils.md5Hex(salt + "$" + clearPassword);
	}

	/**
	 * Get hashed password.
	 * 
	 * @return Returns the hashed password.
	 */
	public String hashedPassword() {
		return createPassword(clearPassword());
	}

	/**
	 * .
	 * 
	 * @return The clear password for authentication purpose.
	 */
	public String clearPassword() {
		return clearPassword;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssachtleben.play.plugin.auth.models.Identity#id()
	 */
	@Override
	public String id() {
		return hashedPassword();
	}
}
