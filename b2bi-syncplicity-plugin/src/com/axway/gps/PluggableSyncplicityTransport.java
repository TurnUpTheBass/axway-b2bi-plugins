/**==============================================================================
 * Program/Module :     PluggableSyncplicityTransport.java
 * Description :       	Pluggable transport that implements the Syncplicity APIs
 * Supported Products :	B2Bi 2.x
 * Author :				Bas van den Berg
 * Copyright :          Axway
 *==============================================================================
 * HISTORY
 * 20180506 bvandenberg	1.0.0	initial version.
 *==============================================================================*/

package com.axway.gps;

import com.cyclonecommerce.tradingengine.transport.UnableToConnectException;
import com.cyclonecommerce.tradingengine.transport.UnableToAuthenticateException;
import com.cyclonecommerce.tradingengine.transport.UnableToConsumeException;
import com.cyclonecommerce.tradingengine.transport.UnableToProduceException;
import com.cyclonecommerce.api.inlineprocessing.Message;
import com.cyclonecommerce.collaboration.MetadataDictionary;
import com.cyclonecommerce.tradingengine.transport.FileNotFoundException;
import com.cyclonecommerce.tradingengine.transport.UnableToDeleteException;
import com.cyclonecommerce.tradingengine.transport.UnableToDisconnectException;
import com.cyclonecommerce.tradingengine.transport.TransportTestException;
import com.cyclonecommerce.tradingengine.transport.TransportInitializationException;
import com.cyclonecommerce.tradingengine.transport.pluggable.api.PluggableClient;
import com.cyclonecommerce.tradingengine.transport.pluggable.api.PluggableException;
import com.cyclonecommerce.tradingengine.transport.pluggable.api.PluggableSettings;
import com.cyclonecommerce.util.VirtualData;
import com.cyclonecommerce.tradingengine.transport.pluggable.api.PluggableMessage;
import util.Serialization;
import util.pattern.PatternKeyValidator;
import util.pattern.PatternKeyValidatorFactory;

import entities.File;
import entities.FileVersionDetails;
import entities.Folder;
import entities.FolderStatus;
import entities.StorageEndpoint;
import entities.SyncPoint;
import oauth.OAuth;
import services.FileService;
import services.FolderService;
import services.NtlmAuthenticator;
import services.StorageEndpointService;
import services.SyncPointService;
import util.APIContext;
import util.APIGateway;


import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


import org.apache.log4j.Level;


public class PluggableSyncplicityTransport implements PluggableClient {
	
	//Set program name and version
	String _PGMNAME = com.axway.gps.PluggableSyncplicityTransport.class.getName();
	String _PGMVERSION = "1.0.0";
	
	/** Constants defining valid configuration tags 
	 *  These tags must NOT contain space or special characters. They MUST match the name element in the pluggabletransports.xml EXACTLY
	 * **/
	private static final String SETTING_APPKEY = "App Key";
	private static final String SETTING_APPSECRET = "App Secret";
	private static final String SETTING_ADMINTOKEN = "Admin Token";
	private static final String SETTING_FOLDERNAME = "Folder";
	private static final String SETTING_PICKUP_PATTERN = "Filter";
	private static final String SETTING_PATTERN_TYPE = "Filter Type";
	private static final String SETTING_CREATE_FOLDER = "Create Folder";
	private static final String SETTING_DELETE = "Delete After Consumption";
	private static final String SETTING_ROUTING = "Sender Identification";
	private static final String SETTING_PROXY = "Use Proxy";
	private static final String SETTING_PROXY_HOST = "Proxy Host";
	private static final String SETTING_PROXY_PORT = "Proxy Port";
	private static final String SETTING_PROXY_USER = "Proxy Username";
	private static final String SETTING_PROXY_PW = "Proxy Password";

	// Setting to distinguish pickup and delivery mode
	private static final String SETTING_EXCHANGE_TYPE = "Exchange Type";

	private final static String CONNECTION_URL  = "https://api.syncplicity.com";

	
	//this is how you get the log4J logger instance for this class
	private static Logger logger = Logger.getLogger(com.axway.gps.PluggableSyncplicityTransport.class.getName());

	/** a Map containing temporary Message metadata **/
	private Map metadata = null;
	
	//Stores the settings from the UI
	private String _appkey;
	private String _appsecret;
	private String _admintoken;
	private String _folder;
	private String _filter;
	private String _filtertype;
	private String _exchangeType;
	private String _deleteAfterConsumption;
	private String _createFolder;
	private String _emailAsRoutingID;
	private String _useProxy;
	private String _proxyHost;
	private String _proxyPort;
	private String _proxyUser;
	private String _proxyPassword;
	
    String messageContent = null;
	private String OathParam[] = new String[3];
	
	Properties env = null;


	//Map containing constant settings from pluggabletransport.xml
	private Map<String,String> constantProperties = null;
	

	/**
	 * Default constructor - the only constructor used by B2Bi
	 */
	public PluggableSyncplicityTransport() {
		
		//Set a default logger level
		if(logger.getLevel() == null) {
			logger.setLevel(Level.INFO);
		}
		// logger.debug(String.format("Executing PluggableTransport: %s version: %s",_PGMNAME,_PGMVERSION));
	}

	/**
	 * Initialize the pluggable client instance.
	 *
	 * @param pluggableSettings the settings provided by GUI configuration or in the pluggabletransports.xml
	 */
	public void init(PluggableSettings pluggableSettings) throws TransportInitializationException {
		
		try {

			// Get all constant settings from the pluggabletransport.xml file and store 
			// them in the local map for later use
			
			constantProperties = new HashMap<String,String>(pluggableSettings.getConstantSettings());
			if (constantProperties != null && !constantProperties.isEmpty()) {
				Iterator<String> i = constantProperties.keySet().iterator();
				while (i.hasNext()) {
					String key = (String) i.next();
					//// logger.debug("Constant setting " + key + "=" + constantProperties.get(key));
				}
			}			
			_exchangeType = pluggableSettings.getConstantSetting(SETTING_EXCHANGE_TYPE);
			
			// Get all settings defined in the GUI for each pluggable transport defined
			
			_appkey = pluggableSettings.getSetting(SETTING_APPKEY);
			_appsecret = pluggableSettings.getSetting(SETTING_APPSECRET);
			_admintoken = pluggableSettings.getSetting(SETTING_ADMINTOKEN);
			_folder = pluggableSettings.getSetting(SETTING_FOLDERNAME);
			
			
			if (_exchangeType.equals("pickup")) {
				_filtertype = pluggableSettings.getSetting(SETTING_PATTERN_TYPE);
				_filter = pluggableSettings.getSetting(SETTING_PICKUP_PATTERN);
				_deleteAfterConsumption = pluggableSettings.getSetting(SETTING_DELETE);
				_emailAsRoutingID = pluggableSettings.getSetting(SETTING_ROUTING);
				
			}
			if (_exchangeType.equals("delivery")) {
				_createFolder = pluggableSettings.getSetting(SETTING_CREATE_FOLDER);
				

			}
			_useProxy = pluggableSettings.getSetting(SETTING_PROXY);
			_proxyHost = pluggableSettings.getSetting(SETTING_PROXY_HOST);
			_proxyPort = pluggableSettings.getSetting(SETTING_PROXY_PORT);
			_proxyUser = pluggableSettings.getSetting(SETTING_PROXY_USER);
			_proxyPassword = pluggableSettings.getSetting(SETTING_PROXY_PW);
			
			
			// set Oath Parameters 
			OathParam[0] = _appkey;
			OathParam[1] = _appsecret;
			OathParam[2] = _admintoken;
			
			
			// Sanitize the folder name (remove leading / if provided)
			
			_folder = _folder.startsWith("/") ? _folder.substring(1) : _folder;

			
			// logger.debug(String.format("Initialization Syncplicity connector Complete"));
			
			
			
		} catch (Exception e ) {
			throw new TransportInitializationException("Error getting settings", e);
		}
	}



	
	/**
	 * Create a session
	 */
	public void connect() throws UnableToConnectException {
		
        URL url = null;
        
		try {
			url = new URL(CONNECTION_URL);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
        
		if (_useProxy.equals("true")) {
			
			System.setProperty("http.proxyHost", _proxyHost);
			System.setProperty("http.proxyPort", _proxyPort);
			System.setProperty("https.proxyHost", _proxyHost);
			System.setProperty("https.proxyPort", _proxyPort);
			
		    Authenticator.setDefault(new NtlmAuthenticator(_proxyUser, _proxyPassword));
		}
        

		try {
			url.openConnection();
		} catch (IOException e) {
			throw new UnableToConnectException("Failed to connect to Syncplicity Server");
		}
	
	}

	
	public void authenticate() throws UnableToAuthenticateException {
		
		try { 

			// logger.debug(String.format("Authenticating"));
			APIGateway.setOAuthParameters(OathParam);

			OAuth.authenticate();
			
			if( !APIContext.isAuthenticated() ) {
				logger.error( "The OAuth authentication has failed, cannot continue." );
				System.exit(1);
			}
			else {
				// logger.debug( "Authentication was successful." );			
			}
		} catch (Exception e) {
			throw new UnableToAuthenticateException("Unable to authenticate to Syncplity server");
		}
		

	}
	
	/**
	 * The Syncplicity interface is pollable. The isPollable
	 * method must return 'true' to tell the TE to call the 'list' method
	 */
	
	@Override
	public boolean isPollable() {
		boolean isPollable = true;
		// logger.debug("isPollable returning: " + isPollable);
		return isPollable;
	}


	/**
	 * Send the message 
	 *
	 * @param message the message being processed by B2Bi
	 * @param returnMessage not used in this example
	 * @return always null
	 * @throws UnableToProduceException if there was a problem producing the message
	 */
	public PluggableMessage produce(PluggableMessage message, PluggableMessage returnMessage) throws UnableToProduceException {
		
		
		try { 
			
			// logger.debug(String.format("Producing message"));
			
			uploadFile(message, _folder, _createFolder);
		 	message.setMetadata("SyncplicityDeliveryFolder", _folder);
		 	


		} catch (Exception e) {
		     throw e;
		}     
		
		return null;
	}

	
	public PluggableMessage consume(PluggableMessage message, String idFromList) throws UnableToConsumeException, FileNotFoundException {
		

		try {

			SyncPoint ConsumptionSyncPoint = getStorageEndpoint(getSyncPointFromPath(_folder));

	        if (ConsumptionSyncPoint == null) {
	            logger.error("The syncpoint was not created at previous steps. No files will be retrieved.");
	            return null;
	        }

	        
	        // Get the Folder ID
	        
        	String FolderID = getFolderID(ConsumptionSyncPoint, _folder);
			
            if (FolderID.equals(null)) {
          	  logger.error("Pickup folder does not exist");
          	  return null;
            }
            
        	// Get Folder contents

        	// logger.debug("Now retrieving requested file(s) from folder: " + _folder);
					
		    Folder listfolder = FolderService.getFolder(ConsumptionSyncPoint.Id, FolderID, true);
	       // logger.debug("Number of files in the folder: " + listfolder.Files.length); 

	        
	        
	        File[] files = listfolder.Files;
	        if (files.length == 0) {
	           // logger.debug("No files to receive");
	            return null;
	        } 
	        
	        for (int i = 0; i < files.length; i++) {
	        	if (files[i].FileId.equals(idFromList)) {

	        		// Download file 

	        		String fileId = files[i].FileId;
	    	        String downloadedFile = FileService.downloadFile(ConsumptionSyncPoint.Id, fileId, true);

	    	        // Attach file content
	    	        
	    	        InputStream targetStream = new ByteArrayInputStream(downloadedFile.getBytes());
				    VirtualData data = new VirtualData();
				    data.readFrom(targetStream);
				 	message.setData(data);
				 	message.setFilename(files[i].Filename);
				 	message.setMetadata("SyncplicityPickupFolder", _folder);
				 	message.setMetadata("SyncplicityFileName", files[i].Filename);
				 	message.setMetadata("SyncplicityFileCreationDate", files[i].CreationTimeUtc);
				 	message.setMetadata("SyncplicityFileLastModificationDate", files[i].LastWriteTimeUtc);

			 		FileVersionDetails[] Fileversions = FileService.getFileVersion(ConsumptionSyncPoint.Id, files[i].LatestVersionId, true );
				 	message.setMetadata("SyncplicityUserEmailAddress", Fileversions[0].User.EmailAddress);

				 		
				 	// Set Routing ID information
				 	// logger.debug("Use the following metadata as routing ID: \"" + _emailAsRoutingID + "\"");
				 	
				 	if (_emailAsRoutingID.equals("-")) {
				 	}
				 	else if (_emailAsRoutingID.equals("User Email")) {
				 		message.setMetadata(MetadataDictionary.SENDER_ROUTING_ID, Fileversions[0].User.EmailAddress);
				 	}
				 	else if (_emailAsRoutingID.equals("Pickup Folder")) {
				 		message.setMetadata(MetadataDictionary.SENDER_ROUTING_ID, _folder);
				 	}				 	
				 	
				 	
				 	// Delete the file if necessary
				 	
				 	if (_deleteAfterConsumption.equals("true")) {
					 	String something = FileService.deleteFile(ConsumptionSyncPoint.Id, fileId, true);
					 	// logger.debug("Consumed file deleted");
				 	
				 	}
				 	
				 	
	        	}
			}


		} catch (Exception e) {
	     logger.error("Error" + e);
		
		}     
		
	return message;	  
	

	}
	
	
	public static String getFolderID (SyncPoint sSyncPoint, String FolderName) {
	
	    String VirtualPath = FolderName;
		String FolderID = "";
		boolean suppressErrors = true;
		Folder cFolder[] = null;
		
	    String splitFolderPath[] = FolderName.split("/");    	
		
		
	    if(splitFolderPath.length < 2) {
	    	
	    	// Folder path must be EndpointPath
	    	FolderID = sSyncPoint.RootFolderId;
	    	
	    } else {
	        
	        String FolderInfo = FolderService.getExistingFolderInfo(sSyncPoint, getRelativeURLPath(FolderName), suppressErrors);
	        cFolder = Serialization.deserizalize(FolderInfo, Folder[].class);
	       
	        int SyncPointId = 0;
	        
	        // Temporary workaround until we the virtual_path filtering is working as query parameter (now the function returns all folders)

	       // logger.debug("Search Path: " + getRelativePath(FolderName));
	        
	        for (int i = 0; i < cFolder.length; i++) {

        		if (cFolder[i].VirtualPath.equals(getRelativePath(FolderName))) {
	        		
	                FolderID = cFolder[i].FolderId;
	                VirtualPath = cFolder[i].VirtualPath;
	                SyncPointId = cFolder[i].SyncpointId;
	                
	        	}
	            
			}

	    }
	    
	    return FolderID;	
	}
	
	@Override
	public String getUrl() throws PluggableException {
		String _URL;
		_URL = "https://my.syncplicity.com/Files ["+ _folder + "]";
		return _URL;
	}

	
	
	/**
	 * Return a list of files waiting in our consumption directory.  The trading engine will subsequently call
	 * consume once for each file in the list.  Since we could be running in a cluster of multiple trading
	 * engine nodes, we cannot say for sure whether consume will be called on the same machine as list was.
	 * Thus, the files need to be on a shared directory accessible from all the nodes in the cluster.
	 */
	public String[] list() throws UnableToConsumeException {
		
       // logger.debug("Retrieving the files from folder: " + _folder);
    	String[] list = null;
        ArrayList<String> result = new ArrayList<String>();
	
		SyncPoint ListSyncPoint = getStorageEndpoint(getSyncPointFromPath(_folder));

        if (ListSyncPoint == null) {
            logger.error("No Syncpoint was found. No files will be retrieved.");
        }
	        
	    // Get the Folder ID
	        
    	String lFolderID = getFolderID(ListSyncPoint, _folder);
       	
        if (lFolderID.equals(null)) {
      	  logger.error("Folder does not exist");
      	  return null;
        } else {
        	// logger.debug("Listing contents of folder: " + _folder );
        }
        
		// Get the Folder Contents
        
        Folder folder = FolderService.getFolder(ListSyncPoint.Id, lFolderID, true);
        File[] files = folder.Files;
        if (files.length == 0) {
           logger.debug("No files in: " + _folder);
        } else {
        
	        
	        for (int i = 0; i < files.length; i++) {
	        	
	        	PatternKeyValidator validator = PatternKeyValidatorFactory.createPatternValidator(_filtertype);
	        	if (validator.isValid(files[i].Filename, _filter)) {
	        		result.add(files[i].FileId);
	        		// logger.debug(files[i].Filename + " added to list."); 
	        	}
	        	else {
	        		// logger.debug(files[i].Filename + " does not match the defined filter (" + _filter +") and /or filter type (" + _filtertype + ")"); 
	        	}
			
			}
	
        }     
  
		list = new String[result.size()];
		for (int i = 0; i < result.size(); i++) {
			list[i] = result.get(i);
			// logger.debug("Adding Item [" + i + "]: " + list[i]);
		}
        
        return list;
 
	}

	

	/**
	 * Delete the specified file in the consumption directory.  The trading engine will call this method after
	 * it has successfully called our consume method for this file.
	 *
	 * @param nameFromList the file reference to delete
	 */
	public void delete(String nameFromList) throws UnableToDeleteException, FileNotFoundException {
		
		
		
		
		
		
	}

	/**
	 * Return an information string if the Pluggable Transport is able to connect to the server.  
	 * Otherwise throw TransportTestException with an appropriate message.
	 */
	public String test() throws TransportTestException {
		
		try {
			connect();
			OAuth.authenticate();

		} catch(Exception e) {
			return "Failed to connect to Syncplicity";
		}
		return "Success, connected to Syncplicity";
	}

	/**
	 * Disconnect
	 */
	public void disconnect() throws UnableToDisconnectException {
		// logger.debug("Disconnecting from Syncplicity server");
		try {
		
		} catch(Exception e) {
			logger.error("Failed to disconnect from Syncplicity server");
		}
	}

	// Syncplicity 

   private static String getSyncPointFromPath (String FullPath) {

    	// Remove leading "/"
    	
    	FullPath = FullPath.startsWith("/") ? FullPath.substring(1) : FullPath;
    	
    	// Retrieve Syncpoint

    	String splitFolderPath[] = FullPath.split("/");    	
        
    	String basePath = Paths.get(splitFolderPath[0]).toString();

    	return basePath;
    	
    
    }
    
    private static String getRelativePath (String FullPath) {

    	// Remove leading "/"
    	
    	FullPath = FullPath.startsWith("/") ? FullPath.substring(1) : FullPath;
    	
    	// Separate Syncpoint and folder Path
    	
        String splitFolderPath[] = FullPath.split("/");    	
        
    	final Path fullPath = Paths.get(FullPath);
    	final Path basePath = Paths.get(splitFolderPath[0]);
    	final Path getRelativePath = basePath.relativize(fullPath);

    	// Replace slashes (this is only effective on Unix, on Windows the Path separator will already be correct)
    	String SyncPath = "\\" + getRelativePath.toString().replace('/', '\\') + "\\";

   	
    	return SyncPath;
    	
    
    }

    
    private static String getRelativeUploadPath (String FullPath) {

    	// Remove leading "/"
    	
    	FullPath = FullPath.startsWith("/") ? FullPath.substring(1) : FullPath;
    	
    	// Separate Syncpoint and folder Path
    	
        String splitFolderPath[] = FullPath.split("/");    	
        
    	final Path fullPath = Paths.get(FullPath);
    	final Path basePath = Paths.get(splitFolderPath[0]);
    	final Path getRelativePath = basePath.relativize(fullPath);

    	// Replace slashes (this is only effective on Windows, on Unix the Path separator will already be correct)
    	String SyncPath = getRelativePath.toString().replace('\\', '/');

   	
    	return SyncPath;
    	
    
    }
    
    private static String getRelativeURLPath (String FullPath) {

    	// Remove leading "/"
    	
    	FullPath = FullPath.startsWith("/") ? FullPath.substring(1) : FullPath;
    	
    	// Separate Syncpoint and folder Path
    	
        String splitFolderPath[] = FullPath.split("/");    	
        
    	final Path fullPath = Paths.get(FullPath);
    	final Path basePath = Paths.get(splitFolderPath[0]);
    	final Path getRelativePath = basePath.relativize(fullPath);

    	
    	// Replace slashes (this is only effective on Unix, on Windows the Path separator will already be correct)
    	String SyncPath = "/" + getRelativePath.toString().replace('\\', '/') + "/";

        String EncodedPath = "";

    	// Encode Path
    	try {
        	EncodedPath = java.net.URLEncoder.encode(SyncPath, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}    	
   	
    	return EncodedPath;
    	
    
    }

	
    private static SyncPoint getStorageEndpoint(String FolderName) {
        SyncPoint[] syncPoints = SyncPointService.getSyncPoints(true);
        for (int i = 0; i < syncPoints.length; i++) {
			if (syncPoints[i].Name.equals(FolderName)) {
		       // logger.debug("Retrieved requested endpoint.");
				return syncPoints[i];
			}
        }
        logger.error("Failed to retrieved storage endpoint.");
        return null;
    }
    
   	
 	private static void uploadFile(PluggableMessage message, String uFolderName, String uCreateFolder) throws UnableToProduceException {
        // logger.debug("Starting File upload..");

		SyncPoint ProductionSyncPoint = getStorageEndpoint(getSyncPointFromPath(uFolderName));
        
		StorageEndpoint[] storageEndpoints = StorageEndpointService.getStorageEndpoints(true);
		          
		StorageEndpoint storageEndpoint = null;
		  for (StorageEndpoint endpoint : storageEndpoints) {
		  	if (endpoint.Id.equals(ProductionSyncPoint.StorageEndpointId)) {
		          storageEndpoint = endpoint;
		    }
		}
          
           

          String UploadFolder = "";
          
          // Retrieve Folder ID
          
          String uFolderID = getFolderID(ProductionSyncPoint, uFolderName);
          
          if (uFolderID.equals("")) {
  
        	  if (uCreateFolder.equals("true")) {
        	  
					Folder folder = new Folder();
			        folder.Name = getRelativeUploadPath(uFolderName);
					folder.Status = FolderStatus.Added;
					Folder[] folders = { folder };
					Folder[] createdFolders = FolderService.createFolders(ProductionSyncPoint.Id, ProductionSyncPoint.RootFolderId,
					        folders);
					if (createdFolders == null || createdFolders.length == 0) {
						throw new UnableToProduceException(
								"Destination folder does not exist and could not be created. Aborting.");
					} 
          
				uFolderID = createdFolders[0].FolderId;
        	  } else {
					throw new UnableToProduceException(
							"Destination folder does not exist and delivery exchange has not been configured to create folders dynamically. Aborting.");
        	  }
        	  
          }
          
          Folder ufolder = FolderService.getFolder(ProductionSyncPoint.Id, uFolderID, true);
                   
         // logger.debug(String.format("Finished Folder creation. New Folder id: %s", uFolderID));
           
         // logger.debug("Starting File upload..");
          String fileName = message.getMetadata(MetadataDictionary.CONSUMPTION_FILENAME);
	    
         VirtualData virtualData = message.getData();
         byte[] byteArray = new byte[(int)virtualData.length()];
         virtualData.setReadPosMarker(0L);
         virtualData.read(byteArray);
         String result = FileService.uploadFile(storageEndpoint.Urls[0].Url, ufolder.VirtualPath, fileName, ufolder.SyncpointId, byteArray);
	        
         // logger.debug(String.format("Finished File upload. File upload result: %s", result));
		
	}
	

	  	
}
