package it.makeit.alfresco.restApi.model;

import java.util.List;

import it.makeit.alfresco.webscriptsapi.model.Paging;

public abstract class AbstractList<T> {

	private List<T> data;

	private Paging paging;

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Paging getPaging() {
		return paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}
}
