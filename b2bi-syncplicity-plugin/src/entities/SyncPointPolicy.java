package entities;

import java.io.Serializable;

import entities.policies.DesktopShareLinkFlowPolicy;
import entities.policies.RemoteWipeSyncpointPolicy;
import entities.policies.ShareLinkExpirationPolicy;
import entities.policies.ShareLinkPasswordComplexity;
import entities.policies.ShareLinkPasswordProtectedPolicy;
import entities.policies.ShareLinkPolicy;
import entities.policies.SharedFolderResharingPolicy;
import entities.policies.StorageCookiePersistancePolicy;
import entities.policies.StoragePasswordComplexity;
import entities.policies.StoragePasswordComplexityPolicy;
import entities.policies.StoragePasswordPolicy;

@SuppressWarnings("serial")
public class SyncPointPolicy implements Serializable {
	public RemoteWipeSyncpointPolicy RemoteWipeSyncpointPolicy = entities.policies.RemoteWipeSyncpointPolicy.Unknown;

	public SharedFolderResharingPolicy ResharingPolicy = entities.policies.SharedFolderResharingPolicy.Unknown;

	public ShareLinkPolicy ShareLinkPolicy = entities.policies.ShareLinkPolicy.Unknown;

	public ShareLinkPasswordProtectedPolicy ShareLinkPasswordProtectedPolicy = entities.policies.ShareLinkPasswordProtectedPolicy.Unknown;

	public ShareLinkPasswordComplexity ShareLinkPasswordComplexity = entities.policies.ShareLinkPasswordComplexity.Unknown;

	public int ShareLinkPasswordLength;

	public ShareLinkExpirationPolicy ShareLinkExpirationPolicy;

	public int ShareLinkExpireInDays;

	public StoragePasswordPolicy StoragePasswordPolicy = entities.policies.StoragePasswordPolicy.Enabled;

	public StoragePasswordComplexityPolicy StoragePasswordComplexityPolicy = entities.policies.StoragePasswordComplexityPolicy.Unknown;

	public StoragePasswordComplexity StoragePasswordComplexity = new StoragePasswordComplexity();

	public StorageCookiePersistancePolicy StorageCookiePersistancePolicy = entities.policies.StorageCookiePersistancePolicy.Unknown;

	public Integer StorageCookiePersistancePolicyLength = null;

	public DesktopShareLinkFlowPolicy DesktopShareLinkFlowPolicy = entities.policies.DesktopShareLinkFlowPolicy.Unknown;
}