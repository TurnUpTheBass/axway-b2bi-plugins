package entities.policies;

import java.io.Serializable;

/**
 * An enumeration of possible mobile unencrypted sync policies for a company
 */
public enum MobileUnencryptedSyncPolicy implements Serializable {
	/**
	 * Default Policy
	 */
	Unknown(0),
	/**
	 * Disallow unencrypted sync and require users to only sync to an encrypted
	 * cache managed by Syncplicity
	 */
	Disabled(1),
	/**
	 * Allow users to synchronize files and folders to an SD card where they
	 * reside unencrypted
	 */
	Enabled(2);

	private int intValue;
	private static java.util.HashMap<Integer, MobileUnencryptedSyncPolicy> mappings;

	private static java.util.HashMap<Integer, MobileUnencryptedSyncPolicy> getMappings() {
		if (mappings == null) {
			synchronized (MobileUnencryptedSyncPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, MobileUnencryptedSyncPolicy>();
				}
			}
		}
		return mappings;
	}

	private MobileUnencryptedSyncPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static MobileUnencryptedSyncPolicy forValue(int value) {
		return getMappings().get(value);
	}
}