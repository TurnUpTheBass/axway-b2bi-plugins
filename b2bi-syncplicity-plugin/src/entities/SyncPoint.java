package entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SyncPoint implements Serializable {
	public int Id;

	public SyncPointType Type = SyncPointType.values()[0];

	public String Name;

	public String RootFolderId;

	public boolean Mapped;

	public String Path;

	public boolean DownloadEnabled;

	public boolean UploadEnabled;

	public boolean Shared;

	public User Owner;

	public SharingPermission Permission;

	public Participant[] Participants;

	public Mapping[] Mappings;

	public SyncPointPolicy Policy;

	public boolean RemoteWipe;

	public String StorageEndpointId;

	public SyncPoint Parent;

	public SyncPoint[] Children;

	public String PathToRoot;

	public User Inviter;

	public int ServerId;

	public Server.Status ServerStatus = Server.Status.values()[0];

	public String toString() {
		return String.format("Syncpoint: Name: '%1$s', Id: %2$s", Name, Id);
	}
}