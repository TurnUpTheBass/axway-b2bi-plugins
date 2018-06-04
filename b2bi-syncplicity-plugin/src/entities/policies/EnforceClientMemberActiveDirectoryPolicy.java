package entities.policies;

import java.io.Serializable;

public enum EnforceClientMemberActiveDirectoryPolicy implements Serializable {
	/**
	 * Default policy.
	 */
	Unknown(0),

	/**
	 * Policy is disabled.
	 */
	Disabled(1),

	/**
	 * All clients on platforms like Windows must be members of an active
	 * directory.
	 */
	Liberal(2),

	/**
	 * All clients, even those on platforms like OSX that don’t support active
	 * directory, must be joined to a known active directory.
	 */
	Strict(3);

	private int intValue;
	private static java.util.HashMap<Integer, EnforceClientMemberActiveDirectoryPolicy> mappings;

	private static java.util.HashMap<Integer, EnforceClientMemberActiveDirectoryPolicy> getMappings() {
		if (mappings == null) {
			synchronized (EnforceClientMemberActiveDirectoryPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, EnforceClientMemberActiveDirectoryPolicy>();
				}
			}
		}
		return mappings;
	}

	private EnforceClientMemberActiveDirectoryPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static EnforceClientMemberActiveDirectoryPolicy forValue(int value) {
		return getMappings().get(value);
	}
}