package it.makeit.alfresco.publicapi.model;

import com.google.api.client.util.Key;


public class Pagination {

	@Key
	private int count;

	@Key
	private boolean hasMoreItems;

	@Key
	private int totalItems;

	@Key
	private int skipCount;

	@Key
	private int maxItems;

	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}

	public boolean isHasMoreItems() {
		return hasMoreItems;
	}

	public void setHasMoreItems(boolean hasMoreItems) {
		this.hasMoreItems = hasMoreItems;
	}

	public int getTotalItems() {
		return totalItems;
	}


	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public int getSkipCount() {
		return skipCount;
	}

	public void setSkipCount(int skipCount) {
		this.skipCount = skipCount;
	}

	public int getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}
}
