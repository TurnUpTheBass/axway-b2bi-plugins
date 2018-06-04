package entities.policies;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AdminPasswordComplexity implements Serializable {

	public PasswordComplexityOptions AdminPasswordComplexityOptions = PasswordComplexityOptions
			.values()[0];

	public int AdminPasswordComplexityMinimumLength;

	public Integer AdminPasswordComplexityMaximumLength = null;
}