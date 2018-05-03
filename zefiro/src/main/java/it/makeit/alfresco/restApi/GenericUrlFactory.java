package it.makeit.alfresco.restApi;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.http.GenericUrl;

public final class GenericUrlFactory {
	private AlfrescoBaseUrl baseUrl;
	private List<AlfrescoBaseUrl> urls;

	public GenericUrlFactory(AlfrescoBaseUrl baseUrl) {
		checkUrl(baseUrl);
		this.baseUrl = baseUrl;
	}

	public GenericUrlFactory add(AlfrescoBaseUrl url) {
		checkUrl(url);
		if (urls == null) {
			urls = new ArrayList<AlfrescoBaseUrl>();
		}
		urls.add(url);
		return this;
	}

	public GenericUrl build() {
		GenericUrl url = new GenericUrl(baseUrl.getCompletePath());

		for (AlfrescoBaseUrl pUrl : urls) {
			String path = pUrl.getSpecificPath();
			if (path != null) {
				url.appendRawPath(path);
			}
		}
		return url;
	}

	private void checkUrl(AlfrescoBaseUrl url) {
		if (url == null) {
			throw new AlfrescoUrlException(AlfrescoUrlException.NULL_URL);
		}
	}
}
