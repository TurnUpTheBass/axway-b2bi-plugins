package entities;

import java.io.Serializable;
import entities.UserEssentials;

@SuppressWarnings("serial")

public class FileVersionDetails implements Serializable {
	public int SyncpointId;
	public long Id;
	public String UserName;
	public String DataSourceName;
	public int Action;
	public long Length;
	public String RevisionAge;
	public UserEssentials User;
	private String DateAddedUtc;
}

