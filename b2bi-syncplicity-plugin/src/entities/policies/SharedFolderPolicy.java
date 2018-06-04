package entities.policies;

import java.io.Serializable;

/**
 * An enumeration of possible shareable link policies
 */
public enum SharedFolderPolicy implements Serializable {
	/**
	 * Default policy
	 */
	Unknown(0),
	/**
	 * Disable shared folders
	 */
	DisallowAll(1),
	/**
	 * Folders can only be shared with folks in the same company
	 */
	InternalOnly(2),
	/**
	 * Allow sharing with anyone (Consumer behavior)
	 */
	AllowAll(3);

	private int intValue;
	private static java.util.HashMap<Integer, SharedFolderPolicy> mappings;

	private static java.util.HashMap<Integer, SharedFolderPolicy> getMappings() {
		if (mappings == null) {
			synchronized (SharedFolderPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, SharedFolderPolicy>();
				}
			}
		}
		return mappings;
	}

	private SharedFolderPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static SharedFolderPolicy forValue(int value) {
		return getMappings().get(value);
	}
}