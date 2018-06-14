package it.makeit.alfresco;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class AlfrescoConfig {

	private URL host;
	private String username;
	private String password;
	private String rootFolderId;
	private List<String> acceptedLanguages;
	private final String SEPARATOR = ",";

	public AlfrescoConfig(URL host, String username, String password, String rootFolderId) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.rootFolderId = rootFolderId;
	}

	public AlfrescoConfig(URL host, String username, String password) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
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

	public List<String> getAcceptedLanguages() {
		return acceptedLanguages;
	}

	public void setAcceptedLanguages(List<String> acceptedLanguages) {
		this.acceptedLanguages = acceptedLanguages;
	}

	public void setAcceptedLanguages(String acceptedLanguages) {
		String al = acceptedLanguages.replaceAll("\\s+", "");
		this.acceptedLanguages = Arrays.asList(al.split(","));
	}

	public String getAcceptedLanguageAsString() {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < acceptedLanguages.size(); i++) {
			String s = acceptedLanguages.get(i);
			if (i == 0) {
				sb.append(s);
				continue;
			}
			sb.append(SEPARATOR).append(s);
		}
		return sb.toString();
	}
}
