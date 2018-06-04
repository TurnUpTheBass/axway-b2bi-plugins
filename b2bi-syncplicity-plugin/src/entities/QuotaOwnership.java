package entities;

import java.io.Serializable;

public enum QuotaOwnership implements Serializable {
	Unknown(0),

	Shared(1),

	Owned(2);

	private int intValue;
	private static java.util.HashMap<Integer, QuotaOwnership> mappings;

	private static java.util.HashMap<Integer, QuotaOwnership> getMappings() {
		if (mappings == null) {
			synchronized (QuotaOwnership.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, QuotaOwnership>();
				}
			}
		}
		return mappings;
	}

	private QuotaOwnership(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static QuotaOwnership forValue(int value) {
		return getMappings().get(value);
	}
}