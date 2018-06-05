/**==============================================================================
 * Program/Module :     PluggableSMBTransport.java
 * Description :       	Pluggable transport that implements the SMB APIs
 * Supported Products :	B2Bi 2.x
 * Author :				Bas van den Berg
 * Copyright :          Axway
 *==============================================================================
 * HISTORY
 * 20180530 bvandenberg	1.0.0	initial version.
 *==============================================================================*/

package com.axway.gps;

import com.cyclonecommerce.tradingengine.transport.UnableToConnectException;
import com.cyclonecommerce.tradingengine.transport.UnableToAuthenticateException;
import com.cyclonecommerce.tradingengine.transport.UnableToConsumeException;
import com.cyclonecommerce.tradingengine.transport.UnableToProduceException;
import com.cyclonecommerce.api.inlineprocessing.Message;
import com.cyclonecommerce.collaboration.MetadataDictionary;
import com.cyclonecommerce.tradingengine.transport.UnableToDeleteException;
import com.cyclonecommerce.tradingengine.transport.UnableToDisconnectException;
import com.cyclonecommerce.tradingengine.transport.TransportTestException;
import com.cyclonecommerce.tradingengine.transport.TransportInitializationException;
import com.cyclonecommerce.tradingengine.transport.pluggable.api.PluggableClient;
import com.cyclonecommerce.tradingengine.transport.pluggable.api.PluggableException;
import com.cyclonecommerce.tradingengine.transport.pluggable.api.PluggableSettings;
import com.cyclonecommerce.util.VirtualData;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.mssmb2.SMBApiException;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;


import com.cyclonecommerce.tradingengine.transport.pluggable.api.PluggableMessage;
import util.pattern.PatternKeyValidator;
import util.pattern.PatternKeyValidatorFactory;




import java.nio.file.Paths;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;


import org.apache.log4j.Level;


public class PluggableSMBTransport implements PluggableClient {
	
	//Set program name and version
	String _PGMNAME = "SMB Adapter";
	String _PGMVERSION = "1.0.0";
	
	private static final String SETTING_SMB_HOST = "SMB Host";
	private static final String SETTING_SMBFULLPATH = "SMB Share";
	private static final String SETTING_PICKUP_PATTERN = "Filter";
	private static final String SETTING_PATTERN_TYPE = "Filter Type";
	private static final String SETTING_OVERWRITE = "Overwrite";
	private static final String SETTING_SMB_USER = "Domain User";
	private static final String SETTING_SMB_PW = "Domain Password";
	private static final String SETTING_SMB_DOMAIN = "Domain";

	// Setting to distinguish pickup and delivery mode
	private static final String SETTING_EXCHANGE_TYPE = "Exchange Type";

	// this is how you get the log4J logger instance for this class
	private static Logger logger = Logger.getLogger(com.axway.gps.PluggableSMBTransport.class.getName());

	/** a Map containing temporary Message metadata **/
	private Map metadata = null;	
	
	// Stores the settings from the UI
	private String _host = "";
	private String _smbFullPath = "";
	private String _smbShare = "";
	private String _smbFolder = "";
	private String _filter = "";
	private String _filtertype = "";
	private String _exchangeType = "";
	private String _overwrite = "";
	private String _DomainUser = "";
	private String _DomainPassword= "";
	private String _Domain= "";

    String messageContent = null;
	Properties env = null;
	

	private AuthenticationContext ac = null;
	private SMBClient client = null;
	private Connection connection = null;
	private Session session = null;


	//Map containing constant settings from pluggabletransport.xml
	private Map<String,String> constantProperties = null;
	

	public PluggableSMBTransport() {
		
		//Set a default logger level
		if(logger.getLevel() == null) {
			logger.setLevel(Level.INFO);
		}
		logger.debug(String.format("Executing PluggableTransport: %s version: %s",_PGMNAME,_PGMVERSION));
	}


	@Override
	public void init(PluggableSettings pluggableSettings) throws TransportInitializationException {
		
		try {

			// Get all constant settings from the pluggabletransport.xml file and store 
			// them in the local map for later use
			logger.debug(String.format("Initializating SMB connector"));
			
			constantProperties = new HashMap<String,String>(pluggableSettings.getConstantSettings());
			if (constantProperties != null && !constantProperties.isEmpty()) {
				Iterator<String> i = constantProperties.keySet().iterator();
				while (i.hasNext()) {
					String key = (String) i.next();
					// //logger.debug("Constant setting " + key + "=" + constantProperties.get(key));
				}
			}			
			_exchangeType = pluggableSettings.getConstantSetting(SETTING_EXCHANGE_TYPE);
			
			// Get all settings defined in the GUI for each pluggable transport defined
			
			_host = pluggableSettings.getSetting(SETTING_SMB_HOST);
			_smbFullPath = pluggableSettings.getSetting(SETTING_SMBFULLPATH);
			_DomainUser = pluggableSettings.getSetting(SETTING_SMB_USER);
			_DomainPassword = pluggableSettings.getSetting(SETTING_SMB_PW);
			_Domain = pluggableSettings.getSetting(SETTING_SMB_DOMAIN);

		
			_smbShare = getSMBShare(_smbFullPath);
			_smbFolder = getRelativeSMBPath(_smbFullPath);

			logger.debug( "SMB Share: " + _smbShare);
			logger.debug( "SMB Folder: " + _smbFolder);
			
			
			if (_exchangeType.equals("pickup")) {
				_filtertype = pluggableSettings.getSetting(SETTING_PATTERN_TYPE);
				_filter = pluggableSettings.getSetting(SETTING_PICKUP_PATTERN);
			}
			if (_exchangeType.equals("delivery")) {
				_overwrite = pluggableSettings.getSetting(SETTING_OVERWRITE); 

			}
			

			logger.debug(String.format("Initialization SMB connector Complete"));
			
			
			
		} catch (Exception e ) {
			throw new TransportInitializationException("Error getting settings", e);
		}
	}


	@Override
	public void connect() throws UnableToConnectException {

		logger.debug( "Connecting to SMB Host: " + _host);

		try {
			client = new SMBClient();
			connection = client.connect(_host);
        } catch (Exception e) {
			throw new UnableToConnectException("Unable to connect to SMB Host");
		}

		logger.debug( "Connected to SMB Host: " + _host);
		
	}

	@Override
	public void authenticate() throws UnableToAuthenticateException {

	
		logger.debug(String.format("Authenticating to SMB Host"));
		
		try { 

            ac = new AuthenticationContext(_DomainUser, _DomainPassword.toCharArray(), _Domain);
            session = connection.authenticate(ac);
            
            logger.debug("Authenticate Session ID: " + session.getSessionId());

		
		} catch (Exception e) {
			throw new UnableToAuthenticateException("Unable to authenticate to SMB Host " + _host);
		}
	
		logger.debug(String.format("Authenticated to SMB Host"));

		
	}
	
	
	@Override
	public boolean isPollable() {
		boolean isPollable = true;
		logger.debug("SMB Interface pollable: " + isPollable);
		return isPollable;
	}


	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PluggableMessage produce(PluggableMessage message, PluggableMessage returnMessage) throws UnableToProduceException {

	   	logger.debug("Starting SMB File Delivery");

		String fileName = "";
       
		try { 
			
			DiskShare share = (DiskShare) session.connectShare(_smbShare);
			fileName = message.getMetadata(MetadataDictionary.CONSUMPTION_FILENAME);
			
	        
    	    // Create subfolders if needed
  	    
	        createSubfolders(share, _smbFolder);    	
	        
	        File smbFile = null;
	    	String produceFileName = _smbFolder + fileName;
	    	logger.debug("Delivery path and file name: " + produceFileName);
	    	
	    	
	    	if (share.fileExists(produceFileName)) {

	    		if (_overwrite.equals("true")) {

			        smbFile = share.openFile(produceFileName, new HashSet(Arrays.asList(new AccessMask[] { AccessMask.GENERIC_WRITE })), new HashSet(
			                Arrays.asList(new FileAttributes[] { FileAttributes.FILE_ATTRIBUTE_NORMAL })), SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OVERWRITE, new HashSet(
			                Arrays.asList(new SMB2CreateOptions[] { SMB2CreateOptions.FILE_DIRECTORY_FILE })));	    		
			        
	    		} else {

		    		// Iterate till we found a unique file name
	    			
			        for (int i = 1;; i++) {
			        	
			        	String[] filenameParts = fileName.split("\\.(?=[^\\.]+$)");
			        	produceFileName = _smbFolder + filenameParts[0] + "["+ i + "]." + filenameParts[1];
			            if (!share.fileExists(produceFileName)) {
			                break;
			            }
			        }
	    			
			        smbFile = share.openFile(produceFileName, new HashSet(Arrays.asList(new AccessMask[] { AccessMask.GENERIC_ALL })), new HashSet(
			                Arrays.asList(new FileAttributes[] { FileAttributes.FILE_ATTRIBUTE_NORMAL })), SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_CREATE, new HashSet(
			                Arrays.asList(new SMB2CreateOptions[] { SMB2CreateOptions.FILE_DIRECTORY_FILE })));	    		
	    			
	    		}
	    		
	    	} else {

		        smbFile = share.openFile(produceFileName, new HashSet(Arrays.asList(new AccessMask[] { AccessMask.GENERIC_ALL })), new HashSet(
		                Arrays.asList(new FileAttributes[] { FileAttributes.FILE_ATTRIBUTE_NORMAL })), SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_CREATE, new HashSet(
		                Arrays.asList(new SMB2CreateOptions[] { SMB2CreateOptions.FILE_DIRECTORY_FILE })));	    		
	    		
	    	}

	    	OutputStream out = smbFile.getOutputStream();
	    	message.getData().setReadPosMarker(0L);
	        message.getData().writeTo(out);
	        out.flush();
	        out.close();
	        
			share.close();
			disconnect();
			
			String msg = "SMB File Delivery complete.";
			returnMessage.setData(new VirtualData(msg.toCharArray()));
			
			

		} catch (Exception e) {
		     throw new UnableToProduceException("Failed to deliver " + fileName + " to SMB Share: " + _smbFullPath + " :" + e.toString());
		}     

		
	   	logger.debug("SMB File Delivery Complete");
		
		return null;
	}

	
	@Override
	public PluggableMessage consume(PluggableMessage message, String nameFromList) throws UnableToConsumeException {
		
    	logger.debug("Now retrieving " + nameFromList + " from folder: " + _smbFolder);

   	
		try {

	        File smbFile = null;
	        InputStream TargetStream = null;

	        DiskShare share = (DiskShare) session.connectShare(_smbShare);
	        
	        String consumptionFileName = _smbFolder + nameFromList;
	    	
	    	if (share.fileExists(consumptionFileName)) {
		  
	            smbFile = share.openFile(consumptionFileName, EnumSet.of(AccessMask.GENERIC_READ), null, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, null);
	            TargetStream = smbFile.getInputStream();
			    VirtualData data = new VirtualData();
			    data.readFrom(TargetStream);
			 	message.setData(data);
			 	message.setMetadata("SMB Host", _host);
			 	message.setMetadata("SMB Share", _smbShare);
			 	message.setMetadata("SMB Folder", _smbFolder);
			 	message.setFilename(nameFromList);
	    	}

			share.close();

	       	logger.debug("Completed retrieving " + nameFromList + " from folder: " + _smbFolder);
			
		} catch (Exception e) {
	      throw new UnableToConsumeException ("Failed to consume " + nameFromList + " from SMB Share: " + _smbFullPath);
		}     

		
	return message;	  
	

	}


	@Override	
	public void delete(String nameFromList) throws UnableToDeleteException {
		
      	logger.debug("Now deleting " + nameFromList + " from folder: " + _smbFolder);

  	
      	try {
			DiskShare share = (DiskShare) session.connectShare(_smbShare);

			String deletionFileName = _smbFolder + nameFromList;

			if (share.fileExists(deletionFileName)) {
	    		share.rm(deletionFileName);	
	    	}
			share.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new UnableToDeleteException("Failed to delete " + nameFromList + " from SMB Share: " + _smbFolder);
		}
 	 		
		
	}	
	
	@Override
	public String getUrl() throws PluggableException {
		String _URL;
		_URL = "SMB [\\\\" + _host + "\\" +_smbFullPath + "]";
		return _URL;
	}

	
	
	@Override	
	public String[] list() throws UnableToConsumeException {

		
        logger.debug("Listing the files from folder: " + _smbFolder);
        
		String[] list = null;
		list = new String[0];


		try {
	   		ArrayList<String> result = new ArrayList<String>();
    		
    		DiskShare share = (DiskShare) session.connectShare(_smbShare);
			
			for (FileIdBothDirectoryInformation f : share.list(_smbFolder, "*.*")) {
	
			    if (f.getFileName().startsWith(".")) {

			    	// Directory
	        		//logger.debug("Entry is a directory - ignore"); 
			    	
			    } else {
			    
		        	PatternKeyValidator validator = PatternKeyValidatorFactory.createPatternValidator(_filtertype);
		        	if (validator.isValid(f.getFileName(), _filter)) {
		        		result.add(f.getFileName());
		        	}
		        	else {
		        		//logger.debug(f.getFileName() + " does not match the defined filter (" + _filter +") and /or filter type (" + _filtertype + ")"); 
		        	}
			    
			    }
			}

			list = new String[result.size()];
			for (int i = 0; i < result.size(); i++) {
				list[i] = result.get(i);
				logger.debug("Adding Item [" + i + "]: " + list[i]);
			}
	
			
			share.close();
			
		} catch (Exception e) {
		
			// throw new UnableToConsumeException("An error occurred while listing the files in " + _smbFolder + ". :" + e.toString());
			// TODO: handle exception
		}
		
        logger.debug("Completed listing the files from folder: " + _smbFolder);

		

        return list;

        
 
	}

	@Override
	public String test() throws TransportTestException {

		try {
		
		     SMBClient testclient = new SMBClient();
		     Connection testconnection = testclient.connect(_host);
		     AuthenticationContext testac = new AuthenticationContext(_DomainUser, _DomainPassword.toCharArray(), _Domain);
		     Session testsession = testconnection.authenticate(testac);
	         DiskShare testshare = (DiskShare) testsession.connectShare(_smbShare);
			 testshare.close();
			 testconnection.close();

		} catch(Exception e) {
			throw new TransportTestException("Failed to connect to SMB Mount");
		}
		return "Successfully connected to SMB Mount";
	}


	@Override	
	public void disconnect() throws UnableToDisconnectException {

		logger.debug("Disconnecting from SMB server");

		try {
			
			session.close();
			connection.close();
			
		} catch(Exception e) {
			logger.error("Failed to disconnect from SMB server");
			throw new UnableToDisconnectException("Failed to disconnect from SMB Server");
		}

		logger.debug("Disconnected from SMB server");
		
	}


    private static String getSMBShare (String FullPath) {

    	// Remove leading "/"
    	String SMBShare = "";

    	try {
          	FullPath = FullPath.startsWith("\\\\") ? FullPath.substring(2) : FullPath;
           	FullPath = FullPath.startsWith("\\") ? FullPath.substring(1) : FullPath;
     
           	String splitFolderPath[] = FullPath.split("\\\\");    	
            
        	
        	if (splitFolderPath.length > 1) {
        		SMBShare = splitFolderPath[0];
        	} else {
        		SMBShare = FullPath;
        	}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
 
     	return SMBShare;
    
    }
	
    private static String getRelativeSMBPath (String FullPath) {

    	// Remove leading "/"
    	String RelativePath = "";

    	try {
          	FullPath = FullPath.startsWith("\\\\") ? FullPath.substring(2) : FullPath;
        	FullPath = FullPath.startsWith("\\") ? FullPath.substring(1) : FullPath;
            String splitFolderPath[] = FullPath.split("\\\\");    	
            
        	
        	if (splitFolderPath.length > 1) {
        	
    	    	for (int i = 1; i < splitFolderPath.length; i++) {
    	    		if (RelativePath.equals("")) {
    	    			RelativePath = splitFolderPath[i];
    	    		} else {
    	    			RelativePath = RelativePath + "\\" + splitFolderPath[i];
    	    		}
    			}
        	} else {
        		RelativePath = FullPath;
        	}
        
           	if (RelativePath.endsWith("\\")) {
        		
        	} else {
        		RelativePath = RelativePath + "\\";
        	}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
        	
     	return RelativePath;
    	
    
    }
    
	public static String getPathWithSMBSeparators(String filePath)
	{
	    String path = "";

	    try {
		    path = filePath.replace("//", "/").replace("/", "\\");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	    return path;
	}
	 
	public static String getPathWithOSSeparators(String filePath)
	{
	    String path = "";
	    try {
		    path = filePath.replace( "\\", java.io.File.separator);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	    return path;
	}	
	
	  
	public static void createSubfolders(DiskShare diskShare, String path) throws SMBApiException
	{

		try {

			  // Get the path with the local OS separators, so we can use path functions
			
			  path = getPathWithOSSeparators(path);
			  

		      int subFoldersCount = Paths.get(path, new String[0]).getNameCount();
		      
		      if (subFoldersCount > 0) {
		        for (int i = 0; i < subFoldersCount; i++)
		        {
		          String folder = Paths.get(path, new String[0]).getName(i).toString();
		          String subFolders = "";
		          if (i == 0) {
		            subFolders = folder;
		          } else {
		            subFolders = Paths.get(path, new String[0]).subpath(0, i + 1).toString();
		            
		          }
		          
		          // Use Windows path notation for folder creation at SMB Mount
		          
		          subFolders = getPathWithSMBSeparators(subFolders);
		          
		          if (!diskShare.folderExists(subFolders))
		          {
		            logger.debug("The folder " + subFolders + " doesn't exist. Try to create it.");
		            diskShare.mkdir(subFolders);
		          }
		        }
		      }
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}		
	}


	

	  	
}
