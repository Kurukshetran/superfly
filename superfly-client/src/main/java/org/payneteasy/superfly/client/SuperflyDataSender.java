package org.payneteasy.superfly.client;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.payneteasy.superfly.client.exception.CollectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import com.payneteasy.superfly.api.ActionDescription;
import com.payneteasy.superfly.api.SSOService;

/**
 * Class that, upon instantiation and initialization in a Spring Application
 * Context, automatically sends subsystem data to SSOService.
 * 
 * @author Roman Puchkovskiy
 */
public class SuperflyDataSender implements InitializingBean {
	
	private Logger logger = LoggerFactory.getLogger(SuperflyDataSender.class);
	
	private SSOService ssoService;
	private ActionDescriptionCollector actionDescriptionCollector;
	private String subsystemIdentifier = null;
	private long delay = 0;

	@Required
	public void setSsoService(SSOService ssoService) {
		this.ssoService = ssoService;
	}

	@Required
	public void setActionDescriptionCollector(
			ActionDescriptionCollector actionDescriptionCollector) {
		this.actionDescriptionCollector = actionDescriptionCollector;
	}

	public String getSubsystemIdentifier() {
		return subsystemIdentifier;
	}

	public void setSubsystemIdentifier(String subsystemIdentifier) {
		this.subsystemIdentifier = subsystemIdentifier;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public void afterPropertiesSet() throws Exception {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					ssoService.sendSystemData(getSubsystemIdentifier(),
							obtainActionDescriptions());
				} catch (CollectionException e) {
					logger.error("Cannot send subsystem data", e);
				}
			}
		}, delay);
	}

	protected ActionDescription[] obtainActionDescriptions() throws CollectionException {
		List<ActionDescription> actions = actionDescriptionCollector.collect();
		return actions.toArray(new ActionDescription[actions.size()]);
	}

}
