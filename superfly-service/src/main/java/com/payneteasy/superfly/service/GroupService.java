package com.payneteasy.superfly.service;

import java.util.List;

import org.springframework.ui.context.support.UiApplicationContextUtils;

import com.payneteasy.superfly.model.RoutineResult;
import com.payneteasy.superfly.model.ui.action.UIActionForCheckboxForGroup;
import com.payneteasy.superfly.model.ui.group.UICloneGroupRequest;
import com.payneteasy.superfly.model.ui.group.UIGroup;
import com.payneteasy.superfly.model.ui.group.UIGroupForList;
import com.payneteasy.superfly.model.ui.group.UIGroupForView;

public interface GroupService {

	List<UIGroupForList> getGroups();

	List<UIGroupForList> getGroupsForSubsystems(int startFrom,
			int recordsCount, int orderFieldNumber, boolean orderType,
			String groupNamePrefix, List<Long> subsystemIds);

	RoutineResult createGroup(UIGroup group);

	RoutineResult updateGroup(UIGroup group);

	RoutineResult deleteGroup(long id);

	RoutineResult cloneGroup(UICloneGroupRequest request);

	UIGroupForView getGroupById(long id);

	int getGroupsCount(String groupName, List<Long> subsystemIds);

	RoutineResult changeGroupActions(long groupId, List<Long> ActionsToLink,
			List<Long> ActionsToUnlink);

	List<UIActionForCheckboxForGroup> getAllGroupMappedActions(int stratFrom,
			int recordsCount, int orderFieldNumber, boolean orderType,
			long groupId, String actionSubstring);

	int getAllGroupMappedActionsCount(long groupId, String actionSubstring);

	List<UIActionForCheckboxForGroup> getAllGroupUnMappedActions(int stratFrom,
			int recordsCount, int orderFieldNumber, boolean orderType,
			long groupId, String actionSubstring);

	int getAllGroupUnMappedActionsCount(long groupId, String actionSubstring);

	List<UIActionForCheckboxForGroup> getAllGroupActions(int startFrom,
			int recordsCount, int orderFieldNumber, boolean orderType,
			long groupId, String actionSubstring);

	int getAllGroupActionsCount(long groupId, String actionSubstring);

}
