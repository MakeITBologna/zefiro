package it.makeit.alfresco.webscriptsapi.entities;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.google.api.client.http.GenericUrl;

import it.makeit.alfresco.restApi.AlfrescoApiPath;
import it.makeit.alfresco.restApi.AlfrescoBaseUrl;
import it.makeit.alfresco.restApi.AlfrescoRESTQueryParamsEnum;
import it.makeit.alfresco.restApi.AlfrescoUrlException;
import it.makeit.alfresco.restApi.RESTQueryParams;
import it.makeit.alfresco.webscriptsapi.AlfrescoWorkflowInstanceQueryParamsEnum;

public class WorkflowInstanceUrl extends GenericUrl implements AlfrescoBaseUrl {

	private static final String PATH = "/workflow-instances";
	private String pathParam;
	private Set<String> params;

	public WorkflowInstanceUrl(URL pHostUrl) {
		super(pHostUrl);
		populateParams();
		this.appendRawPath(AlfrescoApiPath.SERVICE.getPath());
		this.appendRawPath(PATH);
	}

	private void populateParams() {
		if (params == null) {
			params = new HashSet<String>();
		}

		addParam(AlfrescoWorkflowInstanceQueryParamsEnum.INITIATOR, AlfrescoWorkflowInstanceQueryParamsEnum.STATE,
				AlfrescoWorkflowInstanceQueryParamsEnum.INCLUDE_TASKS, AlfrescoRESTQueryParamsEnum.SKIP_COUNT,
				AlfrescoRESTQueryParamsEnum.MAX_ITEMS);
	}

	private void addParam(RESTQueryParams... params) {
		if (params == null) {
			return;
		}
		for (RESTQueryParams param : params) {
			this.params.add(param.getName());
		}
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

	@Override
	public Set<String> getQueryParamNames() {
		return params;
	}

}
