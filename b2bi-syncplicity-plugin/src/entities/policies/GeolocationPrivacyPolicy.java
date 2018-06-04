package entities.policies;

import java.io.Serializable;

public enum GeolocationPrivacyPolicy implements Serializable {
	Unknown(0),

	/**
	 * Disallow geo-location tracking
	 */
	Disallow(1),

	/**
	 * Allow the ability to track geo-location information
	 */
	Allow(2);

	private int intValue;
	private static java.util.HashMap<Integer, GeolocationPrivacyPolicy> mappings;

	private static java.util.HashMap<Integer, GeolocationPrivacyPolicy> getMappings() {
		if (mappings == null) {
			synchronized (GeolocationPrivacyPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, GeolocationPrivacyPolicy>();
				}
			}
		}
		return mappings;
	}

	private GeolocationPrivacyPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static GeolocationPrivacyPolicy forValue(int value) {
		return getMappings().get(value);
	}
}