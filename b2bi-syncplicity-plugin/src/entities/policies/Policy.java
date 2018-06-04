package entities.policies;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Policy implements Serializable {
	public ClientAutoUpdatePolicy ClientAutoUpdatePolicy = entities.policies.ClientAutoUpdatePolicy.values()[0];

	public ShareableLinkPolicy ShareableLinkPolicy = entities.policies.ShareableLinkPolicy.values()[0];

	public ClientPreconfiguredPolicy ClientPreconfiguredPolicy = entities.policies.ClientPreconfiguredPolicy.values()[0];

	public SharedFolderPolicy SharedFolderPolicy = entities.policies.SharedFolderPolicy.values()[0];

	public IncludeOwnerInFolderNamePolicy IncludeOwnerInFolderNamePolicy = entities.policies.IncludeOwnerInFolderNamePolicy.values()[0];

	public RemoteWipeSyncpointPolicy RemoteWipeSyncpointPolicy = entities.policies.RemoteWipeSyncpointPolicy.Unknown;

	public MobileSyncPolicy MobileSyncPolicy = entities.policies.MobileSyncPolicy.values()[0];

	public MobileUnencryptedSyncPolicy MobileUnencryptedSyncPolicy = entities.policies.MobileUnencryptedSyncPolicy.values()[0];

	public MobileSyncLimitsPolicy MobileSyncLimitsPolicy = entities.policies.MobileSyncLimitsPolicy.values()[0];

	public MobileDataSyncLimit MobileDataSyncLimit = new MobileDataSyncLimit();

	public ShareLinkPasswordProtectedPolicy ShareLinkPasswordProtectedPolicy = entities.policies.ShareLinkPasswordProtectedPolicy.values()[0];

	public ShareLinkPasswordComplexity ShareLinkPasswordComplexity = entities.policies.ShareLinkPasswordComplexity.values()[0];

	public int ShareLinkPasswordLength;

	public ShareLinkExpirationPolicy ShareLinkExpirationPolicy = entities.policies.ShareLinkExpirationPolicy.values()[0];

	public int ShareLinkExpireInDays;

	// old clients before implementing VRI feature use ShareableLinkPolicy
	// new clients that support VRI use ShareLinkPolicy
	public ShareLinkPolicy ShareLinkPolicy = entities.policies.ShareLinkPolicy.values()[0];

	public DesktopShareLinkFlowPolicy DesktopShareLinkFlowPolicy = entities.policies.DesktopShareLinkFlowPolicy.values()[0];

	public ReportExportOutputFolderPolicy ReportExportOutputFolderPolicy = entities.policies.ReportExportOutputFolderPolicy.values()[0];

	public AdminIpRestrictionPolicy AdminIpRestrictionPolicy = entities.policies.AdminIpRestrictionPolicy.values()[0];

	public AdminIpRestriction AdminIpRestriction = new AdminIpRestriction();

	public AdminPasswordComplexityPolicy AdminPasswordComplexityPolicy = entities.policies.AdminPasswordComplexityPolicy.values()[0];

	public AdminPasswordComplexity AdminPasswordComplexity = new AdminPasswordComplexity();

	public RestrictMobileAccessPolicy RestrictMobileAccessPolicy = entities.policies.RestrictMobileAccessPolicy.values()[0];

	public RestrictWebsiteAccessPolicy RestrictWebsiteAccessPolicy = entities.policies.RestrictWebsiteAccessPolicy.values()[0];

	public RssNewsFeedPolicy RssNewsFeedPolicy = entities.policies.RssNewsFeedPolicy.values()[0];

	public BranchingPolicy BranchingPolicy = entities.policies.BranchingPolicy.values()[0];

	public RemoteWipeUsersPolicy RemoteWipeUsersPolicy = entities.policies.RemoteWipeUsersPolicy.values()[0];

	public RemoteWipeEndpointPolicy RemoteWipeEndpointPolicy = entities.policies.RemoteWipeEndpointPolicy.values()[0];

	public EnforceClientMemberActiveDirectoryPolicy EnforceClientMemberActiveDirectoryPolicy = entities.policies.EnforceClientMemberActiveDirectoryPolicy.values()[0];

	public String EnforceClientMemberActiveDirectoryPolicyDomains;

	public String DefaultStorageEndpointId;

	public PasscodeEnforcementPolicy PasscodeEnforcementPolicy = entities.policies.PasscodeEnforcementPolicy.values()[0];

	public PasscodeFailurePolicy PasscodeFailurePolicy = entities.policies.PasscodeFailurePolicy.values()[0];

	public PasscodeFailures PasscodeFailures = new PasscodeFailures();

	public OpenInPolicy OpenInPolicy = entities.policies.OpenInPolicy.values()[0];

	public SharedFolderResharingPolicy SharedFolderResharingPolicy = entities.policies.SharedFolderResharingPolicy.values()[0];

	public GeolocationPrivacyPolicy GeolocationPrivacyPolicy = entities.policies.GeolocationPrivacyPolicy.values()[0];

	public HomeDirectoryPolicy HomeDirectoryPolicy;

	public FileExclusionPolicy FileExclusionPolicy = entities.policies.FileExclusionPolicy.values()[0];

	public FileExclusionType[] ExclusionTypes;
}