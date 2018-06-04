package entities.policies;

import java.io.Serializable;

/**
 * An enumeration of possible shareable link policies
 */
public enum ShareLinkPasswordProtectedPolicy implements Serializable {
	/**
	 * Default policy
	 */
	Unknown(0),
	/**
	 * Do not require user to create password for share file links
	 */
	Disabled(1),
	/**
	 * Require user to create password for all share file links
	 */
	Enabled(2);

	private int intValue;
	private static java.util.HashMap<Integer, ShareLinkPasswordProtectedPolicy> mappings;

	private static java.util.HashMap<Integer, ShareLinkPasswordProtectedPolicy> getMappings() {
		if (mappings == null) {
			synchronized (ShareLinkPasswordProtectedPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, ShareLinkPasswordProtectedPolicy>();
				}
			}
		}
		return mappings;
	}

	private ShareLinkPasswordProtectedPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static ShareLinkPasswordProtectedPolicy forValue(int value) {
		return getMappings().get(value);
	}
}