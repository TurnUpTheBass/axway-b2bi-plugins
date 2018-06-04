package oauth;

import util.APIContext;
import util.APIGateway;
import util.ConfigurationHelper;


public class OAuth
	extends APIGateway {
	
	String OauthParameters[] = new String[3];

	
	
			
	public static void authenticate() {
		
		String authorizationUrl = ConfigurationHelper.getOAuthTokenUrl();
		String params           = "grant_type=client_credentials";
		
		TokenResponse tokenResponse = httpPost(true, authorizationUrl, "application/x-www-form-urlencoded", params, TokenResponse.class);
		
		APIContext.setOAuthResponse(tokenResponse);
	}
	
	//This call will invalidate the current oauth 
	//and any refresh-tokens along with removing
	//the grant of access to the application to 
	//the given user account.   
	
	public static void revokeToken() {
		
		String revokeUrl = ConfigurationHelper.getOAuthRevokeTokenUrl();
			
		TokenResponse tokenResponse = httpGet( revokeUrl, TokenResponse.class, false);
		
		APIContext.setOAuthResponse(tokenResponse);
	}
	
	public static void refreshToken() {
		
		//Note: technically refreshToken() which uses grant_type=client_credentials is the same
		//      behavior as just authenticating authenticate() for the first time.  The name is 
		//      just to be explicit in the use-case
		authenticate();
	}
}
