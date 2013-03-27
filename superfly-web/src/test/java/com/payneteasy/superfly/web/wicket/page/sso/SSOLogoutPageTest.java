package com.payneteasy.superfly.web.wicket.page.sso;

import com.payneteasy.superfly.service.SessionService;
import com.payneteasy.superfly.web.wicket.page.AbstractPageTest;
import org.apache.wicket.PageParameters;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.Cookie;
import java.util.HashMap;

/**
 * @author rpuch
 */
public class SSOLogoutPageTest extends AbstractPageTest {
    private SessionService sessionService;

    @Before
    public void setUp() {
        sessionService = EasyMock.createStrictMock(SessionService.class);
    }

    @Override
    protected Object getBean(Class<?> type) {
        if (SessionService.class == type) {
            return sessionService;
        }
        return super.getBean(type);
    }

    @Test
    public void testNoSSOCookie() {
        tester.startPage(SSOLogoutPage.class, new PageParameters(new HashMap<String, Object>() {{
            put("returnUrl", "http://localhost/return-url");
        }}));
        tester.assertRedirected("http://localhost/return-url");
    }

    @Test
    public void testWithSSOCookie() {
        sessionService.deleteSSOSession("super-session-id");
        EasyMock.expectLastCall();
        EasyMock.replay(sessionService);

        tester.getServletResponse().addCookie(new Cookie("SSOSESSIONID", "super-session-id"));
        tester.startPage(SSOLogoutPage.class, new PageParameters(new HashMap<String, Object>() {{
            put("returnUrl", "http://localhost/return-url");
        }}));
        tester.assertRedirected("http://localhost/return-url");

        EasyMock.verify(sessionService);
    }
}
