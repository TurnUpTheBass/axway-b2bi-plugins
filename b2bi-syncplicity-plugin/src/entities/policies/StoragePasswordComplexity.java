package entities.policies;

import java.io.Serializable;


@SuppressWarnings("serial")
public class StoragePasswordComplexity implements Serializable {
	public PasswordComplexityOptions Options = PasswordComplexityOptions
			.values()[0];

	public int MinimumLength;

	public Integer MaximumLength = null;
}