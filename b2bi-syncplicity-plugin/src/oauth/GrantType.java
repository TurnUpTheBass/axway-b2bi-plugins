package oauth;

public enum GrantType {
	AuthorizationCode(1, "code"),
	Implicit(2, "token"),
	Password(3, "");

	private final int grantType;
	private final String responseType;
	
	private GrantType(final int grantType, String responseType ) {
        this.grantType    = grantType;
        this.responseType = responseType;
    }

    public int getGrantType() { 
    	return grantType; 
    }
    
    public String getResponseType() {
    	return responseType;
    }
}