package entities;

import java.io.Serializable;
import java.util.Date;


import entities.CompanyAccountType;


@SuppressWarnings("serial")
public class Company implements Serializable {
	public String Id;

	public String Name;

	public User Owner;

	public String Address1;

	public String Address2;

	public String Address3;

	public String City;

	public String State;

	public String ZipCode;

	public byte CountryId;

	public String Phone1;

	public String Phone2;

	public String Phone3;

	public int Storage;

	public int Seats;

	public SubscriptionType SubscriptionPlan;

	public CompanyAccountType AccountType = CompanyAccountType.values()[0];

	public int SeatsUsed;

	public java.math.BigDecimal StorageUsed = new java.math.BigDecimal(0);

	public Date TrialEndDateUtc = null;
}