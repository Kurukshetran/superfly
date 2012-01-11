package com.payneteasy.superfly.web.wicket.page.smtp_server;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.payneteasy.superfly.email.EmailService;

public class TestPanel extends Panel {
	private static final Logger logger = LoggerFactory.getLogger(TestPanel.class);
	
	@SpringBean
	private EmailService emailService;
	
	public TestPanel(String id, final long serverId, final ModalWindow window,
			final FeedbackPanel feedbackPanel) {
		super(id);
		
		Form<?> form = new Form<Void>("form");
		add(form);
		
		final IModel<String> addressModel = new Model<String>();
		form.add(new RequiredTextField<String>("address", addressModel));
		
		form.add(new AjaxLink<Void>("cancel-link") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				window.close(target);
			}
		});
		form.add(new AjaxSubmitLink("submit-link") {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try {
					emailService.sendTestMesage(serverId, addressModel.getObject());
					info("Test message sent");
					target.addComponent(feedbackPanel);
				} catch (RuntimeException e) {
					logger.error(e.getMessage(), e);
					error(e.getMessage());
					target.addComponent(feedbackPanel);
				}
				window.close(target);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(feedbackPanel);
			}
		});
	}
}
