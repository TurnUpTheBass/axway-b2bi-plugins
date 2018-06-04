package entities.policies;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AdminIpRestriction implements Serializable {
	public String AllowedIPs;

	public String Message;
}