package com.payneteasy.superfly.web.wicket.page.group.wizard;

import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.springframework.security.annotation.Secured;


@Secured("ROLE_ADMIN")
public class CreateGroupWizardPanel extends Wizard {

	public CreateGroupWizardPanel(String id) {
		super(id);
		IModel<GroupWizardModel> groupModel = new Model<GroupWizardModel>(new GroupWizardModel());
	     
		setDefaultModel(groupModel);
		
		WizardModel model = new WizardModel();
		model.add(new GroupPropertiesWizardStep(
				new Model("Set Group properties"), 
				new Model("Provide Group name and Subsystem."), 
				groupModel));
		model.add(new GroupActionsWizardStep(
				new Model("Select Actions for group"), 
				new Model("Choose actions from list"), 
				groupModel));
		init(model);
	}

}
