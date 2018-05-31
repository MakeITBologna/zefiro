package it.makeit.zefiro.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.publicapi.model.Person;
import it.makeit.alfresco.webscriptsapi.model.Group;

public class AuthorityService extends ZefiroAbstractServcie {

	public AuthorityService(AlfrescoConfig pConfig) {
		super(pConfig);
	}

	public List<Person> loadUsers(String charsSeq) {
		return AlfrescoHelper.getUsers(charsSeq.toLowerCase(), httpRequestFactory, alfrescoConfig, null);
	}

	public List<Group> loadGroups(String charsSeq) {
		List<Group> filteredGroups = new ArrayList<Group>();
		List<Group> groups = AlfrescoHelper.getGroups(httpRequestFactory, alfrescoConfig).getGroups();

		for (Group group : groups) {
			if (StringUtils.containsIgnoreCase(group.getDisplayName(), charsSeq.toLowerCase())) {
				filteredGroups.add(group);
			}
		}
		return filteredGroups;
	}

}
