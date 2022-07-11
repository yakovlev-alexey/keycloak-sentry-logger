package dev.yakovlev_alexey.keycloak.sentry;

public enum EventSource {
	COMMON("common"),
	ADMIN("admin");

	private String name;

	EventSource(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
