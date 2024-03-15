package dev.yakovlev_alexey.keycloak.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;

import dev.yakovlev_alexey.keycloak.sentry.EventSource;
import dev.yakovlev_alexey.keycloak.sentry.SentryConfiguration;
import io.sentry.IHub;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.protocol.Message;
import io.sentry.protocol.User;

public class SentryEventListener implements EventListenerProvider {
	private final IHub hub;
	private final boolean errorsOnly;

	private final Set<String> ignoredEventTypes;
	private final Set<String> ignoredErrors;

	SentryEventListener(IHub hub, SentryConfiguration configuration) {
		this.hub = hub;
		this.errorsOnly = configuration.getErrorsOnly();
		this.ignoredEventTypes = configuration.getIgnoredEventTypes();
		this.ignoredErrors = configuration.getIgnoredErrors();
	}

	@Override
	public void close() {
	}

	@Override
	public void onEvent(Event event) {
		if (shouldIgnore(event)) {
			return;
		}

		SentryEvent sentryEvent = new SentryEvent();

		sentryEvent.setMessage(getMessage(event));
		sentryEvent.setLevel(getLevel(event));
		sentryEvent.setUser(getUser(event));
		sentryEvent.setExtras(getExtras(event));

		sentryEvent.setTag("realm", event.getRealmId());
		sentryEvent.setTag("type", event.getType().toString());
		sentryEvent.setTag("source", EventSource.COMMON.toString());

		hub.captureEvent(sentryEvent);
	}

	@Override
	public void onEvent(AdminEvent event, boolean includeRepresentation) {
		if (shouldIgnore(event)) {
			return;
		}

		SentryEvent sentryEvent = new SentryEvent();

		sentryEvent.setMessage(getMessage(event));
		sentryEvent.setLevel(getLevel(event));
		sentryEvent.setUser(getUser(event));
		sentryEvent.setExtras(getExtras(event, includeRepresentation));

		sentryEvent.setTag("realm", event.getRealmId());
		sentryEvent.setTag("type", event.getOperationType().toString());
		sentryEvent.setTag("source", EventSource.ADMIN.toString());

		hub.captureEvent(sentryEvent);

	}

	private boolean shouldIgnore(String error, String type) {
		if (errorsOnly && error == null) {
			return true;
		}

		return ignoredEventTypes.contains(type) || ignoredErrors.contains(error);
	}

	private boolean shouldIgnore(Event event) {
		return shouldIgnore(event.getError(), event.getType().toString());
	}

	private boolean shouldIgnore(AdminEvent event) {
		return shouldIgnore(event.getError(), event.getOperationType().toString());
	}

	private Message getMessage(String error, String type) {
		Message message = new Message();

		if (errorsOnly) {
			message.setMessage(error);
		} else {
			message.setMessage(type);
		}

		return message;
	}

	private Message getMessage(Event event) {
		return getMessage(event.getError(), event.getType().toString());
	}

	private Message getMessage(AdminEvent event) {
		return getMessage(event.getError(), event.getOperationType().toString());
	}

	private SentryLevel getLevel(String error) {
		return error == null ? SentryLevel.INFO : SentryLevel.ERROR;
	}

	private SentryLevel getLevel(Event event) {
		return getLevel(event.getError());
	}

	private SentryLevel getLevel(AdminEvent event) {
		return getLevel(event.getError());
	}

	private User getUser(String userId) {
		if (userId == null) {
			return null;
		}

		User user = new User();
		user.setId(userId);

		return user;
	}

	private User getUser(Event event) {
		return getUser(event.getUserId());
	}

	private User getUser(AdminEvent event) {
		return getUser(event.getAuthDetails().getUserId());
	}

	private Map<String, Object> getExtras(Event event) {
		HashMap<String, Object> extras = new HashMap<>();

		if (event.getDetails() != null) {
			extras.putAll(event.getDetails());
		}

		extras.put("realmId", event.getRealmId());
		extras.put("clientId", event.getClientId());
		extras.put("sessionId", event.getSessionId());
		extras.put("ipAddress", event.getIpAddress());

		return extras;
	}

	private Map<String, Object> getExtras(AdminEvent event, boolean includeRepresentation) {
		HashMap<String, Object> extras = new HashMap<>();

		extras.put("realmId", event.getRealmId());
		extras.put("clientId", event.getAuthDetails().getClientId());
		extras.put("ipAddress", event.getAuthDetails().getIpAddress());
		extras.put("resourcePath", event.getResourcePath());
		extras.put("resourceType", event.getResourceTypeAsString());

		if (includeRepresentation) {
			extras.put("representation", event.getRepresentation());
		}

		return extras;
	}
}
