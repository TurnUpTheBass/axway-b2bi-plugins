package entities.policies;

import java.io.Serializable;

public enum ConcurrentUseNotificationPolicy implements Serializable {
	Unknown(0),

	/**
	 * Don't send notification if multiple sessions was detected.
	 */
	Disable(1),

	/**
	 * Notify only company admins if multiple sessions was detected.
	 */
	OnlyAdmins(2),

	/**
	 * Notify company admins and user if multiple sessions was detected.
	 */
	AdmnsAndEndUser(3);

	private int intValue;
	private static java.util.HashMap<Integer, ConcurrentUseNotificationPolicy> mappings;

	private static java.util.HashMap<Integer, ConcurrentUseNotificationPolicy> getMappings() {
		if (mappings == null) {
			synchronized (ConcurrentUseNotificationPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, ConcurrentUseNotificationPolicy>();
				}
			}
		}
		return mappings;
	}

	private ConcurrentUseNotificationPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static ConcurrentUseNotificationPolicy forValue(int value) {
		return getMappings().get(value);
	}
}