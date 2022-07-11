package dev.yakovlev_alexey.keycloak.events;

import org.keycloak.Config.Scope;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import dev.yakovlev_alexey.keycloak.sentry.SentryConfiguration;
import io.sentry.Hub;
import io.sentry.SentryOptions;
import dev.yakovlev_alexey.keycloak.sentry.EnvSentryConfiguration;

public class SentryEventListenerFactory implements EventListenerProviderFactory {

	@Override
	public String getId() {
		return "sentry-event-listener";
	}

	@Override
	public EventListenerProvider create(KeycloakSession session) {
		SentryConfiguration configuration = new EnvSentryConfiguration();

		Hub hub = new Hub(getOptions(configuration));

		return new SentryEventListener(hub, configuration.getErrorsOnly());
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

	private SentryOptions getOptions(SentryConfiguration configuration) {
		SentryOptions options = new SentryOptions();

		options.setDsn(configuration.getDsn());
		options.setRelease(configuration.getRelease());
		options.setDebug(configuration.getDebug());

		return options;
	}

}
