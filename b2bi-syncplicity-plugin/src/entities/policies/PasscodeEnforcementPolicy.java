package entities.policies;

import java.io.Serializable;

/**
 * Allow IT to enforce the use of a passcode, if enabled users will need to set
 * a passcode on first use or next use.
 */
public enum PasscodeEnforcementPolicy implements Serializable {
	Unknown(0),

	/**
	 * Passcode Enforcement Policy is set OFF. It is default value
	 */
	Disabled(1),

	/**
	 * Passcode Enforcement Policy is set ON.
	 */
	Enabled(2);

	private int intValue;
	private static java.util.HashMap<Integer, PasscodeEnforcementPolicy> mappings;

	private static java.util.HashMap<Integer, PasscodeEnforcementPolicy> getMappings() {
		if (mappings == null) {
			synchronized (PasscodeEnforcementPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, PasscodeEnforcementPolicy>();
				}
			}
		}
		return mappings;
	}

	private PasscodeEnforcementPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static PasscodeEnforcementPolicy forValue(int value) {
		return getMappings().get(value);
	}
}