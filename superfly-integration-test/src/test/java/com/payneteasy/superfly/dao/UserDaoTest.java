package com.payneteasy.superfly.dao;

import com.payneteasy.superfly.model.AuthSession;
import com.payneteasy.superfly.model.RoutineResult;
import com.payneteasy.superfly.model.UserRegisterRequest;
import com.payneteasy.superfly.model.ui.role.UIRole;
import com.payneteasy.superfly.model.ui.subsystem.UISubsystem;
import com.payneteasy.superfly.model.ui.user.UICloneUserRequest;
import com.payneteasy.superfly.model.ui.user.UIUser;
import com.payneteasy.superfly.model.ui.user.UIUserForCreate;
import com.payneteasy.superfly.model.ui.user.UIUserForList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserDaoTest extends AbstractDaoTest {

	private UserDao userDao;
    private SubsystemDao subsystemDao;
    private RoleDao roleDao;

    private static boolean created = false;

    private static UISubsystem subsystem;
    private static UIRole role;

    @Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

    @Autowired
    public void setSubsystemDao(SubsystemDao subsystemDao) {
        this.subsystemDao = subsystemDao;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Before
    public void setUp() throws Exception {
        if (!created) {
       		subsystem = new UISubsystem();
       		subsystem.setName("subsystem-for-user");
       		subsystem.setCallbackUrl("no-callback");
            subsystem.setTitle("The Subsystem");
            subsystem.setSubsystemUrl("subsystem-for-user-url");
            subsystem.setLandingUrl("subsystem-for-user-url");
       		subsystemDao.createSubsystem(subsystem);

   	    	role = new UIRole();
   	    	role.setRoleName("role1");
   	    	role.setPrincipalName("role1");
   	    	role.setSubsystemId(subsystem.getId());
   	    	roleDao.createRole(role);

            UIUserForCreate user = new UIUserForCreate();
            user.setUsername("user-1");
            user.setPassword("abc");
            user.setEmail("email-1");
            user.setName("user-1");
            user.setSurname("user-1");
            user.setSecretQuestion("");
            user.setSecretAnswer("");
            user.setHotpSalt("DEADBEEF");
            userDao.createUser(user);

            userDao.addSubsystemWithRole(2L, role.getRoleId());

   	    	created = true;
       	}
    }

    @Test
	public void testAddSubsystemWithRole() {
        RoutineResult result = userDao.addSubsystemWithRole(1L, role.getRoleId());
        assertRoutineResult(result);
	}

    @Test
	public void testAuthenticate() {
		AuthSession session = userDao.authenticate("user-1", "abc",
				subsystem.getName(), null, null);
        assertNotNull("Must authenticate successfully", session);
	}

    @Test
	public void testGetUsersAndActions() {
        // TODO
//		List<UserWithActions> users = userDao.getUsersAndActions("superfly");
//		assertNotNull("Must authenticate successfully", users);
//		assertTrue("Must authenticate successfully", users.size() > 0);
	}

    @Test
	public void testGetUsers() {
		List<UIUserForList> users;
		users = userDao.getUsers(0, 10, DaoConstants.DEFAULT_SORT_FIELD_NUMBER,
				"asc", null, null, null, null);
        assertNotNull("List cannot be null", users);
		users = userDao.getUsers(0, 10, DaoConstants.DEFAULT_SORT_FIELD_NUMBER,
				"asc", "someprefix", 1L, 2L, 1L);
        assertNotNull("List cannot be null", users);
	}

    @Test
	public void testGetUsersCount() {
		long count = userDao.getUsersCount(null, null, null, null);
        assertTrue("Must get some users", count > 0);
		userDao.getUsersCount("someprefix", 1L, 2L, 1L);
	}

    @Test
	public void testGetUser() {
		UIUser user = userDao.getUser(-111L);
        Assert.assertNull("Must not find a user with ID=111", user);
	}

    @Test
	public void testRegisterUser() {
		UserRegisterRequest registerUser = new UserRegisterRequest();
		registerUser.setUsername("testRegName");
		registerUser.setPassword("passw");
		registerUser.setEmail("reg@gmail.com");
		registerUser.setSubsystemName("superfly");
		registerUser.setPrincipalNames("principal1,principal2");
        registerUser.setName("John");
        registerUser.setSurname("Smith");
        registerUser.setSecretQuestion("What is 2+2?");
        registerUser.setSecretAnswer("4");
        registerUser.setHotpSalt("host-salt");
		RoutineResult result = userDao.registerUser(registerUser);
		assertRoutineResult(result);
	}

    @Test
	public void testCreateUser() {
		UIUserForCreate user = new UIUserForCreate();
		user.setUsername("testusercreate1");
		user.setPassword("secret1");
		user.setEmail("email1");
		user.setRoleId(role.getRoleId());
        user.setName("John");
        user.setSurname("Smith");
        user.setSecretQuestion("What is 2+2?");
        user.setSecretAnswer("4");
        user.setHotpSalt("host-salt");
		RoutineResult result = userDao.createUser(user);
		assertRoutineResult(result);
		assertNotNull("User ID must be generated", user.getId());

		/*
		 * user = userDao.getUser(user.getId());
		 * assertNotNull("Must find a user we have just created", user);
		 */
	}

    @Test
	public void testUpdateUser() {
		UIUser user = getAnyUser();
		user.setPassword("password");
		user.setEmail("email");
		RoutineResult result = userDao.updateUser(user);
		assertRoutineResult(result);
	}

	private UIUser getAnyUser() {
		long userId = getAnyUserId();
        return userDao.getUser(userId);
	}

	private long getAnyUserId() {
		List<UIUserForList> users = userDao.getUsers(0, 1, 1, "asc", null,
				null, null, null);
		UIUserForList userForList = users.get(0);
        return userForList.getId();
	}

    @Test
	public void testDeleteUser() {
		long userId = getAnyUserId();
		RoutineResult result = userDao.deleteUser(userId);
		assertRoutineResult(result);

		// UIUser user = userDao.getUser(userId);
		// assertNull("User must not be found as it has been deleted", user);
	}

    @Test
	public void testLockUser() {
		long userId = getAnyUserId();
		RoutineResult result = userDao.lockUser(userId);
		assertRoutineResult(result);
	}

    @Test
	public void testUnlockUser() {
		long userId = getAnyUserId();
		RoutineResult result = userDao.unlockUser(userId);
		assertRoutineResult(result);
	}

    @Test
	public void testCloneUser() {
		long userId = getAnyUserId();
		UICloneUserRequest request = new UICloneUserRequest();
		request.setUsername("testuserclone");
		request.setPassword("newpassword");
		request.setEmail("newemail");
		request.setTemplateUserId(userId);
        request.setHotpSalt("new-hotp-salt");
        request.setIsPasswordTemp(false);
		RoutineResult result = userDao.cloneUser(request);
		assertRoutineResult(result);

		UIUser newUser = userDao.getUser(request.getId());
		assertNotNull("User must be cloned", newUser);
	}

    @Test
	public void testGetMappedUserRoles() {
		userDao.getMappedUserRoles(0, 10, 1, "asc", getAnyUserId(), null);
		userDao.getMappedUserRoles(0, 10, 1, "asc", getAnyUserId(), "");
		userDao.getMappedUserRoles(0, 10, 1, "asc", getAnyUserId(), "1,2,3");
	}

    @Test
	public void testGetAllUserRoles() {
		userDao.getAllUserRoles(0, 10, 1, "asc", getAnyUserId(), null);
		userDao.getAllUserRoles(0, 10, 1, "asc", getAnyUserId(), "");
		userDao.getAllUserRoles(0, 10, 1, "asc", getAnyUserId(), "1,2,3");
	}

    @Test
	public void testGetAllUserRolesCount() {
		userDao.getAllUserRolesCount(getAnyUserId(), null);
		userDao.getAllUserRolesCount(getAnyUserId(), "");
		userDao.getAllUserRolesCount(getAnyUserId(), "1,2,3");
	}

    @Test
	public void testGetUnmappedUserRoles() {
		userDao.getUnmappedUserRoles(0, 10, 1, "asc", getAnyUserId(), null);
		userDao.getUnmappedUserRoles(0, 10, 1, "asc", getAnyUserId(), "");
		userDao.getUnmappedUserRoles(0, 10, 1, "asc", getAnyUserId(), "1,2,3");
	}

    @Test
	public void testGetUnmappedUserRolesCount() {
		userDao.getUnmappedUserRolesCount(getAnyUserId(), null);
		userDao.getUnmappedUserRolesCount(getAnyUserId(), "");
		userDao.getUnmappedUserRolesCount(getAnyUserId(), "1,2,3");
	}

    @Test
	public void testChangeUserRoles() {
		long userId = getAnyUserId();
		userDao.changeUserRoles(userId, "1,2,3", "4,5,6", "1,2");
		userDao.changeUserRoles(userId, null, "", "");
		userDao.changeUserRoles(userId, "", null, null);
	}

    @Test
	public void testGetAllUserActions() {
		userDao.getAllUserActions(0, 10, 1, "asc", getAnyUserId(), null, null);
		// the following looks for 'admin'
		userDao.getAllUserActions(0, 10, 1, "asc", getAnyUserId(), "1,2,3",
				"dmi");
	}

    @Test
	public void testGetAllUserActionsCount() {
		userDao.getAllUserActionsCount(getAnyUserId(), null, null);
		// the following looks for 'admin'
		userDao.getAllUserActionsCount(getAnyUserId(), "1,2,3", "dmi");
	}

    @Test
	public void testGetUnmappedUserActions() {
		userDao.getUnmappedUserActions(0, 10, 1, "asc", getAnyUserId(), null, 1,
				null);
		// the following looks for 'admin'
		userDao.getUnmappedUserActions(0, 10, 1, "asc", getAnyUserId(),
				"1,2,3", 1, "dmi");
	}

    @Test
	public void testGetUnmappedUserActionsCount() {
		userDao.getUnmappedUserActionsCount(getAnyUserId(), null, 1, null);
		// the following looks for 'admin'
		userDao.getUnmappedUserActionsCount(getAnyUserId(), "1,2,3", 1, "dmi");
	}

    @Test
	public void testChangeUserRoleActions() {
		long userId = getAnyUserId();
		userDao.changeUserRoleActions(userId, "1,2,3", "4,5,6");
		userDao.changeUserRoleActions(userId, null, "");
		userDao.changeUserRoleActions(userId, "", null);
	}

    @Test
	public void testGetUserRoleActions() {
		long userId = getAnyUserId();
		userDao.getUserRoleActions(userId, null, null, null);
		userDao.getUserRoleActions(userId, "1,2,3", "dmi", "dmi");
		userDao.getUserRoleActions(userId, "1,2,3",
				"this substring is expected to not exist",
				"this substring is expected to not exist");
	}

    @Test
    public void testGetUserLoginStatus() {
   		String status;
        status = userDao.getUserLoginStatus("user-1", "abc",
                subsystem.getName());
        assertEquals("Y", status);
        status = userDao.getUserLoginStatus("user-1", "abcd",
                subsystem.getName());
        assertEquals("N", status);
   	}
}
