package entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Quota implements Serializable {
	public long ActiveBytes;

	public long AvailableBytes;

	public String StorageEndpointId;

	public long PreviousVersionBytes;

	public long DeletedBytes;

	public String UserId;

	public QuotaOwnership QuotaOwnership = entities.QuotaOwnership
			.values()[0];

	public QuotaAllocationType QuotaAllocationType = entities.QuotaAllocationType
			.values()[0];

	public long StorageCapacityInBytes;
}