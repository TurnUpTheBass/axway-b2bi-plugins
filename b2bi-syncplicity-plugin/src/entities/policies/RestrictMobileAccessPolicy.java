package entities.policies;

import java.io.Serializable;

public enum RestrictMobileAccessPolicy implements Serializable {
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
	Enabled(2),

	/**
	 * Allow only Mdm devices
	 */
	MdmOnly(3);

	private int intValue;
	private static java.util.HashMap<Integer, RestrictMobileAccessPolicy> mappings;

	private static java.util.HashMap<Integer, RestrictMobileAccessPolicy> getMappings() {
		if (mappings == null) {
			synchronized (RestrictMobileAccessPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, RestrictMobileAccessPolicy>();
				}
			}
		}
		return mappings;
	}

	private RestrictMobileAccessPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static RestrictMobileAccessPolicy forValue(int value) {
		return getMappings().get(value);
	}
}