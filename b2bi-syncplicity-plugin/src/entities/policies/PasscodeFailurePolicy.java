package entities.policies;

import java.io.Serializable;

/**
 * That ensures that if users type incorrect passcode "x" times, the user is
 * logged out and cache is deleted.
 */
public enum PasscodeFailurePolicy implements Serializable {
	Unknown(0),

	/**
	 * Passcode Failure Policy is set OFF. It is default value
	 */
	Disabled(1),

	/**
	 * Passcode Failure Policy is set ON.
	 */
	Enabled(2);

	private int intValue;
	private static java.util.HashMap<Integer, PasscodeFailurePolicy> mappings;

	private static java.util.HashMap<Integer, PasscodeFailurePolicy> getMappings() {
		if (mappings == null) {
			synchronized (PasscodeFailurePolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, PasscodeFailurePolicy>();
				}
			}
		}
		return mappings;
	}

	private PasscodeFailurePolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static PasscodeFailurePolicy forValue(int value) {
		return getMappings().get(value);
	}
}