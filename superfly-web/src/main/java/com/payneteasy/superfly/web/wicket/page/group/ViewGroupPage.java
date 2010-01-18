package com.payneteasy.superfly.web.wicket.page.group;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.annotation.Secured;

import com.payneteasy.superfly.model.ui.action.UIActionForCheckboxForGroup;
import com.payneteasy.superfly.model.ui.group.UIGroup;
import com.payneteasy.superfly.model.ui.subsystem.UISubsystemForFilter;
import com.payneteasy.superfly.service.ActionService;
import com.payneteasy.superfly.service.GroupService;
import com.payneteasy.superfly.service.SubsystemService;
import com.payneteasy.superfly.web.wicket.component.PagingDataView;
import com.payneteasy.superfly.web.wicket.page.BasePage;
import com.payneteasy.superfly.web.wicket.repeater.IndexedSortableDataProvider;

@Secured("ROLE_ADMIN")
public class ViewGroupPage extends BasePage {
	@SpringBean
	ActionService actionService;
	
	@SpringBean
	SubsystemService ssysService;
	
	@SpringBean
	GroupService groupService;
	
	@Override
	protected String getTitle() {
		return "Group details";
	}
	
	public ViewGroupPage(PageParameters param){
		this(param.getAsLong("gid"));		
	}

	public ViewGroupPage(final Long groupId) {
		
		//BACK
		Form formBack = new Form("back-form");
		formBack.add(new Button("btn-back"){
			@Override
			public void onSubmit() {
				setResponsePage(ListGroupsPage.class);
			}
		}.setDefaultFormProcessing(false));
		add(formBack);
		
		
		//FILTER
		final Filter filter = new Filter();
		final Form<Filter> filtersForm = new Form<Filter>("filter-form", new Model<Filter>(filter));
		add(filtersForm);
		filtersForm.add(new TextField<String>("action-name-substr", new PropertyModel<String>(filter, "actionNameSubstring")));
		
		//GROUP PROPERTIES
		final UIGroup curGroup = groupService.getGroupById(groupId);
		
		add(new Label("groupName",curGroup.getName()));
		String ssysName = "";
		List<UISubsystemForFilter> list = ssysService.getSubsystemsForFilter();
		for(UISubsystemForFilter e: list){
			if(e.getId() == curGroup.getSubsystemId())ssysName=e.getName();
		}
		add(new Label("groupSubsystem",ssysName));
		
		
		// SORTABLE DATA PROVIDER
		String[] fieldName = { "actionId", "actionName" };
		final SortableDataProvider<UIActionForCheckboxForGroup> actionDataProvider = new IndexedSortableDataProvider<UIActionForCheckboxForGroup>(
				fieldName) {
			
			public Iterator<? extends UIActionForCheckboxForGroup> iterator(int first,
					int count) {
				
				List<UIActionForCheckboxForGroup> list = groupService.getAllGroupActions(first, count, 
						getSortFieldIndex(), isAscending(), groupId, filter.getActionNameSubstring());
				return list.iterator(); 
			}

			public int size() {
				return groupService.getAllGroupActionsCount(groupId, filter.getActionNameSubstring());
			}

		};
		

		// DATAVIEW
		final DataView<UIActionForCheckboxForGroup> actionDataView = new PagingDataView<UIActionForCheckboxForGroup>("dataView",actionDataProvider){
			@Override
			protected void populateItem(Item<UIActionForCheckboxForGroup> item) {
				final UIActionForCheckboxForGroup action = item.getModelObject();
				item.add(new Label("action-id",String.valueOf(action.getActionId())));
				item.add(new Label("action-name",action.getActionName()));
			}
			
		};
		
		add(actionDataView);
		add(new OrderByLink("order-by-ActionID", "actionId", actionDataProvider));
		add(new OrderByLink("order-by-ActionName", "actionName", actionDataProvider));
		add(new PagingNavigator("paging-navigator", actionDataView));
		
	}
	

	@SuppressWarnings("unused")
	private static class Filter implements Serializable {
		private String actionNameSubstring;

		public String getActionNameSubstring() {
			return actionNameSubstring;
		}

		public void setActionNameSubstring(String actionNameSubstring) {
			this.actionNameSubstring = actionNameSubstring;
		}
	}
}
