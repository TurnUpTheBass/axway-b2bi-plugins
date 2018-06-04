package entities;

import java.io.Serializable;

public enum QuotaAllocationType implements Serializable {
	Unknown(0),

	GroupQuota(1),

	StorageVaultQuota(2);

	private int intValue;
	private static java.util.HashMap<Integer, QuotaAllocationType> mappings;

	private static java.util.HashMap<Integer, QuotaAllocationType> getMappings() {
		if (mappings == null) {
			synchronized (QuotaAllocationType.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, QuotaAllocationType>();
				}
			}
		}
		return mappings;
	}

	private QuotaAllocationType(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static QuotaAllocationType forValue(int value) {
		return getMappings().get(value);
	}
}