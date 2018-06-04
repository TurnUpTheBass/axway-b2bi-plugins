package entities.policies;

import java.io.Serializable;

public enum PasswordComplexityOptions implements Serializable {
	Unknown(0), Number(1), Lower(2), Upper(4), SpecialCharacter(8), NumberOrSpecialCharacter(
			16), NotDictionaryWord(32);

	private int intValue;
	private static java.util.HashMap<Integer, PasswordComplexityOptions> mappings;

	private static java.util.HashMap<Integer, PasswordComplexityOptions> getMappings() {
		if (mappings == null) {
			synchronized (PasswordComplexityOptions.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, PasswordComplexityOptions>();
				}
			}
		}
		return mappings;
	}

	private PasswordComplexityOptions(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static PasswordComplexityOptions forValue(int value) {
		return getMappings().get(value);
	}
}