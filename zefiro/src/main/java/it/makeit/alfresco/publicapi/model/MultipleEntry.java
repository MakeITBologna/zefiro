package it.makeit.alfresco.publicapi.model;

import java.util.List;

import com.google.api.client.util.Key;


public class MultipleEntry<T extends Entry> {

	@Key
	private Pagination pagination;

	@Key
	private List<T> entries;

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public List<T> getEntries() {
		return entries;
	}

	public void setEntries(List<T> entries) {
		this.entries = entries;
	}
}
