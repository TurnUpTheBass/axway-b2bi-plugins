package entities.policies;

import java.io.Serializable;

/**
 * An enumeration of possible shareable link policies
 */
public enum ShareableLinkPolicy implements Serializable {
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
	AllowAll(3);

	private int intValue;
	private static java.util.HashMap<Integer, ShareableLinkPolicy> mappings;

	private static java.util.HashMap<Integer, ShareableLinkPolicy> getMappings() {
		if (mappings == null) {
			synchronized (ShareableLinkPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, ShareableLinkPolicy>();
				}
			}
		}
		return mappings;
	}

	private ShareableLinkPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static ShareableLinkPolicy forValue(int value) {
		return getMappings().get(value);
	}
}