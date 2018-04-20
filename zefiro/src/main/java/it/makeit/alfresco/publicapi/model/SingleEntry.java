package it.makeit.alfresco.publicapi.model;

import com.google.api.client.util.Key;


public class SingleEntry<T extends Entry> {

	@Key
	private T entry;

	// TODO (Alessio): supporto per le relazioni
	// http://docs.alfresco.com/community/pra/1/concepts/pra-response.html

	public T getEntry() {
		return entry;
	}

	public void setEntry(T entry) {
		this.entry = entry;
	}
}
