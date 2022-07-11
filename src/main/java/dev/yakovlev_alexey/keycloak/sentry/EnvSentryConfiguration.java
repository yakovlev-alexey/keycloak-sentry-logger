package dev.yakovlev_alexey.keycloak.sentry;

public class EnvSentryConfiguration implements SentryConfiguration {
	private String dsn;
	private String release;
	private boolean isDebug;
	private boolean isErrorsOnly;

	public EnvSentryConfiguration() {
		this.dsn = System.getenv(Constants.SENTRY_DSN);
		this.release = System.getenv(Constants.SENTRY_RELEASE);
		this.isDebug = Boolean.parseBoolean(System.getenv(Constants.SENTRY_DEBUG));
		this.isErrorsOnly = Boolean.parseBoolean(System.getenv(Constants.SENTRY_ERRORS_ONLY));
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

}
