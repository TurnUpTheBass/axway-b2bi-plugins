package entities.policies;

import java.io.Serializable;

public enum DesktopShareLinkFlowPolicy implements Serializable {
	/**
	 * Default policy
	 */
	Unknown(0),

	/**
	 * Display the share link directly in place
	 */
	InPlace(1),

	/**
	 * Take the user to My site
	 */
	RedirectToMySite(2);

	private int intValue;
	private static java.util.HashMap<Integer, DesktopShareLinkFlowPolicy> mappings;

	private static java.util.HashMap<Integer, DesktopShareLinkFlowPolicy> getMappings() {
		if (mappings == null) {
			synchronized (DesktopShareLinkFlowPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, DesktopShareLinkFlowPolicy>();
				}
			}
		}
		return mappings;
	}

	private DesktopShareLinkFlowPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static DesktopShareLinkFlowPolicy forValue(int value) {
		return getMappings().get(value);
	}
}