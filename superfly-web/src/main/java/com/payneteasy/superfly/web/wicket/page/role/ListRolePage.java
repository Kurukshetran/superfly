package com.payneteasy.superfly.web.wicket.page.role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.payneteasy.superfly.model.ui.role.UIRoleForList;
import com.payneteasy.superfly.model.ui.subsystem.UISubsystemForFilter;
import com.payneteasy.superfly.service.RoleService;
import com.payneteasy.superfly.service.SubsystemService;
import com.payneteasy.superfly.web.wicket.component.PagingDataView;
import com.payneteasy.superfly.web.wicket.component.SubsystemChoiceRenderer;
import com.payneteasy.superfly.web.wicket.page.BasePage;
import com.payneteasy.superfly.web.wicket.page.EmptyPanel;
import com.payneteasy.superfly.web.wicket.repeater.IndexedSortableDataProvider;

public class ListRolePage extends BasePage {
	@SpringBean
	private RoleService roleService;
	@SpringBean
	private SubsystemService subsystemService;

	public ListRolePage() {
		super();
		add(new EmptyPanel("confirmPanel"));
		
		final RoleFilter roleFilter = new RoleFilter();
		Form<RoleFilter> filtersForm = new Form("filters-form");
		add(filtersForm);
		DropDownChoice<UISubsystemForFilter> subsystemDropdown = new DropDownChoice<UISubsystemForFilter>(
				"subsystem-filter", new PropertyModel<UISubsystemForFilter>(
						roleFilter, "subsystem"), subsystemService
						.getSubsystemsForFilter(),
				new SubsystemChoiceRenderer());
		subsystemDropdown.setNullValid(true);
		filtersForm.add(subsystemDropdown);
		String[] fieldNames = { "roleId", "roleName", "principalName","subsystemName" };
		SortableDataProvider<UIRoleForList> rolesDataProvider = new IndexedSortableDataProvider<UIRoleForList>(
				fieldNames) {

			public Iterator<? extends UIRoleForList> iterator(int first,
					int count) {
				UISubsystemForFilter subsystem = roleFilter.getSubsystem();
				List<Long> subsystemId = new ArrayList<Long>();
				if (subsystem == null) {
					return roleService.getRoles(first, count,
							getSortFieldIndex(), isAscending(), null,
							subsystem == null ? null : null).iterator();
				} else {
					subsystemId.add(subsystem.getId());
					return roleService.getRoles(first, count,
							getSortFieldIndex(), isAscending(), null,
							subsystem == null ? null : subsystemId).iterator();
				}
			}

			public int size() {
				UISubsystemForFilter subsystem = roleFilter.getSubsystem();
				List<Long> subsystemId = new ArrayList<Long>();
				if (subsystem == null) {
					return roleService.getRoleCount(null,
							subsystem == null ? null : null);
				} else {
					subsystemId.add(subsystem.getId());
					return roleService.getRoleCount(null,
							subsystem == null ? null : subsystemId);
				}
			}

		};
		DataView<UIRoleForList> rolesDateView = new PagingDataView<UIRoleForList>(
				"roleList", rolesDataProvider) {

			@Override
			protected void populateItem(Item<UIRoleForList> item) {
				final UIRoleForList role = item.getModelObject();
				item.add(new Label("role-name", role.getName()));
				item.add(new Label("principal-name",role.getPrincipalName()));
				item.add(new Label("subsystem-name", role.getSubsystem()));
				item.add(new Link("delete-role"){

					@Override
					public void onClick() {
						roleService.deleteRole(role.getId());
						/*this.getParent().get("confirmPanel").replaceWith(
								new ConfirmPanel("confirmPanel",
										"You are about to delete "
												+ " role permanently?") {
									public void onConfirm() {
										roleService.deleteRole(role.getId());
										this.getParent().setResponsePage(
												ListRolePage.class);
									}

									public void onCancel() {
										this.getPage().get("confirmPanel").replaceWith(
												new EmptyPanel("confirmPanel"));
									}
								});*/
					}
					
				});
				item.add(new BookmarkablePageLink("role-edit",
						EditRolePage.class).setParameter("id", role.getId()));
			}

		};
		add(rolesDateView);
		add(new OrderByLink("order-by-roleName", "roleName", rolesDataProvider));
		add(new OrderByLink("order-by-principalName", "principalName", rolesDataProvider));
		add(new OrderByLink("order-by-subsystemName", "subsystemName",
				rolesDataProvider));
		add(new PagingNavigator("paging-navigator", rolesDateView));
	}
	
	@Override
	protected String getTitle() {
		return "Roles";
	}

	@SuppressWarnings("unused")
	private class RoleFilter implements Serializable {
		private UISubsystemForFilter subsystem;

		public UISubsystemForFilter getSubsystem() {
			return subsystem;
		}

		public void setSubsystem(UISubsystemForFilter subsystem) {
			this.subsystem = subsystem;
		}

	}
}
