package dev.yakovlev_alexey.keycloak.sentry;

import java.util.Set;

public interface SentryConfiguration {
	boolean getErrorsOnly();

	Set<String> getIgnoredEventTypes();

	Set<String> getIgnoredErrors();
}
