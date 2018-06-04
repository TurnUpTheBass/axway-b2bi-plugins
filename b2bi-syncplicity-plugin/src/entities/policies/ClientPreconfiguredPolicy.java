package entities.policies;

import java.io.Serializable;

/**
 * An enumeration of possible update policies for a company
 */
public enum ClientPreconfiguredPolicy implements Serializable {
	/**
	 * Nothing is known about the preconfiguration policy for this company
	 */
	Unknown(0),
	/**
	 * All machines for this company are preconfigured
	 */
	AllMachinesArePreconfigured(1),
	/**
	 * No machines for this company are preconfigured
	 */
	NoMachinesArePreconfigured(2),
	/**
	 * Some machines are preconfigured (NOT IMPLEMENTED)
	 */
	SomeMachinesArePreconfigured(3);

	private int intValue;
	private static java.util.HashMap<Integer, ClientPreconfiguredPolicy> mappings;

	private static java.util.HashMap<Integer, ClientPreconfiguredPolicy> getMappings() {
		if (mappings == null) {
			synchronized (ClientPreconfiguredPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, ClientPreconfiguredPolicy>();
				}
			}
		}
		return mappings;
	}

	private ClientPreconfiguredPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static ClientPreconfiguredPolicy forValue(int value) {
		return getMappings().get(value);
	}
}