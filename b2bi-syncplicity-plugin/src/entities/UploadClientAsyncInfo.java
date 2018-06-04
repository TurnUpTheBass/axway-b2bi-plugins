package entities;

import java.net.HttpURLConnection;

public class UploadClientAsyncInfo {
	public UploadClientAsyncInfo(HttpURLConnection request, String filename) {
		Request = request;
		Filename = filename;
	}

	public HttpURLConnection Request;

	public String Filename;
}