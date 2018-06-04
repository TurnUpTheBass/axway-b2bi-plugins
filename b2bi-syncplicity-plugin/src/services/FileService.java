package services;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;

import entities.File;
import entities.FileVersion;
import entities.FileVersionDetails;
import entities.StorageEndpoint;
import entities.SyncPoint;
import util.APIContext;
import util.APIGateway;
import util.ConfigurationHelper;

/**
 * A service for retrieving File data.
 *
 */
public class FileService extends APIGateway {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm'Z'";
    private static final String CRLF = "\r\n";
    private static final String BOUNDARY = "-------" + String.valueOf(System.currentTimeMillis());
    private static final String TWO_HYPHENS = "--";

    /**
     * Files service URL.
     */
    protected static String filesUrl;

    /**
     * File service URL.
     */
    protected static String fileUrl;
    protected static String fileVersionUrl;

    /**
     * Upload File URL.
     */
    protected static String uploadFileUrl;

    /**
     * Download File URL.
     */
    protected static String downloadUrl;

    static {
        filesUrl = ConfigurationHelper.getBaseApiEndpointUrl() + "sync/file.svc/%s/file/%s";
        fileUrl = ConfigurationHelper.getBaseApiEndpointUrl() + "sync/file.svc/%s/file/%s";
        fileVersionUrl = ConfigurationHelper.getBaseApiEndpointUrl() + "sync/versions.svc/%s/file/%s/versions";
        uploadFileUrl = "%s/saveFile.php?filepath=%s";
        downloadUrl = "%s/retrieveFile.php?vToken=%s&sessionKey=%s";
    }

    /**
     * Retrieves File info.
     * 
     * @param syncPointId
     *            the SyncPoint ID
     * @param fileId
     *            the File ID
     * @param suppressErrors
     *            Indicates whether errors should be suppressed
     * @return the File info
     */
    public static File getFile(long syncPointId, String fileId, boolean suppressErrors) {
        return httpGet(String.format(fileUrl, syncPointId, fileId), File.class, suppressErrors);
    }

    /**
     * Retrieves File Version info.
     * 
     * @param syncPointId
     *            the SyncPoint ID
     * @param fileVersionId
     *            the File Version ID
     * @param suppressErrors
     *            Indicates whether errors should be suppressed
     * @return the File Versions
     */
    public static FileVersionDetails[] getFileVersion(long syncPointId, String fileVersionId, boolean suppressErrors) {
        return httpGet(String.format(fileVersionUrl, syncPointId, fileVersionId), FileVersionDetails[].class, suppressErrors);
    }
    
    
    /**
     * Downloads a file from Syncplicity.
     * 
     * @param syncPointId
     *            the related SyncPoint ID
     * @param fileId
     *            the file ID
     * @param suppressErrors
     *            indicates whether the errors should be suppressed
     * @return the downloaded file contents as string. Others may want to retrieve
     *         the contents as InputStream, Resource or byte[]. In this case it's
     *         just needed to translate to returned response differently.
     */

    /**
     * Downloads a File.
     * 
     * @param syncPointId
     *            the SyncPoint ID
     * @param fileId
     *            the File ID
     * @param suppressErrors
     *            indicates whether the errors should be suppressed
     * @return the File contents as a string
     */
    public static String downloadFile(long syncPointId, String fileId, boolean suppressErrors) {
        File file = getFile(syncPointId, fileId, true);
        SyncPoint syncPoint = SyncPointService.getSyncPoint(syncPointId, suppressErrors);
        StorageEndpoint storageEndpoint = getStorageEndpoint(syncPoint.StorageEndpointId);
        String bearerToken = "Bearer " + APIContext.getAccessToken();
        try {
            bearerToken = URLEncoder.encode(bearerToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Could not encode Bearer token.");
        }
        String vToken = syncPointId + "-" + file.LatestVersionId;
        String url = String.format(downloadUrl, storageEndpoint.Urls[0].Url, vToken, bearerToken);
        
        return httpGet(url, String.class, false);
    }

    public static String deleteFile(long syncPointId, String fileId, boolean suppressErrors) {
        File file = getFile(syncPointId, fileId, true);
        return httpDelete(String.format(fileUrl, syncPointId, file.LatestVersionId), String.class);
    	
    }
   
    
    /**
     * Retrieves a Storage Endpoint by ID.
     * 
     * @param storageEndpointId
     *            the Storage Endpoint ID
     * @return the matching <code>StorageEndpoint</code> object
     */
    private static StorageEndpoint getStorageEndpoint(String storageEndpointId) {
        StorageEndpoint[] storageEndpoints = StorageEndpointService.getStorageEndpoints(true);
        for (StorageEndpoint storageEndpoint : storageEndpoints) {
            if (storageEndpoint.Active && storageEndpointId.equals(storageEndpoint.Id)) {
                return storageEndpoint;
            }
        }
        return null;
    }

    /**
     * Uploads a file.
     * 
     * @param storageEndpointUrl
     *            the Storage Endpoint ID
     * @param filePath
     *            the file path
     * @param filename
     *            the filename
     * @param syncPointId
     *            the SyncPoint ID
     * @param data
     *            the file data as byte array
     * @return the upload result as a string
     */
    public static String uploadFile(String storageEndpointUrl, String filePath, String filename, int syncPointId,  
            byte[] data) {
        filePath += filename;
        try {
            filePath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            System.err.println(String.format("Could not encode file path", filePath));
        }
        String contentType = "multipart/form-data; boundary=" + BOUNDARY;

        String sha256 = getSha256(data);
        String url = String.format(uploadFileUrl, storageEndpointUrl, filePath);
        String sessionKey = "Bearer " + APIContext.getAccessToken();
        String creationTimeUtc = getDateTimeUtc();

        String multipartBody = createMultipartBody(filename, data, sha256, sessionKey, syncPointId, creationTimeUtc);

        return httpPost(false, url, contentType, multipartBody, String.class);
    }

    /**
     * Creates a multipart body.
     * 
     * @param filename
     *            the filename
     * @param data
     *            the file data
     * @param sha256
     *            the SHA256 hash of tile content
     * @param sessionKey
     *            the session key (Bearer token info)
     * @param syncPointId
     *            the SyncPoint ID
     * @param creationTimeUtc
     *            the file creation date time in ISO 8601 format
     * @return the multipart request body as a string
     */
    private static String createMultipartBody(String filename, byte[] data, String sha256, String sessionKey,
            int syncPointId, String creationTimeUtc) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        try {
            out.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
            String fileParamName = "fileData";
            out.writeBytes("Content-Disposition: form-data; name=\"" + fileParamName + "\"; filename=\"" + filename
                    + "\"" + CRLF);
            out.writeBytes("Content-Transfer-Encoding: binary" + CRLF);
            out.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(filename) + CRLF);
            out.writeBytes(CRLF);
            out.write(data);
            out.writeBytes(CRLF);
            addFormField(out, "sha256", sha256);
            addFormField(out, "sessionKey", sessionKey);
            addFormField(out, "virtualFolderId", String.valueOf(syncPointId));
            addFormField(out, "creationTimeUtc", creationTimeUtc);
            addFormField(out, "lastWriteTimeUtc", creationTimeUtc);
            addFormField(out, "fileDone", null);
            out.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + CRLF);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String multipartBody = baos.toString();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return multipartBody;
    }

    /**
     * Generates SHA256 hash for the passed data
     * 
     * @param data
     *            the data to generate SHA256 hash for
     * @return the SHA256 hash
     */
    private static String getSha256(byte[] data) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return DatatypeConverter.printHexBinary(digest.digest(data));
    }

    /**
     * Generates a current date time string in ISO 8601 format.
     * 
     * @return the current date time string
     */
    private static String getDateTimeUtc() {
        // Quoted "Z" to indicate UTC, no timezone offset
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(new Date());
    }

    /**
     * Adds a form field to the request.
     * 
     * @param out
     *            the data output stream to write the form field to
     * @param name
     *            field name
     * @param value
     *            field value
     * @throws IOException
     */
    private static void addFormField(DataOutputStream out, String name, String value) throws IOException {
        out.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
        out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + CRLF);
        if (value != null) {
            out.writeBytes(CRLF);
            out.writeBytes(value + CRLF);
        }
        out.flush();
    }
}