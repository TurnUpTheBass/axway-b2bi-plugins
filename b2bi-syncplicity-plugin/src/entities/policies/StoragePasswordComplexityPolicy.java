package entities.policies;

import java.io.Serializable;

public enum StoragePasswordComplexityPolicy implements Serializable {
	/**
	 * Default policy
	 */
	Unknown(0),
	/**
	 * Users have no restrictions when creating their password
	 */
	Disabled(1),
	/**
	 * Users are restricted to creating password of a specified minimum
	 * complexity
	 */
	Enabled(2);

	private int intValue;
	private static java.util.HashMap<Integer, StoragePasswordComplexityPolicy> mappings;

	private static java.util.HashMap<Integer, StoragePasswordComplexityPolicy> getMappings() {
		if (mappings == null) {
			synchronized (StoragePasswordComplexityPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, StoragePasswordComplexityPolicy>();
				}
			}
		}
		return mappings;
	}

	private StoragePasswordComplexityPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static StoragePasswordComplexityPolicy forValue(int value) {
		return getMappings().get(value);
	}
}