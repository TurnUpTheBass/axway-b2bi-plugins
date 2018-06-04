package entities.policies;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PasswordComplexity implements Serializable {
	public PasswordComplexityOptions Options = PasswordComplexityOptions.values()[0];

	public String[] RestrictedPasswords;

	public Integer MinimumLength = null;

	public Integer MaximumLength = null;
}