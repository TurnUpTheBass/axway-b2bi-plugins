package entities.policies;

import java.io.Serializable;

/**
 * Password complexity options
 */
public enum ShareLinkPasswordComplexity implements Serializable {
	// <summary>
	/**
	 * Default value
	 */
	Unknown(0),
	/**
	 * Password need to be of configirable length and can contain anything
	 */
	Default(1),
	/**
	 * Password need at least one uppercase, one digit and one special character
	 */
	Complex(2);

	private int intValue;
	private static java.util.HashMap<Integer, ShareLinkPasswordComplexity> mappings;

	private static java.util.HashMap<Integer, ShareLinkPasswordComplexity> getMappings() {
		if (mappings == null) {
			synchronized (ShareLinkPasswordComplexity.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, ShareLinkPasswordComplexity>();
				}
			}
		}
		return mappings;
	}

	private ShareLinkPasswordComplexity(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static ShareLinkPasswordComplexity forValue(int value) {
		return getMappings().get(value);
	}
}