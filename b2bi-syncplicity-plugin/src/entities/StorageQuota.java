package entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class StorageQuota implements Serializable {
	public String StorageEndpointId;

	public int StorageQuotaGb;

	public StorageEndpoint StorageEndpoint;
}