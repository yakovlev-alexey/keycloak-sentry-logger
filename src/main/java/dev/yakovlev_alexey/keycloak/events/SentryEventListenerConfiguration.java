package dev.yakovlev_alexey.keycloak.events;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import dev.yakovlev_alexey.keycloak.sentry.Constants;
import dev.yakovlev_alexey.keycloak.sentry.SentryConfiguration;

public class SentryEventListenerConfiguration implements SentryConfiguration {
	private boolean isErrorsOnly;

	private Set<String> ignoredEventTypes;
	private Set<String> ignoredErrors;

	public SentryEventListenerConfiguration() {
		this.isErrorsOnly = Boolean.parseBoolean(System.getenv(Constants.SENTRY_ERRORS_ONLY));

		String ignoredEventTypes = Optional.ofNullable(System.getenv(Constants.SENTRY_IGNORED_EVENT_TYPES)).orElse("");
		String ignoredErrors = Optional.ofNullable(System.getenv(Constants.SENTRY_IGNORED_ERRORS)).orElse("");

		this.ignoredEventTypes = Arrays.stream(ignoredEventTypes.split(";")).collect(Collectors.toSet());
		this.ignoredErrors = Arrays.stream(ignoredErrors.split(";")).collect(Collectors.toSet());
	}

	@Override
	public boolean getErrorsOnly() {
		return this.isErrorsOnly;
	}

	@Override
	public Set<String> getIgnoredEventTypes() {
		return this.ignoredEventTypes;
	}

	@Override
	public Set<String> getIgnoredErrors() {
		return this.ignoredErrors;
	}

}
