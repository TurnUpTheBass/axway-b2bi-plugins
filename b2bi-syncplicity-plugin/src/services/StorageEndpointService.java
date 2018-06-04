package services;

import entities.StorageEndpoint;
import util.APIGateway;
import util.ConfigurationHelper;

/**
 * A service for retrieving Storage Endpoint data.
 *
 */
public class StorageEndpointService extends APIGateway {

    /**
     * Storage Endpoints service URL.
     */
    protected static String storageEndpointsUrl;

    static {
        storageEndpointsUrl = ConfigurationHelper.getBaseApiEndpointUrl() + "storage/storageendpoints.svc/";
    }

    /**
     * Retrieves storage endpoints.
     * 
     * @param suppressErrors
     *            indicates whether errors should be suppressed
     */
    public static StorageEndpoint[] getStorageEndpoints(boolean suppressErrors) {
        StorageEndpoint[] storageEndpoints = httpGet(storageEndpointsUrl, StorageEndpoint[].class, suppressErrors);
        return storageEndpoints == null ? new StorageEndpoint[0] : storageEndpoints;
    }
}
