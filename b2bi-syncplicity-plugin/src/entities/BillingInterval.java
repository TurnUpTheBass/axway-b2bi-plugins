package entities;

import java.io.Serializable;

/** 
 Billing period (Monthly, Yearly). 
 Please note that enum values DO NOT mean specific number of years, month, days or anything else.
 */
public enum BillingInterval implements Serializable {
	// A type wasn't provided.
	Unknown(0),

	Monthly(1),

	Yearly(2);

	private int intValue;
	private static java.util.HashMap<Integer, BillingInterval> mappings;
	private static java.util.HashMap<Integer, BillingInterval> getMappings() {
		if (mappings == null) {
			synchronized (BillingInterval.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, BillingInterval>();
				}
			}
		}
		return mappings;
	}

	private BillingInterval(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static BillingInterval forValue(int value) {
		return getMappings().get(value);
	}
}