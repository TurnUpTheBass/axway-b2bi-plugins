package entities.policies;

import java.io.Serializable;

public enum BranchingPolicy implements Serializable {
	// <summary>
	/**
	 * Default value
	 */
	Unknown(0),
	/**
	 * Don't branch any files
	 */
	Disabled(1),
	/**
	 * Branch all files
	 */
	All(2);

	private int intValue;
	private static java.util.HashMap<Integer, BranchingPolicy> mappings;

	private static java.util.HashMap<Integer, BranchingPolicy> getMappings() {
		if (mappings == null) {
			synchronized (BranchingPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, BranchingPolicy>();
				}
			}
		}
		return mappings;
	}

	private BranchingPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static BranchingPolicy forValue(int value) {
		return getMappings().get(value);
	}
}