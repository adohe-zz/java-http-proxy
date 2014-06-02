package com.westudio.java.socket.pool.metrics;

public abstract class Metric {
	public static final String NO_DESCRIPTION = "{default description}";
	private final String name;
	private final String description;

	public Metric(String name) {
		this.name = name;
		this.description = NO_DESCRIPTION;
	}

	public Metric(String name, String desc) {
		this.name = name;
		this.description = desc;
	}

	public String name() {
		return name;
	}

	public String description() {
		return description;
	}

	public abstract Number value();

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Metric other = (Metric) obj;
		if (!this.name.equals(other.name())) {
			return false;
		}
		if (!this.description.equals(other.description())) {
			return false;
		}
		if (!value().equals(other.value())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return "Metric{" + "name='" + name + "' description='" + description
				+ "' value=" + value() + '}';
	}
}
