package com.payneteasy.superfly.web.page.subsystem;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.payneteasy.superfly.model.ui.UISubsystem;
import com.payneteasy.superfly.service.SubsystemService;
import com.payneteasy.superfly.web.page.BasePage;

public class AddSubsystemPage extends BasePage {
	@SpringBean
	private SubsystemService subsystemService;
	public AddSubsystemPage() {
      final UISubsystem subsystem = new UISubsystem();
      setDefaultModel(new CompoundPropertyModel<UISubsystem>(subsystem));
      Form form = new Form("form"){

		@Override
		protected void onSubmit() {
			subsystemService.createSubsystem(subsystem);
			setResponsePage(SubsystemListPage.class);
		}
    	  
      };
      add(form);
      form.add(new RequiredTextField<String>("name"));
      form.add(new RequiredTextField<String>("callbackInformation"));
      form.add(new Button("cancel"){

		@Override
		public void onSubmit() {
			setResponsePage(SubsystemListPage.class);
		}
    	  
      }.setDefaultFormProcessing(false));
	}
}
