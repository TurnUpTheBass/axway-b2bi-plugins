package entities;

import java.io.Serializable;

public enum SyncPointType 
	implements Serializable {
	Unknown(0),

	MyDocuments(1),

	MyMusic(2),

	MyPictures(3),

	Desktop(4),

	Favorites(5),

	Custom(6);

	private int intValue;
	private static java.util.HashMap<Integer, SyncPointType> mappings;

	private static java.util.HashMap<Integer, SyncPointType> getMappings() {
		if (mappings == null) {
			synchronized (SyncPointType.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, SyncPointType>();
				}
			}
		}
		return mappings;
	}

	private SyncPointType(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static SyncPointType forValue(int value) {
		return getMappings().get(value);
	}
}