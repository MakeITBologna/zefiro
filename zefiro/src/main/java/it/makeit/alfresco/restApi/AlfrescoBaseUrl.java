package it.makeit.alfresco.restApi;

import java.net.URL;

/**
 * @author Alba Quarto
 */
public interface AlfrescoBaseUrl {

	URL getCompletePath();

	String getSpecificPath();

	void addStringPathParam(String pathPart);

	void addIntPathParam(int pathPart);
}
