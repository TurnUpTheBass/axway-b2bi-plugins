/**==============================================================================
 * Program/Module :     PluggableS3Transport.java
 * Description :       	Pluggable transport that implements the S3 APIs
 * Supported Products :	B2Bi 2.x
 * Author :				Bas van den Berg
 * Copyright :          Axway
 *==============================================================================
 * HISTORY
 * 20180506 bvandenberg	1.0.0	initial version.
 * 20180506 bvandenberg	1.0.1	Updates (disconnect)
 * 20190919 bvandenberg	1.0.2	- Fix for nested folders
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

import services.NtlmAuthenticator;

import com.cyclonecommerce.tradingengine.transport.pluggable.api.PluggableMessage;
import util.pattern.PatternKeyValidator;
import util.pattern.PatternKeyValidatorFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
import com.axway.dmznode.DmzAgentFactory;
import com.axway.dmznode.DmzException;


import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


import org.apache.log4j.Level;


public class PluggableS3Transport implements PluggableClient {

	//Set program name and version
	String _PGMNAME = com.axway.gps.PluggableS3Transport.class.getName();
	String _PGMVERSION = "1.0.0";

	/** Constants defining valid configuration tags
	 *  These tags must NOT contain space or special characters. They MUST match the name element in the pluggabletransports.xml EXACTLY
	 * **/
	private static final String SETTING_ACCESSKEY = "Access Key";
	private static final String SETTING_SECRETKEY = "Secret Key";
	private static final String SETTING_REGION = "AWS Region";
	private static final String SETTING_BUCKETNAME = "Bucket";
	private static final String SETTING_FOLDERNAME = "Folder";
	private static final String SETTING_ACL = "ACL";
	private static final String SETTING_PICKUP_PATTERN = "Filter";
	private static final String SETTING_PATTERN_TYPE = "Filter Type";
	private static final String SETTING_PROXY = "Use Proxy";
	private static final String SETTING_PROXY_HOST = "Proxy Host";
	private static final String SETTING_PROXY_PORT = "Proxy Port";
	private static final String SETTING_PROXY_USER = "Proxy Username";
	private static final String SETTING_PROXY_PW = "Proxy Password";

	// Setting to distinguish pickup and delivery mode
	private static final String SETTING_EXCHANGE_TYPE = "Exchange Type";


	//this is how you get the log4J logger instance for this class
	private static Logger logger = Logger.getLogger(com.axway.gps.PluggableS3Transport.class.getName());

	/** a Map containing temporary Message metadata **/
	private Map metadata = null;

	//Stores the settings from the UI
	private String _secretKey;
	private String _accessKey;
	private String _region;
	private String _bucket;
	private String _folder;
	private String _acl;
	private String _filter;
	private String _filtertype;
	private String _exchangeType;
	private String _useProxy;
	private String _proxyHost;
	private String _proxyPort;
	private String _proxyUser;
	private String _proxyPassword;

    String messageContent = null;
	Properties env = null;

	private AmazonS3 amazonS3 = null;
	private BasicAWSCredentials credentials = null;
	private static final String CONTENT_TYPE = "application/json";

	private static final String SUFFIX = "/";


	//Map containing constant settings from pluggabletransport.xml
	private Map<String,String> constantProperties = null;


	/**
	 * Default constructor - the only constructor used by B2Bi
	 */
	public PluggableS3Transport() {

		//Set a default logger level
		if(logger.getLevel() == null) {
			logger.setLevel(Level.INFO);
		}
		logger.debug(String.format("Executing PluggableTransport: %s version: %s",_PGMNAME,_PGMVERSION));
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
					// logger.debug("Constant setting " + key + "=" + constantProperties.get(key));
				}
			}
			_exchangeType = pluggableSettings.getConstantSetting(SETTING_EXCHANGE_TYPE);

			// Get all settings defined in the GUI for each pluggable transport defined

			_accessKey = pluggableSettings.getSetting(SETTING_ACCESSKEY);
			_secretKey = pluggableSettings.getSetting(SETTING_SECRETKEY);
			_region = pluggableSettings.getSetting(SETTING_REGION);
			_bucket = pluggableSettings.getSetting(SETTING_BUCKETNAME);
			_folder = pluggableSettings.getSetting(SETTING_FOLDERNAME);
			
			if (_exchangeType.equals("pickup")) {
				_filtertype = pluggableSettings.getSetting(SETTING_PATTERN_TYPE);
				_filter = pluggableSettings.getSetting(SETTING_PICKUP_PATTERN);

			}
			
			if (_exchangeType.equals("delivery")) {
				_acl = pluggableSettings.getSetting(SETTING_ACL);
			}
			
			_useProxy = pluggableSettings.getSetting(SETTING_PROXY);
			_proxyHost = pluggableSettings.getSetting(SETTING_PROXY_HOST);
			_proxyPort = pluggableSettings.getSetting(SETTING_PROXY_PORT);
			_proxyUser = pluggableSettings.getSetting(SETTING_PROXY_USER);
			_proxyPassword = pluggableSettings.getSetting(SETTING_PROXY_PW);

			logger.debug(String.format("Initialization S3 connector Complete"));



		} catch (Exception e ) {
			throw new TransportInitializationException("Error getting settings", e);
		}
	}




	/**
	 * Create a session
	 */
	public void connect() throws UnableToConnectException {

   		if (_useProxy.equals("true")) {

			System.setProperty("http.proxyHost", _proxyHost);
			System.setProperty("http.proxyPort", _proxyPort);
			System.setProperty("https.proxyHost", _proxyHost);
			System.setProperty("https.proxyPort", _proxyPort);

		    Authenticator.setDefault(new NtlmAuthenticator(_proxyUser, _proxyPassword));
		}


		try {

			credentials = new BasicAWSCredentials(_accessKey, _secretKey);

			amazonS3 = AmazonS3ClientBuilder.standard()
	                .withRegion(_region)
	                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

			logger.info("New Amazon S3 Client Connected");




		} catch (Exception e) {
    	    System.err.println("Failed to connect to S3 Server");
		}
	}


	public void authenticate() throws UnableToAuthenticateException {

		try {



		} catch (Exception e) {
			throw new UnableToAuthenticateException("Unable to authenticate to S3 server");
		}


	}

	@Override
	public boolean isPollable() {
		boolean isPollable = true;
		logger.debug("isPollable returning: " + isPollable);
		return isPollable;
	}


	public PluggableMessage produce(PluggableMessage message, PluggableMessage returnMessage) throws UnableToProduceException {

	    String ConsumptionfileName = message.getMetadata(MetadataDictionary.CONSUMPTION_FILENAME);
		String ProductionFileName = _folder + SUFFIX + ConsumptionfileName;

		try {

			logger.debug(String.format("Producing message"));


			if(message.getData().length() > (10 * 1024 * 1024)) {
				logger.info("Large message - upload in 5 MB chunks");
				MultiPartUpload(message,amazonS3, _bucket, ProductionFileName);
			} else {
				logger.info("Small message - non-chunked upload");

			    File file = message.getData().toFile();

				logger.info("Uploading file with ACL: " + _acl );
			    
			    switch (_acl) {
			    	case "private":
			    		 amazonS3.putObject(new PutObjectRequest(_bucket, ProductionFileName, file)
								.withCannedAcl(CannedAccessControlList.Private));
			    		break;
			    	case "public-read":
			    		amazonS3.putObject(new PutObjectRequest(_bucket, ProductionFileName, file)
								.withCannedAcl(CannedAccessControlList.PublicRead));
			    		break;
			    	case "public-read-write":
			    		amazonS3.putObject(new PutObjectRequest(_bucket, ProductionFileName, file)
								.withCannedAcl(CannedAccessControlList.PublicReadWrite));
			    		break;
			    	case "aws-exec-read":
			    		amazonS3.putObject(new PutObjectRequest(_bucket, ProductionFileName, file)
								.withCannedAcl(CannedAccessControlList.AwsExecRead));
			    		break;
			    	case "authenticated-read":
			    		amazonS3.putObject(new PutObjectRequest(_bucket, ProductionFileName, file)
								.withCannedAcl(CannedAccessControlList.AuthenticatedRead));
			    		break;
			    	case "bucket-owner-read":
			    		amazonS3.putObject(new PutObjectRequest(_bucket, ProductionFileName, file)
								.withCannedAcl(CannedAccessControlList.BucketOwnerRead));
			    		break;
			    	case "bucket-owner-full-control":
			    		amazonS3.putObject(new PutObjectRequest(_bucket, ProductionFileName, file)
								.withCannedAcl(CannedAccessControlList.BucketOwnerFullControl));
			    		break;
			    	case "log-delivery-write":
			    		amazonS3.putObject(new PutObjectRequest(_bucket, ProductionFileName, file)
								.withCannedAcl(CannedAccessControlList.LogDeliveryWrite));
			    		break;
			    	default:
			    		amazonS3.putObject(new PutObjectRequest(_bucket, ProductionFileName, file));
			    }
		    }

			String msg = "S3 File Delivery complete.";
			returnMessage.setData(new VirtualData(msg.toCharArray()));

		} catch (AmazonS3Exception e) {
			throw new UnableToProduceException("Failed to deliver " + ConsumptionfileName +  " to AWS S3 Bucket:" +_bucket + ", Folder: " + _folder + 
	    			" with ACL: " + _acl + ". Please check that the correct ALC is selected for writing to this bucket.");
		} catch (IOException e) {
		     throw new UnableToProduceException("Failed to deliver " + ConsumptionfileName +  " to AWS S3 Bucket:" +_bucket + ", Folder: " + _folder);
		}

		return null;
	}


	public PluggableMessage consume(PluggableMessage message, String consumeNameFromList) throws UnableToConsumeException, FileNotFoundException {


		String ConsumptionFileName = _folder + SUFFIX + consumeNameFromList;

		try {


			logger.info("Consuming message: " + ConsumptionFileName);

			GetObjectRequest getObjectRequest = new GetObjectRequest(_bucket, ConsumptionFileName);
			S3Object object = amazonS3.getObject(getObjectRequest);
			S3ObjectInputStream inputStream = object.getObjectContent();
		    VirtualData data = new VirtualData();
		    data.readFrom(inputStream);
		    message.setData(data);
		 	message.setFilename(consumeNameFromList);
		 	message.setMetadata("AWS S3 Bucket", _bucket);
		 	message.setMetadata("AWS S3 Folder", _folder);



		} catch (Exception e) {
		      throw new UnableToConsumeException ("Failed to consume " + consumeNameFromList + " from AWS S3 Bucket:" +_bucket + ", Folder: " + _folder);
		}

		logger.info("Succesfully consumed message: " + ConsumptionFileName);

	return message;


	}



	@Override
	public String getUrl() throws PluggableException {
		String _URL;
		_URL = "Amazon S3 [" + _bucket + "/" + _folder + "]";
		return _URL;
	}

	public String[] list() throws UnableToConsumeException {

        logger.debug("Retrieving the files from folder: " + _folder);

    	String[] list = null;
        ArrayList<String> result = new ArrayList<String>();

	    ObjectListing listing = amazonS3.listObjects(new ListObjectsRequest().withBucketName(_bucket).withPrefix(_folder));
	    for (S3ObjectSummary objectSummary : listing.getObjectSummaries()) {

	    	if (objectSummary.getKey().endsWith("/")) {

	    		logger.debug("Folder Name: " + objectSummary.getKey());
	    	}
	    	else {

		    	String splitFolderPath[] = objectSummary.getKey().split("/");

		    	if (splitFolderPath.length > 1) {

		    		String entryName = splitFolderPath[splitFolderPath.length-1];

					PatternKeyValidator validator = PatternKeyValidatorFactory.createPatternValidator(_filtertype);


					if (validator.isValid(entryName, _filter)) {
						result.add(entryName);
						logger.info(entryName + " added to list.");
					}
					else {
						logger.debug(entryName + " does not match the defined filter (" + _filter +") and /or filter type (" + _filtertype + ")");
					}

		    	} else {

		    		//logger.debug("Folder Name: " + objectSummary.getKey());

		    	}
	    	}
	    }

		list = new String[result.size()];
		for (int i = 0; i < result.size(); i++) {
			list[i] = result.get(i);
			logger.debug("Adding Item [" + i + "]: " + list[i]);
		}

        return list;

	}

	public void delete(String deleteNameFromList) throws UnableToDeleteException, FileNotFoundException {

        logger.debug("Deleting the consumed file (" + deleteNameFromList + ") from folder: " + _folder);
		String deletionFileName = _folder + SUFFIX + deleteNameFromList;
		DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(_bucket, deletionFileName);
		amazonS3.deleteObject(deleteObjectRequest);

	}


	public String test() throws TransportTestException {

		AmazonS3 testamazonS3 = null;
		BasicAWSCredentials testcredentials = null;

		try {

			testcredentials = new BasicAWSCredentials(_accessKey, _secretKey);

			testamazonS3 = AmazonS3ClientBuilder.standard()
	                .withRegion(_region)
	                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

			testamazonS3.shutdown();


		} catch(Exception e) {
			throw new  TransportTestException("Failed to connect to S3");
		}
		return "Success, connected to S3";
	}

	public void disconnect() throws UnableToDisconnectException {
		logger.debug("Disconnecting from S3 server");
		try {
			// Close the Amazon S3 connection
			amazonS3.shutdown();

		} catch(Exception e) {
			logger.error("Failed to disconnect from S3 server");
		}
	}

	// S3

	public static Bucket getBucketID (AmazonS3 BucketamazonS3, String BucketName) {

		Bucket targetBucket = null;

		List<Bucket> buckets = BucketamazonS3.listBuckets();
		for (Bucket bucket : buckets)
		{
		  if (bucket.getName().equals(BucketName))
		  {
		    targetBucket = bucket;
		    break;
		  }
		}

		return targetBucket;
	}


 	//
	//  Code from https://docs.aws.amazon.com/AmazonS3/latest/dev/llJavaUploadFile.html
	//

    public static void MultiPartUpload(PluggableMessage MPUmessage, AmazonS3 MPUamazonS3, String MPUBucket, String MPUKeyName) throws IOException {


	    File file = MPUmessage.getData().toFile();

        long contentLength = file.length();
        long partSize = 5 * 1024 * 1024; // Set part size to 5 MB.

        try {

            List<PartETag> partETags = new ArrayList<PartETag>();

            // Initiate the multipart upload.
            InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(MPUBucket, MPUKeyName);
            InitiateMultipartUploadResult initResponse = MPUamazonS3.initiateMultipartUpload(initRequest);

            // Upload the file parts.
            long filePosition = 0;
            for (int i = 1; filePosition < contentLength; i++) {
                // Because the last part could be less than 5 MB, adjust the part size as needed.
                partSize = Math.min(partSize, (contentLength - filePosition));

                // Create the request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(MPUBucket)
                        .withKey(MPUKeyName)
                        .withUploadId(initResponse.getUploadId())
                        .withPartNumber(i)
                        .withFileOffset(filePosition)
                        .withFile(file)
                        .withPartSize(partSize);

                // Upload the part and add the response's ETag to our list.
                UploadPartResult uploadResult = MPUamazonS3.uploadPart(uploadRequest);
                partETags.add(uploadResult.getPartETag());

                filePosition += partSize;
            }

            // Complete the multipart upload.
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(MPUBucket, MPUKeyName,
                    initResponse.getUploadId(), partETags);
            MPUamazonS3.completeMultipartUpload(compRequest);
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }

        file.delete();


    }






}
