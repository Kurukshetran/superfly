package com.payneteasy.superfly.web.wicket.page.group.wizard;

import com.payneteasy.superfly.model.RoutineResult;
import com.payneteasy.superfly.model.ui.action.UIActionForCheckboxForGroup;
import com.payneteasy.superfly.model.ui.group.UIGroup;
import com.payneteasy.superfly.model.ui.subsystem.UISubsystemForFilter;
import com.payneteasy.superfly.service.ActionService;
import com.payneteasy.superfly.service.GroupService;
import com.payneteasy.superfly.service.SubsystemService;
import com.payneteasy.superfly.web.wicket.component.PagingDataView;
import com.payneteasy.superfly.web.wicket.component.paging.SuperflyPagingNavigator;
import com.payneteasy.superfly.web.wicket.model.InitializingModel;
import com.payneteasy.superfly.web.wicket.page.BasePage;
import com.payneteasy.superfly.web.wicket.page.group.ListGroupsPage;
import com.payneteasy.superfly.web.wicket.repeater.IndexedSortableDataProvider;
import com.payneteasy.superfly.web.wicket.utils.ObjectHolder;
import org.apache.wicket.Page;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.access.annotation.Secured;

import java.io.Serializable;
import java.util.*;

@Secured("ROLE_ADMIN")
public class GroupActionsPage extends BasePage {
	@SpringBean
	ActionService actionService;
	
	@SpringBean
	SubsystemService ssysService;
	
	@SpringBean
	GroupService groupService;
	
	@Override
	protected String getTitle() {
		return "Create group";
	}

	public GroupActionsPage(PageParameters param) {
		super(ListGroupsPage.class, param);
		
		final Long groupId = param.get("gid").toLong();
		
		// current Group
		final UIGroup curGroup = groupService.getGroupById(groupId);
		
		// Filter
		final Filter filter = new Filter();
		final Form<Filter> filtersForm = new Form<Filter>("filters-form", new Model<Filter>(filter));
		add(filtersForm);
		filtersForm.add(new TextField<String>("action-name-substr", new PropertyModel<String>(filter, "actionNameSubstring")));
		
		
		// groupModel
		final GroupWizardModel groupModel = new GroupWizardModel();		
		groupModel.setGroupName(curGroup.getName());
		List<UISubsystemForFilter> list = ssysService.getSubsystemsForFilter();
		for(UISubsystemForFilter e: list){
			if(e.getId() == curGroup.getSubsystemId())groupModel.setGroupSubsystem(e);
		}
		
		
		
		final ObjectHolder<List<UIActionForCheckboxForGroup>> actionsHolder = new ObjectHolder<List<UIActionForCheckboxForGroup>>();

		// CHECKGROUP
		final InitializingModel<Collection<UIActionForCheckboxForGroup>> actionsCheckGroupModel = new InitializingModel<Collection<UIActionForCheckboxForGroup>>() {
			@Override
			protected Collection<UIActionForCheckboxForGroup> getInitialValue() {
				final Collection<UIActionForCheckboxForGroup> checkedActions = new HashSet<UIActionForCheckboxForGroup>();
				for (UIActionForCheckboxForGroup action : actionsHolder.getObject()) {
					if (action.isMapped()) {
						checkedActions.add(action);
					}
				}
				return checkedActions;
			}
		};
		
		// SORTABLE DATA PROVIDER
		String[] fieldName = { "groupId","groupName","subsystemName","actionId", "actionName" };
		final SortableDataProvider<UIActionForCheckboxForGroup> actionDataProvider = new IndexedSortableDataProvider<UIActionForCheckboxForGroup>(
				fieldName) {
			
			public Iterator<? extends UIActionForCheckboxForGroup> iterator(int first,
					int count) {
				List<UIActionForCheckboxForGroup> list = groupService.getAllGroupActions(first, count, getSortFieldIndex(), isAscending(), groupId, filter.getActionNameSubstring());
				actionsHolder.setObject(list);
				actionsCheckGroupModel.clearInitialized();
				return list.iterator(); 
			}

			public int size() {
				return groupService.getAllGroupActionsCount(groupId, filter.getActionNameSubstring());
			}

		};
		

				
		final CheckGroup<UIActionForCheckboxForGroup> checkGroup = new CheckGroup<UIActionForCheckboxForGroup>("group",actionsCheckGroupModel);

		// DATAVIEW
		final DataView<UIActionForCheckboxForGroup> actionDataView = new PagingDataView<UIActionForCheckboxForGroup>("dataView",actionDataProvider, 15){
			@Override
			protected void populateItem(Item<UIActionForCheckboxForGroup> item) {
				final UIActionForCheckboxForGroup action = item.getModelObject();
				item.add(new Label("name",action.getActionName()));
				item.add(new Label("ssys-name",action.getSubsystemName()));
				item.add(new Check<UIActionForCheckboxForGroup>("selected", item.getModel(), checkGroup));
			}
			
		};
		
		checkGroup.add(actionDataView);
		checkGroup.add(new OrderByLink("order-by-name", "actionName", actionDataProvider));
		//checkGroup.add(new PagingNavigator("paging-navigator", actionDataView));
		checkGroup.add(new SuperflyPagingNavigator("paging-navigator", actionDataView));
		checkGroup.add(new CheckGroupSelector("groupselector",checkGroup));
		
		// FORM
		Form form = new Form("form"){
			@Override
			protected void onSubmit() {				
				List<Long> actionsToLink = new ArrayList<Long>();
				List<Long> actionsToUnlink = new ArrayList<Long>();
				List<UIActionForCheckboxForGroup> allActions = actionsHolder.getObject();
				Collection<UIActionForCheckboxForGroup> selectedActions = actionsCheckGroupModel.getObject();
				for(UIActionForCheckboxForGroup e : allActions){
					if(selectedActions.contains(e)){
						actionsToLink.add(e.getActionId());
					}else{
						actionsToUnlink.add(e.getActionId());
					}
				}
				RoutineResult result = groupService.changeGroupActions(curGroup.getId(), actionsToLink, actionsToUnlink);
				if (result.isOk()) {
					info("Actions successfully changed. Please be aware that some sessions could be invalidated.");
				} else {
					error("Error while changing group actions: " + result.getErrorMessage());
				}
			}
		};
		form.add(new BookmarkablePageLink<Page>("btn-cancel", ListGroupsPage.class));
		
		form.add(checkGroup);
		add(form);
		//---
	}
	

	@SuppressWarnings("unused")
	private static class Filter implements Serializable {
		private static final long serialVersionUID = 1L;
		private String actionNameSubstring;

		public String getActionNameSubstring() {
			return actionNameSubstring;
		}

		public void setActionNameSubstring(String actionNameSubstring) {
			this.actionNameSubstring = actionNameSubstring;
		}
	}
}
