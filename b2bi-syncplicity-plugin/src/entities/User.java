package entities;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class User implements Serializable {
	public String Id;

	public String EmailAddress;

	public String FirstName;

	public String LastName;

	public String Password;

	public Boolean Disabled = null;

	public UserAccountType AccountType = entities.UserAccountType.values()[0];

	public UserAccountStatus Status = entities.UserAccountStatus.values()[0];

	public Integer[] Roles;

	public Date LastLoginDateUtc = null;

	public Date CreatedDateUtc;
}