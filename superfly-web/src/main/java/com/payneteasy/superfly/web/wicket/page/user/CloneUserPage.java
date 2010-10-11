package com.payneteasy.superfly.web.wicket.page.user;

import com.payneteasy.superfly.model.ui.user.UIUser;
import com.payneteasy.superfly.policy.IPolicyValidation;
import com.payneteasy.superfly.policy.password.PasswordCheckContext;
import com.payneteasy.superfly.service.UserService;
import com.payneteasy.superfly.web.wicket.page.BasePage;
import com.payneteasy.superfly.web.wicket.validation.PasswordInputValidator;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.springframework.security.annotation.Secured;

/**
 * Page used to create a user.
 * 
 * @author Roman Puchkovskiy
 */
@Secured("ROLE_ADMIN")
public class CloneUserPage extends BasePage {
	
	@SpringBean
	private UserService userService;

    @SpringBean
    private IPolicyValidation<PasswordCheckContext> policyValidation;

	public CloneUserPage(PageParameters params) {
		super(params);
		
		final long userId = params.getAsLong("userId");
		final UIUser oldUser = userService.getUser(userId);
		
		final UIUserWithPassword2 user = new UIUserWithPassword2();
		Form<UIUserWithPassword2> form = new Form<UIUserWithPassword2>("form", new Model<UIUserWithPassword2>(user)) {
			@Override
			protected void onSubmit() {
				userService.cloneUser(userId, user.getUsername(), user.getPassword(), user.getEmail());
				getRequestCycle().setResponsePage(ListUsersPage.class);
				getRequestCycle().setRedirect(true);
				info("User cloned: " + oldUser.getUsername() + " to " + user.getUsername());
			}
		};
		add(form);
		form.add(new Label("old-userid", String.valueOf(oldUser.getId())));
		form.add(new Label("old-username", oldUser.getUsername()));

        FormComponent<String> userName=new RequiredTextField<String>("username",
				new PropertyModel<String>(user, "username"));

		form.add(userName);
		TextField<String> email = new TextField<String>("email",new PropertyModel<String>(user, "email"));
		email.add(EmailAddressValidator.getInstance());
		form.add(email.setRequired(true));
		FormComponent<String> password1Field = new PasswordTextField("password",
				new PropertyModel<String>(user, "password")).setRequired(true);
		form.add(password1Field);
		FormComponent<String> password2Field = new PasswordTextField("password2",
				new PropertyModel<String>(user, "password2")).setRequired(true);
		form.add(password2Field);
		form.add(new EqualPasswordInputValidator(password1Field, password2Field));
		form.add(new BookmarkablePageLink<Page>("cancel", ListUsersPage.class));
        form.add(new PasswordInputValidator(userName,password1Field,userService));
	}
	
	@Override
	protected String getTitle() {
		return "Clone user";
	}

}
