package entities;

import java.io.Serializable;

public enum SharingPermission implements Serializable {
	None(0),

	ReadWrite(1),

	Contributor(2),

	ReadOnly(3);

	private int intValue;
	private static java.util.HashMap<Integer, SharingPermission> mappings;

	private static java.util.HashMap<Integer, SharingPermission> getMappings() {
		if (mappings == null) {
			synchronized (SharingPermission.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, SharingPermission>();
				}
			}
		}
		return mappings;
	}

	private SharingPermission(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static SharingPermission forValue(int value) {
		return getMappings().get(value);
	}
}