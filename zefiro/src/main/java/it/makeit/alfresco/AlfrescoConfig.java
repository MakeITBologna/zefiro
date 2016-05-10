package it.makeit.alfresco;

import java.net.URL;


public class AlfrescoConfig {

	private URL host;
	private String username;
	private String password;
	private String rootFolderId;


	public AlfrescoConfig(URL host, String username, String password, String rootFolderId) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.rootFolderId = rootFolderId;
	}

	public URL getHost() {
		return host;
	}

	public void setHost(URL host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRootFolderId() {
		return rootFolderId;
	}

	public void setRootFolderId(String rootFolderId) {
		this.rootFolderId = rootFolderId;
	}
}
