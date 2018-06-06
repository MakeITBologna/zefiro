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
 *
 * @author Alba Quarto
 *
 */
public class StartFormModelUrl extends GenericUrl implements AlfrescoBaseUrl {

	private static final String PATH = "/start-form-model";
	private Set<String> params;

	public StartFormModelUrl(URL pHostUrl) {
		super(pHostUrl);
		populateParams();
		this.appendRawPath(AlfrescoApiPath.WORKFLOW.getPath());
		this.appendRawPath(PATH);
	}

	private void populateParams() {
		if (params == null) {
			params = new HashSet<String>();
		}
		addParam(AlfrescoRESTQueryParamsEnum.PROPERTIES);
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

	@Override
	public Set<String> getQueryParamNames() {
		return params;
	}
}
