package entities;

import java.io.Serializable;

import entities.policysets.PolicySet;

@SuppressWarnings("serial")
public class Group implements Serializable {
	public String Id;

	public String Name;

	public Company Company;

	public User Owner;

	public User[] Members;

	public StorageQuota[] StorageQuotas;

	public PolicySet PolicySet;

	public PolicySet[] PolicySets;

	public SyncPoint[] Syncpoints;

}