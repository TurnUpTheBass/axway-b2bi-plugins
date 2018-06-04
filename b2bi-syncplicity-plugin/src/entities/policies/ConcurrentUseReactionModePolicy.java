package entities.policies;

import java.io.Serializable;

public enum ConcurrentUseReactionModePolicy implements Serializable {
	Unknown(0),

	/**
	 * Default value. Do not disable the user’s account when concurrent use of
	 * a device from multiple sessions is detected.
	 */
	Disable(1),

	/**
	 * Disable the user’s account when concurrent use of a device from
	 * multiple sessions is detected.
	 */
	Enable(2);

	private int intValue;
	private static java.util.HashMap<Integer, ConcurrentUseReactionModePolicy> mappings;

	private static java.util.HashMap<Integer, ConcurrentUseReactionModePolicy> getMappings() {
		if (mappings == null) {
			synchronized (ConcurrentUseReactionModePolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, ConcurrentUseReactionModePolicy>();
				}
			}
		}
		return mappings;
	}

	private ConcurrentUseReactionModePolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static ConcurrentUseReactionModePolicy forValue(int value) {
		return getMappings().get(value);
	}
}