package entities;

public enum FileBinaryDataBodyTag {
	FileData(0), File(1);

	private int intValue;
	private static java.util.HashMap<Integer, FileBinaryDataBodyTag> mappings;

	private static java.util.HashMap<Integer, FileBinaryDataBodyTag> getMappings() {
		if (mappings == null) {
			synchronized (FileBinaryDataBodyTag.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, FileBinaryDataBodyTag>();
				}
			}
		}
		return mappings;
	}

	private FileBinaryDataBodyTag(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static FileBinaryDataBodyTag forValue(int value) {
		return getMappings().get(value);
	}
}