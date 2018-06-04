package entities.policies;

import java.io.Serializable;

public enum StoragePasswordPolicy implements Serializable {
	/**
	 * Default policy
	 */
	Unknown(0),
	/**
	 * Users can not create storage password
	 */
	Disabled(1),
	/**
	 * Users can create storage password
	 */
	Enabled(2);

	private int intValue;
	private static java.util.HashMap<Integer, StoragePasswordPolicy> mappings;

	private static java.util.HashMap<Integer, StoragePasswordPolicy> getMappings() {
		if (mappings == null) {
			synchronized (StoragePasswordPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, StoragePasswordPolicy>();
				}
			}
		}
		return mappings;
	}

	private StoragePasswordPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static StoragePasswordPolicy forValue(int value) {
		return getMappings().get(value);
	}
}