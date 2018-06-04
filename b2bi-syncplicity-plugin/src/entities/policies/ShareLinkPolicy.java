package entities.policies;

import java.io.Serializable;

public enum ShareLinkPolicy implements Serializable {
	/**
	 * Default policy
	 */
	Unknown(0),
	/**
	 * Disable shareable links (NOT IMPLEMENTED)
	 */
	DisallowAll(1),
	/**
	 * Links can only be shared with folks in the same company
	 */
	InternalOnly(2),
	/**
	 * Allow sharing with anyone (Consumer behavior)
	 */
	AllowAll(3),
	/**
	 * Links can only be shared with list of intended users and groups
	 */
	IntendedOnly(4);

	private int intValue;
	private static java.util.HashMap<Integer, ShareLinkPolicy> mappings;

	private static java.util.HashMap<Integer, ShareLinkPolicy> getMappings() {
		if (mappings == null) {
			synchronized (ShareLinkPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, ShareLinkPolicy>();
				}
			}
		}
		return mappings;
	}

	private ShareLinkPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static ShareLinkPolicy forValue(int value) {
		return getMappings().get(value);
	}
}