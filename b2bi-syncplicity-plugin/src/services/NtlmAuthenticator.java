package services;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class NtlmAuthenticator extends Authenticator {

	private final String username;
	private final char[] password;

	public NtlmAuthenticator(final String username, final String password) {
	    super();
	    this.username = username;
	    this.password = password.toCharArray(); 
	}	
	public PasswordAuthentication getPasswordAuthentication() {
        // I haven't checked getRequestingScheme() here, since for NTLM
        // and Negotiate, the usrname and password are all the same.
        System.err.println("Feeding username and password for " + getRequestingScheme());
        return (new PasswordAuthentication(username, password));
    }
}