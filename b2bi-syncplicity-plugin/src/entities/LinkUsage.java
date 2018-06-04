package entities;

import java.io.Serializable;
import java.util.Date;


@SuppressWarnings("serial")
public class LinkUsage implements Serializable {
	public User User;

	public int NumDownloads;

	public Date LastDownloadDateUtc;

	public String LastDownloadIpAddress;

	public java.math.BigDecimal LastDownloadGeoLatitude = null;

	public java.math.BigDecimal LastDownloadGeoLongitude = null;
}