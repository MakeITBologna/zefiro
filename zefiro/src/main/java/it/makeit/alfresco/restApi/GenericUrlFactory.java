package it.makeit.alfresco.restApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.api.client.http.GenericUrl;

/**
 * @author Alba Quarto
 */
public final class GenericUrlFactory {
	private AlfrescoBaseUrl baseUrl;
	private List<AlfrescoBaseUrl> urls;

	public GenericUrlFactory(AlfrescoBaseUrl baseUrl) {
		checkUrl(baseUrl);
		urls = new ArrayList<AlfrescoBaseUrl>();
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

	public GenericUrl build(Map<String, Object> parameters) {
		GenericUrl url = this.build();
		Set<String> paramNames = baseUrl.getQueryParamNames();
		if (urls.size() > 0) {
			paramNames = urls.get(urls.size() - 1).getQueryParamNames();
		}
		for (Entry<String, Object> param : parameters.entrySet()) {
			String key = param.getKey();
			if (!paramNames.contains(key)) {
				continue;
			}
			url.put(key, param.getValue());
		}
		return url;
	}

	private void checkUrl(AlfrescoBaseUrl url) {
		if (url == null) {
			throw new AlfrescoUrlException(AlfrescoUrlException.NULL_URL);
		}
	}
}
