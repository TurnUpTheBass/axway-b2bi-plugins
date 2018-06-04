package entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FileVersion implements Serializable {
	public int SyncpointId;

	public int Id;

	public String UserName;

	public String DataSourceName;

	public int Action;

	public long Length;

	public String RevisionAge;
}