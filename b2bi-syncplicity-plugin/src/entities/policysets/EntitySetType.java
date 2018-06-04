package entities.policysets;

import java.io.Serializable;

public enum EntitySetType implements Serializable {
	// <summary>
	/**
	 * Default value
	 */
	Unknown(0),
	/**
	 * Entity set is PolicySet
	 */
	PolicySet(1),
	/**
	 * Entity set is StorageSet
	 */
	StorageSet(2),
	/**
	 * Entity set is HomeDirectorySet
	 */
	HomeDirectorySet(3);

	private int intValue;
	private static java.util.HashMap<Integer, EntitySetType> mappings;

	private static java.util.HashMap<Integer, EntitySetType> getMappings() {
		if (mappings == null) {
			synchronized (EntitySetType.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, EntitySetType>();
				}
			}
		}
		return mappings;
	}

	private EntitySetType(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static EntitySetType forValue(int value) {
		return getMappings().get(value);
	}
}