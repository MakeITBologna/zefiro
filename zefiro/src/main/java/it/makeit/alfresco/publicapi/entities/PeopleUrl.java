package it.makeit.alfresco.publicapi.entities;

import java.net.URL;

import com.google.api.client.http.GenericUrl;


public class PeopleUrl extends GenericUrl {

	private static final String PATH = "/alfresco/api/-default-/public/alfresco/versions/1/people";

	public PeopleUrl(URL pHostUrl) {
		super(pHostUrl);
		this.appendRawPath(PATH);
	}

	public void setUserId(String userId) {
		this.getPathParts().add(userId);
	}

	// TODO (Alessio): definire i possibili parametri (@Key)
}
