package it.makeit.alfresco.workflow.enties;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.google.api.client.http.GenericUrl;

import it.makeit.alfresco.restApi.AlfrescoApiPath;
import it.makeit.alfresco.restApi.AlfrescoBaseUrl;
import it.makeit.alfresco.restApi.AlfrescoRESTQueryParamsEnum;
import it.makeit.alfresco.restApi.AlfrescoUrlException;

/**
 * @author Alba Quarto
 */
public class VariablesUrl extends GenericUrl implements AlfrescoBaseUrl {

	private static final String PATH = "/variables";
	private String pathParam;
	private Set<String> params;

	public VariablesUrl(URL pHostUrl) {
		super(pHostUrl);
		populateParams();
		this.appendRawPath(AlfrescoApiPath.WORKFLOW.getPath());
		this.appendRawPath(PATH);
	}

	private void populateParams() {
		if (params == null) {
			params = new HashSet<String>();
		}
		addParam(AlfrescoRESTQueryParamsEnum.MAX_ITEMS, AlfrescoRESTQueryParamsEnum.SKIP_COUNT,
				AlfrescoRESTQueryParamsEnum.PROPERTIES, AlfrescoRESTQueryParamsEnum.WHERE);
	}

	private void addParam(AlfrescoRESTQueryParamsEnum... params) {
		if (params == null) {
			return;
		}
		for (AlfrescoRESTQueryParamsEnum param : params) {
			this.params.add(param.getName());
		}
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
