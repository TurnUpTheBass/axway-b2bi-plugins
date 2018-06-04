package services;

import entities.Link;
import util.APIGateway;
import util.ConfigurationHelper;

/**
 * A service for retrieving Share Link data.
 *
 */
public class LinkService extends APIGateway {

    /**
     * Share Links service URL.
     */
    protected static String linksUrl;

    /**
     * Share Link service URL.
     */
    protected static String linkUrl;

    static {
        linksUrl = ConfigurationHelper.getBaseApiEndpointUrl() + "syncpoint/links.svc/";
        linkUrl = ConfigurationHelper.getBaseApiEndpointUrl() + "syncpoint/link.svc/%s";
    }

    /**
     * Generates a share link.
     * 
     * @param links
     *            the <code>Link</code> DTOs
     * @return the generated <code>Link</code> records
     */
    public static Link[] generateLinks(Link[] links) {
        Link[] generatedLinks = httpPost(linksUrl, "application/json", links);
        return generatedLinks == null ? new Link[0] : generatedLinks;
    }

    /**
     * Retrieves a Share Link.
     * 
     * @param token
     *            the share link token
     * @param suppressErrors
     *            indicates whether errors should be suppressed
     * @return the matching <code>Link</code> object
     */
    public static Link getLink(String token, boolean suppressErrors) {
        return httpGet(String.format(linkUrl, token), Link.class, suppressErrors);
    }

    /**
     * Deletes a share link.
     * 
     * @param token
     *            the share link token
     */
    public static void deleteLink(String token) {
        httpDelete(String.format(linkUrl, token), Link.class);
    }
}
