package it.makeit.alfresco.webscriptsapi.services;

import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import it.makeit.alfresco.AlfrescoConfig;

public abstract class AbstractService {

	protected static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	protected static final JsonFactory JSON_FACTORY = new JacksonFactory();

	protected final HttpRequestFactory mHttpRequestFactory;
	protected final GenericUrl mBaseUrl;

	public AbstractService(final AlfrescoConfig pConfig) {
		mBaseUrl = new GenericUrl(pConfig.getHost());
		mHttpRequestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {

			@Override
			public void initialize(HttpRequest request) {
				request
		                .setParser(new JsonObjectParser(JSON_FACTORY))
		                .setInterceptor(new BasicAuthentication(pConfig.getUsername(), pConfig.getPassword()));
			}
		});
	}

	public static HttpTransport getHttpTransport() {
		return HTTP_TRANSPORT;
	}

	public static JsonFactory getJsonFactory() {
		return JSON_FACTORY;
	}

	public HttpRequestFactory getHttpRequestFactory() {
		return mHttpRequestFactory;
	}

}
