package entities;

import java.io.Serializable;

/**
 * User Account Status
 */
public enum UserAccountStatus implements Serializable {
	Unknown(0),

	Disabled(1),

	Enabled(2),

	Delinquent(3),

	PendingActivation(4),

	Unverified(5),

	Suspended(6);

	private int intValue;
	private static java.util.HashMap<Integer, UserAccountStatus> mappings;

	private static java.util.HashMap<Integer, UserAccountStatus> getMappings() {
		if (mappings == null) {
			synchronized (UserAccountStatus.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, UserAccountStatus>();
				}
			}
		}
		return mappings;
	}

	private UserAccountStatus(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static UserAccountStatus forValue(int value) {
		return getMappings().get(value);
	}
}
