package com.payneteasy.superfly.service;

import java.util.List;

import com.payneteasy.superfly.api.ActionDescription;
import com.payneteasy.superfly.api.SSOUser;
import com.payneteasy.superfly.api.SSOUserWithActions;

/**
 * Internal service used to implement SSOService.
 * 
 * @author Roman Puchkovskiy
 */
public interface InternalSSOService {
	/**
	 * Authenticates a user.
	 * 
	 * @param username				user name
	 * @param password				user password
	 * @param subsystemIdentifier	identifier of a subsystem from which user
	 * tries to log in
	 * @param userIpAddress			ID address of a user who tries to log in
	 * @param sessionInfo			session info
	 * @return SSOUser instance on success or null on failure
	 */
	SSOUser authenticate(String username, String password,
			String subsystemIdentifier, String userIpAddress,
			String sessionInfo);

	/**
	 * Saves system data.
	 * 
	 * @param subsystemIdentifier	identifier of the system
	 * @param roleDescriptions		descriptions of roles
	 * @param actionDescriptions	descriptions of actions
	 */
	void saveSystemData(String subsystemIdentifier,
			ActionDescription[] actionDescriptions);

	/**
	 * Returns a list of users with their actions granted through role with
	 * the given principal.
	 * 
	 * @param subsystemIdentifier	identifier of the subsystem from which
	 * 								users will be obtained
	 * @param principalName			principal name
	 * @return users with actions
	 */
	List<SSOUserWithActions> getUsersWithActions(String subsystemIdentifier,
			String principalName);
}
