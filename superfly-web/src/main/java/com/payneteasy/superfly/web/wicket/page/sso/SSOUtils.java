package com.payneteasy.superfly.web.wicket.page.sso;

import com.payneteasy.superfly.model.SSOSession;
import com.payneteasy.superfly.model.SubsystemTokenData;
import com.payneteasy.superfly.service.SessionService;
import com.payneteasy.superfly.service.SubsystemService;
import com.payneteasy.superfly.web.wicket.page.SessionAccessorPage;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author rpuch
 */
public class SSOUtils {
    public static final String SSO_SESSION_ID_COOKIE_NAME = "SSOSESSIONID";
    public static final int SSO_SESSION_ID_COOKIE_MAXAGE = 3600; // seconds

    public static String buildRedirectToSubsystemUrl(String landingUrl, String subsystemToken, String targetUrl) {
        StringBuilder buf = new StringBuilder();
        buf.append(landingUrl);
        buf.append(landingUrl.contains("?") ? "&" : "?");
        buf.append("subsystemToken").append("=").append(encodeForUrl(subsystemToken));
        buf.append("&");
        buf.append("targetUrl").append("=").append(encodeForUrl(targetUrl));
        return buf.toString();
    }

    private static String encodeForUrl(String subsystemToken) {
        try {
            return URLEncoder.encode(subsystemToken, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void redirectToCantLoginErrorPage(Component component, SSOLoginData loginData) {
        clearLoginData((SessionAccessorPage) component.getPage());
        redirectToLoginErrorPage(component, new Model<String>("Can't login to " + loginData.getSubsystemIdentifier()));
    }

    public static void redirectToLoginErrorPage(Component component, IModel<String> errorModel) {
        component.getRequestCycle().setResponsePage(
                new SSOLoginErrorPage(errorModel));
        component.getRequestCycle().setRedirect(true);
    }

    public static void saveLoginData(SessionAccessorPage page, SSOLoginData loginData) {
        page.getSession().setSsoLoginData(loginData);
    }

    public static void clearLoginData(SessionAccessorPage page) {
        saveLoginData(page, null);
    }

    public static SSOLoginData getSsoLoginData(SessionAccessorPage page) {
        return page.getSession().getSsoLoginData();
    }

    public static void redirectToSubsystem(SessionAccessorPage page, SSOLoginData loginData, SubsystemTokenData token) {
        SSOUtils.clearLoginData(page);
        String url = buildRedirectToSubsystemUrl(token.getLandingUrl(),
                token.getSubsystemToken(), loginData.getTargetUrl());
        redirect(page, url);
    }

    public static void redirect(Page page, String url) {
        RedirectRequestTarget requestTarget = new RedirectRequestTarget(url);
        page.getRequestCycle().setRequestTarget(requestTarget);
    }

    public static String getSsoSessionIdFromCookie(WebRequest request) {
        String ssoSessionId = null;
        Cookie cookie = request.getCookie(SSOUtils.SSO_SESSION_ID_COOKIE_NAME);
        if (cookie != null) {
            ssoSessionId = cookie.getValue();
        }
        return ssoSessionId;
    }

    public static void processSuccessfulPasswordCheck(String username,
            SessionAccessorPage page,
            SSOLoginData loginData, SessionService sessionService,
            SubsystemService subsystemService) {
        SSOSession ssoSession = sessionService.createSSOSession(username);
        Cookie cookie = new Cookie(SSOUtils.SSO_SESSION_ID_COOKIE_NAME, ssoSession.getIdentifier());
        cookie.setMaxAge(SSOUtils.SSO_SESSION_ID_COOKIE_MAXAGE);
        ((WebResponse) RequestCycle.get().getResponse()).addCookie(cookie);

        SubsystemTokenData token = subsystemService.issueSubsystemTokenIfCanLogin(
                ssoSession.getId(), loginData.getSubsystemIdentifier());
        if (token != null) {
            // can login: redirecting a user to a subsystem
            SSOUtils.redirectToSubsystem(page, loginData, token);
        } else {
            // can't login: just display an error
            // actually, this should not happen as we've already
            // checked user access, but just in case...
            SSOUtils.redirectToCantLoginErrorPage(page, loginData);
        }
    }

}
