package it.makeit.alfresco.webscriptsapi.entities;

import java.net.URL;

import com.google.api.client.http.GenericUrl;

import it.makeit.alfresco.restApi.AlfrescoApiPath;

public class GroupsUrl extends GenericUrl {

	private static final String PATH = "/groups";

	public GroupsUrl(URL pHostUrl) {
		super(pHostUrl);
		this.appendRawPath(AlfrescoApiPath.SERVICE.getPath());
		this.appendRawPath(PATH);
	}

	public void setGroupShortName(String groupShortName) {
		this.getPathParts().add(groupShortName);
	}

	public void addChild(String groupShortName, String userId) {
		this.setGroupShortName(groupShortName);
		this.getPathParts().add("children");
		this.getPathParts().add(userId);
	}

	// TODO (Alessio): definire i possibili parametri (@Key)
}
