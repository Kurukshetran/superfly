package com.payneteasy.superfly.service.impl.remote;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.payneteasy.superfly.api.ActionDescription;
import com.payneteasy.superfly.api.AuthenticationRequestInfo;
import com.payneteasy.superfly.api.BadPublicKeyException;
import com.payneteasy.superfly.api.MessageSendException;
import com.payneteasy.superfly.api.PolicyValidationException;
import com.payneteasy.superfly.api.RoleGrantSpecification;
import com.payneteasy.superfly.api.SSOService;
import com.payneteasy.superfly.api.SSOUser;
import com.payneteasy.superfly.api.SSOUserWithActions;
import com.payneteasy.superfly.api.UserDescription;
import com.payneteasy.superfly.api.UserExistsException;
import com.payneteasy.superfly.api.UserNotFoundException;
import com.payneteasy.superfly.model.ui.user.UserForDescription;
import com.payneteasy.superfly.resetpassword.ResetPasswordStrategy;
import com.payneteasy.superfly.service.InternalSSOService;
import com.payneteasy.superfly.spisupport.HOTPService;

/**
 * Implementation of SSOService.
 * 
 * @author Roman Puchkovskiy
 */
public class SSOServiceImpl implements SSOService {
	
	private InternalSSOService internalSSOService;
	private HOTPService hotpService;
	private ResetPasswordStrategy resetPasswordStrategy;
	private SubsystemIdentifierObtainer subsystemIdentifierObtainer = new AuthRequestInfoObtainer();

	@Required
	public void setInternalSSOService(InternalSSOService internalSSOService) {
		this.internalSSOService = internalSSOService;
	}

	@Required
	public void setHotpService(HOTPService hotpService) {
		this.hotpService = hotpService;
	}

	@Required
	public void setResetPasswordStrategy(ResetPasswordStrategy resetPasswordStrategy) {
		this.resetPasswordStrategy = resetPasswordStrategy;
	}

	public void setSubsystemIdentifierObtainer(
			SubsystemIdentifierObtainer subsystemIdentifierObtainer) {
		this.subsystemIdentifierObtainer = subsystemIdentifierObtainer;
	}

	/**
	 * @see SSOService#authenticate(String, String, AuthenticationRequestInfo)
	 */
	public SSOUser authenticate(String username, String password,
			AuthenticationRequestInfo authRequestInfo) {
		return internalSSOService.authenticate(username, password,
				obtainSubsystemIdentifier(authRequestInfo.getSubsystemIdentifier()),
				authRequestInfo.getIpAddress(),
				authRequestInfo.getSessionInfo());
	}

	/**
	 * @see SSOService#sendSystemData(String, com.payneteasy.superfly.api.ActionDescription[])
	 */
	public void sendSystemData(String systemIdentifier,
			ActionDescription[] actionDescriptions) {
		internalSSOService.saveSystemData(obtainSubsystemIdentifier(systemIdentifier),
				actionDescriptions);
	}
	
	/**
	 * @see SSOService#getUsersWithActions(String)
	 */
	public List<SSOUserWithActions> getUsersWithActions(
			String subsystemIdentifier) {
		return internalSSOService.getUsersWithActions(
				obtainSubsystemIdentifier(subsystemIdentifier));
	}
	
	/**
	 * @see SSOService#registerUser(String, String, String, String, RoleGrantSpecification[])
	 */
	public void registerUser(String username, String password, String email,
			String subsystemIdentifier, RoleGrantSpecification[] roleGrants,
			String name, String surname, String secretQuestion, String secretAnswer,
			String publicKey)
			throws UserExistsException, PolicyValidationException, BadPublicKeyException, MessageSendException {
		internalSSOService.registerUser(username, password, email,
				obtainSubsystemIdentifier(subsystemIdentifier), roleGrants,
				name, surname, secretQuestion, secretAnswer, publicKey);
	}

	/**
	 * @see SSOService#authenticateUsingHOTP(String, String)
	 */
	public boolean authenticateUsingHOTP(String username, String hotp) {
		return internalSSOService.authenticateHOTP(username, hotp);
	}
	
	protected String obtainSubsystemIdentifier(String systemIdentifier) {
		return subsystemIdentifierObtainer.obtainSubsystemIdentifier(systemIdentifier);
	}

	public void changeTempPassword(String userName, String password) throws PolicyValidationException {
		internalSSOService.changeTempPassword(userName, password);
	}

	/**
	 * @see SSOService#getUserDescription(String)
	 */
	public UserDescription getUserDescription(String username) {
		UserForDescription user = internalSSOService.getUserDescription(username);
		UserDescription result = new UserDescription();
		result.setUsername(user.getUsername());
		result.setEmail(user.getEmail());
		result.setFirstName(user.getName());
		result.setLastName(user.getSurname());
		result.setPublicKey(user.getPublicKey());
		return result;
	}

	/**
	 * @see SSOService#resetAndSendOTPTable(String)
	 */
	public void resetAndSendOTPTable(String username) throws UserNotFoundException, MessageSendException {
		UserForDescription user = internalSSOService.getUserDescription(username);
		if (user == null) {
			throw new UserNotFoundException(username);
		}
		hotpService.sendTableIfSupported(user.getUserId());
	}

	/**
	 * @see SSOService#updateUserDescription(UserDescription)
	 */
	public void updateUserDescription(UserDescription user)
			throws UserNotFoundException, BadPublicKeyException {
		UserForDescription userForDescription = internalSSOService.getUserDescription(user.getUsername());
		if (userForDescription == null) {
			throw new UserNotFoundException(user.getUsername());
		}
		userForDescription.setEmail(user.getEmail());
		userForDescription.setName(user.getFirstName());
		userForDescription.setSurname(user.getLastName());
		userForDescription.setPublicKey(user.getPublicKey());
		internalSSOService.updateUserForDescription(userForDescription);
	}

	/**
	 * @see SSOService#resetPassword(String, String)
	 */
	public void resetPassword(String username, String newPassword)
			throws UserNotFoundException, PolicyValidationException {
		UserForDescription user = internalSSOService.getUserDescription(username);
		if (user == null) {
			throw new UserNotFoundException(username);
		}
		resetPasswordStrategy.resetPassword(user.getUserId(), username, newPassword);
	}

}
