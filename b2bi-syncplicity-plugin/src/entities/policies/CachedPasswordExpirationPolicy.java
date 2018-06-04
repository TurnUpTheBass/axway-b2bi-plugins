package entities.policies;

import java.io.Serializable;

/**
 * Mobile change password expiration option
 */
public enum CachedPasswordExpirationPolicy implements Serializable {
	// <summary>
	/**
	 * Default value
	 */
	Unknown(0),
	/**
	 * Expiration policy is set on and measure interval is in Days
	 */
	EnabledInDays(1),
	/**
	 * Expiration policy is set on and measure interval is in Hours
	 */
	EnabledInHours(2),
	/**
	 * Expiration policy is set on and measure interval is in Minutes
	 */
	EnabledInMinutes(3),
	/**
	 * Expiration policy is disabled
	 */
	Disabled(4);

	private int intValue;
	private static java.util.HashMap<Integer, CachedPasswordExpirationPolicy> mappings;

	private static java.util.HashMap<Integer, CachedPasswordExpirationPolicy> getMappings() {
		if (mappings == null) {
			synchronized (CachedPasswordExpirationPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, CachedPasswordExpirationPolicy>();
				}
			}
		}
		return mappings;
	}

	private CachedPasswordExpirationPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static CachedPasswordExpirationPolicy forValue(int value) {
		return getMappings().get(value);
	}
}