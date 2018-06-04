package entities;

import java.io.Serializable;

public enum ShareWithNewParticipantOption implements Serializable {
	/**
	 * Create external accounts, not associated with business account, for new
	 * collaborators
	 */
	CreateExternalAccount(0),

	/**
	 * Add new collaborators to business account
	 */
	CreateInternalAccount(1);

	private int intValue;
	private static java.util.HashMap<Integer, ShareWithNewParticipantOption> mappings;

	private static java.util.HashMap<Integer, ShareWithNewParticipantOption> getMappings() {
		if (mappings == null) {
			synchronized (ShareWithNewParticipantOption.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, ShareWithNewParticipantOption>();
				}
			}
		}
		return mappings;
	}

	private ShareWithNewParticipantOption(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static ShareWithNewParticipantOption forValue(int value) {
		return getMappings().get(value);
	}
}