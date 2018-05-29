package it.makeit.alfresco.publicapi.entities;

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
public class QueriesUrl extends GenericUrl implements AlfrescoBaseUrl {

	private static final String PATH = "/queries";
	private Set<String> params;
	private String pathParam;

	public QueriesUrl(URL pHostUrl) {
		super(pHostUrl);
		this.appendRawPath(AlfrescoApiPath.ALFRESCO.getPath());
		this.appendRawPath(PATH);
	}

	private void populateParams() {
		if (params == null) {
			params = new HashSet<String>();
		}
		addParam(AlfrescoRESTQueryParamsEnum.FIELDS, AlfrescoRESTQueryParamsEnum.SKIP_COUNT,
				AlfrescoRESTQueryParamsEnum.MAX_ITEMS, AlfrescoRESTQueryParamsEnum.ORDER_BY,
				AlfrescoRESTQueryParamsEnum.TERM);
	}

	private void addParam(AlfrescoRESTQueryParamsEnum... params) {
		if (params == null) {
			return;
		}
		for (AlfrescoRESTQueryParamsEnum param : params) {
			this.params.add(param.getName());
		}
	}

	@Override
	public URL getCompletePath() {
		return this.toURL();
	}

	@Override
	public String getSpecificPath() {
		throw new AlfrescoUrlException(AlfrescoUrlException.METHOD);
	}

	@Override
	public void addStringPathParam(String pathPart) {
		throw new AlfrescoUrlException(AlfrescoUrlException.METHOD);
	}

	@Override
	public void addIntPathParam(int pathPart) {
		throw new AlfrescoUrlException(AlfrescoUrlException.METHOD);
	}

	@Override
	public Set<String> getQueryParamNames() {
		return params;
	}

}
