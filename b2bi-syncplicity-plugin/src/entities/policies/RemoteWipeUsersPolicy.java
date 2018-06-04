package entities.policies;

import java.io.Serializable;

public enum RemoteWipeUsersPolicy implements Serializable {
	// <summary>
	/**
	 * Default value
	 */
	Unknown(0),
	/**
	 * Policy is disabled
	 */
	Disabled(1),
	/**
	 * Policy is enabled
	 */
	Enabled(2);

	private int intValue;
	private static java.util.HashMap<Integer, RemoteWipeUsersPolicy> mappings;

	private static java.util.HashMap<Integer, RemoteWipeUsersPolicy> getMappings() {
		if (mappings == null) {
			synchronized (RemoteWipeUsersPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, RemoteWipeUsersPolicy>();
				}
			}
		}
		return mappings;
	}

	private RemoteWipeUsersPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static RemoteWipeUsersPolicy forValue(int value) {
		return getMappings().get(value);
	}
}