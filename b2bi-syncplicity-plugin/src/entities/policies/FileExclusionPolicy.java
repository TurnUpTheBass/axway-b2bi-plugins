package entities.policies;

import java.io.Serializable;

public enum FileExclusionPolicy implements Serializable {
	/**
	 * Default policy.
	 */
	Unknown(0),

	/**
	 * File Exclusion Policy is disabled.
	 */
	Disabled(1),

	/**
	 * Exclusions are applied.
	 */
	Enabled(2);

	private int intValue;
	private static java.util.HashMap<Integer, FileExclusionPolicy> mappings;

	private static java.util.HashMap<Integer, FileExclusionPolicy> getMappings() {
		if (mappings == null) {
			synchronized (FileExclusionPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, FileExclusionPolicy>();
				}
			}
		}
		return mappings;
	}

	private FileExclusionPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static FileExclusionPolicy forValue(int value) {
		return getMappings().get(value);
	}
}