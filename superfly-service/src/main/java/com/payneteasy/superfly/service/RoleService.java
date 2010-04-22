package com.payneteasy.superfly.service;

import java.util.List;

import com.payneteasy.superfly.model.RoutineResult;
import com.payneteasy.superfly.model.ui.action.UIActionForCheckboxForRole;
import com.payneteasy.superfly.model.ui.group.UIGroupForCheckbox;
import com.payneteasy.superfly.model.ui.role.UIRole;
import com.payneteasy.superfly.model.ui.role.UIRoleForFilter;
import com.payneteasy.superfly.model.ui.role.UIRoleForList;
import com.payneteasy.superfly.model.ui.role.UIRoleForView;

public interface RoleService {
	/**
	 * Returns list of roles for UI filter.
	 * 
	 * @return roles
	 */
	List<UIRoleForFilter> getRolesForCreateUser(List<Long> subId);

	List<UIRoleForFilter> getRolesForFilter();

	List<UIRoleForList> getRoles(int startFrom, int recordsCount,
			int orderFieldNumber, boolean asc, String rolesName,
			List<Long> subsystems);

	int getRoleCount(String rolesName, List<Long> subsystems);

	RoutineResult deleteRole(long roleId);

	UIRoleForView getRole(long roleId);

	RoutineResult updateRole(UIRole role);

	RoutineResult createRole(UIRole role);

	List<UIGroupForCheckbox> getAllRoleGroups(int startFrom, int recordsCount,
			int orderFieldNumber, String orderType, long roleId);

	RoutineResult changeRoleGroups(long roleId, List<Long> groupToAddIds,
			List<Long> groupToRemoveIds);

	List<UIActionForCheckboxForRole> getAllRoleActions(int startFrom,
			int recordsCount, int orderFieldNumber, boolean ascending,
			long roleId, String actionName);

	int getAllRoleActionsCount(long roleId, String actionName);

	List<UIActionForCheckboxForRole> getMappedRoleActions(int startFrom,
			int recordsCount, int orderFieldNumber, boolean ascending,
			long roleId, String actionName);

	List<UIActionForCheckboxForRole> getUnMappedRoleActions(int startFrom,
			int recordsCount, int orderFieldNumber, boolean ascending,
			long roleId, String actionName);

	int getMappedRoleActionsCount(long roleId, String actionName);

	int getAllRoleGroupsCount(long roleId);

	RoutineResult changeRoleActions(long roleId, List<Long> actionToAddIds,
			List<Long> actionToRemoveIds);

	List<UIGroupForCheckbox> getMappedRoleGroups(int startFrom,
			int recordsCount, int orderFieldNumber, boolean ascending,
			long roleId);

	List<UIGroupForCheckbox> getUnMappedRoleGroups(int startFrom,
			int recordsCount, int orderFieldNumber, boolean ascending,
			long roleId);
}
