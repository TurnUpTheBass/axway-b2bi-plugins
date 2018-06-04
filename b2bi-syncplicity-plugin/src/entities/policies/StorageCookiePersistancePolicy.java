package entities.policies;

import java.io.Serializable;

public enum StorageCookiePersistancePolicy implements Serializable {
	/**
	 * Default policy
	 */
	Unknown(0),
	/**
	 * Users can not persist storage cookie
	 */
	Disabled(1),
	/**
	 * Users can persist storage cookie
	 */
	Enabled(2);

	private int intValue;
	private static java.util.HashMap<Integer, StorageCookiePersistancePolicy> mappings;

	private static java.util.HashMap<Integer, StorageCookiePersistancePolicy> getMappings() {
		if (mappings == null) {
			synchronized (StorageCookiePersistancePolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, StorageCookiePersistancePolicy>();
				}
			}
		}
		return mappings;
	}

	private StorageCookiePersistancePolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static StorageCookiePersistancePolicy forValue(int value) {
		return getMappings().get(value);
	}
}