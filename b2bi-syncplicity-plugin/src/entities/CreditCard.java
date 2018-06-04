package entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CreditCard  implements Serializable{
	public String Name;

	public String Address;

	public String City;

	public String Country;

	public String StateOrProvince;

	public String ZipOrPostalCode;

	public String Number;

	public String VerificationNumber;

	public String ExpirationMonth;

	public String ExpirationYear;
}