package entities;

import java.io.Serializable;

public enum FolderStatus implements Serializable {
	None(0),

	Added(1),

	Removed(4),

	ConfirmedRemoved(5);

	private int intValue;
	private static java.util.HashMap<Integer, FolderStatus> mappings;
	private static java.util.HashMap<Integer, FolderStatus> getMappings() {
		if (mappings == null) {
			synchronized (FolderStatus.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, FolderStatus>();
				}
			}
		}
		return mappings;
	}

	private FolderStatus(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static FolderStatus forValue(int value) {
		return getMappings().get(value);
	}
}