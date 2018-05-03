package it.makeit.alfresco.workflow.enties;

import java.net.URL;

import com.google.api.client.http.GenericUrl;

import it.makeit.alfresco.restApi.AlfrescoApiPath;
import it.makeit.alfresco.restApi.AlfrescoBaseUrl;
import it.makeit.alfresco.restApi.AlfrescoUrlException;

/**
 * @author Alba Quarto
 */
public class TasksUrl extends GenericUrl implements AlfrescoBaseUrl {

	private static final String PATH = "/tasks";
	private String pathParam;

	public TasksUrl(URL pHostUrl) {
		super(pHostUrl);
		this.appendRawPath(AlfrescoApiPath.WORKFLOW.getPath());
		this.appendRawPath(PATH);
	}

	@Override
	public URL getCompletePath() {
		return this.toURL();
	}

	@Override
	public String getSpecificPath() {
		GenericUrl url = new GenericUrl();
		url.appendRawPath(PATH);
		if (pathParam != null) {
			url.getPathParts().add(pathParam);
		}
		return url.getRawPath();
	}

	/**
	 * @author Alba Quarto
	 * @throws AlfrescoUrlException
	 *             if a path parm has already been added or pathParam is null
	 */
	@Override
	public void addStringPathParam(String pathParam) {
		if (pathParam == null) {
			throw new AlfrescoUrlException(AlfrescoUrlException.PATH_PARAM);
		}
		if (this.pathParam != null) {
			throw new AlfrescoUrlException(AlfrescoUrlException.PATH_PARAM);
		}
		this.pathParam = pathParam;
		this.getPathParts().add(pathParam);
	}

	/**
	 * @author Alba Quarto
	 * @throw AlfrescoUrlException always
	 */
	@Override
	public void addIntPathParam(int pathPart) {
		throw new AlfrescoUrlException(AlfrescoUrlException.METHOD);
	}
}
