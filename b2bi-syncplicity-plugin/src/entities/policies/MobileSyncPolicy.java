package entities.policies;

import java.io.Serializable;

/**
 * An enumeration of possible mobile sync policies for a company
 */
public enum MobileSyncPolicy implements Serializable {
	/**
	 * Default policy
	 */
	Unknown(0),
	/**
	 * Users can synchronize files and folders to their iOS and Android devices
	 * on any network
	 */
	All(1),
	/**
	 * Users can synchronize files and folders to their iOS and Android devices,
	 * but only when the device is connected to a Wi-Fi hotspot (synchronization
	 * on 3G/4G/WiMax is disallowed)
	 */
	WiFiOnly(2),
	/**
	 * Users can synchronize files and folders to their iOS and Android devices,
	 * but only when the device is connected to a home network (i.e. not
	 * roaming) or a Wi-Fi hotspot
	 */
	WiFiCellular(3),
	/**
	 * Automatic synchronization is disabled on all mobile devices
	 */
	Disabled(4);

	private int intValue;
	private static java.util.HashMap<Integer, MobileSyncPolicy> mappings;

	private static java.util.HashMap<Integer, MobileSyncPolicy> getMappings() {
		if (mappings == null) {
			synchronized (MobileSyncPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, MobileSyncPolicy>();
				}
			}
		}
		return mappings;
	}

	private MobileSyncPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static MobileSyncPolicy forValue(int value) {
		return getMappings().get(value);
	}
}