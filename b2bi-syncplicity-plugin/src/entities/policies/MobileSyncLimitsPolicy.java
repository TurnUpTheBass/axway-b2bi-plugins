package entities.policies;

import java.io.Serializable;

/**
 * An enumeration of possible mobile sync limits policies for a company
 */
public enum MobileSyncLimitsPolicy implements Serializable {
	/**
	 * Default policy
	 */
	Unknown(0),
	/**
	 * Allow users to set their own limits on how much data can be transferred
	 * over a cellular network every billing cycle
	 */
	None(1),
	/**
	 * Enforce the special limits on how much data can be transferred over a
	 * cellular network (note: synchronization over a Wi-Fi hotspot will remain
	 * unrestricted)
	 */
	Enforce(2);

	private int intValue;
	private static java.util.HashMap<Integer, MobileSyncLimitsPolicy> mappings;

	private static java.util.HashMap<Integer, MobileSyncLimitsPolicy> getMappings() {
		if (mappings == null) {
			synchronized (MobileSyncLimitsPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, MobileSyncLimitsPolicy>();
				}
			}
		}
		return mappings;
	}

	private MobileSyncLimitsPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static MobileSyncLimitsPolicy forValue(int value) {
		return getMappings().get(value);
	}
}