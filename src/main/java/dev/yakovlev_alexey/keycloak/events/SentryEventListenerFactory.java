package dev.yakovlev_alexey.keycloak.events;

import org.keycloak.Config.Scope;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import dev.yakovlev_alexey.keycloak.sentry.SentryConfiguration;
import io.sentry.IHub;
import io.sentry.Sentry;

public class SentryEventListenerFactory implements EventListenerProviderFactory {

	@Override
	public String getId() {
		return "sentry-event-listener";
	}

	@Override
	public EventListenerProvider create(KeycloakSession session) {
		Sentry.init();

		SentryConfiguration configuration = new SentryEventListenerConfiguration();
		IHub hub = Sentry.getCurrentHub();

		return new SentryEventListener(hub, configuration);
	}

	@Override
	public void init(Scope config) {
	}

	@Override
	public void postInit(KeycloakSessionFactory factory) {
	}

	@Override
	public void close() {
	}
}
