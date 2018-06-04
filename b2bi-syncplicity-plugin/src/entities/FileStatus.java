package entities;

import java.io.Serializable;

public enum FileStatus implements Serializable {
	None(0),

	Added(1),

	Updated(2),

	Removed(3),

	Ignored(4),

	ConfirmedRemoved(5);

	private int intValue;
	private static java.util.HashMap<Integer, FileStatus> mappings;
	private static java.util.HashMap<Integer, FileStatus> getMappings() {
		if (mappings == null) {
			synchronized (FileStatus.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, FileStatus>();
				}
			}
		}
		return mappings;
	}

	private FileStatus(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static FileStatus forValue(int value) {
		return getMappings().get(value);
	}
}