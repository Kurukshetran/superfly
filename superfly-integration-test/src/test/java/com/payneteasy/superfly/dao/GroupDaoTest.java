package com.payneteasy.superfly.dao;

import java.util.ArrayList;
import java.util.List;

import com.payneteasy.superfly.model.ActionToSave;
import com.payneteasy.superfly.model.RoutineResult;
import com.payneteasy.superfly.model.ui.action.UIActionForList;
import com.payneteasy.superfly.model.ui.group.UIGroup;
import com.payneteasy.superfly.model.ui.group.UIGroupForList;
import com.payneteasy.superfly.model.ui.user.UICloneUserRequest;
import com.payneteasy.superfly.model.ui.user.UIUser;
import com.payneteasy.superfly.utils.StringUtils;

public class GroupDaoTest extends AbstractDaoTest {
	private GroupDao groupDao;

	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	
	public void testGetGroupsForSubsystems(){
		List<UIGroupForList> groupList = groupDao.getGroups(0, 10, 1, "asc", null, "1,2");
		assertTrue("Group list should not be empty", groupList.size() > 0);
	}
	
	public void testGetGroupCount(){		
		int count = groupDao.getGroupsCount(null, null);
		assertTrue("Must get some group", count > 0);
	}
	
	public void testCreateGetDeleteUpdateGroup(){
		UIGroup group = new UIGroup();
		group.setName("test");
		group.setSubsystemId(1L);
		RoutineResult routineResult =  groupDao.createGroup(group);
		assertRoutineResult(routineResult);
        groupDao.updateGroup(group.getId(), "test_updated");
        assertEquals(groupDao.getGroupById(group.getId()).getName(),"test_updated");
		routineResult = groupDao.deleteGorup(group.getId());
		assertRoutineResult(routineResult);
	}
	
	private long getAnyGroupId(){
		List<UIGroupForList> list = groupDao.getGroups(0, 1, 1, "asc", null, null);
		return list.get(0).getId();
	}
	
	public void testChangeGroupActions() {
		long groupId = getAnyGroupId();
		assertRoutineResult(groupDao.changeGroupActions(groupId, "1,2,3", "4,5,6"));
	}
	
	public void testGetAllGroupActions() {
		groupDao.getAllGroupMappedActions(0, 10, 1, "asc", getAnyGroupId(), null);
		groupDao.getAllGroupMappedActions(0, 10, 1, "asc", getAnyGroupId(), "dmi");
	}
	
	public void testGetAllGroupActionsCount() {
		groupDao.getAllGroupMappedActionsCount(getAnyGroupId(), null);
		groupDao.getAllGroupMappedActionsCount(getAnyGroupId(), "dmi");
	}
	
	public void testCloneGroup() {
//		long groupId = getAnyGroupId();
//		UICloneUserRequest request = new UICloneUserRequest();
//		request.setUsername("newuser");
//		request.setPassword("newpassword");
//		request.setTemplateUserId(userId);
//		RoutineResult result = userDao.cloneUser(request);
//		assertRoutineResult(result);
//		
//		UIUser newUser = userDao.getUser(request.getId());
//		assertNotNull("User must be cloned", newUser);
	}
	
}
