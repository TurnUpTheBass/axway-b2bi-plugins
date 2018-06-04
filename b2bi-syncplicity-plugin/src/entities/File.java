package entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class File implements Serializable {
	public int SyncpointId;

	public String FileId;

	public String VirtualPath;

	public String Filename;

	public long Length;

	public String Hash;

	public String CreationTimeUtc;

	public String LastWriteTimeUtc;

	public byte SyncPriority;

	public FileStatus Status = FileStatus.values()[0];

	public String LatestVersionId;

	public FileVersion[] Versions;

	public boolean Stored;

	public String ThumbnailUrl;

	public String FolderId;

	/** 
     Returns a logging-friendly string

     @return 
     A <see cref="System.String"/>

	 */
	public final String ToLoggingString() {
		return String.format("Virtual Path: %1$s, Filename: %2$s, Status: %3$s, VirtualFolderId: %4$s, LatestVersionId: %5$s", VirtualPath, Filename, Status.toString(), SyncpointId, LatestVersionId);
	}
}