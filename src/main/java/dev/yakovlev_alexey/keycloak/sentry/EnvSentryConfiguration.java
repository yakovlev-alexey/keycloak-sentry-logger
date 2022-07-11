package dev.yakovlev_alexey.keycloak.sentry;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EnvSentryConfiguration implements SentryConfiguration {
	private String dsn;
	private String release;
	private boolean isDebug;
	private boolean isErrorsOnly;

	private Set<String> ignoredEventTypes;
	private Set<String> ignoredErrors;

	public EnvSentryConfiguration() {
		this.dsn = System.getenv(Constants.SENTRY_DSN);
		this.release = System.getenv(Constants.SENTRY_RELEASE);
		this.isDebug = Boolean.parseBoolean(System.getenv(Constants.SENTRY_DEBUG));
		this.isErrorsOnly = Boolean.parseBoolean(System.getenv(Constants.SENTRY_ERRORS_ONLY));

		String ignoredEventTypes = Optional.ofNullable(System.getenv(Constants.SENTRY_IGNORED_EVENT_TYPES)).orElse("");
		String ignoredErrors = Optional.ofNullable(System.getenv(Constants.SENTRY_IGNORED_ERRORS)).orElse("");

		this.ignoredEventTypes = Arrays.stream(ignoredEventTypes.split(";")).collect(Collectors.toSet());
		this.ignoredErrors = Arrays.stream(ignoredErrors.split(";")).collect(Collectors.toSet());
	}

	@Override
	public String getDsn() {
		return this.dsn;
	}

	@Override
	public String getRelease() {
		return this.release;
	}

	@Override
	public boolean getDebug() {
		return this.isDebug;
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
