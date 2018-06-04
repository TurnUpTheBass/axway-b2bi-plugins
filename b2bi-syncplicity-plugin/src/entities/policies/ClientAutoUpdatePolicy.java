package entities.policies;

import java.io.Serializable;

/**
 * An enumeration of possible update policies for a company
 */
public enum ClientAutoUpdatePolicy implements Serializable {
	/**
	 * Default value
	 */
	Unknown(0),
	/**
	 * Don't notify users about any updates
	 */
	None(1),
	/**
	 * Only notify users about critical (non-optional) updates
	 */
	CriticalOnly(2),
	/**
	 * Notify users about all updates
	 */
	All(3);

	private int intValue;
	private static java.util.HashMap<Integer, ClientAutoUpdatePolicy> mappings;

	private static java.util.HashMap<Integer, ClientAutoUpdatePolicy> getMappings() {
		if (mappings == null) {
			synchronized (ClientAutoUpdatePolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, ClientAutoUpdatePolicy>();
				}
			}
		}
		return mappings;
	}

	private ClientAutoUpdatePolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static ClientAutoUpdatePolicy forValue(int value) {
		return getMappings().get(value);
	}
}