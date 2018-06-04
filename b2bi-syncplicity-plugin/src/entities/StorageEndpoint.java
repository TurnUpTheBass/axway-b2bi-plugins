package entities;

import java.io.Serializable;


@SuppressWarnings("serial")
public class StorageEndpoint implements Serializable {
	public String Id;

	public String Name;

	public String Description;

	public String CompanyId;

	public boolean Active;

	public StorageEndpointUrl[] Urls;

	public StorageEndpointAuth[] Auths;

	public int Version;

	public int SizeGb;

	public boolean RequiresStorageAuthentication;

	public boolean UserHasStoragePassword;

	public Company Company;

	public boolean Default;

	public java.math.BigDecimal ConsumedGb = new java.math.BigDecimal(0);
}