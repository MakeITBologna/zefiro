package it.makeit.alfresco.webscriptsapi.model;

import java.util.List;

import com.google.api.client.util.Key;


public class Group {

	@Key
	// TODO: usare una classe/enum ad hoc?
	private String authorityType;

	@Key
	private String shortName;

	@Key
	private String fullName;

	@Key
	private String displayName;

	@Key
	private String url;

	@Key
	private List<String> zones;
}
