package it.makeit.alfresco.workflow.enties;

import java.net.URL;

import com.google.api.client.http.GenericUrl;

import it.makeit.alfresco.restApi.AlfrescoApiPath;
import it.makeit.alfresco.restApi.AlfrescoBaseUrl;
import it.makeit.alfresco.restApi.AlfrescoUrlException;

public class TaskFormModelUrl extends GenericUrl implements AlfrescoBaseUrl {

	private static final String PATH = "/start-form-model";

	public TaskFormModelUrl(URL pHostUrl) {
		super(pHostUrl);
		this.appendRawPath(AlfrescoApiPath.WORKFLOW.getPath());
		this.appendRawPath(PATH);
	}

	/**
	 * @author Alba Quarto
	 * @throw AlfrescoUrlException always
	 */
	@Override
	public URL getCompletePath() {
		throw new AlfrescoUrlException(AlfrescoUrlException.METHOD);
	}

	@Override
	public String getSpecificPath() {
		GenericUrl url = new GenericUrl();
		url.appendRawPath(PATH);

		return url.getRawPath();
	}

	/**
	 * @author Alba Quarto
	 * @throws AlfrescoUrlException
	 *             if a path parm has already been added or pathParam is null
	 */
	@Override
	public void addStringPathParam(String pathParam) {
		throw new AlfrescoUrlException(AlfrescoUrlException.METHOD);
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
