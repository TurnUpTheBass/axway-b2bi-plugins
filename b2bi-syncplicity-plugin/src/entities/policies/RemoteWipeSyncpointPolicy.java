package entities.policies;

import java.io.Serializable;

/**
 * An enumeration of possible remote wipe syncpoint policies for a company
 */
public enum RemoteWipeSyncpointPolicy implements Serializable {
	/**
	 * Default value
	 */
	Unknown(0),

	/**
	 * Don't remote wipe a syncpoint when a user looses access
	 */
	Disabled(1),

	/**
	 * Remote wipe a syncpoint when a user looses access
	 */
	Enabled(2);

	private int intValue;
	private static java.util.HashMap<Integer, RemoteWipeSyncpointPolicy> mappings;

	private static java.util.HashMap<Integer, RemoteWipeSyncpointPolicy> getMappings() {
		if (mappings == null) {
			synchronized (RemoteWipeSyncpointPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, RemoteWipeSyncpointPolicy>();
				}
			}
		}
		return mappings;
	}

	private RemoteWipeSyncpointPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static RemoteWipeSyncpointPolicy forValue(int value) {
		return getMappings().get(value);
	}
}