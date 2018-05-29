package it.makeit.zefiro.service;

import java.util.List;
import java.util.Map;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.publicapi.model.Person;

public class AuthorityService extends ZefiroAbstractServcie {

	public AuthorityService(AlfrescoConfig pConfig) {
		super(pConfig);
	}

	public List<Person> loadUsers(String charsSeq, Map<String, Object> params) {
		return AlfrescoHelper.getUsers(charsSeq, httpRequestFactory, alfrescoConfig, params);
	}

}
