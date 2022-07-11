package dev.yakovlev_alexey.keycloak.sentry;

import java.util.Set;

public interface SentryConfiguration {
	String getDsn();

	String getRelease();

	boolean getDebug();

	boolean getErrorsOnly();

	Set<String> getIgnoredEventTypes();

	Set<String> getIgnoredErrors();
}
