package com.payneteasy.superfly.dao;

import java.util.List;

import com.payneteasy.superfly.model.AuthRole;
import com.payneteasy.superfly.model.UserRegisterRequest;
import com.payneteasy.superfly.model.RoutineResult;
import com.payneteasy.superfly.model.UserWithActions;
import com.payneteasy.superfly.model.ui.user.UICloneUserRequest;
import com.payneteasy.superfly.model.ui.user.UIUser;
import com.payneteasy.superfly.model.ui.user.UIUserForCreate;
import com.payneteasy.superfly.model.ui.user.UIUserForList;

public class UserDaoTest extends AbstractDaoTest {

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void testAddSubsystemWithRole() {
        RoutineResult result = userDao.addSubsystemWithRole(1L, 1L);
        assertRoutineResult(result);
	}

	public void testAuthenticate() {
		List<AuthRole> roles = userDao.authenticate("admin", "password",
				"superfly-demo", null, null);
		assertNotNull("Must authenticate successfully", roles);
	}

	public void testGetUsersAndActions() {
		List<UserWithActions> users = userDao.getUsersAndActions("superfly",
				"admin");
		assertNotNull("Must authenticate successfully", users);
		assertTrue("Must authenticate successfully", users.size() > 0);
	}

	public void testGetUsers() {
		List<UIUserForList> users;
		users = userDao.getUsers(0, 10, DaoConstants.DEFAULT_SORT_FIELD_NUMBER,
				"asc", null, null, null, null);
		assertNotNull("List cannot be null", users);
		users = userDao.getUsers(0, 10, DaoConstants.DEFAULT_SORT_FIELD_NUMBER,
				"asc", "someprefix", 1L, 2L, 1L);
		assertNotNull("List cannot be null", users);
	}

	public void testGetUsersCount() {
		int count = userDao.getUsersCount(null, null, null, null);
		assertTrue("Must get some users", count > 0);
		userDao.getUsersCount("someprefix", 1L, 2L, 1L);
	}

	public void testGetUser() {
		UIUser user = userDao.getUser(-111L);
		assertNull("Must not find a user with ID=111", user);
	}

	public void testRegisterUser() {
		UserRegisterRequest registerUser = new UserRegisterRequest();
		registerUser.setUsername("testRegName");
		registerUser.setPassword("passw");
		registerUser.setEmail("reg@gmail.com");
		registerUser.setSubsystemName("superfly");
		registerUser.setPrincipalName("principals");
		RoutineResult result = userDao.registerUser(registerUser);
		assertRoutineResult(result);
	}

	public void testCreateUser() {
		UIUserForCreate user = new UIUserForCreate();
		user.setUsername("testusercreate1");
		user.setPassword("secret1");
		user.setEmail("email1");
		user.setRoleId(1L);
		RoutineResult result = userDao.createUser(user);
		assertRoutineResult(result);
		assertNotNull("User ID must be generated", user.getId());

		/*
		 * user = userDao.getUser(user.getId());
		 * assertNotNull("Must find a user we have just created", user);
		 */
	}

	public void testUpdateUser() {
		UIUser user = getAnyUser();
		user.setPassword("password");
		user.setEmail("email");
		RoutineResult result = userDao.updateUser(user);
		assertRoutineResult(result);
	}

	private UIUser getAnyUser() {
		long userId = getAnyUserId();
		UIUser user = userDao.getUser(userId);
		return user;
	}

	private long getAnyUserId() {
		List<UIUserForList> users = userDao.getUsers(0, 1, 1, "asc", null,
				null, null, null);
		UIUserForList userForList = users.get(0);
		long userId = userForList.getId();
		return userId;
	}

	public void testDeleteUser() {
		long userId = getAnyUserId();
		RoutineResult result = userDao.deleteUser(userId);
		assertRoutineResult(result);

		// UIUser user = userDao.getUser(userId);
		// assertNull("User must not be found as it has been deleted", user);
	}

	public void testLockUser() {
		long userId = getAnyUserId();
		RoutineResult result = userDao.lockUser(userId);
		assertRoutineResult(result);
	}

	public void testUnlockUser() {
		long userId = getAnyUserId();
		RoutineResult result = userDao.unlockUser(userId);
		assertRoutineResult(result);
	}

	public void testCloneUser() {
		long userId = getAnyUserId();
		UICloneUserRequest request = new UICloneUserRequest();
		request.setUsername("testuserclone");
		request.setPassword("newpassword");
		request.setEmail("newemail");
		request.setTemplateUserId(userId);
		RoutineResult result = userDao.cloneUser(request);
		assertRoutineResult(result);

		UIUser newUser = userDao.getUser(request.getId());
		assertNotNull("User must be cloned", newUser);
	}

	public void testGetMappedUserRoles() {
		userDao.getMappedUserRoles(0, 10, 1, "asc", getAnyUserId(), null);
		userDao.getMappedUserRoles(0, 10, 1, "asc", getAnyUserId(), "");
		userDao.getMappedUserRoles(0, 10, 1, "asc", getAnyUserId(), "1,2,3");
	}

	public void testGetAllUserRoles() {
		userDao.getAllUserRoles(0, 10, 1, "asc", getAnyUserId(), null);
		userDao.getAllUserRoles(0, 10, 1, "asc", getAnyUserId(), "");
		userDao.getAllUserRoles(0, 10, 1, "asc", getAnyUserId(), "1,2,3");
	}

	public void testGetAllUserRolesCount() {
		userDao.getAllUserRolesCount(getAnyUserId(), null);
		userDao.getAllUserRolesCount(getAnyUserId(), "");
		userDao.getAllUserRolesCount(getAnyUserId(), "1,2,3");
	}

	public void testGetUnmappedUserRoles() {
		userDao.getUnmappedUserRoles(0, 10, 1, "asc", getAnyUserId(), null);
		userDao.getUnmappedUserRoles(0, 10, 1, "asc", getAnyUserId(), "");
		userDao.getUnmappedUserRoles(0, 10, 1, "asc", getAnyUserId(), "1,2,3");
	}

	public void testGetUnmappedUserRolesCount() {
		userDao.getUnmappedUserRolesCount(getAnyUserId(), null);
		userDao.getUnmappedUserRolesCount(getAnyUserId(), "");
		userDao.getUnmappedUserRolesCount(getAnyUserId(), "1,2,3");
	}

	public void testChangeUserRoles() {
		long userId = getAnyUserId();
		userDao.changeUserRoles(userId, "1,2,3", "4,5,6", "1,2");
		userDao.changeUserRoles(userId, null, "", "");
		userDao.changeUserRoles(userId, "", null, null);
	}

	public void testGetAllUserActions() {
		userDao.getAllUserActions(0, 10, 1, "asc", getAnyUserId(), null, null);
		// the following looks for 'admin'
		userDao.getAllUserActions(0, 10, 1, "asc", getAnyUserId(), "1,2,3",
				"dmi");
	}

	public void testGetAllUserActionsCount() {
		userDao.getAllUserActionsCount(getAnyUserId(), null, null);
		// the following looks for 'admin'
		userDao.getAllUserActionsCount(getAnyUserId(), "1,2,3", "dmi");
	}

	public void testGetUnmappedUserActions() {
		userDao.getUnmappedUserActions(0, 10, 1, "asc", getAnyUserId(), null,
				null);
		// the following looks for 'admin'
		userDao.getUnmappedUserActions(0, 10, 1, "asc", getAnyUserId(),
				"1,2,3", "dmi");
	}

	public void testGetUnmappedUserActionsCount() {
		userDao.getUnmappedUserActionsCount(getAnyUserId(), null, null);
		// the following looks for 'admin'
		userDao.getUnmappedUserActionsCount(getAnyUserId(), "1,2,3", "dmi");
	}

	public void testChangeUserRoleActions() {
		long userId = getAnyUserId();
		userDao.changeUserRoleActions(userId, "1,2,3", "4,5,6");
		userDao.changeUserRoleActions(userId, null, "");
		userDao.changeUserRoleActions(userId, "", null);
	}

	public void testGetUserRoleActions() {
		long userId = getAnyUserId();
		userDao.getUserRoleActions(userId, null, null, null);
		userDao.getUserRoleActions(userId, "1,2,3", "dmi", "dmi");
		userDao.getUserRoleActions(userId, "1,2,3",
				"this substring is expected to not exist",
				"this substring is expected to not exist");
	}
}
