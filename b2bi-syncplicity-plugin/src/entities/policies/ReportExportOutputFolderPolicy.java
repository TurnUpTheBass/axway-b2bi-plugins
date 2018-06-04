package entities.policies;

import java.io.Serializable;

/**
 * An enumeration of possible report export output policies for a company
 */
public enum ReportExportOutputFolderPolicy implements Serializable {
	/**
	 * Default policy
	 */
	Unknown(0),

	/**
	 * Export to single virtual folder
	 */
	SingleVirtualFolder(1),

	/**
	 * Use separate virtual folder for each report type
	 */
	SeparateVirtualFoldersByReportType(2);

	private int intValue;
	private static java.util.HashMap<Integer, ReportExportOutputFolderPolicy> mappings;

	private static java.util.HashMap<Integer, ReportExportOutputFolderPolicy> getMappings() {
		if (mappings == null) {
			synchronized (ReportExportOutputFolderPolicy.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, ReportExportOutputFolderPolicy>();
				}
			}
		}
		return mappings;
	}

	private ReportExportOutputFolderPolicy(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	public int getValue() {
		return intValue;
	}

	public static ReportExportOutputFolderPolicy forValue(int value) {
		return getMappings().get(value);
	}
}