package entities;

import java.io.Serializable;

public enum CompanyAccountType implements Serializable {
	Unknown(0),

	BusinessEdition(1),

	BusinessEditionWithPremiumSupport(2),

	EnterpriseEdition(3),

	AccessEdition(4),

	DepartmentEdition(5);

	private int intValue;
	private static java.util.HashMap<Integer, CompanyAccountType> mappings;
	private static java.util.HashMap<Integer, CompanyAccountType> getMappings() {
		if (mappings == null) {
			synchronized (CompanyAccountType.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, CompanyAccountType>();
				}
			}
		}
		return mappings;
	}

	private CompanyAccountType(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static CompanyAccountType forValue(int value) {
		return getMappings().get(value);
	}
}