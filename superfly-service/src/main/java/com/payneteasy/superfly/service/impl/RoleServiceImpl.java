package com.payneteasy.superfly.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.payneteasy.superfly.dao.DaoConstants;
import com.payneteasy.superfly.dao.RoleDao;
import com.payneteasy.superfly.model.ui.group.UIGroupForCheckbox;
import com.payneteasy.superfly.model.ui.role.UIRole;
import com.payneteasy.superfly.model.ui.role.UIRoleForFilter;
import com.payneteasy.superfly.model.ui.role.UIRoleForList;
import com.payneteasy.superfly.service.RoleService;
import com.payneteasy.superfly.utils.StringUtils;

@Transactional
public class RoleServiceImpl implements RoleService {

	private RoleDao roleDao;

	@Required
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public List<UIRoleForFilter> getRolesForFilter() {
		return roleDao.getRolesForFilter(null, null, 0, Integer.MAX_VALUE);
	}

	public int getRoleCount(String rolesName, List<Long> subsystems) {
		return roleDao.getRoleCount(rolesName, StringUtils
				.collectionToCommaDelimitedString(subsystems));
	}

	public List<UIRoleForList> getRoles(int startFrom, int recordsCount,
			int orderFieldNumber, boolean asc, String rolesName,
			List<Long> subsystems) {

		return roleDao.getRoles(startFrom, recordsCount, orderFieldNumber,
				asc ? DaoConstants.ASC : DaoConstants.DESC, rolesName,
				StringUtils.collectionToCommaDelimitedString(subsystems));
	}

	public void deleteRole(long roleId) {
		roleDao.deleteRole(roleId);
	}

	public UIRole getRole(long roleId) {

		return roleDao.getRole(roleId);
	}

	public void updateRole(UIRole role) {
		roleDao.updateRole(role);

	}

	public void createRole(UIRole role) {
		roleDao.createRole(role);
	}

	public List<UIGroupForCheckbox> getAllRoleGroups(long roleId) {
		List<UIGroupForCheckbox> getAllGroups = roleDao.getAllRoleGroups(0,
				Integer.MAX_VALUE, 4, DaoConstants.ASC, roleId);
		return getAllGroups;
	}

	public void changeRoleGroups(long roleId, List<Long> groupToAddIds,
			List<Long> groupToRemoveIds) {
		roleDao.changeRoleGroups(roleId, StringUtils
				.collectionToCommaDelimitedString(groupToAddIds), StringUtils
				.collectionToCommaDelimitedString(groupToRemoveIds));

	}

}
