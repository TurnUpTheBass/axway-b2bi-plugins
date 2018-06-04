package entities;

import java.io.Serializable;
import java.util.Date;


@SuppressWarnings("serial")
public class Link implements Serializable {
	public String Token;

	public int SyncPointId;

	public String VirtualPath;

	public String LandingPageUrl;

	public String DownloadUrl;

	public String Password;

	public String Message;

	public File File;

	public Date SharedDateUtc;

	public int NumDownloads;

	public User[] Users;

	public LinkUsage[] LinkUsage;

	public int LinkExpireInDays;

	public String StorageSignature;

	public String DownloadToken;

	public String ThumbnailName;

	public int LinkExpirationPolicy;

	public int ShareLinkPolicy;

	public int PasswordProtectPolicy;
}