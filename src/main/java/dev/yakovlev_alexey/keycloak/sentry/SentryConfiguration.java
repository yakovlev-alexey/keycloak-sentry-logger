package dev.yakovlev_alexey.keycloak.sentry;

public interface SentryConfiguration {
	String getDsn();

	String getRelease();

	boolean getDebug();

	boolean getErrorsOnly();
}
