package com.payneteasy.superfly.password;

/**
 * Supplies salt values. That salt is used by {@link PasswordEncoder} instances.
 * This is intended to be stable (i.e. it should return the same instance for
 * the same argument), unlike {@link SaltGenerator} which generates new value
 * on each call.
 * 
 * @author Roman Puchkovskiy
 * @see PasswordEncoder
 */
public interface SaltSource {
	/**
	 * Returns salt value.
	 * 
	 * @param username	username for which to get salt
	 * @return salt
	 */
	String getSalt(String username);
}
