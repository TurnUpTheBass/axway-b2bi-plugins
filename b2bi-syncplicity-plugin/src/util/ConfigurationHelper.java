package util;

import java.io.IOException;
import java.io.InputStream;

public class ConfigurationHelper {

	/* Should not need to edit these values */
	
	private final static String OAUTH_AUTORIZATION_URL  = "https://api.syncplicity.com/oauth/authorize";
	private final static String OAUTH_TOKEN_URL         = "https://api.syncplicity.com/oauth/token";
	private final static String OAUTH_REFRESH_TOKEN_URL = "https://api.syncplicity.com/oauth/token";
	private final static String OAUTH_REVOKE_TOKEN_URL  = "https://api.syncplicity.com/oauth/revoke";
	private final static String CONSUMER_REDIRECT_URL   = "https://api.syncplicity.com/oauth/callback";
	private final static String BASE_API_ENDPOINT       = "https://api.syncplicity.com/";
	
	private final static String SIMPLE_PASSWORD = "123123aA";
	private final static String REPORTS_FOLDER  = "Syncplicity Reports"; //Default system name, should not need to change
     private final static String SYNCPOINT_NAME  = "SampleAppSyncpoint-";
    private final static String FOLDER_NAME     = "SampleAppFolder-";
    private final static String REPORT_NAME     = "SampleAppReportStorageByUser-";
    
	private static java.util.Properties settings = null;


	// Return the Oauth url for beginning the authentication
	// process of the app's connection to the api gateway
	public static String getOAuthAutorizationUrl() {
		return OAUTH_AUTORIZATION_URL;
	}

	// Url for retrieving the OAuth token associated with
	// this app's current session with the api gateway
	public static String getOAuthTokenUrl() {
		return OAUTH_TOKEN_URL;
	}

	// Url for retrieving the OAuth token from a previous
	// refresh-token.
	public static String getOAuthRefreshTokenUrl() {
		return OAUTH_REFRESH_TOKEN_URL;
	}

	// URL for loggin the application out of the api
	// gateway and explicitly invalidating the OAuth 
	// token
	public static String getOAuthRevokeTokenUrl() {
		return OAUTH_REVOKE_TOKEN_URL;
	}


	// Returns the url for the api gateway for the
	// callback url.  This is not needed for simple
	// applications
	public static String getConsumerRedirectUrl() {
		return CONSUMER_REDIRECT_URL;
	}


	// Returns the base url of the api gateway
	public static String getBaseApiEndpointUrl() {
		return BASE_API_ENDPOINT;
	}

	// Returns a simple password used for the reporting service
	public static String getSimplePassword() {
		return SIMPLE_PASSWORD;
	}
}
