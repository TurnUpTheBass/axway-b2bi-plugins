package services;

import entities.SyncPoint;
import util.APIGateway;
import util.ConfigurationHelper;

/**
 * A service for retrieving SyncPoint data.
 *
 */
public class SyncPointService extends APIGateway {

    /**
     * SyncPoints service URL.
     */
    protected static String syncPointsUrl;

    /**
     * SyncPoint service URL.
     */
    protected static String syncPointUrl;

    static {
        syncPointsUrl = ConfigurationHelper.getBaseApiEndpointUrl() + "syncpoint/syncpoints.svc/";
        syncPointUrl = ConfigurationHelper.getBaseApiEndpointUrl() + "syncpoint/syncpoint.svc/" + "%s";
    }

    /**
     * Retrieves SyncPoints.
     * 
     * @param suppressErrors
     *            indicates whether errors should be suppressed
     * @return the <code>SyncPoint</code> objects
     */
    public static SyncPoint[] getSyncPoints(boolean suppressErrors) {
        SyncPoint[] syncPoints = httpGet(syncPointsUrl, SyncPoint[].class, suppressErrors);
        return syncPoints == null ? new SyncPoint[0] : syncPoints;
    }

    /**
     * Retrieves a SyncPoint.
     * 
     * @param syncPointId
     *            the SyncPoint ID
     * @param suppressErrors
     *            indicates whether errors should be suppressed
     * @return the matching <code>SyncPoint</code> object
     */
    public static SyncPoint getSyncPoint(long syncPointId, boolean suppressErrors) {
        return httpGet(String.format(syncPointUrl, syncPointId), SyncPoint.class, suppressErrors);
    }

    /**
     * Creates a SyncPoint.
     * 
     * @param syncPoints
     *            the <code>SyncPoint</code> DTOs
     * @return the created <code>SyncPoint</code> records
     */
    public static SyncPoint[] createSyncPoints(SyncPoint[] syncPoints) {
        return httpPost(syncPointsUrl, "application/json", syncPoints);
    }

    /**
     * Deletes a SyncPoint.
     * 
     * @param syncPointId
     *            the SyncPoint ID
     */
    public static void deleteSyncPoint(long syncPointId) {
        httpDelete(String.format(syncPointUrl, syncPointId), SyncPoint.class);
    }
}
