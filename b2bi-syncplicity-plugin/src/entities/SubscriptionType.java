package entities;

import java.io.Serializable;

/** 
 User Account Type
 */
public enum SubscriptionType implements Serializable {
	// A type wasn't provided.
	Unknown(0),

	/** 
     Subscription type includes Free and Paid Personal subscriptions for now
	 */
	PersonalEdition(1),

	/** 
     Subscription type includes BT, Paid Business
	 */
	BusinessEdition(2),

	/** 
     Subscription type includes ET, Paid Enterprise
	 */
	EnterpriseEdition(3),

	DepartmentEdition(4);

	private int intValue;
	private static java.util.HashMap<Integer, SubscriptionType> mappings;
	private static java.util.HashMap<Integer, SubscriptionType> getMappings() {
		if (mappings == null) {
			synchronized (SubscriptionType.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, SubscriptionType>();
				}
			}
		}
		return mappings;
	}

	private SubscriptionType(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static SubscriptionType forValue(int value) {
		return getMappings().get(value);
	}
}