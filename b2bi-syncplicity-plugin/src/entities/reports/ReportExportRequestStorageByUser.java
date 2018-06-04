package entities.reports;

import java.io.Serializable;

import entities.User;

@SuppressWarnings("serial")
public class ReportExportRequestStorageByUser extends ReportExportRequestBase
		implements Serializable {
	public User User;

	public boolean IncludeActive;

	public boolean IncludeDisabled;
}