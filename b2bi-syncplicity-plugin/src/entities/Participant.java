package entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Participant implements Serializable {
	public int SyncPointId;

	public User User;

	public User Inviter;

	public SharingPermission Permission = SharingPermission.values()[0];

	public boolean Mapped;

	public String SharingInviteNote;

	public ShareWithNewParticipantOption NewParticipantOption = ShareWithNewParticipantOption
			.values()[0];
}