package entities.policies;

import java.io.Serializable;

public enum AdminIpRestrictionPolicy implements Serializable {
	// <summary>
	/**
	 * Default value
	 */
	Unknown(0),
	/**
	 * Restriction policy is set off
	 */
	Disabled(1),
	/**
	 * Restriction policy is enabled
	 */
	Enabled(2);

	private int intValue;
	private static java.util.HashMap<Integer, AdminIpRestrictionPolicy> mappings;

	private static java.util.HashMap<Integer, AdminIpRestrictionPolicy> getMappings() {
		if (mappings == null) {
			synchronized (AdminIpRestrictionPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, AdminIpRestrictionPolicy>();
				}
			}
		}
		return mappings;
	}

	private AdminIpRestrictionPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static AdminIpRestrictionPolicy forValue(int value) {
		return getMappings().get(value);
	}
}