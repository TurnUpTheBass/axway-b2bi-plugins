package entities.policies;

import java.io.Serializable;

public enum RemoteWipeEndpointPolicy implements Serializable {
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
	private static java.util.HashMap<Integer, RemoteWipeEndpointPolicy> mappings;

	private static java.util.HashMap<Integer, RemoteWipeEndpointPolicy> getMappings() {
		if (mappings == null) {
			synchronized (RemoteWipeEndpointPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, RemoteWipeEndpointPolicy>();
				}
			}
		}
		return mappings;
	}

	private RemoteWipeEndpointPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static RemoteWipeEndpointPolicy forValue(int value) {
		return getMappings().get(value);
	}
}