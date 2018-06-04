package entities.policies;

import java.io.Serializable;

/**
 * Allow IT to disable the ability for users to open files into other apps.
 */
public enum OpenInPolicy implements Serializable {
	Unknown(0),

	/**
	 * Open-In Policy is set OFF. It is default value
	 */
	Disabled(1),

	/**
	 * Open-In Policy is set ON.
	 */
	Enabled(2);

	private int intValue;
	private static java.util.HashMap<Integer, OpenInPolicy> mappings;

	private static java.util.HashMap<Integer, OpenInPolicy> getMappings() {
		if (mappings == null) {
			synchronized (OpenInPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, OpenInPolicy>();
				}
			}
		}
		return mappings;
	}

	private OpenInPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static OpenInPolicy forValue(int value) {
		return getMappings().get(value);
	}
}