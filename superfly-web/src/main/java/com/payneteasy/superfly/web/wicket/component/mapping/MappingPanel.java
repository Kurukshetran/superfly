package com.payneteasy.superfly.web.wicket.component.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.Strings;

import com.payneteasy.superfly.model.ui.action.UIActionForFilter;
import com.payneteasy.superfly.service.mapping.MappingService;

public abstract class MappingPanel extends Panel {

	public MappingPanel(String id, final long entityId) {
		super(id);
		final MappingModel<MappingService> mappingModel = new MappingModel<MappingService>();
		Form<MappingModel<MappingService>> form = new Form<MappingModel<MappingService>>("form-mapping",
				new Model<MappingModel<MappingService>>(mappingModel));
		add(form);
		WebMarkupContainer searchMappedContainer = new WebMarkupContainer("search-mapped-container");
		WebMarkupContainer searchUnMappedContainer = new WebMarkupContainer("search-unmapped-container");
		form.add(searchMappedContainer.setVisible(isVisibleSearchePanel()));
		form.add(searchUnMappedContainer.setVisible(isVisibleSearchePanel()));
		
		final AutoCompleteTextField<String> acMapped = new AutoCompleteTextField<String>("mapped-str",
				new PropertyModel<String>(mappingModel, "searchMappedString")){
			@Override
			protected Iterator<String> getChoices(String input) {
				if (Strings.isEmpty(input))
                {
                    return Collections.<String>emptyList().iterator();
                }
				List<String> choices = new ArrayList<String>(10);
				List<? extends MappingService> list = getMappedItems(null);
				for(MappingService ms: list){
					final String name = ms.getItemName();
					if(name.toUpperCase().contains(input.toUpperCase())){
						choices.add(name);
						if(choices.size()==10){
							break;
						}
					}
				}
				return choices.iterator();
			}
			
		};
		searchMappedContainer.add(acMapped);
		final AutoCompleteTextField<String> acUnMapped = new AutoCompleteTextField<String>("unmapped-str",
				new PropertyModel<String>(mappingModel, "searchUnMappedString")){
			@Override
			protected Iterator<String> getChoices(String input) {
				if (Strings.isEmpty(input))
                {
                    return Collections.<String>emptyList().iterator();
                }
				List<String> choices = new ArrayList<String>(10);
				List<? extends MappingService> list = getUnMappedItems(null);
				for(MappingService ms: list){
					final String name = ms.getItemName();
					if(name.toUpperCase().contains(input.toUpperCase())){
						choices.add(name);
						if(choices.size()==10){
							break;
						}
					}
				}
				return choices.iterator();
			}
			
		};
		searchUnMappedContainer.add(acUnMapped);

		final CheckGroup<MappingService> checkGroupMapped = new CheckGroup<MappingService>("checkgroup-mapped",
				mappingModel.getSelectedInMapped());
		form.add(checkGroupMapped);
		checkGroupMapped.add(new CheckGroupSelector("master-checkbox-map", checkGroupMapped));
		checkGroupMapped.add(new Label("mapped-item-name", getHeaderItemName()));
		ListView<MappingService> mappedListView = new ListView<MappingService>("mapped-list", new AbstractReadOnlyModel<List<? extends MappingService>>() {

			@Override
			public List<? extends MappingService> getObject() {
				return getMappedItems(mappingModel.getSearchMappedString());
			}
			
		}) {

			@Override
			protected void populateItem(ListItem<MappingService> item) {
				MappingService mapped = item.getModelObject();
				item.add(new Label("mapped-name", createObjectNameModel(mapped.getItemName())));
				item.add(new Check<MappingService>("selected", new Model<MappingService>(mapped)));
			}

		};
		mappedListView.setReuseItems(true);
		checkGroupMapped.add(mappedListView);
		form.add(new SubmitLink("remove-items") {

			@Override
			public void onSubmit() {
				mappingProcess(entityId, null, objectsToIds(checkGroupMapped.getModelObject()));
			}

		});

		final CheckGroup<MappingService> checkGroupUnMapped = new CheckGroup<MappingService>("checkgroup-unmapped",
				mappingModel.getSelectedInUnMapped());
		form.add(checkGroupUnMapped);
		checkGroupUnMapped.add(new CheckGroupSelector("master-checkbox-unmap", checkGroupUnMapped));
		checkGroupUnMapped.add(new Label("unmapped-item-name", getHeaderItemName()));
		ListView<MappingService> unmappedListView = new ListView<MappingService>("unmapped-list", new AbstractReadOnlyModel<List<? extends MappingService>>() {

			@Override
			public List<? extends MappingService> getObject() {
				return getUnMappedItems(mappingModel.getSearchUnMappedString());
			}
			
		}) {

			@Override
			protected void populateItem(ListItem<MappingService> item) {
				MappingService unmapped = item.getModelObject();
				item.add(new Label("mapped-name", createObjectNameModel(unmapped.getItemName())));
				item.add(new Check<MappingService>("selected", new Model<MappingService>(unmapped)));
			}

		};
		unmappedListView.setReuseItems(true);
		checkGroupUnMapped.add(unmappedListView);

		form.add(new SubmitLink("add-items") {
			@Override
			public void onSubmit() {
				mappingProcess(entityId, objectsToIds(checkGroupUnMapped.getModelObject()), null);
			}

		});

	}

	protected abstract List<? extends MappingService> getMappedItems(String searchLabel);

	protected abstract List<? extends MappingService> getUnMappedItems(String searchLabel);

	protected abstract void mappingProcess(long entityId, List<Long> mappedId, List<Long> unmappedId);
	
	protected abstract boolean isVisibleSearchePanel();
	
	protected abstract String getHeaderItemName();

	private IModel<String> createObjectNameModel(String itemName) {
		return new Model<String>(itemName);
	}
    
	private List<Long> objectsToIds(Collection<? extends MappingService> objects) {
		List<Long> ids = new ArrayList<Long>(objects.size());
		for (MappingService object : objects) {
			ids.add(object.getItemId());
		}
		return ids;
	}
}
