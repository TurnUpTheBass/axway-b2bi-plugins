package entities.policies;

import java.io.Serializable;

public enum AdminPasswordComplexityPolicy implements Serializable {
	/**
	 * Default policy
	 */
	Unknown(0),
	/**
	 * Company administrators have no restrictions when creating their password
	 */
	Disabled(1),
	/**
	 * Company administrators are restricted to creating password of a specified
	 * minimum complexity
	 */
	Enabled(2);

	private int intValue;
	private static java.util.HashMap<Integer, AdminPasswordComplexityPolicy> mappings;

	private static java.util.HashMap<Integer, AdminPasswordComplexityPolicy> getMappings() {
		if (mappings == null) {
			synchronized (AdminPasswordComplexityPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, AdminPasswordComplexityPolicy>();
				}
			}
		}
		return mappings;
	}

	private AdminPasswordComplexityPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static AdminPasswordComplexityPolicy forValue(int value) {
		return getMappings().get(value);
	}
}