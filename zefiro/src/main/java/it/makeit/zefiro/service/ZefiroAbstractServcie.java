package it.makeit.zefiro.service;

import java.util.Map;

import com.google.api.client.http.HttpRequestFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.publicapi.model.Person;
import it.makeit.zefiro.DecodedFieldNote.DecodingType;
import it.makeit.zefiro.Util;
import it.makeit.zefiro.dao.BaseData;

public class ZefiroAbstractServcie {
	protected HttpRequestFactory httpRequestFactory;
	protected AlfrescoConfig alfrescoConfig;
	protected String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	protected Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();

	public ZefiroAbstractServcie(AlfrescoConfig pConfig) {
		httpRequestFactory = AlfrescoHelper.getRequestFactory(pConfig);
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
