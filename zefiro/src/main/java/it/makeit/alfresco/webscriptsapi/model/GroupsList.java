package it.makeit.alfresco.webscriptsapi.model;

import java.util.List;

import com.google.api.client.util.Key;


public class GroupsList {

	@Key
	private List<Group> data;

	@Key
	private Paging paging;

	public List<Group> getGroups() {
		return data;
	}

	public void setGroups(List<Group> data) {
		this.data = data;
	}

	public Paging getPaging() {
		return paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}
}
