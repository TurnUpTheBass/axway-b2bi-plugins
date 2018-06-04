package entities;

import java.io.Serializable;

public enum PaymentMethod implements Serializable{
	// A type wasn't provided.
	Unknown(0),

	CreditCard(1),

	PurchaseOrder(2),

	DebitCard(3);

	private int intValue;
	private static java.util.HashMap<Integer, PaymentMethod> mappings;
	private static java.util.HashMap<Integer, PaymentMethod> getMappings() {
		if (mappings == null) {
			synchronized (PaymentMethod.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, PaymentMethod>();
				}
			}
		}
		return mappings;
	}

	private PaymentMethod(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static PaymentMethod forValue(int value) {
		return getMappings().get(value);
	}
}