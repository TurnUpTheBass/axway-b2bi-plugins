package entities.policies;

import java.io.Serializable;

/**
 * An enumeration of possible include owner policies for a company
 */
public enum IncludeOwnerInFolderNamePolicy implements Serializable {
	/**
	 * Nothing is known about the include owner policy for this company
	 */
	Unknown(0),
	/**
	 * Owner name is included
	 */
	Included(1),
	/**
	 * Owner name is ignored
	 */
	Ignored(2);

	private int intValue;
	private static java.util.HashMap<Integer, IncludeOwnerInFolderNamePolicy> mappings;

	private static java.util.HashMap<Integer, IncludeOwnerInFolderNamePolicy> getMappings() {
		if (mappings == null) {
			synchronized (IncludeOwnerInFolderNamePolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, IncludeOwnerInFolderNamePolicy>();
				}
			}
		}
		return mappings;
	}

	private IncludeOwnerInFolderNamePolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static IncludeOwnerInFolderNamePolicy forValue(int value) {
		return getMappings().get(value);
	}
}