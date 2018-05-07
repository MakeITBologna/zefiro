package it.makeit.zefiro.service;

import com.google.api.client.http.HttpRequestFactory;

import it.makeit.alfresco.AlfrescoConfig;

public class AbstractServcie {
	protected HttpRequestFactory httpRequestFactory;
	protected AlfrescoConfig alfrescoConfig;

	public AbstractServcie(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		httpRequestFactory = pHttpRequestFactory;
		alfrescoConfig = pConfig;
	}
}
