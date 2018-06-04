package entities.policies;

import java.io.Serializable;

/**
 * Share link expiration option
 */
public enum ShareLinkExpirationPolicy implements Serializable {
	// <summary>
	/**
	 * Default value
	 */
	Unknown(0),
	/**
	 * Expiration policy is set on
	 */
	Enabled(1),
	/**
	 * Expiration policy is disabled
	 */
	Disabled(2);

	private int intValue;
	private static java.util.HashMap<Integer, ShareLinkExpirationPolicy> mappings;

	private static java.util.HashMap<Integer, ShareLinkExpirationPolicy> getMappings() {
		if (mappings == null) {
			synchronized (ShareLinkExpirationPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, ShareLinkExpirationPolicy>();
				}
			}
		}
		return mappings;
	}

	private ShareLinkExpirationPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static ShareLinkExpirationPolicy forValue(int value) {
		return getMappings().get(value);
	}
}