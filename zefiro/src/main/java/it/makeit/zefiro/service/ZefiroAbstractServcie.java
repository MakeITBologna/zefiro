package it.makeit.zefiro.service;

import java.util.Map;

import com.google.api.client.http.HttpRequestFactory;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.publicapi.model.Person;
import it.makeit.zefiro.DecodedFieldNote.DecodingType;
import it.makeit.zefiro.Util;
import it.makeit.zefiro.dao.BaseData;

public class ZefiroAbstractServcie {
	protected HttpRequestFactory httpRequestFactory;
	protected AlfrescoConfig alfrescoConfig;

	public ZefiroAbstractServcie(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		httpRequestFactory = pHttpRequestFactory;
		alfrescoConfig = pConfig;
	}

	protected void addPersonDecoding(String userId, BaseData data, Map<String, Person> people, DecodingType context) {
		if (userId == null) {
			return;
		}

		Person decoding = people.get(userId);
		if (decoding == null) {
			decoding = AlfrescoHelper.getUser(userId, httpRequestFactory, alfrescoConfig);
			people.put(userId, decoding);
		}
		Util.decodeField(data, decoding, context);
	}
}
